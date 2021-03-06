import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;

public class ExecGroovyTest {

    @Test
    public void SimpleScriptTest() {
        GroovyShell shell = new GroovyShell();
        String script = "return 'hello world'";

        Object result = shell.evaluate(script);
        assertEquals(result, "hello world");
    }

    @Test
    public void ScriptWithBindingTest() {
        Binding binding = new Binding();
        binding.setVariable("a", 1);
        binding.setVariable("b", 2);
        GroovyShell shell = new GroovyShell(binding);

        String script = "return a + b";

        Object result = shell.evaluate(script);
        assertEquals(result, 3);
    }

    @Test
    public void LoadFromFileTest() throws IOException {
        GroovyShell shell = new GroovyShell();

        URL url = Thread.currentThread().getContextClassLoader().getResource("simple.groovy");
        final File file = new File(url.getPath());

        Script script = shell.parse(file);
        Object result = script.run();
        assertEquals(result, "hello world");
    }

    @Test(expected = groovy.lang.MissingPropertyException.class)
    public void ExceptionTest() {
        GroovyShell shell = new GroovyShell();
        String script = "return a";

        shell.evaluate(script);
    }

    @Test
    public void ExecuteFromJsonTest() throws IOException {
        Binding binding = new Binding();
        binding.setVariable("input", 2);
        GroovyShell shell = new GroovyShell(binding);

        URL url = Thread.currentThread().getContextClassLoader().getResource("simpleConfig.json");
        final File file = new File(url.getPath());

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(file, Map.class);

        String script = map.get("script");

        Object result = shell.evaluate(script);
        assertEquals(result, 12);
    }
}
