package test;

import easter.egg.FunnyLanguageFacts;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class TestFunnyLanguageFacts extends TestCase {
	
	private FunnyLanguageFacts languageFacts;
	private List<String> allFacts;
	private String languageFact;
	
	protected void setUp() {
		languageFacts = new FunnyLanguageFacts();
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
	
	public void testRandomFactThrowsWhenFactsEmpty() {
	    FunnyLanguageFacts emptyFacts = new FunnyLanguageFacts() {
	        public List<String> allFacts() {
	            return new ArrayList<String>();
	        }
	    };

	    try {
	        emptyFacts.randomFact();
	        fail();
	    } catch (IllegalStateException e) {
	        // expected path
	    }
	}

}
