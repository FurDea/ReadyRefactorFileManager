import java.util.*;

public class SearchEngine {

    public static void searchInFile(String buffer, String query) {
        if (buffer.isEmpty()) {
            System.out.println("\n  Open file first!");
            return;
        }

        if (query.isEmpty()) {
            System.out.println("\n  Empty query!");
            return;
        }

        DirtyTextEditor.GlobalState.searchResults.clear();

        int index = 0;
        while ((index = buffer.indexOf(query, index)) != -1) {
            DirtyTextEditor.GlobalState.searchResults.add(Integer.toString(index));
            index += query.length();
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();
        UIHelper.printBorder("SEARCH RESULTS");
        System.out.println("  Query: '" + query + "'");
        System.out.println("  Found: " + DirtyTextEditor.GlobalState.searchResults.size());
        UIHelper.printSeparator();

        if (DirtyTextEditor.GlobalState.searchResults.isEmpty()) {
            System.out.println("  Not found");
        } else {
            for (int pos = 0; pos < DirtyTextEditor.GlobalState.searchResults.size() && pos < 20; pos++) {
                System.out.println("  " + (pos + 1) + ". Position " +
                        DirtyTextEditor.GlobalState.searchResults.get(pos));
            }

            if (DirtyTextEditor.GlobalState.searchResults.size() > 20) {
                System.out.println("  ... and " +
                        (DirtyTextEditor.GlobalState.searchResults.size() - 20) + " more");
            }
        }
        UIHelper.printSeparator();
    }
}