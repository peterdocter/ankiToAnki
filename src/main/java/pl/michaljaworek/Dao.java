package pl.michaljaworek;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class Dao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	DeckJsonAnalizer deckJsonAnalizer;

	public Dao() {

	}

	public List<String> getListOfWords(String languageName) {
		String deckId = findIdOfDeckWithName(languageName);
		List<String> noteIds = findAllNotesRelatedToDeckId(deckId);
		return noteIds.stream()//
				.map(this::findFirstWordForNoteWithId)//
				.collect(toList());
	}

	private String findFirstWordForNoteWithId(String noteId) {

		return jdbcTemplate.query("SELECT DISTINCT sfld FROM notes where id = " + noteId, //
				firstColumn()).get(0);
	}

	private List<String> findAllNotesRelatedToDeckId(String deckId) {
		return jdbcTemplate.query("SELECT DISTINCT nid FROM cards where did = " + deckId, //
				firstColumn());

	}

	private RowMapper<String> firstColumn() {
		return (rs, rowNum) -> rs.getString(1);
	}

	private String findIdOfDeckWithName(String deckName) {
		String jsonWithDecks = jdbcTemplate
				.query("SELECT decks FROM col", //
						firstColumn()) //
				.get(0);
		return deckJsonAnalizer.findIdForNameInJson(jsonWithDecks, deckName);
	}

}
