package me.noitcereon.console.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 */
public class Screen {

    private final String headLine;
    private final Map<Integer, ScreenOption> menuOptions;
    /**
     * Creates a scanner that takes System.in input. It should only be closed, when the application exits.
     */
    private static final Scanner SCANNER = new Scanner(System.in);

    public Screen() {
        this.headLine = "Template Screen";
        this.menuOptions = new HashMap<>();
        this.menuOptions.put(0, ScreenOptionFactory.exitApplication());
        this.menuOptions.put(1, ScreenOptionFactory.mainMenuOption());
    }

    public Screen(String headLine, Map<Integer, ScreenOption> menuOptions) {
        this.headLine = headLine;
        this.menuOptions = menuOptions;
        menuOptions.put(0, ScreenOptionFactory.exitApplication());
    }

    public String getHeadLine() {
        return headLine;
    }

    public Map<Integer, ScreenOption> getMenuOptions() {
        return menuOptions;
    }

    public String getDisplayString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, ScreenOption> entry : menuOptions.entrySet()) {
            sb.append("[").append(entry.getKey()).append("]")
                    .append(" ").append(entry.getValue())
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }

    private void displayHeaderAndMenu() {
        System.out.println(headLine);
        System.out.println(getDisplayString());
    }

    /**
     * Displays a screen and asks the user what they want to do on the screen.
     *
     * @return The option the user selected from the menu.
     */
    public ScreenOption displayScreenAndAskForInput() {
        displayHeaderAndMenu();

        return askForValidInput(SCANNER);
    }

    private ScreenOption askForValidInput(Scanner scanner) {
        String userInput = scanner.nextLine();
        int chosenOption = Integer.parseInt(userInput);
        if (isInvalidInput(userInput)) {
            System.err.println("Invalid option. Enter a number from the menu and press Enter.");
            System.out.println("Valid options are: " + getDisplayString());
            return askForValidInput(scanner);
        }
        return menuOptions.get(chosenOption);
    }

    private boolean isInvalidInput(String userInput) {
        try {
            int chosenOption = Integer.parseInt(userInput);
            return chosenOption > menuOptions.size() || chosenOption < 0;
        } catch (NumberFormatException e) {
            System.err.println("Could not parse the given input as a number.");
            askForValidInput(new Scanner(System.in));
        }
        return true;
    }
}
