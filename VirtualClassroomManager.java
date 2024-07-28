//VIRTUAL CLASSROOM MANAGER


logger.java 


public class Logger {
    public static void log(String message) {
        System.out.println(message);
    }
}



//***********************
classroom.java

import java.util.ArrayList;
import java.util.List;

public class Classroom {
    private String name;
    private List<Student> students;
    private List<Assignment> assignments;

    public Classroom(String name) {
        this.name = name;
        this.students = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addStudent(Student student) {
        students.add(student);
        Logger.log("Student " + student.getName() + " has been enrolled in " + name + ".");
    }

    public List<Student> listStudents() {
        return students;
    }

    public void scheduleAssignment(Assignment assignment) {
        assignments.add(assignment);
        Logger.log("Assignment for " + name + " has been scheduled.");
    }

    public List<Assignment> listAssignments() {
        return assignments;
    }
}
//********************************
Student.java

public class Student {
    private String studentId;
    private String name;

    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public String getId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public void submitAssignment(Classroom classroom, Assignment assignment, String submissionDetails) {
        Submission submission = new Submission(this, assignment, submissionDetails);
        Logger.log("Assignment submitted by Student " + studentId + " in " + classroom.getName() + ".");
    }
}
//******************************
Assignment.java


import java.util.Date;

public class Assignment {
    private String details;
    private Date dueDate;

    public Assignment(String details, Date dueDate) {
        this.details = details;
        this.dueDate = dueDate;
    }

    public String getDetails() {
        return details;
    }

    public Date getDueDate() {
        return dueDate;
    }
}
//*******************************************
Submission.java


import java.util.Date;

public class Submission {
    private Student student;
    private Assignment assignment;
    private String submissionDetails;
    private Date submittedOn;

    public Submission(Student student, Assignment assignment, String submissionDetails) {
        this.student = student;
        this.assignment = assignment;
        this.submissionDetails = submissionDetails;
        this.submittedOn = new Date();
    }

    public String getSubmissionDetails() {
        return submissionDetails;
    }

    public Date getSubmittedOn() {
        return submittedOn;
    }
}
//**************************************************
ClassroomManager.java

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassroomManager {
    private List<Classroom> classrooms;

    public ClassroomManager() {
        this.classrooms = new ArrayList<>();
    }

    public void addClassroom(String name) {
        classrooms.add(new Classroom(name));
        Logger.log("Classroom " + name + " has been created.");
    }

    public void removeClassroom(String name) {
        classrooms.removeIf(classroom -> classroom.getName().equals(name));
        Logger.log("Classroom " + name + " has been removed.");
    }

    public List<Classroom> listClassrooms() {
        return classrooms;
    }

    public void scheduleAssignment(String className, String assignmentDetails, Date dueDate) {
        for (Classroom classroom : classrooms) {
            if (classroom.getName().equals(className)) {
                classroom.scheduleAssignment(new Assignment(assignmentDetails, dueDate));
                return;
            }
        }
        Logger.log("Classroom " + className + " not found.");
    }

    public void addStudent(String studentId, String studentName, String className) {
        for (Classroom classroom : classrooms) {
            if (classroom.getName().equals(className)) {
                classroom.addStudent(new Student(studentId, studentName));
                return;
            }
        }
        Logger.log("Classroom " + className + " not found.");
    }

    public void submitAssignment(String studentId, String className, String assignmentDetails, String submissionDetails) {
        for (Classroom classroom : classrooms) {
            if (classroom.getName().equals(className)) {
                for (Student student : classroom.listStudents()) {
                    if (student.getId().equals(studentId)) {
                        for (Assignment assignment : classroom.listAssignments()) {
                            if (assignment.getDetails().equals(assignmentDetails)) {
                                student.submitAssignment(classroom, assignment, submissionDetails);
                                return;
                            }
                        }
                    }
                }
            }
        }
        Logger.log("Submission failed: Classroom or Student or Assignment not found.");
    }

    public void listStudents(String className) {
        for (Classroom classroom : classrooms) {
            if (classroom.getName().equals(className)) {
                Logger.log("Students in " + className + ":");
                for (Student student : classroom.listStudents()) {
                    Logger.log(" - " + student.getName() + " (ID: " + student.getId() + ")");
                }
                return;
            }
        }
        Logger.log("Classroom " + className + " not found.");
    }
}
//*************************************************
Command.java

public interface Command {
    void execute(String[] args);
}

//***********************************************
 AddClassroomCommand.java

public class AddClassroomCommand implements Command {
    private ClassroomManager manager;

    public AddClassroomCommand(ClassroomManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            Logger.log("Invalid command format. Usage: add_classroom <class_name>");
            return;
        }
        manager.addClassroom(args[0]);
    }
}
//*****************************************************
RemoveClassroomCommand.java


public class RemoveClassroomCommand implements Command {
    private ClassroomManager manager;

    public RemoveClassroomCommand(ClassroomManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            Logger.log("Invalid command format. Usage: remove_classroom <class_name>");
            return;
        }
        manager.removeClassroom(args[0]);
    }
}

//*******************************************************
ListClassroomsCommand.java


public class ListClassroomsCommand implements Command {
    private ClassroomManager manager;

