/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigon.ngram.ops;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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

    public static List<SentenceLike> returnSentenceLikeFragments(List<TextFragment> textFragments) {
        List<SentenceLike> listOfSentenceLikeFragments = new ArrayList();
        List<NGram> listOfNGrams = new ArrayList();
        Iterator<TextFragment> it = textFragments.iterator();
        SentenceLike sentenceLike = new SentenceLike();
        sentenceLike.setIndexCardinal(0);
        sentenceLike.setIndexOrdinal(0);

        while (it.hasNext()) {
            TextFragment nextTextFragment = it.next();
            TypeOfTextFragment.TypeOfTextFragmentEnum typeOfTextFragment = nextTextFragment.getTypeOfTextFragmentEnum();

            if (!typeOfTextFragment.equals(TypeOfTextFragment.TypeOfTextFragmentEnum.PUNCTUATION)) {
                sentenceLike.getTextFragments().add(nextTextFragment);
            }
            switch (typeOfTextFragment) {
                case TERM:
                    if (sentenceLike.getNgrams().isEmpty()) {
                        sentenceLike.setIndexCardinal(nextTextFragment.getIndexCardinal());
                    }
                    nextTextFragment.setIndexOrdinal(listOfNGrams.size());
                    Term term = (Term) nextTextFragment;
                    NGram ngram = new NGram();
                    ngram.setIndexCardinal(term.getIndexCardinal());
                    ngram.setIndexOrdinal(term.getIndexOrdinal());
                    ngram.setIndexOrdinalInSentence(term.getIndexOrdinalInSentence());
                    ngram.setIndexCardinalInSentence(term.getIndexCardinalInSentence());
                    ngram.getTerms().add(term);
                    StringBuilder sb = new StringBuilder();
                    for (Term termLoop : ngram.getTerms()) {
                        sb.append(termLoop.getOriginalForm());
                        sb.append(" ");
                    }
                    ngram.setOriginalForm(sb.toString().trim());
                    listOfNGrams.add(ngram);
                    break;
                case PUNCTUATION:
                    String s = nextTextFragment.getOriginalForm();
                    if (s.contains(".") || s.contains(",") || s.contains(")") || s.contains("\"") || s.contains("»") || s.contains("”") || s.contains("•")) {
                        sentenceLike.getTextFragments().add(nextTextFragment);
                        sentenceLike.getNgrams().addAll(listOfNGrams);
                        sentenceLike.setIndexOrdinal(listOfSentenceLikeFragments.size());
                        listOfSentenceLikeFragments.add(sentenceLike);
                        sentenceLike = new SentenceLike();
                        listOfNGrams = new ArrayList();
                    } else if (s.contains("(") || s.contains("«") || s.contains("“") || s.contains("„")) {
                        sentenceLike.getNgrams().addAll(listOfNGrams);
                        sentenceLike.setIndexOrdinal(listOfSentenceLikeFragments.size());
                        if (!sentenceLike.getTextFragments().isEmpty()) {
                            listOfSentenceLikeFragments.add(sentenceLike);
                        }
                        sentenceLike = new SentenceLike();
                        sentenceLike.getTextFragments().add(nextTextFragment);
                        listOfNGrams = new ArrayList();
                    } else {
                        sentenceLike.getTextFragments().add(nextTextFragment);
                    }
                    break;
            }
        }
        sentenceLike.getNgrams().addAll(listOfNGrams);
        if (!sentenceLike.getTextFragments().isEmpty()) {
            listOfSentenceLikeFragments.add(sentenceLike);
        }
        return listOfSentenceLikeFragments;
    }
}
