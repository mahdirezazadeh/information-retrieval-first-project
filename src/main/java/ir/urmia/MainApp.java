package ir.urmia;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainApp {

    final static String absolutePath = "src/main/resources/texts/";
    final static String absolutePathTarget = "src/main/resources/target/";
    final static String relativePath = "/texts/";

    public static void main(String[] args) throws IOException {

        IndexFile.indexToFiles(absolutePath, absolutePathTarget, relativePath);
        int numberOfBooks = 100;
        simpleSearch(numberOfBooks);

    }

    private static void simpleSearch(int numberOfBooks) throws IOException {
        while (true) {
            System.out.println("Enter the vocabs (use \"AND\" \"OR\" \"NOT\"):");
            try {
                Scanner in = new Scanner(System.in);
                String line = in.nextLine();
                searchLine(line, numberOfBooks);

            } catch (Exception e) {
                continue;
            }

        }
    }

    private static void searchLine(String line, int numberOfBooks) throws IOException {
        String[] splited = line.split(" ");
        PostIndex res = new PostIndex(new Integer[0]);
        String nextWord;
        String currentWord;
        Integer[] postIndexOfNextWord;

        for (int index = 0; index < splited.length; index++) {
            currentWord = splited[index];
            switch (currentWord) {
                case "AND" -> {
                    nextWord = splited[++index];
                    if (nextWord.equals("NOT")) {
                        nextWord = splited[++index];
                        postIndexOfNextWord = getPostIndexOf(nextWord);
                        if (postIndexOfNextWord==null)
                            continue;
                        res.and(
                                new PostIndex(postIndexOfNextWord)
                                        .not(numberOfBooks)
                        );
                    } else {
                        res.and(
                                new PostIndex(getPostIndexOf(nextWord)));
                    }
                }
                case "OR" -> {
                    nextWord = splited[++index];
                    if (nextWord.equals("NOT")) {
                        nextWord = splited[++index];
                        res.or(
                                new PostIndex(getPostIndexOf(nextWord))
                                        .not(numberOfBooks)
                        );
                    } else {
                        res.or(
                                new PostIndex(getPostIndexOf(nextWord)));
                    }
                }
                case "NOT" -> {
                    nextWord = splited[++index];
                    res = new PostIndex(getPostIndexOf(nextWord)).not(numberOfBooks);
                }
                default -> {
                    res = new PostIndex(getPostIndexOf(currentWord));
                }
            }


        }

        System.out.println(res);
    }

    private static Integer[] getPostIndexOf(String word) throws IOException {
        String fileName = String.valueOf(word.charAt(0)) + word.length() + ".txt";

        File file = new File(absolutePathTarget + fileName);
        FileInputStream iFile = new FileInputStream(file);

        String text = new String(iFile.readAllBytes());
        String[] rows = text.split("\n");
        for (String row : rows) {
            String[] hash = row.split(" ");
            if (hash[0].equals(word)) {
                Integer[] postIndexOfWord = new Integer[hash.length - 1];
                for (int index = 1; index < hash.length; index++) {
                    postIndexOfWord[index - 1] = Integer.parseInt(hash[index]);
                }
                return postIndexOfWord;
            }
        }
        return null;
    }


}
