package ir.urmia;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Search {

    public static void simpleSearch(String absolutePathTarget, int numberOfBooks) {
        while (true) {
            System.out.println("Enter the vocabs (use \"AND\" \"OR\" \"NOT\"):");
            try {
                Scanner in = new Scanner(System.in);
                String line = in.nextLine();
                if (line.trim().equals("QUIT"))
                    return;
                searchLine(line, numberOfBooks, absolutePathTarget);

            } catch (Exception e) {
                System.out.println("Oops");
            }

        }
    }

    private static void searchLine(String line, int numberOfBooks, String absolutePathTarget) throws IOException {
        String[] splitted = line.split(" ");
        PostIndex res = new PostIndex(new Integer[0]);
        String nextWord;
        String currentWord;
        Integer[] postIndexOfNextWord;

        for (int index = 0; index < splitted.length; index++) {
            currentWord = splitted[index];
            switch (currentWord) {
                case "AND" -> {
                    nextWord = splitted[++index];
                    if (nextWord.equals("NOT")) {
                        nextWord = splitted[++index];
                        postIndexOfNextWord = getPostIndexOf(nextWord, absolutePathTarget);
                        if (postIndexOfNextWord == null) {
                            System.out.println("[]");
                            return;
                        }
                        res.and(
                                new PostIndex(postIndexOfNextWord)
                                        .not(numberOfBooks)
                        );
                    } else {
                        postIndexOfNextWord = getPostIndexOf(nextWord, absolutePathTarget);
                        if (postIndexOfNextWord == null) {
                            System.out.println("[]");
                            return;
                        }
                        res.and(
                                new PostIndex(postIndexOfNextWord));
                    }
                }
                case "OR" -> {
                    nextWord = splitted[++index];
                    if (nextWord.equals("NOT")) {
                        nextWord = splitted[++index];
                        postIndexOfNextWord = getPostIndexOf(nextWord, absolutePathTarget);
                        if (postIndexOfNextWord == null) {
                            System.out.println("[]");
                            return;
                        }
                        res.or(
                                new PostIndex(postIndexOfNextWord)
                                        .not(numberOfBooks)
                        );
                    } else {
                        postIndexOfNextWord = getPostIndexOf(nextWord, absolutePathTarget);
                        if (postIndexOfNextWord == null) {
                            System.out.println("[]");
                            return;
                        }
                        res.or(
                                new PostIndex(postIndexOfNextWord));
                    }
                }
                case "NOT" -> {
                    nextWord = splitted[++index];
                    postIndexOfNextWord = getPostIndexOf(nextWord, absolutePathTarget);
                    if (postIndexOfNextWord == null) {
                        System.out.println("[]");
                        return;
                    }
                    res = new PostIndex(postIndexOfNextWord).not(numberOfBooks);
                }
                default -> {
                    Integer[] postIndexOfCurrentWord = getPostIndexOf(currentWord, absolutePathTarget);
                    if (postIndexOfCurrentWord == null) {
                        System.out.println("[]");
                        return;
                    }
                    res = new PostIndex(postIndexOfCurrentWord);
                }
            }


        }

        System.out.println(res);
    }

    private static Integer[] getPostIndexOf(String word, String absolutePathTarget) throws IOException {
        String fileName = IndexFile.getFileName(word);

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
