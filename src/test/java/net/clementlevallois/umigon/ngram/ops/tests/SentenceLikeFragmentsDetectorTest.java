package net.clementlevallois.umigon.ngram.ops.tests;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.clementlevallois.umigon.model.SentenceLike;
import net.clementlevallois.umigon.model.TextFragment;
import net.clementlevallois.umigon.ngram.ops.SentenceLikeFragmentsDetector;
import net.clementlevallois.umigon.tokenizer.controller.UmigonTokenizer;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

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
        String example4 = " \"We take seriously the government's responsibility -- yes. We welcome it.\" ";
        String example5 = " \"We take seriously the government's responsibility, really";
        String example6 = "\"We take 'seriously' the government's responsibility\"";
        List<String> examples = List.of(example1, example2, example3, example4, example5, example6);
        int counterExamples = 1;
        for (String example : examples) {
            List<SentenceLike> operations = operations(example);
            StringBuilder sb;
            
            System.out.println("counter of examples: "+ counterExamples);

            if (counterExamples == 1) {
                int i = 0;
                for (SentenceLike sentenceLike : operations) {
                    sb = new StringBuilder();
                    for (TextFragment textFragment : sentenceLike.getTextFragments()) {
                        sb.append(textFragment.getOriginalForm());
                    }
                    String result = sb.toString();
                    if (i == 0) {
                        assertThat(result).isEqualTo("Je vais super bien :-)");
                    }
                    if (i == 1) {
                        assertThat(result).isEqualTo(", vraiment vous êtes des champions ");
                    }
                    if (i == 2) {
                        assertThat(result).isEqualTo("(même toi!)");
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
                        assertThat(result).isEqualTo("(j'adore le chocolat)");
                    }
                    if (i == 1) {
                        assertThat(result).isEqualTo(",wow");
                    }
                    if (i == 2) {
                        assertThat(result).isEqualTo(".");
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
                        assertThat(result).isEqualTo("c'est ");
                    }
                    if (i == 1) {
                        assertThat(result).isEqualTo("‘Beautiful ,And Sad At The Same Time’");
                    }
                    if (i == 2) {
                        assertThat(result).isEqualTo(" comme qui dirait");
                    }
                    i++;
                }
            }

            if (counterExamples == 4) {
                int i = 0;
                for (SentenceLike sentenceLike : operations) {
                    sb = new StringBuilder();
                    for (TextFragment textFragment : sentenceLike.getTextFragments()) {
                        sb.append(textFragment.getOriginalForm());
                    }
                    String result = sb.toString();
                    System.out.println("fragment " + i + ": " + result);
                    if (i == 0) {
                        assertThat(result).isEqualTo(" ");
                    }
                    if (i == 1) {
                        assertThat(result).isEqualTo(example4.trim());
                    }
                    if (i == 2) {
                        assertThat(result).isEqualTo(" ");
                    }
                    i++;
                }
            }
            if (counterExamples == 5) {
                int i = 0;
                for (SentenceLike sentenceLike : operations) {
                    sb = new StringBuilder();
                    for (TextFragment textFragment : sentenceLike.getTextFragments()) {
                        sb.append(textFragment.getOriginalForm());
                    }
                    String result = sb.toString();
                    System.out.println("fragment " + i + ": " + result);
                    if (i == 0) {
                        assertThat(result).isEqualTo(" ");
                    }
                    if (i == 1) {
                        assertThat(result).isEqualTo(example5.trim());
                    }
                    i++;
                }
            }
            if (counterExamples == 6) {
                int i = 0;
                for (SentenceLike sentenceLike : operations) {
                    sb = new StringBuilder();
                    for (TextFragment textFragment : sentenceLike.getTextFragments()) {
                        sb.append(textFragment.getOriginalForm());
                    }
                    String result = sb.toString();
                    System.out.println("fragment " + i + ": " + result);
                    if (i == 0) {
                        assertThat(result).isEqualTo(example6);
                    }
                    i++;
                }
            }
            counterExamples++;
        }
    }

    private List<SentenceLike> operations(String example) {
        Set<String> languageSpecificLexicon = new HashSet();
        List<TextFragment> allTextFragments = UmigonTokenizer.tokenize(example, languageSpecificLexicon);
        SentenceLikeFragmentsDetector sentenceDetector = new SentenceLikeFragmentsDetector();
        List<SentenceLike> sentenceLikeFragments = sentenceDetector.returnSentenceLikeFragments(allTextFragments);
        int i = 0;
        System.out.println("testing: " + example);
        for (SentenceLike fragment : sentenceLikeFragments) {
            System.out.println("fragment " + i++ + ": " + fragment.toString());
        }
        System.out.println("");
        System.out.println("");
        return sentenceLikeFragments;
    }

}
