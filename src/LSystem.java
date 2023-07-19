
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LSystem {
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

        System.out.println(axiom);

        List<String> tokens = Arrays.asList(axiom.split(""));
        tokens.replaceAll((String token) -> {
            String substitute = rule.get(token);
            return substitute != null ? substitute : token;
        });
        axiom = String.join("", tokens);

        return produce(axiom, iterations - 1);
    }

    public static void main(String[] args) {
//        System.out.println(
//            (new LSystem(new String[]{"a", "b"}, new String[]{"a-b", "(a)"}))
//                .produce("a+b", 4)
//        );
        System.out.println(
            (new LSystem(new String[]{"a", "b"}, new String[]{"ab", "a"}))
                .produce("b", 6)
        );
    }
}
