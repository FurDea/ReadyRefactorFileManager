
import java.io.*;

public class TextEditor {

    public static void readFile(String filename) {
        try {
            StringBuilder content = new StringBuilder();
            int lines = 0;

            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                    lines++;
                }
            }

            DirtyTextEditor.GlobalState.openedFilePath = filename;
            DirtyTextEditor.GlobalState.editorBuffer = content.toString();
            DirtyTextEditor.GlobalState.hasUnsavedChanges = false;
            DirtyTextEditor.GlobalState.totalFileReads++;

            System.out.print("\033[H\033[2J");
            System.out.flush();
            UIHelper.printBorder("FILE CONTENT");
            System.out.println("  File: " + filename);
            System.out.println("  Size: " + content.length() + " bytes");
            System.out.println("  Lines: " + lines);
            UIHelper.printSeparator();

            System.out.print(content.toString());

            UIHelper.printSeparator();
            System.out.println("\n  File loaded successfully");
        } catch (FileNotFoundException e) {
            System.out.println("\n  Error: File not found\n");
        } catch (IOException e) {
            System.out.println("\n  Error: " + e.getMessage() + "\n");
        }
    }

    public static void editFile() {
        if (DirtyTextEditor.GlobalState.openedFilePath.isEmpty()) {
            System.out.println("\n  Open file first!");
            return;
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();
        UIHelper.printBorder("EDIT FILE");
        System.out.println("  File: " + DirtyTextEditor.GlobalState.openedFilePath);
        System.out.println("  Size: " + DirtyTextEditor.GlobalState.editorBuffer.length() + " bytes");
        UIHelper.printSeparator();
        System.out.println("\n  Enter new content (EOF on new line to finish):");
        UIHelper.printSeparator();

        StringBuilder newContent = new StringBuilder();
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String input;

        while (!(input = scanner.nextLine()).equals("EOF")) {
            newContent.append(input).append("\n");
        }

        DirtyTextEditor.GlobalState.editorBuffer = newContent.toString();
        DirtyTextEditor.GlobalState.hasUnsavedChanges = true;

        System.out.println("\n  Content modified (not saved)!");
    }

    public static void saveFile() {
        if (DirtyTextEditor.GlobalState.openedFilePath.isEmpty()) {
            System.out.println("\n  No open file!");
            return;
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();
        UIHelper.printBorder("SAVE FILE");
        System.out.println("  File: " + DirtyTextEditor.GlobalState.openedFilePath);
        System.out.println("  Size: " + DirtyTextEditor.GlobalState.editorBuffer.length() + " bytes");
        UIHelper.printSeparator();

        try (FileWriter writer = new FileWriter(DirtyTextEditor.GlobalState.openedFilePath)) {
            writer.write(DirtyTextEditor.GlobalState.editorBuffer);
            writer.flush();

            DirtyTextEditor.GlobalState.totalFileWrites++;
            DirtyTextEditor.GlobalState.hasUnsavedChanges = false;

            System.out.println("  File saved successfully!");
        } catch (IOException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    public static void createBackup() {
        if (DirtyTextEditor.GlobalState.openedFilePath.isEmpty()) {
            System.out.println("\n  No open file!");
            return;
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();
        UIHelper.printBorder("CREATE BACKUP");
        System.out.println("  File: " + DirtyTextEditor.GlobalState.openedFilePath);
        UIHelper.printSeparator();

        try {
            String backupName = DirtyTextEditor.GlobalState.openedFilePath + ".backup";
            int counter = 1;

            File backupFile = new File(backupName);
            while (backupFile.exists()) {
                backupName = DirtyTextEditor.GlobalState.openedFilePath + ".backup" + counter;
                backupFile = new File(backupName);
                counter++;
            }

            try (FileWriter writer = new FileWriter(backupName)) {
                writer.write(DirtyTextEditor.GlobalState.editorBuffer);
                writer.flush();
            }

            System.out.println("  Backup created: " + backupName);
        } catch (IOException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }
}