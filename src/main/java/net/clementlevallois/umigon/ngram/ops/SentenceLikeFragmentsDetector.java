/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigon.ngram.ops;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.SentenceLike;
import net.clementlevallois.umigon.model.Term;
import net.clementlevallois.umigon.model.TextFragment;
import net.clementlevallois.umigon.model.TypeOfTextFragment;

/**
 *
 * @author LEVALLOIS
 */
public class SentenceLikeFragmentsDetector {

    private final static Set<String> stopPunctuations = Set.of(".", ":", ";", ",", "(", ")", "\"", "«", "»", "“", "”", "•", "‘", "’", "'", "„", "[", "]");

    public static List<SentenceLike> returnSentenceLikeFragments(List<TextFragment> textFragments) {
        List<SentenceLike> listOfSentenceLikeFragments = new ArrayList();
        List<NGram> listOfNGrams = new ArrayList();
        SentenceLike sentenceLike = new SentenceLike();
        sentenceLike.setIndexCardinal(0);
        sentenceLike.setIndexOrdinal(0);

        Iterator<TextFragment> it = textFragments.iterator();
        while (it.hasNext()) {
            TextFragment nextTextFragment = it.next();
            TypeOfTextFragment.TypeOfTextFragmentEnum typeOfTextFragment = nextTextFragment.getTypeOfTextFragmentEnum();
            if (sentenceLike.getTextFragments().isEmpty()) {
                sentenceLike.setIndexCardinal(nextTextFragment.getIndexCardinal());
            }
            nextTextFragment.setIndexOrdinalInSentence(sentenceLike.getTextFragments().size());
            sentenceLike.getTextFragments().add(nextTextFragment);

            switch (typeOfTextFragment) {
                case TERM:
                    Term term = (Term) nextTextFragment;
                    term.setIndexOrdinalInSentence(listOfNGrams.size());
                    NGram ngram = new NGram();
                    ngram.setIndexCardinal(term.getIndexCardinal());
                    ngram.setIndexOrdinal(term.getIndexOrdinal());
                    ngram.setIndexOrdinalInSentence(term.getIndexOrdinalInSentence());
                    ngram.getTerms().add(term);
                    ngram.setOriginalForm(term.getOriginalForm());
                    listOfNGrams.add(ngram);
                    break;
                case PUNCTUATION:
                    String punctuationSign = nextTextFragment.getOriginalForm();
                    if (stopPunctuations.contains(punctuationSign)) {
                        sentenceLike.getNgrams().addAll(listOfNGrams);
                        if (!sentenceLike.getTextFragments().isEmpty()) {
                            listOfSentenceLikeFragments.add(sentenceLike);
                        }
                        sentenceLike = new SentenceLike();
                        sentenceLike.setIndexOrdinal(listOfSentenceLikeFragments.size());
                        listOfNGrams = new ArrayList();
                    }
                    break;
            }
        }
        sentenceLike.getNgrams().addAll(listOfNGrams);
        sentenceLike.setIndexOrdinal(listOfSentenceLikeFragments.size());
        if (!sentenceLike.getTextFragments().isEmpty()) {
            listOfSentenceLikeFragments.add(sentenceLike);
        }
        return listOfSentenceLikeFragments;
    }
}
