package net.clementlevallois.umigon.ngram.ops;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.Term;

/**
 *
 * @author LEVALLOIS adapted from https://stackoverflow.com/a/13977401/798502
 */
public class NGramFinderBisForTextFragments {

    public static void main(String[] args) throws IOException {
    }

    public static List<NGram> generateNgramsUpto(List<NGram> ngrams, int maxGramSize) {

        List<NGram> textFragmentsAugmentedWithNGrams = new ArrayList();
        int ngramSize;
        NGram ngram;

        ListIterator<NGram> it = ngrams.listIterator();
        while (it.hasNext()) {
            NGram unigram = it.next();

            if (!(unigram instanceof NGram)) {
                System.out.println("alert a non ngram detected in method generateNgramsUpto");
                System.out.println("textFragment was: " + unigram.getOriginalForm());
                continue;
            }
            
            // set sentence based indices on the ngram
            unigram.setIndexOrdinalInSentence(ngrams.indexOf(unigram));

            //1- add the word itself
            textFragmentsAugmentedWithNGrams.add(unigram);

            //2- open a new NGram
            ngram = new NGram();
            ngram.getTerms().add(unigram.getTerms().get(0));
            ngramSize = 1;

            // call to 'previous()' to stay on the same term on the next iteration forward
            it.previous();

            //2- insert prevs of the word and add those too
            while (it.hasPrevious() && ngramSize < maxGramSize) {
                NGram previousUnigram = it.previous();
                Term previousTerm = previousUnigram.getTerms().get(0);
                ngram.getTerms().add(0, previousTerm);
                NGram newNgram = new NGram();
                newNgram.getTerms().addAll(ngram.getTerms());
                newNgram.setIndexCardinal(previousUnigram.getIndexCardinal());
                newNgram.setIndexOrdinal(previousUnigram.getIndexOrdinal());
                newNgram.setIndexCardinalInSentence(previousUnigram.getIndexCardinalInSentence());
                newNgram.setIndexOrdinalInSentence(previousUnigram.getIndexOrdinalInSentence());
                StringBuilder sb = new StringBuilder();
                for (Term term : newNgram.getTerms()) {
                    sb.append(term.getOriginalForm());
                    sb.append(" ");
                }
                newNgram.setOriginalForm(sb.toString().trim());
                textFragmentsAugmentedWithNGrams.add(newNgram);
                ngramSize++;
            }

            //go back to initial position + 1
            while (ngramSize > 0) {
                ngramSize--;
                it.next();
            }
        }
        return textFragmentsAugmentedWithNGrams;
    }
}
