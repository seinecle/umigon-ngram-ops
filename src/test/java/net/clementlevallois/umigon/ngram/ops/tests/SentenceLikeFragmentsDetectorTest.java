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
        List<SentenceLike> operations = operations(example1);
        StringBuilder sb;
        int i = 0;
        for (SentenceLike sentenceLike : operations) {
            sb = new StringBuilder();
            for (TextFragment textFragment : sentenceLike.getTextFragments()) {
                sb.append(textFragment.getOriginalForm());
            }
            String result = sb.toString();
            if (i == 0) {
                assertEquals(result, "Je vais super bien :-),");
            }
            if (i == 1) {
                assertEquals(result, " vraiment vous êtes des champions ");
            }
            if (i == 2) {
                assertEquals(result, "(même toi!)");
            }
            i++;
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
