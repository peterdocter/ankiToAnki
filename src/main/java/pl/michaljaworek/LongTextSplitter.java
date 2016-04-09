package pl.michaljaworek;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.List;

public class LongTextSplitter {
	private static final String DUMMY_CHAR = "#";

	private String addDummySign(String input) {
		return input.replaceAll("([.!?]+) ", "$1" + DUMMY_CHAR);
	}

	private String removeQuotations(String input) {
		return input.replaceAll("[\"”“]", "");
	}

	private String removeUnnesesaryWhiteChars(String input) {
		return input.replaceAll("[ \\t\\n\\r]{1,}", " ");
	}

	public List<String> splitIntoSentences(String fullText) {
		String textWithoutUnnesesaryWhiteChars = removeUnnesesaryWhiteChars(fullText);
		String withoutQuotations = removeQuotations(textWithoutUnnesesaryWhiteChars);
		String withDummyChar = addDummySign(withoutQuotations);
		return asList(//
				withDummyChar.split(DUMMY_CHAR))//
						.stream()//
						.map(String::trim)//
						.collect(toList());
	}

}
