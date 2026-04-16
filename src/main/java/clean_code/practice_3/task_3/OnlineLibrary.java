package clean_code.practice_3.task_3;

import java.util.HashMap;
import java.util.Map;

public class OnlineLibrary {
    private final Map<Integer, BookProxy> catalog = new HashMap<>();


    public String getBookMetadata(int id) {
        BookProxy book = getOrCreateProxy(id);
        if (book == null) {
            throw new IllegalArgumentException("Book with ID " + id +" not found in library.");
        }
        return book.toString();
    }

    public String readBook(int id) {
        BookProxy book = getOrCreateProxy(id);
        if (book == null) {
            throw new IllegalArgumentException("Book with ID " + id + " not found in library.");
        }
        return book.getContent();
    }

    private BookProxy getOrCreateProxy(int id) {
        if (!catalog.containsKey(id)) {
            if (!BookData.bookMetadata.containsKey(id)) {
                return null;
            } else {
                catalog.put(id, new BookProxy(id));
            }
        }
        return catalog.get(id);
    }
}
