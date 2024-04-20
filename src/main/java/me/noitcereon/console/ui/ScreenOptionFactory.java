package me.noitcereon.console.ui;

/**
 * Contains the available options for the application.
 */
public class ScreenOptionFactory {
    private ScreenOptionFactory(){
        // Don't instantiate, because the ScreenOptions should only be available through static methods.
    }
    public static ScreenOption mainMenuOption() {
        return new ScreenOption("Displays the main menu", () -> ScreenFactory.createMainMenu().displayScreenAndAskForInput().execute());
    }
    public static ScreenOption exitApplication(){
        return new ScreenOption("Exits the application.", () -> {
            System.exit(0);
            return null; // this should never be reached.
        });
    }

}
