package nl.xx1.hooks;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nl.xx1.analyzer.AbstractAnalyzer;

public class Hooks {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<AbstractAnalyzer> analyzers;

    public Hooks(List<AbstractAnalyzer> analyzers) {
        this.analyzers = analyzers;
    }

    public void write(final String fileName) {
        final List<HookedClass> hookedClasses = new ArrayList<>();
        for (AbstractAnalyzer analyzer : analyzers) {
            hookedClasses.add(HookedClass.of(analyzer));
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
            prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

            objectMapper.writer(prettyPrinter).writeValue(new File("./hooks/" + fileName), hookedClasses);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
