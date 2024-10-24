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
        //printSumAllTransactionsByClientAndMonth(transactions);
