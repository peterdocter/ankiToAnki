package pl.michaljaworek;

import static java.util.Comparator.comparing;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

public class VocabularyPrinter {

	private Map<String, List<String>> vocabularyFromSentences;

	public VocabularyPrinter(Map<String, List<String>> vocabularyFromSentences) {
		this.vocabularyFromSentences = vocabularyFromSentences;

	}

	public String printVocabulary(boolean withExamples) {
		StringBuilder result = new StringBuilder();
		vocabularyFromSentences.entrySet()//
				.stream()//
				.sorted(comparing(numberOfSentences()).reversed())//
				.limit(10)//
				.forEach(entry -> {
					result.append(entry.getKey() + "\n");
					if (withExamples) {
						entry.getValue()//
								.forEach(sentence -> result.append("\t" + sentence + "\n"));
					}
				});
		return result.toString();
	}

	private static Function<? super Entry<String, List<String>>, ? extends Integer> numberOfSentences() {
		return entry -> entry.getValue().size();
	}

}
