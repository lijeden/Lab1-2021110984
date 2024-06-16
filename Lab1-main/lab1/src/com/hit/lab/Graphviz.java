package com.hit.lab;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eden
 */
@SuppressWarnings({"checkstyle:Indentation", "checkstyle:LineLength", "checkstyle:MissingJavadocType"})
public class Graphviz {

    @SuppressWarnings("checkstyle:Indentation")
    //临时文件存储的目录路径
    private static final String TEMP_DIR = "C:\\Users\\eden\\Desktop\\软件工程\\lab1\\temp";
    @SuppressWarnings("checkstyle:Indentation")
    //Graphviz 可执行文件的路径
    private static final String DOT_EXECUTABLE_PATH = "C:\\Program Files\\Graphviz\\bin\\dot.exe";
    //从 DOT 源代码生成图形
    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:CommentsIndentation", "checkstyle:EmptyLineSeparator", "checkstyle:MissingJavadocMethod", "checkstyle:LineLength"})
    public static void generateGraph(String dotSource, String outputFilePath, String imageFormat) throws IOException {
        File dotFile = writeDotSourceToFile(dotSource);
        if (dotFile != null) {
//            System.out.println("DOT file generated successfully: " + dotFile.getAbsolutePath());
            convertDotToImage(dotFile, outputFilePath, imageFormat);
//            dotFile.delete(); // Delete the temporary DOT file
        } else {
            System.err.println("Error: Failed to generate DOT file.");
        }
    }
    //将 DOT 源代码写入临时的 DOT 文件
    @SuppressFBWarnings({"DMI_HARDCODED_ABSOLUTE_FILENAME", "DM_DEFAULT_ENCODING"})
    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:CommentsIndentation", "checkstyle:EmptyLineSeparator"})
    private static File writeDotSourceToFile(String dotSource) throws IOException {
//        File tempDotFile = Files.createTempFile(Paths.get(TEMP_DIR), "graph_", ".dot").toFile();
        File tempDotFile = Files.createTempFile(Paths.get(TEMP_DIR), "graph", ".dot").toFile();

        try (FileWriter writer = new FileWriter(tempDotFile)) {
            writer.write(dotSource);
        }
//        System.out.println("DOT file path: " + tempDotFile.getAbsolutePath());
        return tempDotFile;
    }
    //使用 Graphviz 将 DOT 文件转换为图像文件
    @SuppressFBWarnings("COMMAND_INJECTION")
    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:LineLength", "checkstyle:EmptyLineSeparator"})
    private static void convertDotToImage(File dotFile, String outputFilePath, String imageFormat) throws IOException {
        String[] command = {DOT_EXECUTABLE_PATH, "-T" + imageFormat, dotFile.getAbsolutePath(), "-o", outputFilePath};
        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.start();
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error: Failed to convert DOT to image. Exit code: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error: Interrupted while converting DOT to image.");
        }
    }
    //从 WordGraph 对象生成并展示一个有向图
    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:CommentsIndentation", "checkstyle:EmptyLineSeparator", "checkstyle:MissingJavadocMethod"})
    public static void showDirectedGraph(WordGraph wordGraph) {
        Map<String, Integer> edgeWeights = wordGraph.getEdgeWeights();
        String dotSource = generateDotSource(wordGraph.getAdjacencyList(), edgeWeights);

//        System.out.println(dotSource);
        //./picture/graph.png
        String outputFilePath = "C:\\Users\\Eden\\Desktop\\Lab1-main\\lab1\\picture\\graph.png";
        String imageFormat = "png";
        try {
//            System.out.println("Generating graph...");

            Graphviz.generateGraph(dotSource, outputFilePath, imageFormat);
            System.out.println("成功生成有向图: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("生成有向图失败: " + e.getMessage());
        }
    }

    //生成有向图的 DOT 源代码
    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:LineLength"})
    private static String generateDotSource(Map<String, List<String>> adjacencyList, Map<String, Integer> edgeWeights) {
        StringBuilder dotSource = new StringBuilder();
        dotSource.append("digraph G {\n");

        // Add nodes with corresponding labels
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            String node = entry.getKey();
            dotSource.append("\t\"" + node + "\" [label=\"" + node + "\"];\n");
        }

        // Add edges with corresponding weights
        // Keep track of processed edges to avoid duplicates
        Set<String> processedEdges = new HashSet<>();
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            String node = entry.getKey();
            List<String> adjacentNodes = entry.getValue();
            for (String adjNode : adjacentNodes) {
                String edge = node + " -> " + adjNode;
                // Get weight from edgeWeights map
                int weight = edgeWeights.getOrDefault(edge, 1);
                if (!processedEdges.contains(edge)) {
                    dotSource.append("\t\"" + node + "\" -> \"" + adjNode + "\" [label=\"" + weight + "\"];\n");
                    processedEdges.add(edge);
                }
            }
        }

        dotSource.append("}");
        return dotSource.toString();
    }
    //生成带有最短路径高亮的图形
    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:LineLength", "checkstyle:WhitespaceAround", "checkstyle:EmptyLineSeparator", "checkstyle:MissingJavadocMethod"})
    public static void generateGraphWithShortestPath(WordGraph wordGraph, String shortestPath, String outputFileName, String imageFormat) {
        Map<String, Integer> edgeWeights = wordGraph.getEdgeWeights();
        String dotSource = generateDotSourceWithShortestPath(wordGraph.getAdjacencyList(), edgeWeights, shortestPath);
        String outputFilePath = "C:\\Users\\Eden\\Desktop\\Lab1-main\\lab1\\picture\\"+ outputFileName;
        try {
            generateGraph(dotSource, outputFilePath, imageFormat);
            System.out.println("Graph with shortest path generated successfully: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error generating graph with shortest path: " + e.getMessage());
        }
    }
    //生成带有最短路径高亮的有向图的 DOT 源代码
    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:LineLength", "checkstyle:EmptyLineSeparator"})
    private static String generateDotSourceWithShortestPath(Map<String, List<String>> adjacencyList, Map<String, Integer> edgeWeights, String shortestPath) {
        StringBuilder dotSource = new StringBuilder();
        dotSource.append("digraph G {\n");

        // Add nodes with corresponding labels
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            String node = entry.getKey();
            dotSource.append("\t\"" + node + "\" [label=\"" + node + "\"];\n");
        }

        // Add edges with corresponding weights, highlighting the edges in the shortest path
        // Keep track of processed edges to avoid duplicates
        Set<String> processedEdges = new HashSet<>();
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            String node = entry.getKey();
            List<String> adjacentNodes = entry.getValue();
            for (String adjNode : adjacentNodes) {
                String edge = node + " -> " + adjNode;
                // Get weight from edgeWeights map
                int weight = edgeWeights.getOrDefault(edge, 1);
                if (!processedEdges.contains(edge)) {
                    if (shortestPath.contains(edge)) {
                        dotSource.append("\t\"" + node + "\" -> \"" + adjNode + "\" [label=\"" + weight + "\", color=\"red\"];\n");
                    } else {
                        dotSource.append("\t\"" + node + "\" -> \"" + adjNode + "\" [label=\"" + weight + "\"];\n");
                    }
                    processedEdges.add(edge);
                }
            }
        }

        dotSource.append("}");
        return dotSource.toString();
    }

}

