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
        List<List<TextFragment>> listOfTextFragments = new FragmentSelectorForNGramOps().returnListsOfTextFragmentsWithTermsOnly(allTextFragments);
        for (List<TextFragment> textFragments : listOfTextFragments) {
            for (TextFragment textFragment : textFragments) {
                System.out.print(textFragment.getString());
                System.out.print(" ");
            }
            System.out.println("");
        }
    }

    public List<List<TextFragment>> returnListsOfTextFragmentsWithTermsOnly(List<TextFragment> textFragments) {
        List<List<TextFragment>> listOfListsOfTextFragments = new ArrayList();
        List<TextFragment> listOfTextFragments = new ArrayList();
        Iterator<TextFragment> it = textFragments.iterator();
        while (it.hasNext()) {
            TextFragment nextTextFragment = it.next();
            TypeOfTextFragment.TypeOfTextFragmentEnum typeOfTextFragment = nextTextFragment.getTypeOfTextFragment();
//            if (typeOfTextFragment == null){
//                System.out.println("stop");
//            }
            switch (typeOfTextFragment) {
                case TERM:
                    listOfTextFragments.add(nextTextFragment);
                    break;
                case WHITE_SPACE, TEXTO_SPEAK, ONOMATOPAE, EMOTICON_IN_ASCII, EMOJI, HASHTAG, TOO_SHORT, QUESTION:
                    // do nothing
                    break;
                case PUNCTUATION:
                    String s = nextTextFragment.getString();
                    if (s.contains(",") || s.contains("(") || s.contains(")") || s.contains("\"") || s.contains("«") || s.contains("»") || s.contains("“") || s.contains("”") || s.contains("„")) {
                        listOfListsOfTextFragments.add(listOfTextFragments);
                        listOfTextFragments = new ArrayList();
                    }
            }
        }
        if (!listOfTextFragments.isEmpty()) {
            listOfListsOfTextFragments.add(listOfTextFragments);
        }
        return listOfListsOfTextFragments;
    }

}
