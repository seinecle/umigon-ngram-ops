/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigon.ngram.ops;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.SentenceLike;
import net.clementlevallois.umigon.model.Term;
import net.clementlevallois.umigon.model.TextFragment;
import net.clementlevallois.umigon.model.TypeOfTextFragment.TypeOfTextFragmentEnum;

/**
 *
 * @author LEVALLOIS
 */
public class SentenceLikeFragmentsDetector {

    private final Set<String> stopPunctuations = Set.of(".", ":", ";", ",", "(", ")", "\"", "«", "»", "“", "”", "•", "‘", "’", "'", "„", "[", "]", "", "<", ">");
    private final Map<String, String> matchingPunctuations = Map.of("(", ")", "\"", "\"", "«", "»", "“", "”", "‘", "’", "'", "'", "„", "“", "[", "]", "<", ">");
    List<SentenceLike> listOfSentenceLikeFragments = new ArrayList();
    List<NGram> listOfNGrams = new ArrayList();
    SentenceLike sentenceLike = new SentenceLike();
    boolean textFagmentAlreadyAddedToASentence;
    boolean openingPunctuationIdentified = false;
    String expectedClosingPunctuationSign = "";

    public List<SentenceLike> returnSentenceLikeFragments(List<TextFragment> textFragments) {
        sentenceLike.setIndexCardinal(0);
        sentenceLike.setIndexOrdinal(0);

        Iterator<TextFragment> it = textFragments.iterator();
        while (it.hasNext()) {
            textFagmentAlreadyAddedToASentence = false;
            TextFragment nextTextFragment = it.next();
            TypeOfTextFragmentEnum typeOfTextFragment = nextTextFragment.getTypeOfTextFragmentEnum();

            switch (typeOfTextFragment) {
                case TERM:
                    addTermToNGramsOfCurrentSentence(nextTextFragment);
                    break;
                case PUNCTUATION:
                    String punctuationSign = nextTextFragment.getOriginalForm();
                    if (matchingPunctuations.containsKey(punctuationSign) && !openingPunctuationIdentified) {
                        openingPunctuationIdentified = true;
                        expectedClosingPunctuationSign = matchingPunctuations.get(punctuationSign);
                        Boolean isEndOfClosingSigns = false;
                        closeCurrentSentenceAndOpenNewOne(isEndOfClosingSigns);
                        addTextFragmentToCurrentSentence(nextTextFragment);
                        break;
                    }
                    if (stopPunctuations.contains(punctuationSign)) {
                        if (openingPunctuationIdentified) {
                            addTextFragmentToCurrentSentence(nextTextFragment);
                            if (punctuationSign.equals(expectedClosingPunctuationSign)) {
                                Boolean isEndOfClosingSigns = true;
                                closeCurrentSentenceAndOpenNewOne(isEndOfClosingSigns);
                            }
                        } else {
                            Boolean isEndOfClosingSigns = false;
                            closeCurrentSentenceAndOpenNewOne(isEndOfClosingSigns);
                        }
                    }
                    break;
            }

            addTextFragmentToCurrentSentence(nextTextFragment);
        }
        Boolean isEndOfClosingSigns = false;
        closeCurrentSentenceAndOpenNewOne(isEndOfClosingSigns);
        return listOfSentenceLikeFragments;
    }

    private void closeCurrentSentenceAndOpenNewOne(boolean isEndOfMatchingSigns) {
        sentenceLike.getNgrams().addAll(listOfNGrams);
        if (!sentenceLike.getTextFragments().isEmpty()) {
            listOfSentenceLikeFragments.add(sentenceLike);
        }
        sentenceLike = new SentenceLike();
        sentenceLike.setIndexOrdinal(listOfSentenceLikeFragments.size());
        listOfNGrams = new ArrayList();
        if (isEndOfMatchingSigns) {
            openingPunctuationIdentified = false;
            expectedClosingPunctuationSign = "";
        }
    }

    private void addTextFragmentToCurrentSentence(TextFragment tf) {
        if (textFagmentAlreadyAddedToASentence) {
            return;
        }
        if (sentenceLike.getTextFragments().isEmpty()) {
            sentenceLike.setIndexCardinal(tf.getIndexCardinal());
        }
        tf.setIndexOrdinalInSentence(sentenceLike.getTextFragments().size());
        sentenceLike.getTextFragments().add(tf);
        textFagmentAlreadyAddedToASentence = true;
    }

    private void addTermToNGramsOfCurrentSentence(TextFragment nextTextFragment) {
        Term term = (Term) nextTextFragment;
        NGram ngram = new NGram();
        ngram.setIndexCardinal(term.getIndexCardinal());
        ngram.setIndexOrdinal(term.getIndexOrdinal());
        ngram.setIndexOrdinalInSentence(term.getIndexOrdinalInSentence());
        ngram.getTerms().add(term);
        ngram.setOriginalForm(term.getOriginalForm());
        listOfNGrams.add(ngram);
    }
}
