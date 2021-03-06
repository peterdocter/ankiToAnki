package pl.michaljaworek;

import static java.util.stream.IntStream.range;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class VocabularyPrinter {

	private Map<String, List<String>> vocabularyFromSentences;

	public VocabularyPrinter(Map<String, List<String>> vocabularyFromSentences) {
		this.vocabularyFromSentences = vocabularyFromSentences;

	}

	public String printVocabulary(boolean withExamples) {
		StringBuilder result = new StringBuilder();
		for (Entry<String, List<String>> entry : vocabularyFromSentences.entrySet()) {
			// result.append((i++) + ". " + entry.getKey() + "\n");
			result.append(entry.getKey() + " (");
			result.append(occurences(entry) + " occurences)\n");
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

	private int occurences(Entry<String, List<String>> entry) {
		String word = entry.getKey();
		return entry.getValue().stream()//
				.map(sentence -> StringUtils.countMatches(sentence.toLowerCase(), word.toLowerCase()))//
				.collect(Collectors.summingInt(Integer::valueOf));
	}

}
