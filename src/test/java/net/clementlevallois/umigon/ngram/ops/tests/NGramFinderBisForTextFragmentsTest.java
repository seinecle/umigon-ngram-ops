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
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

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
    public void testGenerateNgramsUpto() {
        String example = "This sentence is hard)(";

        Set<String> languageSpecificLexicon = new HashSet();
        List<TextFragment> allTextFragments = UmigonTokenizer.tokenize(example, languageSpecificLexicon);
        SentenceLikeFragmentsDetector sentenceDetector = new SentenceLikeFragmentsDetector();

        List<SentenceLike> listOfSentenceLike = sentenceDetector.returnSentenceLikeFragments(allTextFragments);

        assertThat(listOfSentenceLike.get(0).getTextFragments().get(0).getOriginalForm()).isEqualTo("This");
        assertThat(listOfSentenceLike.get(1).getTextFragments().get(0).getOriginalForm()).isEqualTo(")");

        List<NGram> generateNgramsUpto = NGramFinderBisForTextFragments.generateNgramsUpto(listOfSentenceLike.get(0).getNgrams(), 5);
        assertThat(generateNgramsUpto.get(0).getOriginalForm()).isEqualTo("This");

    }

}
