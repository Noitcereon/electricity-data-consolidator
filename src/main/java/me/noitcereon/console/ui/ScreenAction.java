package me.noitcereon.console.ui;

/**
 * Represents a function that happens when a screen is loaded.
 */
@FunctionalInterface
public interface ScreenAction {
    Screen actionToPerform();
}
