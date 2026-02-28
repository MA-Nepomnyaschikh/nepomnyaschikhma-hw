package clean_code.practice_2.task_9;

public class PdfAdapter implements File {

    private PdfFile pdfFile;

    public PdfAdapter(PdfFile pdfFile) {
        this.pdfFile = pdfFile;
    }

    @Override
    public void process() {
        String content = pdfFile.getContent();
        System.out.println("Происходит обработка DOC файла..." + "\n" + content + "\n" + "Файл обработан");
    }
}
