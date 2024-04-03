public class Person {
    private String lastName;
    private String firstName;
    private String patronymic;
    private String birthDate;

    public Person(String lastName, String firstName, String patronymic, String birthDate) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.birthDate = birthDate;
    }

    public void display() {
        Date date = new Date();
        String gender = date.getGender(patronymic);
        int age = date.calcAge(birthDate);

        System.out.println("Фамилия + Инициалы: " + lastName + " " + firstName.charAt(0) + "." + patronymic.charAt(0) + ".");
        System.out.println("Пол: " + gender);
        System.out.println("Возраст: " + age + " лет");
    }
}
