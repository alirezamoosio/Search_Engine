package ir.nimbo.searchengine;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
//FindInStreams.java
public class FindInStreams {

    private static List<Employee> employeeList = Arrays.asList(
            new Employee("Tom Jones", 5),
            new Employee("Harry Major", 25),
            new Employee("Ethan Hardy", 65),
            new Employee("Nancy Smith", 22),
            new Employee("Deborah Sprightly", 29),
            new Employee("Billy Kid", 22),
            new Employee("George King",44),
            new Employee("Annie Barrey", 19));
    public static void main(String[] args) {
        Optional<Employee> anyEmpAbove40 = employeeList.stream()
                .filter(emp -> emp.getAge() > 40)
                .findFirst();
        anyEmpAbove40.ifPresent(employee -> System.out.println("Any Employee above age 40: " + employee));
    }
}
class Employee{
    private String name;
    private Integer age;
    public Employee(String name, Integer age){
        this.name=name;
        this.age=age;
    }

    public String getName() {
        System.out.println("vay");
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        System.out.println("vay");

        return age;
    }

    public void setAge(Integer age) {
        System.out.println("vay");
        this.age = age;
    }

    public String toString(){
        return "Employee Name:"+this.name
                +"  Age:"+this.age;
    }
}