    public ListClassroomsCommand(ClassroomManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(String[] args) {
        Logger.log("Classrooms:");
        for (Classroom classroom : manager.listClassrooms()) {
            Logger.log(" - " + classroom.getName());
        }
    }
}
//*********************************************************
ListStudentsCommand.java

public class ListStudentsCommand implements Command {
    private ClassroomManager manager;

    public ListStudentsCommand(ClassroomManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            Logger.log("Invalid command format. Usage: list_students <class_name>");
            return;
        }
        manager.listStudents(args[0]);
    }
}
//***************************************************
ScheduleAssignmentCommand.java


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleAssignmentCommand implements Command {
    private ClassroomManager manager;
    private SimpleDateFormat dateFormat;

    public ScheduleAssignmentCommand(ClassroomManager manager) {
        this.manager = manager;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 3) {
            Logger.log("Invalid command format. Usage: schedule_assignment <class_name> <assignment_details> <due_date>");
            return;
        }
        try {
            Date dueDate = dateFormat.parse(args[2]);
            manager.scheduleAssignment(args[0], args[1], dueDate);
        } catch (ParseException e) {
            Logger.log("Invalid date format. Please use yyyy-MM-dd.");
        }
    }
}
//**********************************************************
AddStudentCommand.java


public class AddStudentCommand implements Command {
    private ClassroomManager manager;
    private String studentId;
    private String studentName;

    public AddStudentCommand(ClassroomManager manager, String studentId, String studentName) {
        this.manager = manager;
        this.studentId = studentId;
        this.studentName = studentName;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            Logger.log("Invalid command format. Usage: join_classroom <class_name>");
            return;
        }
        manager.addStudent(studentId, studentName, args[0]);
    }
}
//***************************************************************
SubmitAssignmentCommand.java 


public class SubmitAssignmentCommand implements Command {
    private ClassroomManager manager;
    private String studentId;

    public SubmitAssignmentCommand(ClassroomManager manager, String studentId) {
        this.manager = manager;
        this.studentId = studentId;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 3) {
            Logger.log("Invalid command format. Usage: submit_assignment <class_name> <assignment_details> <submission_details>");
            return;
        }
        manager.submitAssignment(studentId, args[0], args[1], args[2]);
    }
}
//****************************************************************************
Main.java


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ClassroomManager manager = new ClassroomManager();
        Scanner scanner = new Scanner(System.in);

        startProcess(manager, scanner);
    }

    private static void startProcess(ClassroomManager manager, Scanner scanner) {
        System.out.print("Are you a teacher or a student? ");
        String role = scanner.nextLine().trim().toLowerCase();

        if ("teacher".equals(role)) {
            processTeacherCommands(manager, scanner);
        } else if ("student".equals(role)) {
            if (manager.listClassrooms().isEmpty()) {
                System.out.println("No classrooms available. Please wait for a teacher to create a classroom.");
                startProcess(manager, scanner);
            } else {
                System.out.print("Enter your student ID: ");
                String studentId = scanner.nextLine();
                System.out.print("Enter your name: ");
                String studentName = scanner.nextLine();
                processStudentCommands(manager, scanner, studentId, studentName);
            }
        } else {
            System.out.println("Invalid role. Please enter 'teacher' or 'student'.");
            startProcess(manager, scanner);
        }
    }

    private static void processTeacherCommands(ClassroomManager manager, Scanner scanner) {
        Map<String, Command> commands = new HashMap<>();
        commands.put("add_classroom", new AddClassroomCommand(manager));
        commands.put("remove_classroom", new RemoveClassroomCommand(manager));
        commands.put("list_classrooms", new ListClassroomsCommand(manager));
        commands.put("schedule_assignment", new ScheduleAssignmentCommand(manager));
        commands.put("list_students", new ListStudentsCommand(manager));

        System.out.print("Enter the command (add_classroom, remove_classroom, list_classrooms, schedule_assignment, list_students): ");
        String commandKey = scanner.nextLine().trim();
        System.out.print("Enter command arguments separated by spaces: ");
        String[] commandArgs = scanner.nextLine().trim().split(" ");

        if (commands.containsKey(commandKey)) {
            commands.get(commandKey).execute(commandArgs);
        } else {
            System.out.println("Invalid command. Please try again.");
        }

        System.out.print("Do you want to perform another teacher action? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            processTeacherCommands(manager, scanner);
        } else {
            startProcess(manager, scanner);
        }
    }

    private static void processStudentCommands(ClassroomManager manager, Scanner scanner, String studentId, String studentName) {
        Map<String, Command> commands = new HashMap<>();
        commands.put("join_classroom", new AddStudentCommand(manager, studentId, studentName));
        commands.put("submit_assignment", new SubmitAssignmentCommand(manager, studentId));

        System.out.print("Enter the classroom name to join: ");
        String className = scanner.nextLine();
        commands.get("join_classroom").execute(new String[]{className});

        System.out.print("Do you want to submit an assignment for this class? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            System.out.print("Enter the assignment details: ");
            String assignmentDetails = scanner.nextLine();
            System.out.print("Enter your submission details: ");
            String submissionDetails = scanner.nextLine();
            commands.get("submit_assignment").execute(new String[]{className, assignmentDetails, submissionDetails});
        }

        System.out.print("Do you want to join another classroom? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            processStudentCommands(manager, scanner, studentId, studentName);
        } else {
            startProcess(manager, scanner);
        }
    }
}
//*************************************************************************



