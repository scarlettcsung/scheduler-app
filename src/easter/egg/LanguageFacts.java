package easter.egg;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LanguageFacts {
	
	public List<String> allFacts() {
		List<String> facts = Arrays.asList(
				"""
				Fun Chinese Language Fact: A character pronounced "biang", known for its use in a famous Shaanxi noodle dish, has 58 strokes.
				It is so complicated that it cannot be typed on a standard digital keyboard.
				One may remember its stroke order by using a specific rhythmic poem.
				""",
				"""
				Fun Turkish Language Fact: Çekoslovakyalılaştıramadıklarımızdan mısınız is a 43-letter word that means 
				'Are you one of those whom we could not make into a Czechoslovakian?'.
				""",
				"""
				Fun Turkish Language Fact: Muvaffakiyetsizleştiricileştiriveremeyebileceklerimizdenmişsinizcesine is a 70-latter word that means
				'As if you were one of those we might not be able to make unsuccessful'.
				""");	
		return facts;
		}
	
	public String randomFact() {
		List<String> facts = allFacts();
		int randomIndex = ThreadLocalRandom.current().nextInt(facts.size());
		return facts.get(randomIndex);
	}

}
