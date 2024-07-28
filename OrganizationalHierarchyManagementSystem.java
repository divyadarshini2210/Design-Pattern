//Composite Pattern Implementation




//"Organizational Hierarchy Management System"



import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Component Interface
interface Employee {
    void display(String indent);
    String getName(); // For checking if an employee is already in another group
}

// Leaf Class
class IndividualEmployee implements Employee {
    private String name;
    private String position;

    public IndividualEmployee(String name, String position) {
        this.name = name;
        this.position = position;
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "Employee: " + name + ", Position: " + position);
    }

    @Override
    public String getName() {
        return name;
    }

    // Getter for position
    public String getPosition() {
        return position;
    }
}

// Composite Class
class Group implements Employee {
    private String name;
    private IndividualEmployee leader;
    private List<Employee> employees = new ArrayList<>();
    private static List<Employee> allEmployees = new ArrayList<>(); // Track all employees

    public Group(String name) {
        this.name = name;
    }

    public void setLeader(IndividualEmployee leader) {
        this.leader = leader;
    }

    public void addEmployee(Employee employee) {
        if (employee instanceof IndividualEmployee) {
            for (Employee emp : allEmployees) {
                if (emp.getName().equals(employee.getName())) {
                    System.out.println("Error: Employee " + employee.getName() + " is already in another group.");
                    return;
                }
            }
        }
        employees.add(employee);
        if (employee instanceof IndividualEmployee) {
            allEmployees.add(employee); // Track this employee as well
        }
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        if (employee instanceof IndividualEmployee) {
            allEmployees.remove(employee); // Remove this employee from the global list
        }
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "Group: " + name);
        if (leader != null) {
            System.out.println(indent + "  Leader: " + leader.getName() + ", Position: " + leader.getPosition());
        }
        for (Employee employee : employees) {
            employee.display(indent + "  ");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    // Display leader
    public void displayLeader() {
        if (leader != null) {
            System.out.println("Leader of " + name + ": " + leader.getName() + ", Position: " + leader.getPosition());
        } else {
            System.out.println("No leader assigned for group " + name);
        }
    }

    // Display members along with leader's name
    public void displayMembers() {
        System.out.println("Members of " + name + ":");
        if (leader != null) {
            System.out.println("  Leader: " + leader.getName() + ", Position: " + leader.getPosition());
        }
        for (Employee employee : employees) {
            employee.display("  ");
        }
    }

    // Get list of employees in this group
    public List<Employee> getEmployees() {
        return employees;
    }
}

// Command Interface
interface Command {
    void execute();
}

// Concrete Command for adding an IndividualEmployee
class AddIndividualEmployeeCommand implements Command {
    private Group parentGroup;
    private Scanner scanner;
    private List<Group> allGroups;

    public AddIndividualEmployeeCommand(Group parentGroup, Scanner scanner, List<Group> allGroups) {
        this.parentGroup = parentGroup;
        this.scanner = scanner;
        this.allGroups = allGroups;
    }

    @Override
    public void execute() {
        // Ask for the group to add the employee
        Group selectedGroup = selectGroup();
        if (selectedGroup == null) {
            System.out.println("No valid group selected. Operation canceled.");
            return;
        }

        System.out.print("Enter employee name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter employee position: ");
        String position = scanner.nextLine().trim();

        IndividualEmployee employee = new IndividualEmployee(name, position);
        selectedGroup.addEmployee(employee);
    }

    private Group selectGroup() {
        System.out.println("Select a group to add the employee:");
        for (int i = 0; i < allGroups.size(); i++) {
            System.out.println((i + 1) + ". " + allGroups.get(i).getName());
        }

        System.out.print("Enter group number: ");
        int choice = Integer.parseInt(scanner.nextLine().trim());

        if (choice > 0 && choice <= allGroups.size()) {
            return allGroups.get(choice - 1);
        } else {
            System.out.println("Invalid group number.");
            return null;
        }
    }
}

// Concrete Command for adding a Group
class AddGroupCommand implements Command {
    private Group parentGroup;
    private Scanner scanner;
    private List<Group> allGroups;

    public AddGroupCommand(Group parentGroup, Scanner scanner, List<Group> allGroups) {
        this.parentGroup = parentGroup;
        this.scanner = scanner;
        this.allGroups = allGroups;
    }

    @Override
    public void execute() {
        System.out.print("Enter group name: ");
        String groupName = scanner.nextLine().trim();

        Group newGroup = new Group(groupName);

        // Set leader if required
        System.out.print("Enter leader name (or press Enter to skip): ");
        String leaderName = scanner.nextLine().trim();
        if (!leaderName.isEmpty()) {
            System.out.print("Enter leader position: ");
            String leaderPosition = scanner.nextLine().trim();
            IndividualEmployee leader = new IndividualEmployee(leaderName, leaderPosition);
            newGroup.setLeader(leader);
        }

        parentGroup.addEmployee(newGroup);
        allGroups.add(newGroup);

        System.out.println("Adding employees and groups to " + groupName);
        handleGroup(newGroup);
    }

    private void handleGroup(Group group) {
        List<Command> commands = List.of(
            new AddIndividualEmployeeCommand(group, scanner, allGroups),
            new AddGroupCommand(group, scanner, allGroups)
        );

        System.out.println("1. Add Individual Employee");
        System.out.println("2. Add Group");
        System.out.println("3. Display Leader");
        System.out.println("4. Display Members");
        System.out.println("5. Done");

        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            commands.get(0).execute();
            handleGroup(group); // Continue handling after adding an individual
        } else if (choice.equals("2")) {
            commands.get(1).execute();
            handleGroup(group); // Continue handling after adding a group
        } else if (choice.equals("3")) {
            group.displayLeader();
            handleGroup(group); // Continue handling after displaying the leader
        } else if (choice.equals("4")) {
            group.displayMembers();
            handleGroup(group); // Continue handling after displaying members
        } else if (choice.equals("5")) {
            return; // End the current group addition
        } else {
            System.out.println("Invalid choice. Please enter 1, 2, 3, 4, or 5.");
            handleGroup(group); // Retry
        }
    }
}

// Main Class
public class OrganizationalChartDemo {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Group company = new Group("Company");
        List<Group> allGroups = new ArrayList<>();
        allGroups.add(company);
        handleGroup(company, allGroups);

        // Display the organizational chart
        System.out.println("Organizational Chart:");
        company.display("");
    }

    private static void handleGroup(Group parentGroup, List<Group> allGroups) {
        List<Command> commands = List.of(
            new AddIndividualEmployeeCommand(parentGroup, scanner, allGroups),
            new AddGroupCommand(parentGroup, scanner, allGroups)
        );

        System.out.println("1. Add Individual Employee");
        System.out.println("2. Add Group");
        System.out.println("3. Display Leader");
        System.out.println("4. Display Members");
        System.out.println("5. Done");

        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            commands.get(0).execute();
            handleGroup(parentGroup, allGroups); // Continue handling after adding an individual
        } else if (choice.equals("2")) {
            commands.get(1).execute();
            handleGroup(parentGroup, allGroups); // Continue handling after adding a group
        } else if (choice.equals("3")) {
            parentGroup.displayLeader();
            handleGroup(parentGroup, allGroups); // Continue handling after displaying the leader
        } else if (choice.equals("4")) {
            parentGroup.displayMembers();
            handleGroup(parentGroup, allGroups); // Continue handling after displaying members
        } else if (choice.equals("5")) {
            return; // End the current group addition
        } else {
            System.out.println("Invalid choice. Please enter 1, 2, 3, 4, or 5.");
            handleGroup(parentGroup, allGroups); // Retry
        }
    }
}


