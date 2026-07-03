package org.example;

import org.example.entities.Employee;
import org.example.repository.EmployeeRepository;
import org.example.service.EmployeeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
        EmployeeService service = ctx.getBean(EmployeeService.class);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1) Create employee");
            System.out.println("2) Update employee");
            System.out.println("3) Simulate optimistic lock conflict");
            System.out.println("4) Soft delete employee");
            System.out.println("5) List employees");
            System.out.println("6) List deleted employees (admin)");
            System.out.println("7) Restore employee");
            System.out.println("8) Force delete (hard)");
            System.out.println("9) Exit");
            System.out.print("Choose: ");
            String opt = sc.nextLine().trim();
            try {
                switch (opt) {
                    case "1": {
                        System.out.print("Email: "); String email = sc.nextLine();
                        System.out.print("Salary: "); BigDecimal sal = new BigDecimal(sc.nextLine());
                        Employee e = new Employee();
                        e.setEmail(email);
                        e.setSalary(sal);
                        e.setDepartmentId(1);
                        Employee created = service.create(e, "console");
                        System.out.println("Created: id=" + created.getId());
                        break;
                    }
                    case "2": {
                        System.out.print("Employee id: "); Integer id = Integer.valueOf(sc.nextLine());
                        System.out.print("New email: "); String newEmail = sc.nextLine();
                        service.update(id, emp -> emp.setEmail(newEmail), "console");
                        System.out.println("Updated (or conflict reported).");
                        break;
                    }
                    case "3": {
                        System.out.print("Employee id: "); Integer id = Integer.valueOf(sc.nextLine());
                        try {
                            service.simulateOptimisticConflict(id);
                        } catch (Exception ex) {
                            System.err.println("Conflict: " + ex.getMessage());
                        }
                        break;
                    }
                    case "4": {
                        System.out.print("Employee id: "); Integer id = Integer.valueOf(sc.nextLine());
                        service.softDelete(id, "consoleUser");
                        System.out.println("Soft deleted.");
                        break;
                    }
                    case "5": {
                        List<Employee> list = service.listAll();
                        list.forEach(emp -> System.out.println(emp.getId() + " | " + emp.getEmail() + " | ver=" + emp.getVersion()));
                        break;
                    }
                    case "6": {
                        List<Employee> del = service.listDeleted();
                        del.forEach(emp -> System.out.println(emp.getId() + " | " + emp.getEmail() + " | deleted_at=" + emp.getDeletedAt()));
                        break;
                    }
                    case "7": {
                        System.out.print("Employee id: "); Integer id = Integer.valueOf(sc.nextLine());
                        service.restore(id, "consoleAdmin");
                        System.out.println("Restored.");
                        break;
                    }
                    case "8": {
                        System.out.print("Employee id: "); Integer id = Integer.valueOf(sc.nextLine());
                        service.hardDelete(id);
                        System.out.println("Hard deleted.");
                        break;
                    }
                    case "9":
                        System.out.println("Bye");
                        System.exit(0);
                    default:
                        System.out.println("Unknown");
                }
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
                ex.printStackTrace(System.err);
            }
        }
    }
}