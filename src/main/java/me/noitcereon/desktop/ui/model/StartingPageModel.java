package me.noitcereon.desktop.ui.model;

import me.noitcereon.desktop.ui.controller.actions.FetchMeterDataAction;

public class StartingPageModel {
  private String dynamicParagraphText;
  private FetchMeterDataAction fetchMeterDataAction;

  public StartingPageModel() {
    // Defined, because I dislike implicit empty constructors.
  }

  public String getDynamicParagraphText() {
    return dynamicParagraphText;
  }

  public void setDynamicParagraphText(String dynamicParagraphText) {
    this.dynamicParagraphText = dynamicParagraphText;
  }

  public FetchMeterDataAction getFetchMeterDataAction() {
    return fetchMeterDataAction;
  }

  public void setFetchMeterDataAction(FetchMeterDataAction fetchMeterDataAction) {
    this.fetchMeterDataAction = fetchMeterDataAction;
  }
}
