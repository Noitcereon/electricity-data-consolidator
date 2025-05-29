package me.noitcereon.desktop.ui;

import me.noitcereon.desktop.ui.controller.StartingPageController;
import me.noitcereon.desktop.ui.model.StartingPageModel;
import me.noitcereon.desktop.ui.view.StartingPageView;

public class DesktopUIMain {
  public static void main(String[] args) {
    StartingPageModel startingPageModel = new StartingPageModel();
    StartingPageView startingPage = new StartingPageView();
    StartingPageController startingPageController = new StartingPageController(startingPageModel, startingPage);

    startingPageController.initializePage();
  }
}
