package me.noitcereon;


import me.noitcereon.console.ui.Screen;
import me.noitcereon.console.ui.ScreenFactory;
import me.noitcereon.console.ui.ScreenOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            Screen mainMenu = ScreenFactory.createMainMenu();
            ScreenOption option = mainMenu.displayScreenAndAskForInput();
            int allowedAppLoops = 500000; // safeguard against infinite loop.
            int appLoops = 0;
            while (appLoops < allowedAppLoops) { // Application loop. It can be stopped via a menu option (See the ScreenOptionFactory.exitApplication() method)
                Screen nextScreen = option.execute();
                option = nextScreen.displayScreenAndAskForInput();
                appLoops++;
            }
        }
        catch (NumberFormatException numberFormatEx){
            LOG.error("Couldn't parse the given input as a number");
            main(new String[]{""});
        }
        catch (Exception e) {
            LOG.error("An error occurred, which the application was not built to handle. Showing information that can help debug the problem: ", e);
            System.out.println("### Press ENTER to close the application ####");
            Screen.getScannerInstance().nextLine();
            System.exit(1);
        }
        finally {
            Screen.getScannerInstance().close();
        }
    }
}