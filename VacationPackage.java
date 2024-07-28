//Builder Design Pattern


import java.util.*;
import java.util.logging.*;

// Product Class
class VacationPackage {
    String hotel;
    String flight;
    List<String> activities = new ArrayList<>();

    @Override
    public String toString() {
        return "Vacation Package [Hotel: " + hotel + ", Flight: " + flight + ", Activities: " + activities + "]";
    }
}

// Builder Interface
interface VacationPackageBuilder {
    void setHotel(String hotel);
    void setFlight(String flight);
    void addActivity(String activity);
    VacationPackage build();
}

// Concrete Builder
class CustomVacationPackageBuilder implements VacationPackageBuilder {
    private VacationPackage vacationPackage = new VacationPackage();

    public void setHotel(String hotel) {
        vacationPackage.hotel = hotel;
    }

    public void setFlight(String flight) {
        vacationPackage.flight = flight;
    }

    public void addActivity(String activity) {
        vacationPackage.activities.add(activity);
    }

    public VacationPackage build() {
        return vacationPackage;
    }
}

// Director Class
class VacationPackageDirector {
    private VacationPackageBuilder builder;

    public VacationPackageDirector(VacationPackageBuilder builder) {
        this.builder = builder;
    }

    public void constructPackage() {
        // Can be customized or pre-defined
    }
}

// Main Class
public class BuilderDemo {

    private static final Logger logger = Logger.getLogger(BuilderDemo.class.getName());
    private static final List<VacationPackage> vacationPackages = new ArrayList<>();

    public static void main(String[] args) {
        configureLogging();
        Scanner scanner = new Scanner(System.in);

        // Command Map for user actions
        Map<String, Runnable> commandMap = new HashMap<>();
        
        commandMap.put("Book", () -> bookVacationPackage(scanner));
        commandMap.put("Cancel", () -> cancelVacationPackage(scanner));
        commandMap.put("Display", BuilderDemo::displayVacationPackages);
        commandMap.put("Exit", () -> exitApplication(scanner));

        while (true) {
            System.out.println("Enter command (Book, Cancel, Display, Exit): ");
            String command = scanner.nextLine().trim();
            Runnable action = commandMap.getOrDefault(command, () -> System.out.println("Invalid command."));
            action.run();
        }
    }

    private static void configureLogging() {
        try {
            LogManager.getLogManager().readConfiguration(BuilderDemo.class.getResourceAsStream("/logging.properties"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error configuring logging", e);
        }
    }

    private static void bookVacationPackage(Scanner scanner) {
        CustomVacationPackageBuilder builder = new CustomVacationPackageBuilder();
        VacationPackageDirector director = new VacationPackageDirector(builder);

        try {
            String hotel = promptUser(scanner, "Enter hotel: ");
            builder.setHotel(hotel);

            String flight = promptUser(scanner, "Enter flight: ");
            builder.setFlight(flight);

            List<String> activities = getActivitiesFromUser(scanner);
            for (String activity : activities) {
                builder.addActivity(activity);
            }

            // Build and store the custom vacation package
            VacationPackage vacationPackage = builder.build();
            vacationPackages.add(vacationPackage);
            System.out.println("Custom Vacation Package: " + vacationPackage);

        } catch (Exception e) {
            System.out.println("An error occurred during the booking process.");
            logger.log(Level.SEVERE, "Error in vacation package booking", e);
        }
    }

    private static void cancelVacationPackage(Scanner scanner) {
        if (vacationPackages.isEmpty()) {
            System.out.println("No vacation packages to cancel.");
            return;
        }

        displayVacationPackages();
        System.out.print("Enter the index of the package to cancel: ");
        int index = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        if (index >= 0 && index < vacationPackages.size()) {
            vacationPackages.remove(index);
            System.out.println("Vacation package at index " + index + " has been canceled.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void displayVacationPackages() {
        if (vacationPackages.isEmpty()) {
            System.out.println("No vacation packages to display.");
        } else {
            for (int i = 0; i < vacationPackages.size(); i++) {
                System.out.println("Index " + i + ": " + vacationPackages.get(i));
            }
        }
    }

    private static void exitApplication(Scanner scanner) {
        System.out.println("Exiting...");
        scanner.close();
        System.exit(0);
    }

    private static String promptUser(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static List<String> getActivitiesFromUser(Scanner scanner) {
        List<String> activities = new ArrayList<>();
        String activity;

        do {
            System.out.print("Enter activity (or 'done' to finish): ");
            activity = scanner.nextLine().trim();
            if (!activity.equalsIgnoreCase("done") && !activity.isEmpty()) {
                activities.add(activity);
            } else if (activity.isEmpty()) {
                System.out.println("Activity cannot be empty. Please enter a valid activity.");
            }
        } while (!activity.equalsIgnoreCase("done"));

        return activities;
    }
}
