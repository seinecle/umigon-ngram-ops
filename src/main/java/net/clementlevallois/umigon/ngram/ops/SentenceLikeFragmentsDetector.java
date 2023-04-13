/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigon.ngram.ops;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    public static void main(String args[]) throws IOException {
//        String example = "Because of this dual effect, firms are likely to underestimate the returns on service intervention if measured using only voices. Furthermore, we find an “error-correction” effect in certain situations, where customers compliment or complain when others voice the opposite opinions. Finally, we characterize the distinct voicing tendencies in different relationship states";
//        String example = "I love chocolate, because it is so sweet and delicious. Really (I am honest!)";
//        String example = "Je vais super bien :-), vraiment vous êtes des champions (même toi!)";
//        Set<String> languageSpecificLexicon = new HashSet();
//        List<TextFragment> allTextFragments = UmigonTokenizer.tokenize(example, languageSpecificLexicon);
//        List<SentenceLike> sentenceLikeFragments = returnSentenceLikeFragments(allTextFragments);
//        for (SentenceLike sentenceLike : sentenceLikeFragments) {
//            for (TextFragment textFragment : sentenceLike.getNgrams()) {
//                System.out.print(textFragment.getOriginalForm());
//                System.out.print(" ");
//            }
//            System.out.println("");
//        }
    }

    public static List<SentenceLike> returnSentenceLikeFragments(List<TextFragment> textFragments) {
        List<SentenceLike> listOfSentenceLikeFragments = new ArrayList();
        List<NGram> listOfNGrams = new ArrayList();
        Iterator<TextFragment> it = textFragments.iterator();
        SentenceLike sentenceLike = new SentenceLike();
        sentenceLike.setIndexCardinal(0);
        sentenceLike.setIndexOrdinal(0);
        
        while (it.hasNext()) {
            TextFragment nextTextFragment = it.next();
//            if (nextTextFragment.getOriginalForm().equals("it")) {
//                System.out.println("stop");
//            }
            sentenceLike.getTextFragments().add(nextTextFragment);

            TypeOfTextFragment.TypeOfTextFragmentEnum typeOfTextFragment = nextTextFragment.getTypeOfTextFragmentEnum();
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
//                    if (term.getOriginalForm().equals("amazing")){
//                        System.out.println("stop");
//                    }
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
//                    if (s.equals(",")) {
//                        System.out.println("stop");
//                    }
                    if (s.contains(".") || s.contains(",") || s.contains("(") || s.contains(")") || s.contains("\"") || s.contains("«") || s.contains("»") || s.contains("“") || s.contains("”") || s.contains("„") || s.contains("•")) {
                        sentenceLike.getNgrams().addAll(listOfNGrams);
                        sentenceLike.setIndexOrdinal(listOfSentenceLikeFragments.size());
                        listOfSentenceLikeFragments.add(sentenceLike);
                        sentenceLike = new SentenceLike();
                        listOfNGrams = new ArrayList();
                    }
                    break;
            }
        }
        sentenceLike.getNgrams().addAll(listOfNGrams);
        if (!sentenceLike.getNgrams().isEmpty()) {
            listOfSentenceLikeFragments.add(sentenceLike);
        }
        return listOfSentenceLikeFragments;
    }
}
