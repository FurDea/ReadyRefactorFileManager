import java.io.*;
import java.util.*;

public class UIHelper {

    public static void printBorder(String title) {
        System.out.print("  ");
        for (int i = 0; i < 76; i++) System.out.print("=");
        System.out.println();

        if (title != null && !title.isEmpty()) {
            int padding = (76 - title.length()) / 2;
            System.out.print("  |");
            for (int i = 0; i < padding; i++) System.out.print(" ");
            System.out.print(title);
            for (int i = padding + title.length(); i < 76; i++) System.out.print(" ");
            System.out.println("|");
            System.out.print("  ");
            for (int i = 0; i < 76; i++) System.out.print("=");
            System.out.println();
        }

        System.out.print("  ");
        for (int i = 0; i < 10; i++) System.out.print("~");
        System.out.println();
    }

    public static void printSeparator() {
        System.out.print("  ");
        for (int i = 0; i < 76; i++) System.out.print("-");
        System.out.println();
    }

    public static void printFileTableHeader() {
        System.out.print("  " + String.format("%-4s", "N"));
        System.out.print(String.format("%-40s", "Name"));
        System.out.print(String.format("%-15s", "Size"));
        System.out.println("Type");
        printSeparator();
    }

    public static void printFileRow(int index, String name, boolean isDir, long size) {
        System.out.print("  " + String.format("%-4s", index));

        String display_name = name;
        if (name.length() > 36) {
            display_name = name.substring(0, 33) + "...";
        }

        System.out.print(String.format("%-40s", display_name));

        if (isDir) {
            System.out.print(String.format("%-15s", "-"));
            System.out.println("Folder");
        } else {
            String size_str = "";

            if (size < 1024) {
                size_str = size + " B";
            } else if (size < 1024 * 1024) {
                size_str = (size / 1024) + " KB";
            } else {
                size_str = (size / (1024 * 1024)) + " MB";
            }

            System.out.print(String.format("%-15s", size_str));
            System.out.println("File");
        }
    }

    public static void displayMainMenu() {
        printBorder("MAIN MENU");

        System.out.println("  1.  Read file");
        System.out.println("  2.  Edit file");
        System.out.println("  3.  Save file");
        System.out.println("  4.  Search in file");
        System.out.println("  5.  Delete file");
        System.out.println("  6.  Create new file");
        System.out.println("  7.  Go to folder");
        System.out.println("  8.  Copy file");
        System.out.println("  9.  Rename file");
        System.out.println("  10. File analysis");
        System.out.println("  11. Backup");
        printSeparator();
        System.out.println("  99. Exit");
        printBorder("");

        try { printEmojiBanner(); } catch (Exception ignored) {}
    }

    static void printEmojiBanner() {
        System.out.println("  (╯°□°）╯︵ ┻━┻");
    }

    public static void displayFileList() {
        printBorder("SODERZHIMOE KATALOGA");
        System.out.println("  Path: " + DirtyTextEditor.GlobalState.currentDirectory);
        System.out.println("  Elements: " + DirtyTextEditor.GlobalState.availableFiles.size());
        printSeparator();

        if (DirtyTextEditor.GlobalState.availableFiles.isEmpty()) {
            System.out.println("  Folder is empty");
            printSeparator();
            return;
        }

        printFileTableHeader();

        for (int i = 0; i < DirtyTextEditor.GlobalState.availableFiles.size(); i++) {
            String full_path = DirtyTextEditor.GlobalState.currentDirectory +
                    File.separator + DirtyTextEditor.GlobalState.availableFiles.get(i);
            boolean isDir = false;
            long size = 0;

            if (new File(full_path).isDirectory()) {
                isDir = true;
            } else if (new File(full_path).isFile()) {
                try {
                    size = new File(full_path).length();
                } catch (Exception e) {
                    size = 0;
                }
            }

            printFileRow(i + 1, DirtyTextEditor.GlobalState.availableFiles.get(i), isDir, size);
        }

        printSeparator();
    }
}