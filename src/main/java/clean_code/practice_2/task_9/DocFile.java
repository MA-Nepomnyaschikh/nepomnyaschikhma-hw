package clean_code.practice_2.task_9;

public class DocFile implements File {

    private String content;

    public DocFile(String content) {
        this.content = content;
    }

    @Override
    public void process() {
        System.out.println("Происходит обработка DOC файла..." + "\n" + content + "\n" + "Файл обработан");
    }

    public String getContent() {
        return content;
    }
}
