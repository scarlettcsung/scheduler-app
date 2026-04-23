package test;

import easter.egg.LanguageFacts;
import junit.framework.TestCase;
import java.util.List;

public class testLanguageFacts extends TestCase {

    private LanguageFacts languageFacts;
    private List<String> allFacts;
    private String languageFact;

    protected void setUp() {
        languageFacts = new LanguageFacts();
        allFacts = languageFacts.allFacts();
        languageFact = languageFacts.randomFact();
    }

    public void testFactExists() {
        assertNotNull(languageFact);
    }

    public void testFactInAllFacts() {
        String randomFact = languageFacts.randomFact();
        List<String> facts = languageFacts.allFacts();

        assertEquals(3, facts.size());
        assertTrue(facts.contains(randomFact));
    }

    public void testListIsNotEmpty() {
        assertFalse(allFacts.isEmpty());
        assertEquals(allFacts, languageFacts.allFacts());
    }

    public void testAllFactsContainsThreeFacts() {
        assertEquals(3, allFacts.size());
    }

    public void testAllFactsReturnsSameFactsAcrossCalls() {
        assertEquals(allFacts, languageFacts.allFacts());
    }

}
