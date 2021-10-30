package ir.urmia;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Search {

    public static void simpleSearch(String absolutePathTarget, int numberOfBooks, String absolutePathTargetBooks) {
        String menu =
                """
                        <<<---Search--->>>
                        1BACK: return main menu.
                        Enter Terms (you can use "AND", "OR", "NOT"):""";
        while (true) {
            System.out.println(menu);
            try {
                Scanner in = new Scanner(System.in);
                String line = in.nextLine();
                if (line.trim().equals("1BACK"))
                    return;
                searchLine(line, numberOfBooks, absolutePathTarget, absolutePathTargetBooks);

            } catch (Exception e) {
                System.out.println("Oops");
            }

        }
    }

    private static void searchLine(String line, int numberOfBooks, String absolutePathTarget, String absolutePathTargetBooks) throws IOException {
        String[] splitted = line.split(" ");
        PostIndex res = new PostIndex(new Integer[0]);
        String nextWord;
        String currentWord;
        Integer[] postIndexOfNextWord;
        Integer[] all = new Integer[numberOfBooks];
        for (int bookId = 1; bookId <= numberOfBooks; bookId++) {
            all[bookId - 1] = bookId;
        }

        for (int index = 0; index < splitted.length; index++) {
            currentWord = splitted[index];
            switch (currentWord) {
                case "AND" -> {
                    nextWord = splitted[++index];
                    if (nextWord.equals("NOT")) {
                        nextWord = splitted[++index];
                        postIndexOfNextWord = getPostIndexOf(nextWord, absolutePathTarget);
                        if (postIndexOfNextWord == null) {
                            continue;
                        }
                        res.and(
                                new PostIndex(postIndexOfNextWord)
                                        .not(numberOfBooks)
                        );
                    } else {
                        postIndexOfNextWord = getPostIndexOf(nextWord, absolutePathTarget);
                        if (postIndexOfNextWord == null) {
                            res = new PostIndex(new Integer[0]);
                            continue;
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
                            res = new PostIndex(all.clone());
                            continue;
                        }
                        res.or(
                                new PostIndex(postIndexOfNextWord)
                                        .not(numberOfBooks)
                        );
                    } else {
                        postIndexOfNextWord = getPostIndexOf(nextWord, absolutePathTarget);
                        if (postIndexOfNextWord == null) {
                            continue;
                        }
                        res.or(
                                new PostIndex(postIndexOfNextWord));
                    }
                }
                case "NOT" -> {
                    nextWord = splitted[++index];
                    postIndexOfNextWord = getPostIndexOf(nextWord, absolutePathTarget);
                    if (postIndexOfNextWord == null) {
                        res = new PostIndex(all.clone());
                        continue;
                    }
                    res = new PostIndex(postIndexOfNextWord).not(numberOfBooks);
                }
                default -> {
                    Integer[] postIndexOfCurrentWord = getPostIndexOf(currentWord, absolutePathTarget);
                    if (postIndexOfCurrentWord == null) {
                        res = new PostIndex(new Integer[0]);
                        continue;
                    }
                    res = new PostIndex(postIndexOfCurrentWord);
                }
            }


        }

        String[] booksNameByIds = getBooksNameByIds(res, absolutePathTargetBooks);
        System.out.println(Arrays.toString(booksNameByIds));
    }

    private static String[] getBooksNameByIds(PostIndex res, String absolutePathTargetBooks) throws IOException {
        Integer[] postIndex = res.getPostIndex();
        String[] booksName = new String[postIndex.length];

        File file = new File(absolutePathTargetBooks);

        FileInputStream iFile = new FileInputStream(file);
        String text = new String(iFile.readAllBytes());
        String[] rows = text.split("\n");

        int j = 0;

        for (int i = 0; i < postIndex.length;) {
            String[] idAndName = rows[j].split(" ");
            if(Integer.parseInt(idAndName[0]) == postIndex[i]){
                booksName[i]= idAndName[1];
                i++;
                j++;
            }else if (Integer.parseInt(idAndName[0]) < postIndex[i]){
                j++;
            }else {
                i++;
            }
        }


        return booksName;
    }

    private static Integer[] getPostIndexOf(String word, String absolutePathTarget) throws IOException {
        try {
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
        }catch (Exception e){
            return null;
        }
        return null;
    }
}
