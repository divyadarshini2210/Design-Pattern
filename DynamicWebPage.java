//Decorator Pattern:Dynamic Web Page Styling



import java.util.*;

// Component Interface
interface WebPage {
    String getContent();
}

// Concrete Component
class BasicWebPage implements WebPage {
    @Override
    public String getContent() {
        return "Basic Web Page";
    }
}

// Decorator Abstract Class
abstract class WebPageDecorator implements WebPage {
    protected WebPage webPage;

    public WebPageDecorator(WebPage webPage) {
        this.webPage = webPage;
    }

    @Override
    public String getContent() {
        return webPage.getContent();
    }
}

// Concrete Decorators
class DarkModeDecorator extends WebPageDecorator {
    public DarkModeDecorator(WebPage webPage) {
        super(webPage);
    }

    @Override
    public String getContent() {
        return webPage.getContent() + " with Dark Mode Styling";
    }
}

class LightModeDecorator extends WebPageDecorator {
    public LightModeDecorator(WebPage webPage) {
        super(webPage);
    }

    @Override
    public String getContent() {
        return webPage.getContent() + " with Light Mode Styling";
    }
}

class BorderDecorator extends WebPageDecorator {
    public BorderDecorator(WebPage webPage) {
        super(webPage);
    }

    @Override
    public String getContent() {
        return webPage.getContent() + " with Border Styling";
    }
}

class BackgroundImageDecorator extends WebPageDecorator {
    public BackgroundImageDecorator(WebPage webPage) {
        super(webPage);
    }

    @Override
    public String getContent() {
        return webPage.getContent() + " with Background Image";
    }
}

// Command Interface
interface Command {
    void execute();
}

// Concrete Commands
class ApplyDarkModeCommand implements Command {
    private CommandExecutor executor;

    public ApplyDarkModeCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute() {
        executor.applyDecorator(new DarkModeDecorator(executor.getCurrentDecorator()));
    }
}

class ApplyLightModeCommand implements Command {
    private CommandExecutor executor;

    public ApplyLightModeCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute() {
        executor.applyDecorator(new LightModeDecorator(executor.getCurrentDecorator()));
    }
}

class ApplyBorderCommand implements Command {
    private CommandExecutor executor;

    public ApplyBorderCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute() {
        executor.applyDecorator(new BorderDecorator(executor.getCurrentDecorator()));
    }
}

class ApplyBackgroundImageCommand implements Command {
    private CommandExecutor executor;

    public ApplyBackgroundImageCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute() {
        executor.applyDecorator(new BackgroundImageDecorator(executor.getCurrentDecorator()));
    }
}

// Display Command
class DisplayCommand implements Command {
    private CommandExecutor executor;

    public DisplayCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute() {
        System.out.println("Current Web Page Content: " + executor.getCurrentDecorator().getContent());
    }
}

// Delete Command
class DeleteCommand implements Command {
    private CommandExecutor executor;

    public DeleteCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute() {
        executor.resetDecorator();
        System.out.println("Web Page Content has been reset.");
    }
}

// Command Executor
class CommandExecutor {
    private WebPageDecorator currentDecorator;

    public CommandExecutor(WebPageDecorator initialDecorator) {
        this.currentDecorator = initialDecorator;
    }

    public void applyDecorator(WebPageDecorator decorator) {
        this.currentDecorator = decorator;
        System.out.println("Updated Web Page Content: " + currentDecorator.getContent());
    }

    public void resetDecorator() {
        this.currentDecorator = new WebPageDecorator(new BasicWebPage()) {
            @Override
            public String getContent() {
                return new BasicWebPage().getContent();
            }
        };
    }

    public WebPageDecorator getCurrentDecorator() {
        return currentDecorator;
    }
}

// State Interface
interface State {
    void handleInput(String input);
}

// Concrete State
class CommandInputState implements State {
    private final CommandExecutor commandExecutor;
    private final Map<String, Command> commandMap = new HashMap<>();

    public CommandInputState(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;

        // Register commands
        commandMap.put("DarkMode", new ApplyDarkModeCommand(commandExecutor));
        commandMap.put("LightMode", new ApplyLightModeCommand(commandExecutor));
        commandMap.put("Border", new ApplyBorderCommand(commandExecutor));
        commandMap.put("BackgroundImage", new ApplyBackgroundImageCommand(commandExecutor));
        commandMap.put("Display", new DisplayCommand(commandExecutor));
        commandMap.put("Delete", new DeleteCommand(commandExecutor));
    }

    @Override
    public void handleInput(String input) {
        if ("done".equalsIgnoreCase(input)) {
            System.out.println("Final Web Page Content: " + commandExecutor.getCurrentDecorator().getContent());
            System.exit(0);
        } else {
            Command command = commandMap.get(input);
            if (command != null) {
                command.execute();
            } else {
                System.out.println("Invalid command.");
            }
        }
    }
}

// Main Class
public class DynamicWebPageStyling {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        WebPage basicPage = new BasicWebPage();
        WebPageDecorator initialDecorator = new WebPageDecorator(basicPage) {
            @Override
            public String getContent() {
                return basicPage.getContent();
            }
        };

        CommandExecutor commandExecutor = new CommandExecutor(initialDecorator);
        State currentState = new CommandInputState(commandExecutor);

        // User interaction
        System.out.println("Enter decorator to apply (DarkMode, LightMode, Border, BackgroundImage), 'Display' to view, 'Delete' to reset, or 'done' to finish:");
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            currentState.handleInput(input);
        }
    }
}