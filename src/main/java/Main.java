import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Transaction> transactions = new ArrayList<>();

        // Загрузка файла из папки resources
        ClassLoader classLoader = Main.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("db.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = br.readLine()) != null) {
                // Пропускаем первую строку с заголовками
                if (line.startsWith("user_email")) {
                    continue;
                }

                // Разделяем строку по символу ";"
                String[] values = line.split(";");

                // Парсим значения и создаем объект Transaction
                String userEmail = values[0];
                String generalStatus = values[1];
                double amount = Double.parseDouble(values[2]);
                String currency = values[3];
                String createdAt = values[4];

                Transaction transaction = new Transaction(userEmail, generalStatus, amount, currency, createdAt);
                transactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Фильтрация только завершенных транзакций
        List<Transaction> completedTransactions = transactions.stream()
                .filter(t -> t.getGeneralStatus().equals("purchase_complete"))
                .collect(Collectors.toList());

        // Вызов метода для вывода максимальных транзакций по клиентам и месяцам
        //printSortedMaxTransactionsByClientAndMonth(completedTransactions);
        //printSumTransactionsByClientAndMonth(completedTransactions);

        // Вызов метода для вычисления суммы транзакций по месяцам для purchase_complete
        //printSumCompletedTransactionsByClientAndMonth(transactions);

        // Вызов метода для вычисления суммы транзакций по месяцам для purchase_complete и purchase_declined
        printSumAllTransactionsByClientAndMonth(transactions);
    }

    // Метод для вывода отсортированных по месяцам максимальных транзакций для каждого клиента
    public static void printSortedMaxTransactionsByClientAndMonth(List<Transaction> completedTransactions) {
        // Группировка по пользователю и месяцу с нахождением максимальной транзакции за месяц
        Map<String, Map<String, Optional<Transaction>>> result = completedTransactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getEmail, // группировка по клиенту
                        Collectors.groupingBy(
                                t -> t.getCreatedAt().substring(0, 7), // группировка по месяцу (yyyy-MM)
                                Collectors.maxBy(Comparator.comparingDouble(Transaction::getAmount)) // нахождение максимальной суммы
                        )
                ));

        // Выводим результаты, отсортированные по email пользователя и затем по месяцу
        result.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Сортировка по email клиента
                .forEach(entry -> {
                    String userEmail = entry.getKey();
                    Map<String, Optional<Transaction>> transactionsByMonth = entry.getValue();

                    System.out.println("Client: " + userEmail);

                    // Сортировка по месяцу (yyyy-MM)
                    transactionsByMonth.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey()) // Сортировка по ключу (yyyy-MM)
                            .forEach(monthEntry -> {
                                String month = monthEntry.getKey();
                                Optional<Transaction> maxTransaction = monthEntry.getValue();
                                maxTransaction.ifPresent(transaction ->
                                        System.out.println("Month: " + month + ", Max Transaction: " + transaction.getAmount()));
                            });
                    System.out.println();
                });
    }

    // Метод для вычисления и вывода суммы транзакций по клиентам и месяцам
    public static void printSumTransactionsByClientAndMonth(List<Transaction> completedTransactions) {
        Map<String, Map<String, Double>> sumByClientAndMonth = completedTransactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getEmail, // группировка по клиенту
                        Collectors.groupingBy(
                                t -> t.getCreatedAt().substring(0, 7), // группировка по месяцу (yyyy-MM)
                                Collectors.summingDouble(Transaction::getAmount) // сумма транзакций
                        )
                ));

        // Выводим результаты, отсортированные по email пользователя и затем по месяцу
        sumByClientAndMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Сортировка по email клиента
                .forEach(entry -> {
                    String userEmail = entry.getKey();
                    Map<String, Double> transactionsByMonth = entry.getValue();

                    System.out.println("Client: " + userEmail);

                    // Сортировка по месяцу (yyyy-MM)
                    transactionsByMonth.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey()) // Сортировка по ключу (yyyy-MM)
                            .forEach(monthEntry -> {
                                String month = monthEntry.getKey();
                                Double totalAmount = monthEntry.getValue();
                                System.out.println("Month: " + month + ", Total Amount: " + totalAmount);
                            });
                    System.out.println();
                });
    }

    // Метод для вычисления и вывода суммы транзакций по клиентам и месяцам для purchase_complete
    public static void printSumCompletedTransactionsByClientAndMonth(List<Transaction> transactions) {
        Map<String, Map<String, Double>> sumByClientAndMonth = transactions.stream()
                .filter(t -> t.getGeneralStatus().equals("purchase_complete")) // Фильтрация по статусу
                .collect(Collectors.groupingBy(
                        Transaction::getEmail, // группировка по клиенту
                        Collectors.groupingBy(
                                t -> t.getCreatedAt().substring(0, 7), // группировка по месяцу (yyyy-MM)
                                Collectors.summingDouble(Transaction::getAmount) // сумма транзакций
                        )
                ));

        // Выводим результаты, отсортированные по email пользователя и затем по месяцу
        sumByClientAndMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Сортировка по email клиента
                .forEach(entry -> {
                    String userEmail = entry.getKey();
                    Map<String, Double> transactionsByMonth = entry.getValue();

                    System.out.println("Client: " + userEmail);

                    // Сортировка по месяцу (yyyy-MM)
                    transactionsByMonth.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey()) // Сортировка по ключу (yyyy-MM)
                            .forEach(monthEntry -> {
                                String month = monthEntry.getKey();
                                Double totalAmount = monthEntry.getValue();
                                System.out.println("Month: " + month + ", Total Amount: " + totalAmount);
                            });
                    System.out.println();
                });
    }

    // Метод для вычисления и вывода суммы транзакций по клиентам и месяцам для purchase_complete и purchase_declined
    public static void printSumAllTransactionsByClientAndMonth(List<Transaction> transactions) {
        Map<String, Map<String, Double>> sumByClientAndMonth = transactions.stream()
                .filter(t -> t.getGeneralStatus().equals("purchase_complete") || t.getGeneralStatus().equals("purchase_declined")) // Фильтрация по статусам
                .collect(Collectors.groupingBy(
                        Transaction::getEmail, // группировка по клиенту
                        Collectors.groupingBy(
                                t -> t.getCreatedAt().substring(0, 7), // группировка по месяцу (yyyy-MM)
                                Collectors.summingDouble(Transaction::getAmount) // сумма транзакций
                        )
                ));

        // Выводим результаты, отсортированные по email пользователя и затем по месяцу
        sumByClientAndMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Сортировка по email клиента
                .forEach(entry -> {
                    String userEmail = entry.getKey();
                    Map<String, Double> transactionsByMonth = entry.getValue();

                    System.out.println("Client: " + userEmail);

                    // Сортировка по месяцу (yyyy-MM)
                    transactionsByMonth.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey()) // Сортировка по ключу (yyyy-MM)
                            .forEach(monthEntry -> {
                                String month = monthEntry.getKey();
                                Double totalAmount = monthEntry.getValue();
                                System.out.println("Month: " + month + ", Total Amount: " + totalAmount);
                            });
                    System.out.println();
                });
    }
}
