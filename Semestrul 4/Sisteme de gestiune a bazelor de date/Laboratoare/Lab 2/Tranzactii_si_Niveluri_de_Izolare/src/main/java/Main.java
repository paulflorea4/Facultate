import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {

    static String connStr =
            "jdbc:sqlserver://FLOREA\\SQLEXPRESS:1433;" +
                    "databaseName=SGBD_Lab2;" +
                    "encrypt=true;trustServerCertificate=true;" +
                    "user=sgbd;" +
                    "password=flo;";

    private static final DateTimeFormatter TX_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static void logTransaction(String message) {
        System.out.println(LocalTime.now().format(TX_TIME_FORMAT) + " " + message);
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1 - Dirty Read");
            System.out.println("2 - Non-Repeatable Read");
            System.out.println("3 - Phantom Read");
            System.out.println("4 - Lost Update");
            System.out.println("5 - Deadlock");
            System.out.println("6 - Auto-Commit");
            System.out.println("7 - Batch Commit");
            System.out.println("8 - Single Transaction");
            System.out.println("9 - Average Time");
            System.out.println("10 - Reset Table");
            System.out.println("11 - Prevent Dirty Read");
            System.out.println("12 - Prevent Non-Repeatable Read");
            System.out.println("13 - Prevent Phantom Read");
            System.out.println("14 - Prevent Deadlock");
            System.out.println("0 - Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> dirtyRead();
                case 2 -> nonRepeatableRead();
                case 3 -> phantomRead();
                case 4 -> lostUpdate();
                case 5 -> deadlock();
                case 6 -> testAutoCommit();
                case 7 -> testBatchCommit();
                case 8 -> testSingleTransaction();
                case 9 -> averageTime();
                case 10 -> clearTable();
                case 11 -> preventDirtyRead();
                case 12 -> preventNonRepeatableRead();
                case 13 -> preventPhantomRead();
                case 14 -> preventDeadlock();
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
            }
        }
    }

    static void dirtyRead() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try (Connection conn = DriverManager.getConnection(connStr)) {

                conn.setAutoCommit(false);

                PreparedStatement stmt =
                        conn.prepareStatement("UPDATE employees SET salary = 10000 WHERE id = 1");

                stmt.executeUpdate();
                logTransaction("A: updated salary to 10000 but not committed");

                Thread.sleep(5000);

                conn.rollback();
                logTransaction("A: rollback");

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try {
                Thread.sleep(1000);

                Connection conn = DriverManager.getConnection(connStr);
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT salary FROM employees WHERE id = 1");

                if (rs.next())
                    logTransaction("B reads: " + rs.getInt(1));

                conn.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    static void preventDirtyRead() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try (Connection conn = DriverManager.getConnection(connStr)) {

                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

                PreparedStatement stmt =
                        conn.prepareStatement("UPDATE employees SET salary = 10000 WHERE id = 1");

                stmt.executeUpdate();
                logTransaction("A: updated salary to 10000 but not committed");

                Thread.sleep(5000);

                conn.rollback();
                logTransaction("A: rollback");

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try {
                Thread.sleep(1000);

                Connection conn = DriverManager.getConnection(connStr);
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT salary FROM employees WHERE id = 1");

                if (rs.next())
                    logTransaction("B reads: " + rs.getInt(1));

                conn.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    static void nonRepeatableRead() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try (Connection conn = DriverManager.getConnection(connStr)) {

                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

                Statement stmt = conn.createStatement();

                ResultSet rs1 = stmt.executeQuery("SELECT salary FROM employees WHERE id = 1");
                rs1.next();
                logTransaction("A first read: " + rs1.getInt(1));

                Thread.sleep(4000);

                ResultSet rs2 = stmt.executeQuery("SELECT salary FROM employees WHERE id = 1");
                rs2.next();
                logTransaction("A second read: " + rs2.getInt(1));

                conn.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try {
                Thread.sleep(1000);

                Connection conn = DriverManager.getConnection(connStr);
                conn.setAutoCommit(false);

                Statement stmt = conn.createStatement();
                stmt.executeUpdate("UPDATE employees SET salary = 12000 WHERE id = 1");

                conn.commit();
                logTransaction("B updated salary to 12000");

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    static void preventNonRepeatableRead() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try (Connection conn = DriverManager.getConnection(connStr)) {

                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

                Statement stmt = conn.createStatement();

                ResultSet rs1 = stmt.executeQuery("SELECT salary FROM employees WHERE id = 1");
                rs1.next();
                logTransaction("A first read: " + rs1.getInt(1));

                Thread.sleep(4000);

                ResultSet rs2 = stmt.executeQuery("SELECT salary FROM employees WHERE id = 1");
                rs2.next();
                logTransaction("A second read: " + rs2.getInt(1));

                conn.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try {
                Thread.sleep(1000);

                Connection conn = DriverManager.getConnection(connStr);
                conn.setAutoCommit(false);

                Statement stmt = conn.createStatement();
                stmt.executeUpdate("UPDATE employees SET salary = 12000 WHERE id = 1");

                conn.commit();
                logTransaction("B updated salary to 12000");

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    static void phantomRead() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try (Connection conn = DriverManager.getConnection(connStr)) {

                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

                Statement stmt = conn.createStatement();

                ResultSet rs1 = stmt.executeQuery(
                        "SELECT COUNT(*) FROM employees WHERE department_id = 5");
                rs1.next();
                logTransaction("A first count: " + rs1.getInt(1));

                Thread.sleep(4000);

                ResultSet rs2 = stmt.executeQuery(
                        "SELECT COUNT(*) FROM employees WHERE department_id = 5");
                rs2.next();
                logTransaction("A second count: " + rs2.getInt(1));

                conn.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try {
                Thread.sleep(1000);

                Connection conn = DriverManager.getConnection(connStr);
                conn.setAutoCommit(false);

                Statement stmt = conn.createStatement();
                stmt.executeUpdate(
                        "INSERT INTO employees(name, salary, department_id) VALUES('New',3000,5)");

                conn.commit();
                logTransaction("B inserted new employee in department 5");

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    static void preventPhantomRead() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try (Connection conn = DriverManager.getConnection(connStr)) {

                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

                Statement stmt = conn.createStatement();

                ResultSet rs1 = stmt.executeQuery(
                        "SELECT COUNT(*) FROM employees WHERE department_id = 5");
                rs1.next();
                logTransaction("A first count: " + rs1.getInt(1));

                Thread.sleep(4000);

                ResultSet rs2 = stmt.executeQuery(
                        "SELECT COUNT(*) FROM employees WHERE department_id = 5");
                rs2.next();
                logTransaction("A second count: " + rs2.getInt(1));

                conn.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try {
                Thread.sleep(1000);

                Connection conn = DriverManager.getConnection(connStr);
                conn.setAutoCommit(false);

                Statement stmt = conn.createStatement();
                stmt.executeUpdate(
                        "INSERT INTO employees(name, salary, department_id) VALUES('New',3000,5)");

                conn.commit();
                logTransaction("B inserted new employee in department 5");

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    static void lostUpdate() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try (Connection conn = DriverManager.getConnection(connStr)) {

                conn.setAutoCommit(false);

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT salary FROM employees WHERE id = 1");
                rs.next();

                int salary = rs.getInt(1);
                int newSalary = salary + 1000;

                Thread.sleep(3000);

                stmt.executeUpdate(
                        "UPDATE employees SET salary = " + newSalary + " WHERE id = 1");

                conn.commit();
                logTransaction("A updated to " + newSalary);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try (Connection conn = DriverManager.getConnection(connStr)) {

                conn.setAutoCommit(false);

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT salary FROM employees WHERE id = 1");
                rs.next();

                int salary = rs.getInt(1);
                int newSalary = salary + 500;

                stmt.executeUpdate(
                        "UPDATE employees SET salary = " + newSalary + " WHERE id = 1");

                conn.commit();
                logTransaction("B updated to " + newSalary);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    static void deadlock() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try (Connection conn = DriverManager.getConnection(connStr)) {

                conn.setAutoCommit(false);

                Statement stmt = conn.createStatement();
                stmt.executeUpdate("UPDATE employees SET salary=6000 WHERE id=1");

                Thread.sleep(2000);

                stmt.executeUpdate("UPDATE employees SET salary=7000 WHERE id=2");

                conn.commit();

            } catch (SQLException e) {
                logTransaction("A deadlock!");
                logTransaction(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try (Connection conn = DriverManager.getConnection(connStr)) {

                conn.setAutoCommit(false);

                Statement stmt = conn.createStatement();
                stmt.executeUpdate("UPDATE employees SET salary=6000 WHERE id=2");

                Thread.sleep(2000);

                stmt.executeUpdate("UPDATE employees SET salary=7000 WHERE id=1");

                conn.commit();

            } catch (SQLException e) {
                logTransaction("B deadlock!");
                logTransaction(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    static void preventDeadlock() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try (Connection conn = DriverManager.getConnection(connStr)) {

                conn.setAutoCommit(false);

                Statement stmt = conn.createStatement();
                stmt.executeUpdate("UPDATE employees SET salary=6000 WHERE id=1");
                stmt.executeUpdate("UPDATE employees SET salary=7000 WHERE id=2");

                conn.commit();

            } catch (SQLException e) {
                logTransaction("A deadlock!");
                logTransaction(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try (Connection conn = DriverManager.getConnection(connStr)) {

                conn.setAutoCommit(false);

                Statement stmt = conn.createStatement();
                stmt.executeUpdate("UPDATE employees SET salary=6000 WHERE id=1");
                stmt.executeUpdate("UPDATE employees SET salary=7000 WHERE id=2");

                conn.commit();

            } catch (SQLException e) {
                logTransaction("B deadlock!");
                logTransaction(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    static long testAutoCommit() throws Exception {
        clearTable();

        long start = System.currentTimeMillis();
        String sql = "INSERT INTO employees(name,salary,department_id) VALUES(?, 0, 0)";

        try (Connection conn = DriverManager.getConnection(connStr);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < 5000; i++) {
                stmt.setString(1, "Employee" + i);
                stmt.executeUpdate();
            }
        }

        logTransaction("AutoCommit: " + (System.currentTimeMillis() - start));
        return System.currentTimeMillis() - start;
    }

    static long testBatchCommit() throws Exception {
        clearTable();

        long start = System.currentTimeMillis();

        String sql = "INSERT INTO employees(name,salary,department_id) VALUES(?, 0, 0)";

        try (Connection conn = DriverManager.getConnection(connStr)) {

            conn.setAutoCommit(false);

            try(PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < 5000; i++) {

                    stmt.setString(1, "Employee" + i);
                    stmt.executeUpdate();

                    if (i % 100 == 0)
                        conn.commit();
                }

                conn.commit();

            } catch(SQLException e) {
                conn.rollback();
                throw e;
            }
        }

        logTransaction("Batch Commit: " + (System.currentTimeMillis() - start));
        return System.currentTimeMillis() - start;
    }

    static long testSingleTransaction() throws Exception {
        clearTable();

        long start = System.currentTimeMillis();
        String sql = "INSERT INTO employees(name,salary,department_id) VALUES(?, 0, 0)";

        try (Connection conn = DriverManager.getConnection(connStr)) {

            conn.setAutoCommit(false);

            try(PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < 5000; i++) {
                    stmt.setString(1, "Employee" + i);
                    stmt.addBatch();
                }
                stmt.executeBatch();
                conn.commit();
            }catch(SQLException e) {
                conn.rollback();
                throw e;
            }
        }

        logTransaction("Single Transaction: " + (System.currentTimeMillis() - start));
        return System.currentTimeMillis() - start;
    }

    static void averageTime() throws Exception {
        long autoCommitTime = 0, batchCommitTime = 0, singleTransactionTime = 0;
        int runs = 5;

        for (int i = 0; i < runs; i++) {
            autoCommitTime += testAutoCommit();
            batchCommitTime += testBatchCommit();
            singleTransactionTime += testSingleTransaction();
        }

        logTransaction("Average AutoCommit: " + (autoCommitTime / runs));
        logTransaction("Average Batch Commit: " + (batchCommitTime / runs));
        logTransaction("Average Single Transaction: " + (singleTransactionTime / runs));
    }

    static void clearTable() throws Exception {
        try (Connection conn = DriverManager.getConnection(connStr);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM employees WHERE id > 3");

            stmt.executeUpdate("UPDATE employees SET salary = 5000 WHERE id = 1");
            stmt.executeUpdate("UPDATE employees SET salary = 6000 WHERE id = 2");
            stmt.executeUpdate("UPDATE employees SET salary = 7000 WHERE id = 3");
        }
    }
}
