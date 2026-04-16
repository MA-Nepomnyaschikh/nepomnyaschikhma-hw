package clean_code.practice_3.task_3;

import java.util.HashMap;
import java.util.Map;

public class BookData {

    public static Map<Integer, String[]> bookMetadata = new HashMap<>();
    public static Map<Integer, String[]> bookContent = new HashMap<>();

    static {
        bookMetadata.put(1, new String[] {
                "War and Peace",
                "Leo Tolstoy",
                "A historical novel about the Napoleonic Wars."
        });

        bookMetadata.put(2, new String[] {
                "1984",
                "George Orwell",
                "Dystopian novel about totalitarianism."
        });

        bookContent.put(1, new String[] {
                "Chapter 1: Well, Prince, so Genoa and Lucca are now just family estates of the Buonapartes...",
                "Chapter 2: The war continues..."
        });

        bookContent.put(2, new String[] {
                "Chapter 1: It was a bright cold day in April, and the clocks were striking thirteen...",
                "Chapter 2: Winston Smith works in the Ministry of Truth..."
        });
    }
}
