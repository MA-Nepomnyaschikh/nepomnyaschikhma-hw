package clean_code.practice_2.task_9;

public class Main {
    public static void main(String[] args) {
        DocFile docFile = new DocFile("Контент из DOC файла");
        docFile.process();

        System.out.println();

        PdfAdapter pdfFile = new PdfAdapter(new PdfFile("Контент из PDF файла"));
        pdfFile.process();
    }
}
