package com.hit.lab;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        String filePath = "C:\\Users\\Eden\\Desktop\\Lab1-main\\lab1\\src\\test.txt";
        // Create WordGraph instance
        WordGraph wordGraph = new WordGraph();


        try {
            String processedContent = readFileContent(filePath);
            wordGraph.buildGraph(processedContent);
            Graphviz.showDirectedGraph(wordGraph);

            boolean exit = false;
            Scanner scanner = new Scanner(System.in);
            while (!exit) {
                System.out.println("-----------------");
                System.out.println("选择以下任一项：");
                System.out.println("1. 查询桥接词");
                System.out.println("2. 生成新文本");
                System.out.println("3. 计算最短路径");
                System.out.println("4. 一个单词——最短路径");
                System.out.println("5. 随机游走");
                System.out.println("6. 退出");
                System.out.println("------------------");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        queryBridgeWords(wordGraph, scanner);
                        break;
                    case 2:
                        generateNewText(wordGraph, scanner);
                        break;
                    case 3:
                        calculateShortestPath(wordGraph, scanner);
                        break;
                    case 4:
                        printShortestDistancesFromWord(wordGraph, scanner);
                        break;
                    case 5:
                        randomWalk(wordGraph, scanner);
                        break;
                    case 6:
                        exit = true;
                        break;
                    default:
                        System.out.println("选择无效，请输入1到5之间的数字。");
                }
            }
        } catch (IOException e) {
            System.out.println("读取文件时出错: " + e.getMessage());
        }

    }

    private static String readFileContent(String filePath) throws IOException {
        // Read the file content
            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();

//         Process the content: replace newline and punctuation with space, ignore non-alphabetic characters
            String processedContent = content.toString().replaceAll("[^a-zA-Z\\s]", " ").replaceAll("\\s+", " ").toLowerCase();

        // Process the content: replace newline and punctuation with space, ignore non-alphabetic characters
        return processedContent;
    }

    private static void queryBridgeWords(WordGraph wordGraph, Scanner scanner) {
        System.out.println("输入两个单词查找桥接词:");
        String word1 = scanner.nextLine();
        String word2 = scanner.nextLine();
        String bridgeWords = wordGraph.queryBridgeWords(word1, word2);
        System.out.println(bridgeWords);
//        System.out.println("Bridge words between " + word1 + " and " + word2 + ": " + bridgeWords);
    }

    private static void generateNewText(WordGraph wordGraph, Scanner scanner) {
        System.out.println("输入源文本:");
        String sourceText = scanner.nextLine();
//        String sourceText = "explore new worlds to seek new life new lzl";
        String newText = wordGraph.generateNewText(sourceText);
        System.out.println(newText);

//        System.out.println(wordGraph.generateNewText("explore new worlds to seek new life new lzl"));
    }

    private static void calculateShortestPath(WordGraph wordGraph, Scanner scanner) {
        System.out.println("输入两个单词计算最短路径:");
        String word1 = scanner.nextLine();
        String word2 = scanner.nextLine();
        String shortestPath = wordGraph.calcShortestPath(word1, word2);
        System.out.println(shortestPath);
        String outputFileName = word1 + "_to_" + word2 + ".png";
        Graphviz.generateGraphWithShortestPath(wordGraph, shortestPath, outputFileName, "png");
//        System.out.println("Shortest path between " + word1 + " and " + word2 + ": " + shortestPath);
    }

    private static void printShortestDistancesFromWord(WordGraph wordGraph, Scanner scanner){
        System.out.println("输入一个单词计算最短路径:");
        String word = scanner.nextLine();
        wordGraph.printShortestDistancesFromWord(word);
    }

    private static void randomWalk(WordGraph wordGraph, Scanner scanner) {
        System.out.println("开始随机行走...");
        String randomWalkResult = wordGraph.randomWalk();
        System.out.println("随机行走结果: " + randomWalkResult);
        wordGraph.writeToFile("C:\\Users\\Eden\\Desktop\\Lab1-main\\lab1\\src\\random_walk_result.txt", randomWalkResult);
    }

}
