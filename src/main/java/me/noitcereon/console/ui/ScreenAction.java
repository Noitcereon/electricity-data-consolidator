package me.noitcereon.console.ui;

/**
 * Represents a function that happens when a screen is loaded.
 */
@FunctionalInterface
public interface ScreenAction {
    /**
     * Performs an action.
     * @return The screen to be displayed after the action has been taken.
     */
    Screen actionToPerform();
}
