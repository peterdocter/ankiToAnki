package pl.michaljaworek;

import static java.util.stream.IntStream.range;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class VocabularyPrinter {

	private Map<String, List<String>> vocabularyFromSentences;

	public VocabularyPrinter(Map<String, List<String>> vocabularyFromSentences) {
		this.vocabularyFromSentences = vocabularyFromSentences;

	}

	public String printVocabulary(boolean withExamples) {
		StringBuilder result = new StringBuilder();
		int i = 1;
		for (Entry<String, List<String>> entry : vocabularyFromSentences.entrySet()) {
			// result.append((i++) + ". " + entry.getKey() + "\n");
			result.append(entry.getKey() + "\n");
			if (withExamples) {
				List<String> sentences = entry.getValue();
				range(0, sentences.size())//
						.forEach(sentenceId -> result
								.append("\t" + (sentenceId + 1) + ". " + sentences.get(sentenceId) + "\n"));
			}
		}

		vocabularyFromSentences.entrySet()//
				.stream()//
				.forEach(entry -> {

				});
		return result.toString();
	}

}
