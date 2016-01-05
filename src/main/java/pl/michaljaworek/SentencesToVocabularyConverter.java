package pl.michaljaworek;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentencesToVocabularyConverter {

	private List<String> wordsToBeSkipped;

	public SentencesToVocabularyConverter() {
		this(emptyList());
	}

	public SentencesToVocabularyConverter(List<String> wordsToBeSkipped) {
		this.wordsToBeSkipped = wordsToBeSkipped//
				.stream()//
				.map(String::toLowerCase)//
				.collect(toList());

	}

	public Map<String, List<String>> convertSentencesToVocabulary(List<String> sentences) {
		Map<String, List<String>> map = new HashMap<>();
		for (String sentence : sentences) {
			String withoutIntepuncion = sentence.replaceAll("[^a-zA-z \\-’']", "");
			String[] words = withoutIntepuncion.split(" ");
			for (String word : words) {
				if (isNoneEmpty(word) //
						&& //
						!wordsToBeSkipped.contains(word.toLowerCase())) {
					String key = word.toLowerCase();
					List<String> list = map.get(key);
					if (list == null) {
						list = new ArrayList<>();
						map.put(key, list);
					}
					list.add(sentence);
				}
			}
		}
		return map;
	}

}
