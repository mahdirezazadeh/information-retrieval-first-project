package ir.urmia;

import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;

public class MainApp {

    final static String absolutePathTarget = "src/main/resources/target/";
    final static String absolutePathTargetBooks = "src/main/resources/books.txt";
    final static String absolutePath = "src/main/resources/texts/";
    final static String relativePath = "/texts/";

    public static void main(String[] args) throws IOException {

//

        boolean run = true;
        String menu =
                """
                        <<<---MENU--->>>
                        1.Search in Indexed Files
                        2.Index All Files In text directory
                        3.Exit""";
        while (run) {
            int choice = readInteger(menu);
            switch (choice) {
                case 1 -> {
                    int numberOfBooks = 100;
                    Search.simpleSearch(absolutePathTarget, numberOfBooks, absolutePathTargetBooks);
                }
                case 2 -> {
                    Calendar calendar = Calendar.getInstance();
                    long timeInMillis = calendar.getTimeInMillis();
                    IndexFile.indexToFiles(absolutePath, absolutePathTarget, relativePath, absolutePathTargetBooks);
                    System.out.println("process of indexing takes: "+(Calendar.getInstance().getTimeInMillis() - timeInMillis)+"ms");
                }
                case 3 -> run = false;
            }
        }


    }

    public static int readInteger(String input) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(input);
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid Number");
                scanner.nextLine();
                continue;
            }
            int number = scanner.nextInt();
            scanner.nextLine();
            return number;
        }
    }
}
