import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.hit.lab.WordGraph;
import java.util.*;

public class GraphTest {

    private WordGraph graph;
    private Random random;

    @Before
    public void setUp() {
        graph = new WordGraph();
        random = new Random();
    }



    @Test
    public void testRandomWalkSingleNode() {
        // 测试只有一个节点的图
        graph.adjacencyList.put("A", new ArrayList<>());
        assertEquals("A", graph.randomWalk());
    }

    @Test
    public void testRandomWalkWithNoNeighbors() {
        // 测试所有节点都没有邻居
        graph.adjacencyList.put("A", new ArrayList<>());
        graph.adjacencyList.put("B", new ArrayList<>());
        assertEquals("A", graph.randomWalk()); // 随机选择的节点
    }

    @Test
    public void testRandomWalkLinearGraph() {
        // 测试线性图
        graph.adjacencyList.put("A", Arrays.asList("B"));
        graph.adjacencyList.put("B", Arrays.asList("C"));
        graph.adjacencyList.put("C", new ArrayList<>());
        String result = graph.randomWalk();
        assertTrue(result.equals("A -> B -> C") || result.equals("C"));
    }

    @Test
    public void testRandomWalkCycleGraph() {
        // 测试环状图
        graph.adjacencyList.put("A", Arrays.asList("B"));
        graph.adjacencyList.put("B", Arrays.asList("A", "C"));
        graph.adjacencyList.put("C", Arrays.asList("B"));
        String result = graph.randomWalk();
        assertTrue(result.contains("A -> B") && result.contains("B -> A"));
    }


    // 以下是一个辅助方法，用于添加节点和边到图中
    private void addNodeWithNeighbors(String node, List<String> neighbors) {
        graph.adjacencyList.put(node, new ArrayList<>(neighbors));
        for (String neighbor : neighbors) {
            graph.adjacencyList.computeIfAbsent(neighbor, k -> new ArrayList<>()).add(node);
        }
    }
}

