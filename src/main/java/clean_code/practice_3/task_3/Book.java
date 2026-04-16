package clean_code.practice_3.task_3;

public class Book {
    private final String title;
    private final String author;
    private final String description;
    private final String content;

    public Book(Builder builder) {
        this.title = builder.title;
        this.author = builder.author;
        this.description = builder.description;
        this.content = builder.content;
    }

    public String getContent() {
        return content;
    }

    static class Builder {
        private String title;
        private String author;
        private String description;
        private String content;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}
