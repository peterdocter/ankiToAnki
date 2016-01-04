package pl.michaljaworek;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class LongTextSplitterText {

	@Test
	public void shouldSplitSentencesCorrectly() throws Exception {
		String input = "This is sententence 1. This is another!? This is Sentence 2...\tSentence 3.\n\nThis is sentence 4.";
		List<String> output = new LongTextSplitter().splitIntoSentences(input);
		assertEquals(5, output.size());
		assertEquals("This is sententence 1.", output.get(0));
		assertEquals("This is another!?", output.get(1));
		assertEquals("This is Sentence 2...", output.get(2));
		assertEquals("Sentence 3.", output.get(3));
		assertEquals("This is sentence 4.", output.get(4));
	}

}
