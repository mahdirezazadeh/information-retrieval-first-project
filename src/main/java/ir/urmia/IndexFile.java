package ir.urmia;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

public class IndexFile {

    public static void indexToFiles(String absolutePath, String absolutePathTarget, String relativePath, String absolutePathTargetBooks) throws IOException {
        File folder = new File(absolutePath);
        File[] listOfFiles = folder.listFiles();

        if(new File(absolutePathTargetBooks).delete()){
            System.out.println("Books ids file deleted!");
        }

        HashMap<String, ArrayList<Integer>> vocabs = new HashMap<>();


        System.out.println("Extracting words from books...");
        if (listOfFiles != null)
            for (File file : listOfFiles) {
                String text = getFileText(file, relativePath);
                Document document = new Document(file.getName(), text);

                addDocumentToFile(document, absolutePathTargetBooks);

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
            }
        System.out.println("Extracted words number: " + vocabs.size());


//      clean folder before indexing words
        File targetFolder = new File(absolutePathTarget);
        FileUtils.cleanDirectory(targetFolder);

        System.out.println("Indexing vocabs to files...");
        for (Map.Entry<String, ArrayList<Integer>> set : vocabs.entrySet()) {

            String key = set.getKey();

            File file = new File(absolutePathTarget + getFileName(key));

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
        }
    }

    private static void addDocumentToFile(Document document, String absolutePathTargetBooks) throws IOException {
        StringBuilder res = new StringBuilder();
        res.append(document.getDocId()).append(" ").append(document.getName()).append("\n");
        String book = res.toString();
//        System.out.println(document.getDocId());
//        System.out.println(book);

        File file = new File(absolutePathTargetBooks);
        FileOutputStream oFile = new FileOutputStream(file, true);

        Writer writer = new BufferedWriter(new OutputStreamWriter(oFile, StandardCharsets.UTF_8));
        writer.write(book);
        writer.flush();
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
