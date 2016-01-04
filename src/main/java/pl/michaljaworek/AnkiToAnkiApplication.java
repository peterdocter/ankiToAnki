package pl.michaljaworek;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AnkiToAnkiApplication {

	/**
	 * 
	 * @param args
	 *            --limit=30 (default 30)
	 * 
	 *            --inputText=path/to/file/in/foreign/language.txt (required, no
	 *            default)
	 * 
	 *            --showStatistics - display statistics related to book
	 * 
	 *            --databasePath - default
	 *            "/home/mj/Documents/Anki/Użytkownik 1/collection.anki2"
	 * 
	 *            --deckName - default "LWT - English"
	 * 
	 *            --dontSkipEasyWords
	 * 
	 *            --easyWordsFile - by default words provided by author
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Object[] configutationClasses = { AnkiToAnkiApplication.class, DataSourceConfiguration.class };
		ApplicationContext context = SpringApplication.run(configutationClasses, args);

		String fullText = FileUtils.readFileToString(
				new File("/media/mj/daneWin/Dokumenty/Języki/Angielski/Grisham - A time to kill.txt"));

		List<String> sentences = new LongTextSplitter()//
				.splitIntoSentences(fullText);

		ListOfKnowsWordsProvider knownWordsProvider = context.getBean(ListOfKnowsWordsProvider.class);

		List<String> listOfKnowsWords = knownWordsProvider.getKnownWords();
		Map<String, List<String>> vocabularyFromSentences = new SentencesToVocabularyConverter(listOfKnowsWords)
				.convertSentencesToVocabulary(sentences);

		System.out.println("Most popular words which you don't know in this book are:");

		System.out.println(//
				new VocabularyPrinter(vocabularyFromSentences)//
						.printVocabulary(false));

	}

}
