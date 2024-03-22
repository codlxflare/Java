import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Counter {
    private static final int ASCII_UPPER_A = 65;
    private static final int ASCII_UPPER_Z = 91;
    private static final int ASCII_LOWER_A = 97;
    private static final int ASCII_LOWER_Z = 123;

    private final HashMap<Character, Integer> symbolCounter = new HashMap<>();

    public Counter() {
        initializeCounter();
    }

    public void readfile(String filename) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(filename))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                SymbolCounter(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    private void SymbolCounter(String data) {
        for (char symbol : data.toCharArray()) {
            if (Character.isLetter(symbol)) {
                if (symbolCounter.containsKey(symbol)) {
                    symbolCounter.put(symbol, symbolCounter.get(symbol) + 1);
                } else {
                    symbolCounter.put(symbol, 1);
                }
            }
        }
    }

    private void initializeCounter() {
        for (int i = ASCII_UPPER_A; i < ASCII_UPPER_Z; i++) {
            symbolCounter.put((char) i, 0);
        }
        for (int i = ASCII_LOWER_A; i < ASCII_LOWER_Z; i++) {
            symbolCounter.put((char) i, 0);
        }
    }

    public void writefile(String filename) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<Character, Integer> entry : symbolCounter.entrySet()) {
                bufferedWriter.write(entry.getKey() + " : " + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла: " + e.getMessage());
        }
    }
}


