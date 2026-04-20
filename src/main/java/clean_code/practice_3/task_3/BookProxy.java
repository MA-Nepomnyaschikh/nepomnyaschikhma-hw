package clean_code.practice_3.task_3;

public class BookProxy {
    private Book book;

    private final int id;
    private final String title;
    private final String author;
    private final String description;

    public BookProxy(int id) {
        String [] metaData = BookData.bookMetadata.get(id);

        this.id = id;
        this.title = metaData[0];
        this.author = metaData[1];
        this.description = metaData[2];
    }

    @Override
    public String toString() {
        return "title: '" + title + '\'' +
                ", author: '" + author + '\'' +
                ", description: '" + description + '\'';
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        if (book == null) {
            String[] content = BookData.bookContent.get(id);

            if (content == null) {
                throw new IllegalStateException("No content for book with id: " + id);
            }

            this.book = new Book.Builder()
                    .setTitle(this.title)
                    .setAuthor(this.author)
                    .setDescription(this.description)
                    .setContent(String.join("\n", content))
                    .build();
        }

        return book.getContent();
    }
}
