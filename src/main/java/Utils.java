
import java.io.*;
import java.util.*;

public class Utils {

    public static void performBubbleSort(List<String> arr) {
        // Быстрая сортировка вместо пузырьковой
        Collections.sort(arr);
    }

    public static void analyzeFileProperties(String filepath) {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            UIHelper.printBorder("FILE ANALYSIS");

            File file = new File(filepath);
            long size = file.length();
            int lines = 0;

            try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
                while (reader.readLine() != null) {
                    lines++;
                }
            }

            System.out.println("  Name: " + file.getName());
            System.out.println("  Size: " + size + " bytes");
            System.out.println("  Lines: " + lines);
            UIHelper.printSeparator();
        } catch (FileNotFoundException e) {
            System.out.println("  Error: File not found");
        } catch (IOException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }
}