import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private int salary;
    private Integer managerId;

    public Employee(int id, String firstName, String lastName, int salary, Integer managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    public int getId() {
        return id;
    }

    public int getSalary() {
        return salary;
    }

    public Integer getManagerId() {
        return managerId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", managerId=" + (managerId != null ? managerId : "None") +
                '}';
    }

    public static List<Employee> readEmployeesFromCSV(String filePath) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines.skip(1) // Skip header
                    .map(line -> line.split(","))
                    .map(parts -> new Employee(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            parts[2],
                            Integer.parseInt(parts[3]),
                            parts.length > 4 && !parts[4].isEmpty() ? Integer.parseInt(parts[4].trim()) : null
                    ))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static void findUnderpaidManagers(List<Employee> employees) {
        Map<Integer, Integer> managerSalaries = employees.stream()
                .collect(Collectors.toMap(Employee::getId, Employee::getSalary));

        Map<Integer, Integer> maxSubordinateSalaries = employees.stream()
                .filter(e -> e.getManagerId() != null)
                .collect(Collectors.groupingBy(Employee::getManagerId, 
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(Employee::getSalary)),
                                opt -> opt.map(Employee::getSalary).orElse(0))));

        maxSubordinateSalaries.forEach((managerId, maxSubSalary) -> {
            int managerSalary = managerSalaries.getOrDefault(managerId, 0);
            if (managerSalary < maxSubSalary) {
                System.out.println("Manager ID " + managerId + " earns " + managerSalary + ", should earn at least " + maxSubSalary + ", underpaid by " + (maxSubSalary - managerSalary));
            }
        });
    }

    public static void findOverpaidManagers(List<Employee> employees) {
        Map<Integer, Integer> managerSalaries = employees.stream()
                .collect(Collectors.toMap(Employee::getId, Employee::getSalary));

        Map<Integer, Integer> maxSubordinateSalaries = employees.stream()
                .filter(e -> e.getManagerId() != null)
                .collect(Collectors.groupingBy(Employee::getManagerId, 
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(Employee::getSalary)),
                                opt -> opt.map(Employee::getSalary).orElse(0))));

        maxSubordinateSalaries.forEach((managerId, maxSubSalary) -> {
            int managerSalary = managerSalaries.getOrDefault(managerId, 0);
            if (managerSalary > maxSubSalary) {
                System.out.println("Manager ID " + managerId + " earns " + managerSalary + ", should earn at most " + maxSubSalary + ", overpaid by " + (managerSalary - maxSubSalary));
            }
        });
    }

    public static void findLongReportingLines(List<Employee> employees, int maxDepth) {
        Map<Integer, Employee> employeeMap = employees.stream().collect(Collectors.toMap(Employee::getId, e -> e));

        for (Employee employee : employees) {
            int depth = 0;
            Integer managerId = employee.getManagerId();
            while (managerId != null && employeeMap.containsKey(managerId)) {
                depth++;
                managerId = employeeMap.get(managerId).getManagerId();
            }
            if (depth > maxDepth) {
                System.out.println("Employee ID " + employee.getId() + " has a reporting line too long by " + (depth - maxDepth));
            }
        }
    }

    public static void main(String[] args) {
        String filePath = ".\\src\\employee.csv";
        List<Employee> employees = readEmployeesFromCSV(filePath);
        employees.forEach(System.out::println);
        findUnderpaidManagers(employees);
        findOverpaidManagers(employees);
        findLongReportingLines(employees, 2); // Adjust maxDepth as needed
    }
}
