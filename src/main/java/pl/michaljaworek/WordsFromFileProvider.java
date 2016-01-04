package pl.michaljaworek;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.IOUtils.readLines;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class WordsFromFileProvider {

	private static final String DEFAULT_FILE_WITH_EASY_WORDS = "/easyWords.txt";
	private static final String OPTION_NAME = "knownWordsFile";
	private String filePath = "";

	@Autowired
	public WordsFromFileProvider(ApplicationArguments arguments) {
		if (!arguments.containsOption("dontSkipEasyWords")) {
			if (arguments.containsOption(OPTION_NAME)) {
				this.filePath = arguments.getOptionValues(OPTION_NAME).get(0);
			} else {
				this.filePath = DEFAULT_FILE_WITH_EASY_WORDS;
			}
		}
	}

	public List<String> getWords() throws Exception {
		if (isEmpty(this.filePath)) {
			return emptyList();
		}

		return readLines(//
				WordsFromFileProvider.class.getResourceAsStream(filePath))//
						.stream()//
						.map(String::trim)//
						.collect(toList());
	}

}
