### What?
Functions to create ngrams __and then to clean them__. Explanation:

- it is easy to create unigrams, bi-grams, tri-grams and 4-grams in a text. An example of a 4-gram is ```United States of America``` (4 terms long)
- howevever, such a function will also list the following 3-grams: ```United States of``` and also ```States of America```, which are clearly irrelevant
- for this reason, this repo includes a function called ```NGramDuplicatesCleaner``` which removes these quasi-duplicates.
- optionally, the cleaner can take a set of stopwords as a parameter to its constructor, which will help achieve better results.

### How to use it?

#### To detect n-grams:
```java
List<String> input = new ArrayList();
input.add("This is my first sentence");
input.add("This is my second sentence");
NGramFinder ngramFinder = new NGramFinder(input);
Multiset<String> ngrams = ngramFinder.run(4, false);
```

- The result is __not__ a Guava Multiset but a convenience Collection method doing the same thing (in a very light way).
- The parameter ```4``` means that the nGram detector will identify 2, 3 and 4 grams.
- The parameter ```false``` means that _for a given input String_, an ngram will be counted just once (even if it appears several times within this piece of text).

#### To clean n-grams (see above)
```java
NGramDuplicatesCleaner cleaner = new NGramDuplicatesCleaner("en");
Multiset<String> cleanedNGrams = cleaner.removeDuplicates(ngrams, 4,true);
```
- The result is __not__ a Guava Multiset but a convenience Collection method doing the same thing (in a very light way).
- The parameter ```4``` means that the nGram cleaner will work on 2, 3 and 4 grams.
- The parameter ```true``` means that ngrams appearing just once will be removed.

### Dependencies
None

### Origin?
This function is developed by Clement Levallois, in support of academic work published [in various places](https://scholar.google.fr/citations?user=r0R0vekAAAAJ&hl=en). It is now used in support of [a web app providing free text analysis for non coders](https://nocodefunctions.com).

### Contributions
Your contributions are very welcome.

### License
Apache v2
