package nl.xx1.analyzer;

import java.util.*;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class AnalyzerSorter {

    public List<AbstractAnalyzer> getSortedAnalyzers(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> analyzerClasses = reflections.getTypesAnnotatedWith(Analyzer.class);

        Map<String, AbstractAnalyzer> analyzerMap = new HashMap<>();
        Map<String, Set<String>> graph = new HashMap<>();

        // Create analyzers and build the dependency graph
        for (Class<?> clazz : analyzerClasses) {
            try {
                Analyzer annotation = clazz.getAnnotation(Analyzer.class);
                String name = annotation.name();
                AbstractAnalyzer analyzer =
                        (AbstractAnalyzer) clazz.getDeclaredConstructor().newInstance();
                analyzerMap.put(name, analyzer);
                graph.put(name, new HashSet<>(Arrays.asList(annotation.runBefore())));
                for (String afterName : annotation.runAfter()) {
                    graph.computeIfAbsent(afterName, k -> new HashSet<>()).add(name);
                }
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to instantiate analyzer: " + clazz.getName(), e);
            }
        }

        // Perform topological sort
        List<String> sortedNames = topologicalSort(graph);

        // Create the final list of sorted analyzers
        return sortedNames.stream()
                .map(analyzerMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<String> topologicalSort(Map<String, Set<String>> graph) {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> tempMark = new HashSet<>();

        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                visit(node, graph, visited, tempMark, result);
            }
        }

        Collections.reverse(result);
        return result;
    }

    private void visit(
            String node,
            Map<String, Set<String>> graph,
            Set<String> visited,
            Set<String> tempMark,
            List<String> result) {
        if (tempMark.contains(node)) {
            throw new RuntimeException("Circular dependency detected involving node: " + node);
        }
        if (!visited.contains(node)) {
            tempMark.add(node);
            for (String neighbor : graph.getOrDefault(node, Collections.emptySet())) {
                visit(neighbor, graph, visited, tempMark, result);
            }
            visited.add(node);
            tempMark.remove(node);
            result.add(node);
        }
    }
}
