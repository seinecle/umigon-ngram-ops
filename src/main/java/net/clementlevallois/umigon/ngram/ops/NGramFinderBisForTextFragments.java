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
            NGram word = it.next();
            if (!(word instanceof NGram)) {
                System.out.println("alert a non ngram detected in method generateNgramsUpto");
                System.out.println("textFragment was: " + word.getOriginalForm());
                continue;
            }

            //1- add the word itself
            textFragmentsAugmentedWithNGrams.add(word);

            //2- open a new NGram
            ngram = new NGram();
            ngram.getTerms().add(word.getTerms().get(0));
            ngramSize = 1;

            // call to 'previous()' to stay on the same term on the next iteration forward
            it.previous();

            //2- insert prevs of the word and add those too
            while (it.hasPrevious() && ngramSize < maxGramSize) {
                ngram.getTerms().add(0, it.previous().getTerms().get(0));
                NGram newNgram = new NGram();
                newNgram.getTerms().addAll(ngram.getTerms());
                newNgram.setIndexCardinal(newNgram.getTerms().get(0).getIndexCardinal());
                newNgram.setIndexOrdinal(newNgram.getTerms().get(0).getIndexOrdinal());
                newNgram.setIndexCardinalInSentence(newNgram.getTerms().get(0).getIndexCardinalInSentence());
                newNgram.setIndexOrdinalInSentence(newNgram.getTerms().get(0).getIndexOrdinalInSentence());
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
