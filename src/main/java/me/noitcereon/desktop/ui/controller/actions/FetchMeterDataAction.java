package me.noitcereon.desktop.ui.controller.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FetchMeterDataAction implements ActionListener {

  @Override
  public void actionPerformed(ActionEvent e) {
    System.out.println("Triggered action: '" + e.getActionCommand() + "'");
    FetchMeterDataActionType actionType = FetchMeterDataActionType.valueOf(e.getActionCommand());
    switch (actionType){
      case FETCH_METER_DATA_FROM_YESTERDAY:
        break;
      case FETCH_METER_DATA_BASED_ON_LAST_FETCH_TIME:
        break;
      case FETCH_METER_DATA_CUSTOM_PERIOD:
        break;
      default:
    }
  }
}
