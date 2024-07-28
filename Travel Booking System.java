//Strategy Pattern

import java.util.*;
import java.util.logging.*;

public class TravelBookingSystem {

    private static final Logger logger = Logger.getLogger(TravelBookingSystem.class.getName());

    public static void main(String[] args) {
        configureLogging();

        Scanner scanner = new Scanner(System.in);
        TravelContext context = new TravelContext();

        Map<String, TravelStrategy> strategies = new HashMap<>();
        strategies.put("Bus", new BusStrategy());
        strategies.put("Train", new TrainStrategy());
        strategies.put("Flight", new FlightStrategy());

        Map<String, Runnable> commandMap = new HashMap<>();

        commandMap.put("Book", () -> {
            try {
                System.out.print("Enter distance (km): ");
                int distance = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Enter number of members: ");
                int numberOfMembers = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Choose transport mode (Bus, Train, Flight): ");
                String mode = scanner.nextLine().trim();

                TravelStrategy strategy = strategies.get(mode);
                if (strategy == null) {
                    System.out.println("Invalid transport mode.");
                    logger.warning("Invalid transport mode selected: " + mode);
                    return;
                }

                context.setTravelStrategy(strategy);
                context.book(distance, numberOfMembers);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter numeric values for distance and number of members.");
                logger.log(Level.WARNING, "Invalid numeric input", e);
            } catch (Exception e) {
                System.out.println("An unexpected error occurred.");
                logger.log(Level.SEVERE, "Unexpected error", e);
            }
        });

        commandMap.put("Cancel", () -> {
            try {
                context.displayAllBookings();
                System.out.print("Enter Booking ID to cancel: ");
                int bookingId = Integer.parseInt(scanner.nextLine().trim());
                context.cancel(bookingId);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric value for Booking ID.");
                logger.log(Level.WARNING, "Invalid numeric input", e);
            } catch (Exception e) {
                System.out.println("An unexpected error occurred.");
                logger.log(Level.SEVERE, "Unexpected error", e);
            }
        });

        commandMap.put("Display", context::displayBooking);

        commandMap.put("DisplayAll", context::displayAllBookings);

        commandMap.put("Exit", () -> {
            System.out.println("Exiting...");
            logger.info("Application exiting.");
            System.exit(0);
        });

        try {
            while (executeCommand(scanner, commandMap));
        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
            logger.log(Level.SEVERE, "Unexpected error", e);
        } finally {
            scanner.close();
        }
    }

    private static boolean executeCommand(Scanner scanner, Map<String, Runnable> commandMap) {
        System.out.println("Enter command (Book, Cancel, Display, DisplayAll, Exit): ");
        String command = scanner.nextLine().trim();
        Runnable action = commandMap.get(command);
        if (action != null) {
            action.run();
            return !command.equals("Exit");
        } else {
            System.out.println("Invalid command.");
            logger.warning("Invalid command entered: " + command);
            return true;
        }
    }

