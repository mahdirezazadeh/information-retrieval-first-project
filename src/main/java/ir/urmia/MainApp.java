package ir.urmia;

import java.io.IOException;
import java.util.Calendar;

public class MainApp {

    final static String absolutePathTarget = "src/main/resources/target/";
    final static String absolutePath = "src/main/resources/texts/";
    final static String relativePath = "/texts/";

    public static void main(String[] args) throws IOException {

        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();

        IndexFile.indexToFiles(absolutePath, absolutePathTarget, relativePath);

        System.out.println(Calendar.getInstance().getTimeInMillis()-timeInMillis);
//        int numberOfBooks = 100;
//        Search.simpleSearch(absolutePathTarget, numberOfBooks);

    }
}
