package net.clementlevallois.umigon.ngram.ops;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.Term;
import net.clementlevallois.umigon.model.TextFragment;
import net.clementlevallois.umigon.model.TypeOfTextFragment;
import net.clementlevallois.umigon.tokenizer.controller.UmigonTokenizer;

/**
 *
 * @author LEVALLOIS adapted from https://stackoverflow.com/a/13977401/798502
 */
public class NGramFinderBisForTextFragments {

    public static void main(String[] args) throws IOException {
        String example = "J'aime, vraiment vous êtes des champions (même toi!) http://momo";
        Set<String> languageSpecificLexicon = new HashSet();
        List<TextFragment> allTextFragments = UmigonTokenizer.tokenize(example, languageSpecificLexicon);
        List<TextFragment> allTextFragmentsAugmentedWithNGrams = new ArrayList();
        List<List<TextFragment>> listOfTextFragments = new FragmentSelectorForNGramOps().returnListsOfTextFragmentsWithTermsOnly(allTextFragments);
        for (List<TextFragment> listOfFragments : listOfTextFragments) {
            List<TextFragment> generateNgramsUpto = NGramFinderBisForTextFragments.generateNgramsUpto(listOfFragments, 5);
            allTextFragmentsAugmentedWithNGrams.addAll(generateNgramsUpto);
        }
        for (TextFragment textFragment : allTextFragmentsAugmentedWithNGrams) {
            if (textFragment.getTypeOfTextFragment() == null) {
                System.out.println("stop null fragment type");
            }
            if (textFragment.getTypeOfTextFragment().equals(TypeOfTextFragment.TypeOfTextFragmentEnum.TERM)) {
                System.out.println(textFragment.getString());
            }
            if (textFragment.getTypeOfTextFragment().equals(TypeOfTextFragment.TypeOfTextFragmentEnum.NGRAM)) {
                NGram ngram = (NGram) textFragment;
                List<Term> terms = ngram.getTerms();
                for (Term term : terms) {
                    System.out.print(term.getString());
                    System.out.print(' ');
                }
                System.out.println("");
            }
        }

    }

    public static List<TextFragment> generateNgramsUpto(List<TextFragment> textFragments, int maxGramSize) {

        List<TextFragment> textFragmentsAugmentedWithNGrams = new ArrayList();
        int ngramSize;
        NGram ngram;

        ListIterator<TextFragment> it = textFragments.listIterator();
        while (it.hasNext()) {
            TextFragment word = it.next();
            if (!(word instanceof Term)) {
                System.out.println("alert a non term detected in method generateNgramsUpto");
                System.out.println("textFragment was: " + word.getString());
                continue;
            }
            Term term = (Term) word;

            //1- add the word itself
            NGram newNgramOneTerm = new NGram();
            newNgramOneTerm.getTerms().add(term);
            newNgramOneTerm.setTypeOfTextFragment(TypeOfTextFragment.TypeOfTextFragmentEnum.NGRAM);
            newNgramOneTerm.setIndexCardinal(term.getIndexCardinal());
            textFragmentsAugmentedWithNGrams.add(newNgramOneTerm);

            //2- open a new NGram
            ngram = new NGram();
            ngram.setTypeOfTextFragment(TypeOfTextFragment.TypeOfTextFragmentEnum.NGRAM);
            ngram.getTerms().add(term);
            ngramSize = 1;

            // call to 'previous()' to stay on the same term on the next iteration forward
            it.previous();

            //2- insert prevs of the word and add those too
            while (it.hasPrevious() && ngramSize < maxGramSize) {
                ngram.getTerms().add(0, (Term) it.previous());
                NGram newNgram = new NGram();
                newNgram.getTerms().addAll(ngram.getTerms());
                newNgram.setTypeOfTextFragment(TypeOfTextFragment.TypeOfTextFragmentEnum.NGRAM);
                newNgram.setIndexCardinal(newNgram.getTerms().get(0).getIndexCardinal());
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
