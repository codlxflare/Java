import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Date {
    public String getGender(String patronymic) {
        char lastChar = patronymic.charAt(patronymic.length() - 1);
        if (lastChar == 'ч') {
            return "Мужской";
        } else if (lastChar == 'а') {
            return "Женский";
        } else {
            return "Не удалось определить";
        }
    }

    public int calcAge(String birthDateStr) {
        LocalDate birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }
}
