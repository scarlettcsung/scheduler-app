package easter.egg;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LanguageFacts {

    private static final List<String> FACTS = List.of(
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
            Fun Turkish Language Fact: Muvaffakiyetsizleştiricileştiriveremeyebileceklerimizdenmişsinizcesine is a 70-letter word that means
            'As if you were one of those we might not be able to make unsuccessful'.
            """);
    private static final int FACT_COUNT = FACTS.size();

    public List<String> allFacts() {
        return FACTS;
    }

    public String randomFact() {
        if (FACT_COUNT == 0) {
            throw new IllegalStateException("No language facts available");
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(FACT_COUNT);
        return FACTS.get(randomIndex);
    }

}
