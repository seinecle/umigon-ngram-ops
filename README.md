# Umigon
A family of modules for essential NLP tasks and sentiment analysis, done well.

# The umigon-ngram-ops
Why another tool for ngrams ?? **Because ngrams are not just a couple of terms separated by whitespaces**

## Installation

```xml
<dependency>
	<groupId>net.clementlevallois.functions</groupId>
	<artifcactId>umigon-ngram-ops</artifactId>
	<version>0.18</version>
</dependency>
```
Or [check on Maven](https://central.sonatype.com/artifact/net.clementlevallois.functions/umigon-ngram-ops) to see the latest version.


* 2023, Oct 10: version 0.18

Fixed an incorrect setting of the index when creating ngrams

* 2023, Oct 10: version 0.17

Improved the sentence detector so that it captures correctly the content in double quotes, parentheses and other forms of apostrophs. Simplified the code of the sentence detector by offloading the code of the main method in separate smaller methods.

* 2023, Aug 25: version 0.16

Updated the dependencies on umigon-model and umigon-tokenizer

* 2023, April 13: version 0.13

Refactored a loop to a parallel stream in the ngram duplicate cleaner

* 2023, April 13: version 0.12

Removes the tokenizer as a dependency bevcause it is needed only for tests. I'll reintroduce it later but with the scope "test", not runtime.

* 2023, March 28: version 0.11

Fixes a critical issue with the dependency on the tokenizer.

* 2023, March 24: version 0.10

Initial release


## Example 

Functions to create ngrams and then to clean them. Explanation:

- ✅ it is easy to create unigrams, bi-grams, tri-grams and 4-grams in a text. An example of a 4-gram is ```United States of America``` (4 terms long)
- 🔴 howevever, such a function will also list the following 3-grams: ```United States of``` and also ```States of America```, which are clearly irrelevant
- ✅ for this reason, this repo includes a function called ```NGramDuplicatesCleaner``` which removes these quasi-duplicates.
- ➕ optionally, the cleaner can take a set of stopwords as a parameter to its constructor, which will help achieve better results.

## How to use it?

### To detect n-grams:
```java
String example = "provides a fine-grained analysis";
Set<String> languageSpecificLexicon = new HashSet();
List<TextFragment> allTextFragments = UmigonTokenizer.tokenize(example, languageSpecificLexicon);
List<SentenceLike> listOfSentenceLike = SentenceLikeFragmentsDetector.returnSentenceLikeFragments(allTextFragments);
int countSentenceLikeFragments = 1;
for (SentenceLike sentenceLikeFragment : listOfSentenceLike) {
	System.out.println("sentence like fragment #" + countSentenceLikeFragments++);
	List<NGram> generateNgramsUpto = NGramFinderBisForTextFragments.generateNgramsUpto(sentenceLikeFragment.getNgrams(), 5);
	for (TextFragment textFragment : generateNgramsUpto) {
		if (textFragment.getOriginalForm().equals("amazing")){
			System.out.println("stop");
		}
		if (textFragment.getTypeOfTextFragmentEnum() == null) {
			System.out.println("stop null fragment type");
		}
		if (textFragment.getTypeOfTextFragmentEnum().equals(TypeOfTextFragment.TypeOfTextFragmentEnum.TERM)) {
			System.out.println(textFragment.getOriginalForm());
		}
		if (textFragment.getTypeOfTextFragmentEnum().equals(TypeOfTextFragment.TypeOfTextFragmentEnum.NGRAM)) {
			NGram ngram = (NGram) textFragment;
			List<Term> terms = ngram.getTerms();
			for (Term term : terms) {
				System.out.print(term.getOriginalForm());
				System.out.print(' ');
			}
			System.out.println("");
		}
	}
}
```
### To clean n-grams (see above)

```java
NGramDuplicatesCleaner cleaner = new NGramDuplicatesCleaner();
Map<String, Integer> cleanedNGrams = cleaner.removeDuplicates(ngrams, 4,true);
```
- The result is a map where they keys are the cleaned n-grams, and the values are the counts for cleaned n-grams.
- The parameter ```4``` means that the nGram cleaner will work on 2, 3 and 4 grams.
- The parameter ```true``` means that ngrams appearing just once will be removed.


### Origin
This function is developed by Clement Levallois, in support of academic work published [in various places](https://scholar.google.fr/citations?user=r0R0vekAAAAJ&hl=en). It is now used in support of [a web app providing free text analysis for non coders](https://nocodefunctions.com).

### Contributions
Your contributions are very welcome.

### License
Creative Commons Attribution 4.0 International Public License
