import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);


        System.out.println("Введите Фамилию:");
        String lastName = scan.nextLine();

        System.out.println("Введите Имя:");
        String firstName = scan.nextLine();

        System.out.println("Введите Отчество:");
        String patronymic = scan.nextLine();

        System.out.println("Введите дату рождения в формате DD.MM.YYYY:");
        String birthDate = scan.nextLine();


        Person per = new Person(lastName, firstName, patronymic, birthDate);


        per.display();

        scan.close();
    }
}
