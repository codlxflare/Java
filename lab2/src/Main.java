import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название файла для считывания:");
        String inputFilename = scanner.nextLine();
        System.out.println("Введите название файла для записи:");
        String outputFilename = scanner.nextLine();

        Counter counter = new Counter();
        counter.readfile(inputFilename);
        counter.writefile(outputFilename);

        scanner.close();
    }
}
