package com.hit.lab;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;


/**
 * @author Eden
 */
@SuppressWarnings({"checkstyle:Indentation", "checkstyle:SummaryJavadoc"})
public class WordGraph {
    @SuppressWarnings("checkstyle:Indentation")
    private Map<String, List<String>> adjacencyList;
    @SuppressWarnings("checkstyle:Indentation")
    private Map<String, Integer> edgeWeights;

    @SuppressWarnings("checkstyle:Indentation")
    public WordGraph() {
        this.adjacencyList = new HashMap<>();
        this.edgeWeights = new HashMap<>();
    }
    //根据文本构建有向图的邻接表和边的权重信息
    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:EmptyLineSeparator", "checkstyle:MissingJavadocMethod"})
    public void buildGraph(String text) {
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i].toLowerCase();
            String word2 = words[i + 1].toLowerCase();
            if (!adjacencyList.containsKey(word1)) {
                adjacencyList.put(word1, new ArrayList<>());
            }
            adjacencyList.get(word1).add(word2);

            String edge = word1 + " -> " + word2;
            edgeWeights.put(edge, edgeWeights.getOrDefault(edge, 0) + 1);
        }
        String lastWord = words[words.length - 1].toLowerCase();
        if (!adjacencyList.containsKey(lastWord)) {
            adjacencyList.put(lastWord, new ArrayList<>());
        }
    }

    @SuppressWarnings("checkstyle:Indentation")
    public Map<String, List<String>> getAdjacencyList() {
        return adjacencyList;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP")
    @SuppressWarnings("checkstyle:Indentation")
    public Map<String, Integer> getEdgeWeights() {
        return edgeWeights;
    }
    //查询从 word1 到 word2 的桥接词
    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:LineLength", "checkstyle:CommentsIndentation", "checkstyle:EmptyLineSeparator", "checkstyle:MissingJavadocMethod"})
    public String queryBridgeWords(String word1, String word2) {

//        if (!adjacencyList.containsKey(word1) || !adjacencyList.containsKey(word2)) {
//            return "No " + (adjacencyList.containsKey(word1) ? "word2" : "word1") + " in the graph!";
//        }
        boolean word1Exists = adjacencyList.containsKey(word1);
        boolean word2Exists = adjacencyList.containsKey(word2);

        if (!word1Exists || !word2Exists) {
            if (!word1Exists && !word2Exists) {
                return "Neither " + word1 + " nor " + word2 + " is in the graph!";
            } else if (!word1Exists) {
                return "No " + word1 + " in the graph!";
            } else {
                return "No " + word2 + " in the graph!";
            }
        }

        List<String> bridgeWords = new ArrayList<>();
        for (String neighbor : adjacencyList.get(word1)) {
            if (adjacencyList.get(neighbor).contains(word2)) {
                bridgeWords.add(neighbor);
            }
        }

        if (bridgeWords.isEmpty()) {
            return "No bridge words from " + word1 + " to " + word2 + "!";
        } else {
            StringBuilder result = new StringBuilder("The bridge words from " + word1 + " to " + word2 + " are: ");
            for (int i = 0; i < bridgeWords.size(); i++) {
                result.append(bridgeWords.get(i));
                if (i < bridgeWords.size() - 1) {
                    result.append(", ");
                }
            }
            return result.toString();
        }
    }
    //生成一个新的文本，其中桥接了输入文本中相邻的单词
    @SuppressFBWarnings({"PREDICTABLE_RANDOM", "DMI_RANDOM_USED_ONLY_ONCE"})
    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:LineLength", "checkstyle:EmptyLineSeparator", "checkstyle:MissingJavadocMethod"})
    public String generateNewText(String inputText) {

        String[] words = inputText.split("\\s+");

        List<String> newTextWords = new ArrayList<>();

        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];

            newTextWords.add(word1);

            String bridgeWords = queryBridgeWords(word1, word2);
            // 如果存在桥接词
            if (!bridgeWords.startsWith("No")) {
                // 获取桥接词部分
                int startIndex = ("The bridge words from " + word1 + " to " + word2 + " are: ").length();

                String bridgeWordsPart = bridgeWords.substring(startIndex);
                // 将桥接词部分分割为列表
                List<String> bridgeWordList = Arrays.asList(bridgeWordsPart.split(", "));
                // 随机选择一个桥接词
                Random random = new Random();
                String bridgeWord = bridgeWordList.get(random.nextInt(bridgeWordList.size()));

                newTextWords.add(bridgeWord);
            }
        }

        newTextWords.add(words[words.length - 1]);
        // 将新文本单词列表连接为一个字符串
        return String.join(" ", newTextWords);
    }

    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:LineLength", "checkstyle:VariableDeclarationUsageDistance", "checkstyle:MissingJavadocMethod"})
    public String calcShortestPath(String word1, String word2) {
        // Check if word1 and word2 are in the graph
        if (!adjacencyList.containsKey(word1) || !adjacencyList.containsKey(word2)) {
            return "No " + (adjacencyList.containsKey(word1) ? "word2" : "word1") + " in the graph!";
        }

        // Initialize a queue for breadth-first search
        Queue<String> queue = new LinkedList<>();
        // Initialize a map to track visited nodes
        Map<String, Boolean> visited = new HashMap<>();
        // Initialize a map to track the shortest path length to each node
        Map<String, Integer> shortestPathLength = new HashMap<>();
        // Initialize a map to track the predecessors for each node in the shortest path
        Map<String, String> predecessors = new HashMap<>();

        // Initialize the shortest path length for all nodes to be infinity
        for (String node : adjacencyList.keySet()) {
            shortestPathLength.put(node, Integer.MAX_VALUE);
            visited.put(node, false);
        }

        // Start from word1
        queue.offer(word1);
        visited.put(word1, true);
        shortestPathLength.put(word1, 0);

        // 执行广度优先搜索
        while (!queue.isEmpty()) {
            String currentWord = queue.poll();
            List<String> neighbors = adjacencyList.get(currentWord);
            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    if (!visited.getOrDefault(neighbor, false)) {
                        visited.put(neighbor, true);
                        int weight = edgeWeights.get(currentWord + " -> " + neighbor);
                        shortestPathLength.put(neighbor, shortestPathLength.get(currentWord) + weight);
                        predecessors.put(neighbor, currentWord);
                        queue.offer(neighbor);
                    }
                }
            }
        }

        // Retrieve the shortest paths from word1 to word2
        List<List<String>> shortestPaths = new ArrayList<>();
        int shortestLength = shortestPathLength.get(word2);
        if (shortestLength == Integer.MAX_VALUE) {
            return "No shortest path from " + word1 + " to " + word2 + "!";
        } else {
            List<String> path = new ArrayList<>();
            String currentWord = word2;
            while (currentWord != null) {
                path.add(0, currentWord);
                currentWord = predecessors.get(currentWord);
            }
            shortestPaths.add(path);
        }

        // Output the shortest paths
        StringBuilder result = new StringBuilder();
        result.append("The shortest path(s) from " + word1 + " to " + word2 + " with length " + shortestLength + " are:\n");
        for (List<String> path : shortestPaths) {
            result.append(String.join(" -> ", path)).append("\n");
        }
        return result.toString();
    }

    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:MissingJavadocMethod"})
    public void printShortestDistancesFromWord(String word) {
        // 遍历每个单词，并计算其与给定单词之间的最短距离
        for (String otherWord : adjacencyList.keySet()) {
            // 跳过给定单词本身
            if (!otherWord.equals(word)) {
                // 调用 calcShortestPath 计算最短路径，并打印结果
                String shortestPathResult = calcShortestPath(word, otherWord);
                System.out.println(shortestPathResult);

            }
        }
    }


    @SuppressFBWarnings("PREDICTABLE_RANDOM")
    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:LineLength", "checkstyle:MissingJavadocMethod"})
    public String randomWalk() {
        // Randomly select a starting node
        Random random = new Random();
        List<String> nodes = new ArrayList<>(adjacencyList.keySet());
        String currentNode = nodes.get(random.nextInt(nodes.size()));

        StringBuilder result = new StringBuilder();
        result.append(currentNode).append(" ");

        Set<String> visitedEdges = new HashSet<>();

        while (adjacencyList.containsKey(currentNode) && !adjacencyList.get(currentNode).isEmpty()) {
            // Randomly choose the next node
            List<String> neighbors = adjacencyList.get(currentNode);
            String nextNode = neighbors.get(random.nextInt(neighbors.size()));

            // Check if the edge has been visited before
            String edge = currentNode + " -> " + nextNode;
            if (visitedEdges.contains(edge)) {
                result.append(nextNode).append(" ");
                break;
            }

            // Append the next node to the result
            result.append(nextNode).append(" ");

            // Mark the edge as visited
            visitedEdges.add(edge);

            // Move to the next node
            currentNode = nextNode;

            // Check if the current node has no outgoing edges
            if (!adjacencyList.containsKey(currentNode) || adjacencyList.get(currentNode).isEmpty()) {
                break;
            }
        }

        return result.toString().trim();
    }

    @SuppressFBWarnings({"DM_DEFAULT_ENCODING", "PATH_TRAVERSAL_OUT"})
    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:CommentsIndentation",
            "checkstyle:WhitespaceAfter", "checkstyle:MissingJavadocMethod"})
    public void writeToFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath,true)) {
//            writer.write(content);
            writer.write(content + System.lineSeparator());
            System.out.println("Random walk result has been written to file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing random walk result to file: " + e.getMessage());
        }
    }


}