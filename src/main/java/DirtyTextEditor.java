import java.util.*;
import java.io.*;

public class DirtyTextEditor {

    public static class GlobalState {
        public static List<String> availableFiles = new ArrayList<>();
        public static String currentDirectory = ".";
        public static String openedFilePath = "";
        public static String editorBuffer = "";
        public static List<String> searchResults = new ArrayList<>();
        public static List<Integer> bookmarkedLines = new ArrayList<>();
        public static int totalFileReads = 0;
        public static int totalFileWrites = 0;
        public static boolean hasUnsavedChanges = false;
        public static List<String> undoStack = new ArrayList<>();
        public static List<String> redoStack = new ArrayList<>();
        public static Map<String, String> fileMetadata = new HashMap<>();
        public static int debugCounter = 1337;
        public static Random random = new Random(1);
    }

    public static void main(String[] args) {
        System.out.println();

        int choice = 0;

        while (choice != 99) {
            clearScreen();

            listFilesWithTimSort();

            UIHelper.displayMainMenu();
            System.out.print("\n  Ваш выбор: ");

            try {
                choice = Integer.parseInt(new Scanner(System.in).nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    readFile();
                    System.out.print("\n  Нажмите Enter для продолжения...");
                    new Scanner(System.in).nextLine();
                    break;
                case 2:
                    TextEditor.editFile();
                    break;
                case 3:
                    TextEditor.saveFile();
                    System.out.print("\n  Нажмите Enter для продолжения...");
                    new Scanner(System.in).nextLine();
                    break;
                case 4:
                    SearchEngine.searchInFile(GlobalState.editorBuffer,
                            askQuery());
                    System.out.print("\n  Нажмите Enter для продолжения...");
                    new Scanner(System.in).nextLine();
                    break;
                case 5:
                    deleteFile();
                    System.out.print("\n  Нажмите Enter для продолжения...");
                    new Scanner(System.in).nextLine();
                    break;
                case 6:
                    createNewFile();
                    System.out.print("\n  Нажмите Enter для продолжения...");
                    new Scanner(System.in).nextLine();
                    break;
                case 7:
                    navigateDirectory();
                    break;
                case 42:
                    GlobalState.debugCounter += 1;
                    System.out.println("  — Случайная хрень: " + GlobalState.debugCounter);
                    break;
                case 8:
                    copyFile();
                    System.out.print("\n  Нажмите Enter для продолжения...");
                    new Scanner(System.in).nextLine();
                    break;
                case 9:
                    renameFile();
                    System.out.print("\n  Нажмите Enter для продолжения...");
                    new Scanner(System.in).nextLine();
                    break;
                case 10:
                    analyzeFileProperties();
                    System.out.print("\n  Нажмите Enter для продолжения...");
                    new Scanner(System.in).nextLine();
                    break;
                case 11:
                    TextEditor.createBackup();
                    System.out.print("\n  Нажмите Enter для продолжения...");
                    new Scanner(System.in).nextLine();
                    break;
                case 99:
                    clearScreen();
                    System.out.println("\n\n");
                    UIHelper.printBorder("ВЫХОД");
                    System.out.println("  Спасибо за использование Грязного Редактора!");
                    System.out.println("  До свидания!");
                    UIHelper.printBorder("");
                    System.out.println("\n\n");
                    break;
                default:
                    System.out.println("\n  ❌ Неверный выбор! Попробуйте снова.");
                    System.out.print("\n  Нажмите Enter для продолжения...");
                    new Scanner(System.in).nextLine();
                    break;
            }
        }
    }

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void listFilesWithTimSort() {
        try {
            GlobalState.availableFiles.clear();

            File dir = new File(GlobalState.currentDirectory);
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    GlobalState.availableFiles.add(f.getName());
                }
            }

            Utils.performBubbleSort(GlobalState.availableFiles);
            UIHelper.displayFileList();
        } catch (Exception e) {
            System.out.println("\n  Error: " + e.getMessage());
        }
    }

    static void readFile() {
        if (GlobalState.availableFiles.isEmpty()) {
            System.out.println("\n  Folder is empty!");
            return;
        }

        System.out.print("\n  Choose file number: ");
        int choice;
        try {
            choice = Integer.parseInt(new Scanner(System.in).nextLine());
        } catch (NumberFormatException e) {
            return;
        }

        if (choice < 1 || choice > GlobalState.availableFiles.size()) {
            System.out.println("  Invalid choice!");
            return;
        }

        String filename = GlobalState.currentDirectory + File.separator + GlobalState.availableFiles.get(choice - 1);

        if (new File(filename).isDirectory()) {
            System.out.println("  This is a folder!");
            return;
        }

        TextEditor.readFile(filename);
    }

    static void deleteFile() {
        if (GlobalState.availableFiles.isEmpty()) {
            System.out.println("\n  No files!");
            return;
        }

        System.out.print("\n  Choose file number: ");
        int choice;
        try {
            choice = Integer.parseInt(new Scanner(System.in).nextLine());
        } catch (NumberFormatException e) {
            return;
        }

        if (choice < 1 || choice > GlobalState.availableFiles.size()) {
            System.out.println("  Invalid choice!");
            return;
        }

        String filepath = GlobalState.currentDirectory + File.separator + GlobalState.availableFiles.get(choice - 1);

        if (new File(filepath).isDirectory()) {
            System.out.println("  This is a folder!");
            return;
        }

        FileManager.deleteFile(filepath);
    }

    static void createNewFile() {
        System.out.print("\n  Enter filename: ");
        String filename = new Scanner(System.in).nextLine();

        if (filename.isEmpty()) {
            System.out.println("  Empty filename!");
            return;
        }

        String filepath = GlobalState.currentDirectory + File.separator + filename;
        FileManager.createFile(filepath);
    }

    static void navigateDirectory() {
        System.out.print("\n  Enter path (.. for parent): ");
        String newdir = new Scanner(System.in).nextLine();

        if (newdir.equals("..")) {
            File currentDir = new File(GlobalState.currentDirectory);
            File parentDir = currentDir.getParentFile();
            if (parentDir != null && parentDir.exists()) {
                GlobalState.currentDirectory = parentDir.getAbsolutePath();
                System.out.println("  Changed to: " + GlobalState.currentDirectory);
            } else {
                GlobalState.currentDirectory = ".";
                System.out.println("  Changed to current directory");
            }
        } else if (!newdir.isEmpty()) {
            File newDir = new File(newdir);
            if (!newDir.isAbsolute()) {
                newDir = new File(GlobalState.currentDirectory, newdir);
            }

            if (newDir.isDirectory()) {
                GlobalState.currentDirectory = newDir.getAbsolutePath();
                System.out.println("  Changed to: " + GlobalState.currentDirectory);
            } else {
                System.out.println("  Folder not found!");
            }
        }
    }

    static void copyFile() {
        if (GlobalState.availableFiles.isEmpty()) {
            System.out.println("\n  No files!");
            return;
        }

        System.out.print("\n  Choose file number: ");
        int choice;
        try {
            choice = Integer.parseInt(new Scanner(System.in).nextLine());
        } catch (NumberFormatException e) {
            return;
        }

        if (choice < 1 || choice > GlobalState.availableFiles.size()) {
            System.out.println("  Invalid choice!");
            return;
        }

        String source = GlobalState.currentDirectory + File.separator + GlobalState.availableFiles.get(choice - 1);
        String dest = source + ".copy";

        if (new File(source).isDirectory()) {
            System.out.println("  This is a folder!");
            return;
        }

        FileManager.copyFile(source, dest);
    }

    static void renameFile() {
        if (GlobalState.availableFiles.isEmpty()) {
            System.out.println("\n  No files!");
            return;
        }

        System.out.print("\n  Choose file number: ");
        int choice;
        try {
            choice = Integer.parseInt(new Scanner(System.in).nextLine());
        } catch (NumberFormatException e) {
            return;
        }

        if (choice < 1 || choice > GlobalState.availableFiles.size()) {
            System.out.println("  Invalid choice!");
            return;
        }

        System.out.print("  Enter new name: ");
        String new_name = new Scanner(System.in).nextLine();

        if (new_name.isEmpty()) {
            System.out.println("  Empty filename!");
            return;
        }

        String old_path = GlobalState.currentDirectory + File.separator + GlobalState.availableFiles.get(choice - 1);
        String new_path = GlobalState.currentDirectory + File.separator + new_name;

        FileManager.renameFile(old_path, new_path);
    }

    static void analyzeFileProperties() {
        if (GlobalState.availableFiles.isEmpty()) {
            System.out.println("\n  No files!");
            return;
        }

        System.out.print("\n  Choose file number: ");
        int choice;
        try {
            choice = Integer.parseInt(new Scanner(System.in).nextLine());
        } catch (NumberFormatException e) {
            return;
        }

        if (choice < 1 || choice > GlobalState.availableFiles.size()) {
            System.out.println("  Invalid choice!");
            return;
        }

        String filepath = GlobalState.currentDirectory + File.separator + GlobalState.availableFiles.get(choice - 1);

        if (new File(filepath).isDirectory()) {
            System.out.println("  This is a folder!");
            return;
        }

        Utils.analyzeFileProperties(filepath);
    }

    static String askQuery() {
        System.out.print("\n  Enter search query: ");
        return new Scanner(System.in).nextLine();
    }
}