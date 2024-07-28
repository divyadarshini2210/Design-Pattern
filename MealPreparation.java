#Template Pattern

import java.util.*;
import java.util.logging.*;

// Abstract Class: MealPreparationTemplate
abstract class MealPreparationTemplate {

    // Template method defining the steps for meal preparation
    public final void prepareMeal() {
        gatherIngredients();
        prepareIngredients();
        cook();
        serve();
    }

    // Common steps for meal preparation
    private void gatherIngredients() {
        System.out.println("Gathering ingredients for the meal.");
    }

    protected abstract void prepareIngredients();  // Step to be implemented by concrete classes

    protected abstract void cook();  // Step to be implemented by concrete classes

    private void serve() {
        System.out.println("Serving the meal.");
    }

    @Override
    public abstract String toString(); // Provide a meaningful string representation of the meal

    // Get the name of the dish for searching
    public abstract String getDishName();
}

// Concrete Class: UserDefinedMealPreparation
class UserDefinedMealPreparation extends MealPreparationTemplate {

    private final String dishName;
    private final String ingredientDetails;
    private final String cookingInstructions;

    public UserDefinedMealPreparation(String dishName, String ingredientDetails, String cookingInstructions) {
        this.dishName = dishName;
        this.ingredientDetails = ingredientDetails;
        this.cookingInstructions = cookingInstructions;
    }

    @Override
    protected void prepareIngredients() {
        System.out.println("Preparing ingredients: " + ingredientDetails);
    }

    @Override
    protected void cook() {
        System.out.println("Cooking instructions: " + cookingInstructions);
    }

    @Override
    public String toString() {
        return "Dish: " + dishName + "\nIngredients: " + ingredientDetails + "\nCooking Instructions: " + cookingInstructions;
    }

    @Override
    public String getDishName() {
        return dishName;
    }
}

// Main Class: MealPreparationTest
public class MealPreparationTest {

    private static final Logger logger = Logger.getLogger(MealPreparationTest.class.getName());

    public static void main(String[] args) {
        configureLogging();
        Scanner scanner = new Scanner(System.in);

        List<MealPreparationTemplate> userMeals = new ArrayList<>();
        Map<String, Runnable> commandMap = initializeCommandMap(scanner, userMeals);

        try {
            executeUserCommands(scanner, commandMap);
        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
            logger.log(Level.SEVERE, "Unexpected error", e);
        } finally {
            scanner.close();
        }
    }

    private static Map<String, Runnable> initializeCommandMap(Scanner scanner, List<MealPreparationTemplate> userMeals) {
        Map<String, Runnable> commandMap = new HashMap<>();

        commandMap.put("Enter Recipe", () -> {
            try {
                System.out.println("Enter details for the meal preparation:");

                System.out.print("Enter the name of the dish: ");
                String dishName = scanner.nextLine().trim();

                System.out.print("Enter ingredients: ");
                String ingredients = scanner.nextLine().trim();

                System.out.print("Enter cooking instructions: ");
                String instructions = scanner.nextLine().trim();

                MealPreparationTemplate mealPreparation = new UserDefinedMealPreparation(dishName, ingredients, instructions);
                mealPreparation.prepareMeal();
                userMeals.add(mealPreparation);
            } catch (Exception e) {
                System.out.println("An error occurred while entering the recipe.");
                logger.log(Level.SEVERE, "Error entering recipe", e);
            }
        });

        commandMap.put("DisplayAll", () -> {
            if (userMeals.isEmpty()) {
                System.out.println("No previous meals prepared.");
            } else {
                System.out.println("Previously prepared meals:");
                userMeals.forEach(System.out::println);
            }
        });

        commandMap.put("Search", () -> {
            try {
                System.out.print("Enter the keyword to search for: ");
                String keyword = scanner.nextLine().trim().toLowerCase();

                List<MealPreparationTemplate> foundMeals = new ArrayList<>();
                for (MealPreparationTemplate meal : userMeals) {
                    if (meal.getDishName().toLowerCase().contains(keyword)) {
                        foundMeals.add(meal);
                    }
                }

                if (foundMeals.isEmpty()) {
                    System.out.println("No meals found with the keyword \"" + keyword + "\". Displaying all meals.");
                    commandMap.get("DisplayAll").run();
                } else {
                    System.out.println("Found meals matching the keyword \"" + keyword + "\":");
                    foundMeals.forEach(System.out::println);
                }
            } catch (Exception e) {
                System.out.println("An error occurred during the search.");
                logger.log(Level.SEVERE, "Error during search", e);
            }
        });

        commandMap.put("Exit", () -> {
            System.out.println("Exiting...");
            logger.info("Application exiting.");
            System.exit(0);
        });

        return commandMap;
    }

    private static void executeUserCommands(Scanner scanner, Map<String, Runnable> commandMap) {
        String command;
        do {
            System.out.println("Enter command (Enter Recipe, DisplayAll, Search, Exit): ");
            command = scanner.nextLine().trim();
            Runnable action = commandMap.getOrDefault(command, () -> System.out.println("Invalid command."));
            action.run();
        } while (!command.equals("Exit"));
    }

    private static void configureLogging() {
        try {
            LogManager.getLogManager().readConfiguration(MealPreparationTest.class.getResourceAsStream("/logging.properties"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error configuring logging", e);
        }
    }
}