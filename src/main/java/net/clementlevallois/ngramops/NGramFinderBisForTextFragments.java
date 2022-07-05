package net.clementlevallois.ngramops;

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
import net.clementlevallois.umigon.model.TextFragment;
import net.clementlevallois.umigon.model.TypeOfTextFragment;
import net.clementlevallois.umigon.tokenizer.controller.UmigonTokenizer;

/**
 *
 * @author LEVALLOIS copied from https://stackoverflow.com/a/13977401/798502
 */
public class NGramFinderBisForTextFragments {

    public static void main(String[] args) throws IOException {
        String example = "J'aime, vraiment vous êtes des champions (même toi!) http://momo";
        UmigonTokenizer tokenizer = new UmigonTokenizer();
        List<TextFragment> allTextFragments = tokenizer.tokenize(example);
        List<TextFragment> allTextFragmentsAugmentedWithNGrams = new ArrayList();
        List<List<TextFragment>> listOfTextFragments = new FragmentSelectorForNGramOps().doTheSelection(allTextFragments);
        for (List<TextFragment> listOfFragments : listOfTextFragments) {
            List<TextFragment> generateNgramsUpto = new NGramFinderBisForTextFragments().generateNgramsUpto(listOfFragments, 5);
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

    public List<TextFragment> generateNgramsUpto(List<TextFragment> textFragments, int maxGramSize) {

        List<TextFragment> textFragmentsAugmentedWithNGrams = new ArrayList();
        int ngramSize;
        NGram ngram;

        ListIterator<TextFragment> it = textFragments.listIterator();
        while (it.hasNext()) {
            TextFragment word = it.next();

            //1- add the word itself
            textFragmentsAugmentedWithNGrams.add(word);

            //2- open a new NGram
            ngram = new NGram();
            ngram.setTypeOfTextFragment(TypeOfTextFragment.TypeOfTextFragmentEnum.NGRAM);
            ngram.getTerms().add((Term) word);
            ngramSize = 1;

            // call to 'previous()' to stay on the same term on the next iteration forward
            it.previous();

            //2- insert prevs of the word and add those too
            while (it.hasPrevious() && ngramSize < maxGramSize) {
                ngram.getTerms().add(0, (Term) it.previous());
                NGram newNgram = new NGram();
                newNgram.getTerms().addAll(ngram.getTerms());
                newNgram.setTypeOfTextFragment(TypeOfTextFragment.TypeOfTextFragmentEnum.NGRAM);
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
