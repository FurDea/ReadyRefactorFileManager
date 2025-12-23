import java.io.*;

public class FileManager {

    public static void deleteFile(String filepath) {
        try {
            File file = new File(filepath);
            if (file.delete()) {
                System.out.println("\n  File deleted!");
            } else {
                System.out.println("\n  Error: Cannot delete file");
            }
        } catch (SecurityException e) {
            System.out.println("\n  Error: " + e.getMessage() + "\n");
        }
    }

    public static void copyFile(String source, String dest) {
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(dest)) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            System.out.println("\n  File copied!");
        } catch (FileNotFoundException e) {
            System.out.println("\n  Error: File not found\n");
        } catch (IOException e) {
            System.out.println("\n  Error: cannot copy file\n");
        }
    }

    public static void renameFile(String oldPath, String newPath) {
        try {
            File oldFile = new File(oldPath);
            File newFile = new File(newPath);

            if (oldFile.renameTo(newFile)) {
                System.out.println("  File renamed!");
            } else {
                System.out.println("  Error: Cannot rename file");
            }
        } catch (SecurityException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    public static void createFile(String filepath) {
        try {
            File file = new File(filepath);
            if (file.createNewFile()) {
                System.out.println("  File created!");
            } else {
                System.out.println("  Error: File already exists");
            }
        } catch (IOException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }
}