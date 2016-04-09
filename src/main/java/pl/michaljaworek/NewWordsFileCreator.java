package pl.michaljaworek;

import static java.util.Comparator.comparing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NewWordsFileCreator {

	private static final String CASE_INSITIVE_INDICATOR = "(?i)";
	private static final int OPTIMUM_NUMBER_OF_WORDS = 5;
	private static final Object SEPARATOR = "\t";
	private Map<String, List<String>> wordsWithExamples;

	public NewWordsFileCreator(Map<String, List<String>> wordsWithExamples) {
		this.wordsWithExamples = wordsWithExamples;
	}

	public void saveAnkiToDisc(String outputFileName, String inputFilename) throws Exception {
		saveToDiscUsingSingleWordTextCreator(outputFileName, inputFilename, this::buildAnkiString);
	}

	public void saveLwtToDisc(String outputFileName, String inputFilename) throws Exception {
		saveToDiscUsingSingleWordTextCreator(outputFileName, inputFilename, this::buildLwtString);
	}

	private void saveToDiscUsingSingleWordTextCreator(String outputFileName, String inputFilename,
			SingleWordTextCreator singleWordCreator) throws FileNotFoundException {
		String filenameWithoutExtention = inputFilename.split("[.]")[0];
		PrintWriter writer = new PrintWriter(new File(outputFileName));
		for (Entry<String, List<String>> entry : this.wordsWithExamples.entrySet()) {
			String word = entry.getKey();
			String example = findBestExample(entry.getValue());
			String singleLine = singleWordCreator.buildSingleWordText(word, example, filenameWithoutExtention);
			writer.println(singleLine);
		}
		writer.close();
	}

	private String findBestExample(List<String> value) {
		return value.stream()//
				.sorted(comparing(this::howFarFromOprimumNumbersOfWords))//
				.findFirst()//
				.get();

	}

	private int howFarFromOprimumNumbersOfWords(String string) {
		return Math.abs(OPTIMUM_NUMBER_OF_WORDS - numberOfWords(string));
	}

	private int numberOfWords(String string) {
		return string.split(" ").length + 1;
	}

	interface SingleWordTextCreator {
		String buildSingleWordText(String word, String example, String textTitle);
	}

	private String buildLwtString(String word, String example, String textTitle) {
		return example;
	}

	private String buildAnkiString(String word, String example, String textTitle) {
		StringBuilder result = new StringBuilder();
		result.append(word);
		result.append(SEPARATOR);
		result.append("TRANSLATE_ME");
		result.append(SEPARATOR);
		result.append(SEPARATOR); // because lwt syntax expects romanization
		String styleExample = styled(word, example);
		result.append(masked(word, styleExample));
		result.append(SEPARATOR);
		result.append(styleExample);
		result.append(SEPARATOR);
		result.append(textTitle);
		return result.toString();
	}

	private String styled(String word, String example) {
		try {
			return example.replaceAll(CASE_INSITIVE_INDICATOR + "(" + word + ")",
					"<span style=\"font-weight:600; color:#0000ff;\">$1</span>");
		} catch (Throwable t) {
			System.out.println("Error for:" + word + " " + example);
			return null;
		}
	}

	private String masked(String word, String example) {
		StringBuilder dots = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			dots.append("•");
		}
		return example.replaceAll(word, dots.toString());
	}

}
