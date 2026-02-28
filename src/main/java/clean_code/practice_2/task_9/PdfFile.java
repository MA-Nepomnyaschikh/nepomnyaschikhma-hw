package clean_code.practice_2.task_9;

public class PdfFile {

    private String content;

    public PdfFile(String content) {
        this.content = content;
    }

    public void read() {
        System.out.println(content);
    }

    public String getContent() {
        return content;
    }
}
