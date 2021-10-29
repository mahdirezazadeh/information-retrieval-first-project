package ir.urmia;

import java.io.IOException;

public class MainApp {

    final static String absolutePathTarget = "src/main/resources/target/";
//    final static String absolutePath = "src/main/resources/texts/";
//    final static String relativePath = "/texts/";

    public static void main(String[] args) throws IOException {

//        IndexFile.indexToFiles(absolutePath, absolutePathTarget, relativePath);
        int numberOfBooks = 100;
        Search.simpleSearch(absolutePathTarget, numberOfBooks);

    }
}
