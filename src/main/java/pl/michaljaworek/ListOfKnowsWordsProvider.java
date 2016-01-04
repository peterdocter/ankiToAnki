package pl.michaljaworek;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class ListOfKnowsWordsProvider {

	private static final String DECK_NAME_ARGUMENT = "deckName";

	private static final String DEFAULT_DECK_NAME = "LWT - English";

	@Autowired
	Dao dao;

	@Autowired
	WordsFromFileProvider wordsFromFileProvider;

	private final String deckName;

	@Autowired
	public ListOfKnowsWordsProvider(ApplicationArguments arguments) {
		if (arguments.containsOption(DECK_NAME_ARGUMENT)) {
			this.deckName = arguments.getOptionValues(DECK_NAME_ARGUMENT).get(0);
		} else {
			this.deckName = DEFAULT_DECK_NAME;
		}
	}

	public List<String> getKnownWords() throws Exception {
		List<String> allKnownWords = new ArrayList<>();

		List<String> wordsKnownFromAnki = dao.getListOfWords(deckName);
		allKnownWords.addAll(wordsKnownFromAnki);

		List<String> wordsKnownFromFile = wordsFromFileProvider.getWords();
		allKnownWords.addAll(wordsKnownFromFile);

		return allKnownWords;
	}

}
