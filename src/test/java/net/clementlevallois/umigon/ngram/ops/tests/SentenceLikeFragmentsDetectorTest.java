package net.clementlevallois.umigon.ngram.ops.tests;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.clementlevallois.umigon.model.SentenceLike;
import net.clementlevallois.umigon.model.TextFragment;
import static net.clementlevallois.umigon.ngram.ops.SentenceLikeFragmentsDetector.returnSentenceLikeFragments;
import net.clementlevallois.umigon.tokenizer.controller.UmigonTokenizer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author LEVALLOIS
 */
public class SentenceLikeFragmentsDetectorTest {

    public SentenceLikeFragmentsDetectorTest() {
    }

    /**
     * Test of returnSentenceLikeFragments method, of class
     * SentenceLikeFragmentsDetector.
     */
    @Test
    public void testOnMultipleSentences() {
        String example1 = "Je vais super bien :-), vraiment vous êtes des champions (même toi!)";
        String example2 = "(j'adore le chocolat),wow.";
        String example3 = "c'est ‘Beautiful ,And Sad At The Same Time’ comme qui dirait";
        List<String> examples = List.of(example1, example2, example3);
        int counterExamples = 1;
        for (String example : examples) {
            List<SentenceLike> operations = operations(example);
            StringBuilder sb;

            if (counterExamples == 1) {
                int i = 0;
                for (SentenceLike sentenceLike : operations) {
                    sb = new StringBuilder();
                    for (TextFragment textFragment : sentenceLike.getTextFragments()) {
                        sb.append(textFragment.getOriginalForm());
                    }
                    String result = sb.toString();
                    if (i == 0) {
                        assertEquals("Je vais super bien :-),", result);
                    }
                    if (i == 1) {
                        assertEquals(" vraiment vous êtes des champions (", result);
                    }
                    if (i == 2) {
                        assertEquals("même toi!)", result);
                    }
                    i++;
                }
            }
            if (counterExamples == 2) {
                int i = 0;
                for (SentenceLike sentenceLike : operations) {
                    sb = new StringBuilder();
                    for (TextFragment textFragment : sentenceLike.getTextFragments()) {
                        sb.append(textFragment.getOriginalForm());
                    }
                    String result = sb.toString();
                    if (i == 0) {
                        assertEquals("(", result);
                    }
                    if (i == 1) {
                        assertEquals("j'adore le chocolat)", result);
                    }
                    if (i == 2) {
                        assertEquals(",", result);
                    }
                    if (i == 3) {
                        assertEquals("wow.", result);
                    }
                    i++;
                }
            }
            if (counterExamples == 3) {
                int i = 0;
                for (SentenceLike sentenceLike : operations) {
                    sb = new StringBuilder();
                    for (TextFragment textFragment : sentenceLike.getTextFragments()) {
                        sb.append(textFragment.getOriginalForm());
                    }
                    String result = sb.toString();
                    System.out.println("fragment " + i + ": " + result);
                    if (i == 0) {
                        assertEquals("c'est ‘", result);
                    }
                    if (i == 1) {
                        assertEquals("Beautiful ,", result);
                    }
                    if (i == 2) {
                        assertEquals("And Sad At The Same Time’", result);
                    }
                    if (i == 3) {
                        assertEquals(" comme qui dirait", result);
                    }
                    i++;
                }
            }
            counterExamples++;
        }
    }

    private List<SentenceLike> operations(String example) {
        try {
            Set<String> languageSpecificLexicon = new HashSet();
            List<TextFragment> allTextFragments = UmigonTokenizer.tokenize(example, languageSpecificLexicon);
            List<SentenceLike> sentenceLikeFragments = returnSentenceLikeFragments(allTextFragments);
            return sentenceLikeFragments;
        } catch (IOException ex) {
            System.out.println("error in test");
            return null;
        }
    }

}