    private static void configureLogging() {
        try {
            LogManager.getLogManager().readConfiguration(TravelBookingSystem.class.getResourceAsStream("/logging.properties"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error configuring logging", e);
        }
    }
}

interface TravelStrategy {
    void bookTicket(int distance, int numberOfMembers);
    void cancelTicket();
    String getBookingDetails();
    String getBookingType();
    double calculateAmount(int distance, int numberOfMembers);
    boolean isAvailable(int numberOfMembers);
}

class BusStrategy implements TravelStrategy {
    private boolean booked = false;
    private int distance;
    private int numberOfMembers;
    private static final double PRICE_PER_KM = 5.0;
    private static final int MAX_SEATS = 60;
    private int availableSeats = MAX_SEATS;

    public void bookTicket(int distance, int numberOfMembers) {
        if (isAvailable(numberOfMembers)) {
            this.distance = distance;
            this.numberOfMembers = numberOfMembers;
            booked = true;
            availableSeats -= numberOfMembers;
            System.out.println("Booked a bus ticket for distance: " + distance + " km, for " + numberOfMembers + " members.");
        } else {
            System.out.println("No seats available on the bus.");
        }
    }

    public void cancelTicket() {
        if (booked) {
            booked = false;
            availableSeats += numberOfMembers;
            System.out.println("Bus booking canceled.");
        } else {
            System.out.println("No bus booking to cancel.");
        }
    }

    public String getBookingDetails() {
        return booked ? "Bus ticket booked for distance: " + distance + " km, for " + numberOfMembers + " members." : "No bus booking.";
    }

    public String getBookingType() {
        return "Bus";
    }

    public double calculateAmount(int distance, int numberOfMembers) {
        return PRICE_PER_KM * distance * numberOfMembers;
    }

    public boolean isAvailable(int numberOfMembers) {
        return availableSeats >= numberOfMembers;
    }
}

class TrainStrategy implements TravelStrategy {
    private boolean booked = false;
    private int distance;
    private int numberOfMembers;
    private static final double PRICE_PER_KM = 10.0;
    private static final int MAX_SEATS = 1000;
    private int availableSeats = MAX_SEATS;

    public void bookTicket(int distance, int numberOfMembers) {
        if (isAvailable(numberOfMembers)) {
            this.distance = distance;
            this.numberOfMembers = numberOfMembers;
            booked = true;
            availableSeats -= numberOfMembers;
            System.out.println("Booked a train ticket for distance: " + distance + " km, for " + numberOfMembers + " members.");
        } else {
            System.out.println("No seats available on the train.");
        }
    }

    public void cancelTicket() {
        if (booked) {
            booked = false;
            availableSeats += numberOfMembers;
            System.out.println("Train booking canceled.");
        } else {
            System.out.println("No train booking to cancel.");
        }
    }

    public String getBookingDetails() {
        return booked ? "Train ticket booked for distance: " + distance + " km, for " + numberOfMembers + " members." : "No train booking.";
    }

    public String getBookingType() {
        return "Train";
    }

    public double calculateAmount(int distance, int numberOfMembers) {
        return PRICE_PER_KM * distance * numberOfMembers;
    }

    public boolean isAvailable(int numberOfMembers) {
        return availableSeats >= numberOfMembers;
    }
}

class FlightStrategy implements TravelStrategy {
    private boolean booked = false;
    private int distance;
    private int numberOfMembers;
    private static final double PRICE_PER_KM = 20.0;
    private static final int MAX_SEATS = 50;
    private int availableSeats = MAX_SEATS;

    public void bookTicket(int distance, int numberOfMembers) {
        if (isAvailable(numberOfMembers)) {
            this.distance = distance;
            this.numberOfMembers = numberOfMembers;
            booked = true;
            availableSeats -= numberOfMembers;
            System.out.println("Booked a flight ticket for distance: " + distance + " km, for " + numberOfMembers + " members.");
        } else {
            System.out.println("No seats available on the flight.");
        }
    }

    public void cancelTicket() {
        if (booked) {
            booked = false;
            availableSeats += numberOfMembers;
            System.out.println("Flight booking canceled.");
        } else {
            System.out.println("No flight booking to cancel.");
        }
    }

    public String getBookingDetails() {
        return booked ? "Flight ticket booked for distance: " + distance + " km, for " + numberOfMembers + " members." : "No flight booking.";
    }

    public String getBookingType() {
        return "Flight";
    }

    public double calculateAmount(int distance, int numberOfMembers) {
        return PRICE_PER_KM * distance * numberOfMembers;
    }

    public boolean isAvailable(int numberOfMembers) {
        return availableSeats >= numberOfMembers;
    }
}

class TravelContext {
    private TravelStrategy strategy;
    private List<Booking> bookings = new ArrayList<>();
    private int bookingIdCounter = 1;

    private class Booking {
        int id;
        TravelStrategy strategy;
        int distance;
        int numberOfMembers;
        double amount;

        Booking(int id, TravelStrategy strategy, int distance, int numberOfMembers, double amount) {
            this.id = id;
            this.strategy = strategy;
            this.distance = distance;
            this.numberOfMembers = numberOfMembers;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Booking ID: " + id + ", " + strategy.getBookingType() + " ticket for distance: " + distance + " km, for " + numberOfMembers + " members. Total Amount: $" + amount;
        }
    }

    public void setTravelStrategy(TravelStrategy strategy) {
        this.strategy = strategy;
    }

    public void book(int distance, int numberOfMembers) {
        if (strategy != null) {
            if (strategy.isAvailable(numberOfMembers)) {
                strategy.bookTicket(distance, numberOfMembers);
                double amount = strategy.calculateAmount(distance, numberOfMembers);
                bookings.add(new Booking(bookingIdCounter++, strategy, distance, numberOfMembers, amount));
                System.out.println("Cost of traveling: $" + amount);
            } else {
                System.out.println("No seats available.");
            }
        } else {
            System.out.println("No strategy set.");
        }
    }

    public void cancel(int bookingId) {
        Optional<Booking> bookingToCancel = bookings.stream()
                .filter(booking -> booking.id == bookingId)
                .findFirst();

        if (bookingToCancel.isPresent()) {
            TravelStrategy strategy = bookingToCancel.get().strategy;
            strategy.cancelTicket();
            bookings.removeIf(booking -> booking.id == bookingId);
        } else {
            System.out.println("Booking ID not found.");
        }
    }

    public void displayBooking() {
        if (strategy != null) {
            System.out.println(strategy.getBookingDetails());
        } else {
            System.out.println("No strategy set.");
        }
    }

    public void displayAllBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No previous bookings.");
        } else {
            System.out.println("Previous bookings:");
            for (Booking booking : bookings) {
                System.out.println(booking);
            }
        }
    }
}


