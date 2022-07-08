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
import static net.clementlevallois.umigon.model.TypeOfTextFragment.TypeOfTextFragmentEnum.TEXTO_SPEAK;
import net.clementlevallois.umigon.tokenizer.controller.UmigonTokenizer;

/**
 *
 * @author LEVALLOIS
 */
public class FragmentSelectorForNGramOps {

    public static void main(String args[]) throws IOException {
        String example = "Je vais super bien :-), vraiment vous êtes des champions (même toi!)";
        Set<String> languageSpecificLexicon = new HashSet();
        List<TextFragment> allTextFragments = UmigonTokenizer.tokenize(example, languageSpecificLexicon);
        List<SentenceLike> sentenceLikeFragments = new FragmentSelectorForNGramOps().returnSentenceLikeFragmentsWithTermsOnly(allTextFragments);
        for (SentenceLike sentenceLike : sentenceLikeFragments) {
            for (TextFragment textFragment : sentenceLike.getNgrams()) {
                System.out.print(textFragment.getString());
                System.out.print(" ");
            }
            System.out.println("");
        }
    }

    public List<SentenceLike> returnSentenceLikeFragmentsWithTermsOnly(List<TextFragment> textFragments) {
        List<SentenceLike> listOfSentenceLikeFragments = new ArrayList();
        List<NGram> listOfNGrams = new ArrayList();
        Iterator<TextFragment> it = textFragments.iterator();
        SentenceLike sentenceLike = new SentenceLike();
        sentenceLike.setIndexCardinal(0);
        sentenceLike.setIndexOrdinal(0);

        while (it.hasNext()) {
            TextFragment nextTextFragment = it.next();
            TypeOfTextFragment.TypeOfTextFragmentEnum typeOfTextFragment = nextTextFragment.getTypeOfTextFragment();
//            if (typeOfTextFragment == null){
//                System.out.println("stop");
//            }
            switch (typeOfTextFragment) {
                case TERM:
                    if (sentenceLike.getNgrams().isEmpty()) {
                        sentenceLike.setIndexCardinal(nextTextFragment.getIndexCardinal());
                    }
                    nextTextFragment.setIndexOrdinal(listOfNGrams.size());
                    Term term = (Term) nextTextFragment;
                    term.setTypeOfTextFragment(TypeOfTextFragment.TypeOfTextFragmentEnum.TERM);
                    NGram ngram = new NGram();
                    ngram.setTypeOfTextFragment(TypeOfTextFragment.TypeOfTextFragmentEnum.NGRAM);
                    ngram.setIndexCardinal(term.getIndexCardinal());
                    ngram.setIndexOrdinal(term.getIndexOrdinal());
                    ngram.setIndexOrdinalInSentence(term.getIndexOrdinalInSentence());
                    ngram.setIndexCardinalInSentence(term.getIndexCardinalInSentence());
                    ngram.getTerms().add(term);
                    listOfNGrams.add(ngram);
                    break;
                case WHITE_SPACE, TEXTO_SPEAK, ONOMATOPAE, EMOTICON_IN_ASCII, EMOJI, HASHTAG, TOO_SHORT, QUESTION:
                    // do nothing
                    break;
                case PUNCTUATION:
                    String s = nextTextFragment.getString();
                    if (!sentenceLike.getNgrams().isEmpty() && s.contains(",") || s.contains("(") || s.contains(")") || s.contains("\"") || s.contains("«") || s.contains("»") || s.contains("“") || s.contains("”") || s.contains("„")) {
                        sentenceLike.getNgrams().addAll(listOfNGrams);
                        sentenceLike.setIndexOrdinal(listOfSentenceLikeFragments.size());
                        listOfSentenceLikeFragments.add(sentenceLike);
                        sentenceLike = new SentenceLike();
                        listOfNGrams = new ArrayList();
                    }
            }
        }
        sentenceLike.getNgrams().addAll(listOfNGrams);
        if (!sentenceLike.getNgrams().isEmpty()) {
            listOfSentenceLikeFragments.add(sentenceLike);
        }
        return listOfSentenceLikeFragments;
    }

}
