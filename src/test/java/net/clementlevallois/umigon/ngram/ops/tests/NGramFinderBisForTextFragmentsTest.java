/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package net.clementlevallois.umigon.ngram.ops.tests;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.SentenceLike;
import net.clementlevallois.umigon.model.TextFragment;
import net.clementlevallois.umigon.ngram.ops.NGramFinderBisForTextFragments;
import net.clementlevallois.umigon.ngram.ops.SentenceLikeFragmentsDetector;
import net.clementlevallois.umigon.tokenizer.controller.UmigonTokenizer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author LEVALLOIS
 */
public class NGramFinderBisForTextFragmentsTest {

    public NGramFinderBisForTextFragmentsTest() {
    }

    /**
     * Test of generateNgramsUpto method, of class
     * NGramFinderBisForTextFragments.
     */
    @Test
    public void testGenerateNgramsUpto() throws IOException {
        String example = "This sentence is hard)(";

        Set<String> languageSpecificLexicon = new HashSet();
        List<TextFragment> allTextFragments = UmigonTokenizer.tokenize(example, languageSpecificLexicon);
        List<SentenceLike> listOfSentenceLike = SentenceLikeFragmentsDetector.returnSentenceLikeFragments(allTextFragments);

        assertEquals("This", listOfSentenceLike.get(0).getTextFragments().get(0).getOriginalForm());
        assertEquals("(", listOfSentenceLike.get(1).getTextFragments().get(0).getOriginalForm());

        List<NGram> generateNgramsUpto = NGramFinderBisForTextFragments.generateNgramsUpto(listOfSentenceLike.get(0).getNgrams(), 5);
        assertEquals("This", generateNgramsUpto.get(0).getOriginalForm());

    }

}
