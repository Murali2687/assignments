import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class EmployeeTest {

    private List<Employee> getSampleEmployees() {
        return Arrays.asList(
            new Employee(123, "Joe", "Doe", 60000, null),
            new Employee(124, "Martin", "Chekov", 45000, 123),
            new Employee(125, "Bob", "Ronstad", 47000, 123),
            new Employee(300, "Alice", "Hasacat", 50000, 124),
            new Employee(305, "Brett", "Hardleaf", 34000, 300)
        );
    }

    @Test
    public void testFindUnderpaidManagers() {
        List<Employee> employees = getSampleEmployees();
        Employee.findUnderpaidManagers(employees);
        // Here, we could capture System.out and verify the expected output.
    }

    @Test
    public void testFindOverpaidManagers() {
        List<Employee> employees = getSampleEmployees();
        Employee.findOverpaidManagers(employees);
        // Capture output and check for expected results.
    }

    @Test
    public void testFindLongReportingLines() {
        List<Employee> employees = getSampleEmployees();
        Employee.findLongReportingLines(employees, 2);
        // Check output for employees exceeding reporting depth.
    }

    @Test
    public void testReadEmployeesFromCSV() {
        List<Employee> employees = Employee.readEmployeesFromCSV("employees.csv");
        assertNotNull(employees);
        assertFalse(employees.isEmpty());
    }

}
