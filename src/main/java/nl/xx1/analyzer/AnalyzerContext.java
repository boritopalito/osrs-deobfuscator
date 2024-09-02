package nl.xx1.analyzer;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnalyzerContext {
    private final Map<String, AbstractAnalyzer> analyzers;

    public AnalyzerContext(List<AbstractAnalyzer> analyzerList) {
        this.analyzers =
                analyzerList.stream().collect(Collectors.toMap(a -> a.getClass().getSimpleName(), Function.identity()));
    }

    public AbstractAnalyzer getAnalyzer(String name) {
        return analyzers.get(name);
    }
}
