package pl.michaljaworek;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AnkiToAnkiApplication {

	private static final long DEFAULT_LIMIT = 20L;

	/**
	 * 
	 * @param args
	 *            --limit=30 (default 30)
	 * 
	 *            arg[0] path/to/file/in/foreign/language.txt (required, no
	 *            default)
	 * 
	 *            --dontShowExamples - do not print example sencentes
	 * 
	 *            --dontCreateAnkiFile - do not create anki file
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

		ApplicationArguments arguments = context.getBean(ApplicationArguments.class);
		if (argumentsAreNotCorrect(arguments) //
				|| arguments.containsOption("help")) {
			printHelp();
		} else {
			out.println("Let me think...");

			String filePath = arguments.getNonOptionArgs().get(0);
			System.out.println(filePath);
			File inputFile = new File(filePath);
			String fullText = FileUtils.readFileToString(inputFile);

			List<String> sentences = new LongTextSplitter()//
					.splitIntoSentences(fullText);

			ListOfKnowsWordsProvider knownWordsProvider = context.getBean(ListOfKnowsWordsProvider.class);

			List<String> listOfKnowsWords = knownWordsProvider.getKnownWords();

			Map<String, List<String>> vocabularyFromSentences = new SentencesToVocabularyConverter(listOfKnowsWords)
					.convertSentencesToVocabulary(sentences);

			out.println("Text: " + inputFile.getName());

			out.println(format("This book has %d unique words.", vocabularyFromSentences.size()));
			out.println(format("You know %d of them which is %.2f%%. (including easy words)", //
					listOfKnowsWords.size(), //
					100.0f * listOfKnowsWords.size() / vocabularyFromSentences.size()));

			long numberOfNotFullyUnderstanbableSentences = vocabularyFromSentences//
					.values()//
					.stream()//
					.flatMap(Collection::stream)//
					.distinct()//
					.count();

			long numberOfAllSentences = sentences.stream()//
					.distinct()//
					.count();

			long numberOfFullyUnderstanbableSentences = numberOfAllSentences - numberOfNotFullyUnderstanbableSentences;
			out.println(format("It gives you %.2f%% fully understandabe unique sensentences.",
					100.0f * numberOfFullyUnderstanbableSentences / numberOfAllSentences));

			long limit = Optional.ofNullable(arguments.getOptionValues("limit"))//
					.filter(CollectionUtils::isNotEmpty)//
					.map(list -> list.get(0))//
					.map(Long::parseLong)//
					.orElse(DEFAULT_LIMIT);

			Map<String, List<String>> limitedWords = vocabularyFromSentences.entrySet()//
					.stream()//
					.sorted(comparing(numberOfSentences()).reversed())//
					.limit(limit)//
					.collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));

			out.println("\nMost popular words which you don't know in this book are:");

			out.println(//
					new VocabularyPrinter(limitedWords)//
							.printVocabulary(false));

			if (!arguments.containsOption("dontCreateAnkiFile")) {
				String fileName = "newAnkis.txt";
				new AnkiFileCreator(limitedWords)//
						.saveAnkiToDisc(fileName);

				out.println(format("\n I have created file named %s with examples ready to import into anki. \n",
						fileName));
			}
			if (!arguments.containsOption("dontShowExamples")) {
				out.println("Below you can review sentences for these words.");
				out.println(//
						new VocabularyPrinter(limitedWords)//
								.printVocabulary(true));
			}
		}

	}

	private static boolean argumentsAreNotCorrect(ApplicationArguments arguments) {

		return isEmpty(arguments.getNonOptionArgs());
	}

	private static void printHelp() {
		out.println(
				"Correct invocation: ./ankiToAnki fileInForeignLanguage.txt [--limit=LIMIT] [--dontShowExamples] [--dontCreateAnkiFile]");
	}

	private static Function<Entry<String, List<String>>, Integer> numberOfSentences() {
		return entry -> entry.getValue().size();
	}

}
