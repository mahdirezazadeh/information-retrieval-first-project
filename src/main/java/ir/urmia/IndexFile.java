package ir.urmia;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IndexFile {

    public static void indexToFiles(String absolutePath, String absolutePathTarget, String relativePath) throws IOException {
        File folder = new File(absolutePath);
        File[] listOfFiles = folder.listFiles();

        HashMap<String, ArrayList<Integer>> vocabs = new HashMap<>();


        System.out.println("indexing vocabs");
        if (listOfFiles != null)
            for (File file : listOfFiles) {
                String text = getFileText(file, relativePath);
                Document document = new Document(file.getName(), text);
                text = cleanRedundancy(text);
                List<String> vocabList = splitIntoWords(text);
                for (String vocab : vocabList) {
                    if (!vocabs.containsKey(vocab)) {
                        vocabs.put(vocab, new ArrayList<>());
                        vocabs.get(vocab).add(document.getDocId());
                    } else if (!vocabs.get(vocab).contains(document.getDocId())) {
                        vocabs.get(vocab).add(document.getDocId());
                    }
                }
//            TODO implement method
//            addBookToFile(document);
            }
        System.out.println(vocabs.size());

        System.out.println("indexing vocabs to files");
        for (Map.Entry<String, ArrayList<Integer>> set : vocabs.entrySet()) {

            String key = set.getKey();

            File file = new File(absolutePathTarget + getFileName(key));
//            try {
//                boolean isCreated = file.createNewFile();

                FileOutputStream oFile = new FileOutputStream(file, true);
//            new BufferedWriter()
                Writer writer = new BufferedWriter(new OutputStreamWriter(oFile, StandardCharsets.UTF_8));
                StringBuilder res = new StringBuilder(key);
                for (int num : set.getValue()) {
                    res.append(" ").append(num);
                }
                res.append("\n");
                writer.write(res.toString());
                writer.flush();

//                writer.close();
//                oFile.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    private static List<String> splitIntoWords(String text) {
        Predicate<String> predicate = s -> !s.isBlank() && !s.isEmpty();
        return Arrays.stream(text.toLowerCase().split("\s")).filter(predicate).collect(Collectors.toList());
    }

    private static String getFileText(File listOfFile, String relativePath) throws IOException {
        InputStream inputStream = Objects.requireNonNull(MainApp.class.getResource(relativePath + listOfFile.getName()))
                .openStream();
        String output = new String(inputStream.readAllBytes());
        inputStream.close();
        return output;
    }

    private static String cleanRedundancy(String text) {
        text = text.replaceAll("[^a-zA-Z0-9\s’.]+", "\s");
        text = text.replaceAll("\\.{2,}", "\s");
        text = text.replaceAll("\s’|\\.\s", "\s");
        text = text.replaceAll("\\.\s|,\s|:\s|’\s|\n|\r", "\s");

        return text;
    }

    public static String getFileName(String word) {
        return String.valueOf(word.charAt(0)) + word.length() + ".txt";
    }
}
