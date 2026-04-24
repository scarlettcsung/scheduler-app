package test;

import easter.egg.LanguageFacts;
import junit.framework.TestCase;
import java.util.List;

public class TestLanguageFacts extends TestCase {
	
	private LanguageFacts languageFacts;
	private List<String> allFacts;
	private String languageFact;
	
	protected void setUp() {
		languageFacts = new LanguageFacts();
		allFacts = languageFacts.allFacts();
		languageFact = languageFacts.randomFact();
	}
	
	public void testFactExists() {assertNotNull(languageFact);}
	public void testFactInAllFacts() {
		assertEquals(3, allFacts.size());
		assertTrue(allFacts.contains(languageFact));
	}
	public void testListIsNotEmpty() {
		assertFalse(allFacts.isEmpty());
		assertNotSame(allFacts, languageFacts.allFacts());
	}

}
