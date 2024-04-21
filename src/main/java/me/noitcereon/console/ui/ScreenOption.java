package me.noitcereon.console.ui;

public class ScreenOption {
    /**
     * The description of what will happen when this option is selected.
     */
    private final String description;
    private final ScreenAction action;

    /**
     *
     * @param description Describes what will happen when this option is selected.
     * @param callbackFunction The action to perform when this option is selected.
     */
    public ScreenOption(String description, ScreenAction callbackFunction){
        this.description = description;
        this.action = callbackFunction;
    }

    /**
     * Executes the ScreenAction inside the ScreenOption object.
     */
    public Screen execute(){
        return action.actionToPerform();
    }
    public String getDescription(){
        return description;
    }
    @Override
    public String toString(){
        return description;
    }
}