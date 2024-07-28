//Factory Pattern


import java.util.*;

// Product Interface
interface Furniture {
    void design();
    String getMaterial();
    String getType();  // Added for identifying type
}

// Concrete Product: Table
class Table implements Furniture {
    private String material;

    public Table(String material) {
        this.material = material;
    }

    @Override
    public void design() {
        System.out.println("Designing a table.");
    }

    @Override
    public String getMaterial() {
        return material;
    }

    @Override
    public String getType() {
        return "Table";
    }
}

// Concrete Product: Chair
class Chair implements Furniture {
    private String material;

    public Chair(String material) {
        this.material = material;
    }

    @Override
    public void design() {
        System.out.println("Designing a chair.");
    }

    @Override
    public String getMaterial() {
        return material;
    }

    @Override
    public String getType() {
        return "Chair";
    }
}

// Concrete Product: Sofa
class Sofa implements Furniture {
    private String material;

    public Sofa(String material) {
        this.material = material;
    }

    @Override
    public void design() {
        System.out.println("Designing a sofa.");
    }

    @Override
    public String getMaterial() {
        return material;
    }

    @Override
    public String getType() {
        return "Sofa";
    }
}

// Factory Interface
interface FurnitureFactory {
    Furniture createFurniture(String material);
}

// Concrete Factory: TableFactory
class TableFactory implements FurnitureFactory {
    @Override
    public Furniture createFurniture(String material) {
        return new Table(material);
    }
}

// Concrete Factory: ChairFactory
class ChairFactory implements FurnitureFactory {
    @Override
    public Furniture createFurniture(String material) {
        return new Chair(material);
    }
}

// Concrete Factory: SofaFactory
class SofaFactory implements FurnitureFactory {
    @Override
    public Furniture createFurniture(String material) {
        return new Sofa(material);
    }
}

// Main Class
public class FurnitureDesignDemo {
    private static List<Furniture> furnitureList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    // Map to store command actions
    private static Map<String, Runnable> commandMap = new HashMap<>();

    // Map to store factory creation methods
    private static Map<String, FurnitureFactory> factoryMap = new HashMap<>();

    public static void main(String[] args) {
        initializeFactories();
        initializeCommands();
        executeUserCommands();
    }

    private static void initializeFactories() {
        factoryMap.put("Table", new TableFactory());
        factoryMap.put("Chair", new ChairFactory());
        factoryMap.put("Sofa", new SofaFactory());
    }

    private static void initializeCommands() {
        commandMap.put("Add", () -> {
            System.out.print("Select Furniture Type (Table, Chair, Sofa): ");
            String type = scanner.nextLine().trim();

            System.out.print("Enter material for the furniture: ");
            String material = scanner.nextLine().trim();

            FurnitureFactory factory = factoryMap.get(type);
            if (factory != null) {
                Furniture furniture = factory.createFurniture(material);
                furniture.design();
                System.out.println("Material used: " + furniture.getMaterial());
                furnitureList.add(furniture);
            } else {
                System.out.println("Invalid furniture type.");
            }
        });

        commandMap.put("DisplayAll", () -> {
            if (furnitureList.isEmpty()) {
                System.out.println("No furniture items available.");
            } else {
                System.out.println("List of all furniture:");
                for (Furniture furniture : furnitureList) {
                    System.out.println("Type: " + furniture.getType() + ", Material: " + furniture.getMaterial());
                }
            }
        });

        commandMap.put("Delete", () -> {
            System.out.print("Enter type of furniture to delete (Table, Chair, Sofa): ");
            String typeToDelete = scanner.nextLine().trim();

            boolean removed = furnitureList.removeIf(furniture -> furniture.getType().equalsIgnoreCase(typeToDelete));

            if (removed) {
                System.out.println(typeToDelete + " items removed successfully.");
            } else {
                System.out.println("No " + typeToDelete + " items found.");
            }
        });

        commandMap.put("Exit", () -> {
            System.out.println("Exiting...");
            System.exit(0);
        });
    }

    private static void executeUserCommands() {
        String command;
        do {
            System.out.print("Enter command (Add, DisplayAll, Delete, Exit): ");
            command = scanner.nextLine().trim();
            Runnable action = commandMap.getOrDefault(command, () -> System.out.println("Invalid command."));
            action.run();
        } while (!command.equalsIgnoreCase("Exit"));
    }
}