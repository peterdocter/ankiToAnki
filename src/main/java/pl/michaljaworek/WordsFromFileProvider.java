package pl.michaljaworek;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.IOUtils.readLines;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class WordsFromFileProvider {

	private static final String DEFAULT_FILE_WITH_EASY_WORDS = "/easyWords.txt";
	private static final String OPTION_NAME = "knownWordsFile";
	private String filePath = "";
	private boolean isFromResources;

	@Autowired
	public WordsFromFileProvider(ApplicationArguments arguments) {
		if (!arguments.containsOption("dontSkipEasyWords")) {
			if (arguments.containsOption(OPTION_NAME)) {
				this.filePath = arguments.getOptionValues(OPTION_NAME).get(0);
				this.isFromResources = false;
			} else {
				this.filePath = DEFAULT_FILE_WITH_EASY_WORDS;
				this.isFromResources = true;
			}
		}
	}

	public List<String> getWords() throws Exception {
		if (isEmpty(this.filePath)) {
			return emptyList();
		}

		List<String> listOfEasyWords = isFromResources ? //
				readLines(WordsFromFileProvider.class.getResourceAsStream(filePath))//
				: //
				FileUtils.readLines(new File(filePath));

		return listOfEasyWords//
				.stream()//
				.map(String::trim)//
				.collect(toList());
	}

}
