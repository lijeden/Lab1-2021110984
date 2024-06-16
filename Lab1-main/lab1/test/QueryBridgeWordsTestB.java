import static com.hit.lab.Main.readFileContent;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.hit.lab.WordGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class QueryBridgeWordsTestB {

    private WordGraph wordGraph;
    private static final String filePath = "C:\\Users\\Eden\\Desktop\\Lab1-main\\lab1\\src\\test.txt";

    @Before
    public void setUp() throws IOException {
        wordGraph = new WordGraph();
        String processedContent = readFileContent(filePath);
        wordGraph.buildGraph(processedContent);
        //String processedContent = processFileContent(TEST_TXT_PATH);
        //wordGraph.buildGraph(processedContent);
    }



    @Test
    //两个单词之间存在一个桥接词
    public void testQueryBridgeWords_ExistingOneBridgeWords() {
        String word1 = "moon";
        String word2 = "shine";
        String expectedOutput = "The bridge words from moon to shine are: they";
        String result = wordGraph.queryBridgeWords(word1, word2);
        assertTrue("Expected bridge words not found.", result.contains("bridge"));
        assertEquals("Unexpected bridge words found.", expectedOutput, result);
    }

    @Test
    //两个单词之间存在多个桥接词
    public void testQueryBridgeWords_ExistingMultipleBridgeWords() {
        String word1 = "the";
        String word2 = "and";
        String expectedOutput = "The bridge words from the to and are: sun, star";
        String result = wordGraph.queryBridgeWords(word1, word2);
        assertTrue("Expected bridge words not found.", result.contains("bridge"));
        assertEquals("Unexpected bridge words found.", expectedOutput, result);
    }

    @Test
    //其中一个单词不存在于图中
    public void testQueryBridgeWords_OneWordNotInGraph() {
        String word1 = "happy";
        String word2 = "sun";
        String expectedOutput = "No happy in the graph!";
        String result = wordGraph.queryBridgeWords(word1, word2);
        assertEquals("Unexpected result when one word is not in the graph.", expectedOutput, result);
    }

    //两个单词都不在图中
    @Test
    public void testQueryBridgeWords_BothWordsNotInGraph() {
        String word1 = "happy";
        String word2 = "unhappy";
        String expectedOutput = "Neither happy nor unhappy is in the graph!";
        String result = wordGraph.queryBridgeWords(word1, word2);
        assertEquals("Unexpected result when both words are not in the graph.", expectedOutput, result);
    }

    //没有桥接词
    @Test
    public void testQueryBridgeWords_NoBridgeWords() {
        String word1 = "moon";
        String word2 = "sun";
        String expectedOutput = "No bridge words from moon to sun!";
        String result = wordGraph.queryBridgeWords(word1, word2);
        assertEquals("Expected no bridge words message not received.", expectedOutput, result);
    }

    //一些特殊符号
    @Test
    public void testQueryBridgeWords_SpecialCharacters() {
        String word1 = "the@";
        String word2 = "#sun!";
        String expectedOutput = "Neither the@ nor #sun! is in the graph!"; // Assuming special characters are not allowed
        String result = wordGraph.queryBridgeWords(word1, word2);
        assertEquals("Special characters not handled as expected.", expectedOutput, result);
    }

    //输入的是空
    @Test
    public void testQueryBridgeWords_EmptyInput() {
        String word1 = "";
        String word2 = "sun";
        String expectedOutput = "No  in the graph!"; // Assuming empty strings are not allowed
        String result = wordGraph.queryBridgeWords(word1, word2);
        assertEquals("Empty input not handled as expected.", expectedOutput, result);
    }

    //...
}