package nl.xx1.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnalyzerContext {
    private final Map<String, AbstractAnalyzer> analyzers;
    private final HashMap<String, Integer> multipliers;

    public AnalyzerContext(List<AbstractAnalyzer> analyzerList, HashMap<String, Integer> multipliers) {
        this.analyzers =
                analyzerList.stream().collect(Collectors.toMap(a -> a.getClass().getSimpleName(), Function.identity()));
        this.multipliers = multipliers;
    }

    public AbstractAnalyzer getAnalyzer(String name) {
        return analyzers.get(name);
    }

    public int getMultiplier(String name) {
        Integer multiplier = this.multipliers.get(name);

        if (multiplier == null) {
            return 1;
        }
        return multiplier;
    }
}
