import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.hit.lab.Main.readFileContent;
import static org.junit.Assert.*;
import com.hit.lab.WordGraph;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GenerateNewTextTest {

    private WordGraph wordGraph;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    String filePath = "C:\\Users\\Eden\\Desktop\\Lab1-main\\lab1\\src\\test.txt";

    @Before
    public void setUp() throws Exception {
        wordGraph = new WordGraph();
        // 构建图的代码，这里使用了一个示例文本
        //String text = "apple banana banana cherry apple dog cat cat dog";
        String text = readFileContent(filePath);
        //wordGraph.buildGraph(text);
        wordGraph.buildGraph(text);
    }

    @Test
    public void testGenerateNewText_NormalCase() {
        String inputText = "apple dog";
        String expectedBridgeWord = "banana"; // 假设已知桥接词
        String newText = wordGraph.generateNewText(inputText);
        assertTrue("New text should contain the original words and a bridge word.",
                newText.contains("apple") && newText.contains("dog") && newText.contains(expectedBridgeWord));
    }

    @Test
    public void testGenerateNewText_EmptyInputText() {
        String inputText = "";
        String newText = wordGraph.generateNewText(inputText);
        assertEquals("Should return an empty string for empty input text.", "", newText);
    }

    @Test
    public void testGenerateNewText_NonTextInput() {
        String inputText = "123!@#";
        String newText = wordGraph.generateNewText(inputText);
        assertEquals("Should return an error message for non-text input.",
                "Error: Input text contains non-text characters or is not properly formatted.", newText);
    }

    @Test
    public void testGenerateNewText_InvalidGraph() {
        // 清空邻接表模拟无效图
        wordGraph.getAdjacencyList().clear();
        String inputText = "apple dog";
        String newText = wordGraph.generateNewText(inputText);
        assertEquals("Should return an error message for invalid graph structure.",
                "Error: Graph structure is invalid.", newText);
    }

    @Test
    public void testGenerateNewText_Boundary_LongText() {
        StringBuilder longInputText = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longInputText.append("apple banana cherry dog cat ");
        }
        String newText = wordGraph.generateNewText(longInputText.toString());
        assertFalse("Should handle long text without errors.", newText.isEmpty());
    }

    @Test
    public void testGenerateNewText_Boundary_ShortText() {
        String inputText = "apple";
        String newText = wordGraph.generateNewText(inputText);
        assertEquals("Should return the same word for single word input.", "apple", newText);
    }

    @Test
    public void testGenerateNewText_SpecialCharacters() {
        String inputText = "apple, banana! cherry?";
        String newText = wordGraph.generateNewText(inputText);
        assertTrue("Should handle special characters correctly.",
                newText.contains("apple") && newText.contains("banana") && newText.contains("cherry"));
    }

    @Test
    public void testGenerateNewText_MultiLanguageInput() {
        // 假设wordGraph支持中文
        String inputText = "苹果 香蕉";
        String newText = wordGraph.generateNewText(inputText);
        assertTrue("Should support multi-language input.",
                newText.contains("苹果") && newText.contains("香蕉"));
    }

    @Test
    public void testGenerateNewText_ConcurrentCalls() throws InterruptedException {
        executorService.submit(() -> {
            String inputText = "apple dog";
            wordGraph.generateNewText(inputText);
        });
        executorService.submit(() -> {
            String inputText = "cat dog";
            wordGraph.generateNewText(inputText);
        });
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

    @Test
    public void testGenerateNewText_ExceptionHandling() {
        // 这里需要一个方法来触发异常，例如通过修改wordGraph的内部状态
        // 然后调用generateNewText方法
    }

    // 添加更多的测试用例...

    @After
    public void tearDown() {
        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }
}