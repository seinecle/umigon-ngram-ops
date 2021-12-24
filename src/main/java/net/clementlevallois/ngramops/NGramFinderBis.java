package net.clementlevallois.ngramops;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 *
 * @author LEVALLOIS
 * copied from https://stackoverflow.com/a/13977401/798502
 */
public class NGramFinderBis {
    
    
    
    public Map<String, Integer> generateNgramsUpto(String str, int maxGramSize) {

    List<String> sentence = Arrays.asList(str.split("[\\W+]"));

    Multiset<String> ngrams = new Multiset();
    int ngramSize = 0;
    StringBuilder sb = null;

    //sentence becomes ngrams
    for (ListIterator<String> it = sentence.listIterator(); it.hasNext();) {
        String word = it.next();

        //1- add the word itself
        sb = new StringBuilder(word);
        ngrams.addOne(word);
        ngramSize=1;
        it.previous();

        //2- insert prevs of the word and add those too
        while(it.hasPrevious() && ngramSize<maxGramSize){
            sb.insert(0,' ');
            sb.insert(0,it.previous());
            ngrams.addOne(sb.toString());
            ngramSize++;
        }

        //go back to initial position
        while(ngramSize>0){
            ngramSize--;
            it.next();
        }                   
    }
    
       
    return ngrams.getInternalMap();
}
    
}
