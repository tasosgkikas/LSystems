package base;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LSystem {
    Map<Character, String> rule;

    public LSystem(char[] variables, String[] mappings) {
        if (variables.length != mappings.length)
            throw new IllegalArgumentException("Argument arrays must have the same length.");

        rule = new HashMap<>();
        for (int i = 0; i < variables.length; i++)
            rule.put(variables[i], mappings[i]);
    }

    String produce(String axiom, int iterations) {
        for (int i = 0; i < iterations; i++)
            axiom = axiom.chars()
                    .mapToObj(c -> (char) c)
                    .map(c -> rule.getOrDefault(c, String.valueOf(c)))
                    .collect(Collectors.joining());
        return axiom;
    }
}
