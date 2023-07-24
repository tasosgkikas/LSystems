import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LSystem {
    Map<String, String> rule;

    LSystem(String[] variables, String[] mappings) {
        if (variables.length != mappings.length)
            throw new IllegalArgumentException("Argument arrays must have the same length.");

        rule = new HashMap<>();
        for (int i = 0; i < variables.length; i++)
            rule.put(variables[i], mappings[i]);
    }

    String produce(String axiom, int iterations) {
        if (iterations == 0) return axiom;

        List<String> tokens = Arrays.asList(axiom.split(""));
        tokens.replaceAll((String token) -> rule.getOrDefault(token, token));
        axiom = String.join("", tokens);

        return produce(axiom, iterations - 1);
    }

    @Override
    public String toString() {
        return "LSystem{" +
                "rule=" + rule +
                '}';
    }

    public static void main(String[] args) {
        // dragon curve
        LSystem lSystem = new LSystem(
                new String[]{"F", "G"},
                new String[]{"F+G", "F-G"}
        );
        System.out.println(lSystem);

        String product = lSystem.produce("F", 3);
        System.out.println(product);
    }
}
