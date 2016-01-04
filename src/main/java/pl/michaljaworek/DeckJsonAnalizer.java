package pl.michaljaworek;

import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class DeckJsonAnalizer {

	public DeckJsonAnalizer() {

	}

	public String findIdForNameInJson(String json, String deckName) {
		JsonObject root = new JsonParser().parse(json).getAsJsonObject();
		return root.entrySet()//
				.stream()//
				.filter(entryWithMatchingName(deckName))//
				.map(toId())//
				.findAny().orElseThrow(() -> new RuntimeException("There is no deck with name " + deckName));

	}

	private Function<Entry<String, JsonElement>, String> toId() {
		return entry -> entry.getKey();
	}

	private Predicate<Entry<String, JsonElement>> entryWithMatchingName(String deckName) {
		return entry -> entry.getValue().getAsJsonObject().get("name").getAsString().equals(deckName);
	}

}
