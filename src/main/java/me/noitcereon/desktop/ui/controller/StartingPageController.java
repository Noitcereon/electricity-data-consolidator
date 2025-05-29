package me.noitcereon.desktop.ui.controller;

import me.noitcereon.desktop.ui.controller.actions.FetchMeterDataAction;
import me.noitcereon.desktop.ui.model.StartingPageModel;
import me.noitcereon.desktop.ui.view.StartingPageView;

public class StartingPageController {

  private final StartingPageModel model;
  private final StartingPageView view;

  public StartingPageController(StartingPageModel model, StartingPageView view) {
    this.model = model;
    this.view = view;
  }

  public void initializePage(){
    model.setDynamicParagraphText("Hello World!");
    model.setFetchMeterDataAction(new FetchMeterDataAction());
    view.initialize(model);
  }

}
