package me.noitcereon.desktop.ui.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import me.noitcereon.desktop.ui.model.StartingPageModel;
import me.noitcereon.desktop.ui.controller.actions.FetchMeterDataActionType;
import me.noitcereon.desktop.ui.view.shared.Constants;
import me.noitcereon.desktop.ui.view.shared.DefaultStyling;

public class StartingPageView {

  private StartingPageModel model;

  public void initialize(StartingPageModel startingPageModel) {
    this.model = startingPageModel;

    // Create Frame
    JFrame frame = new JFrame(Constants.APP_NAME);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(DefaultStyling.FRAME_WIDTH, DefaultStyling.FRAME_HEIGHT);
    frame.setLayout(new BorderLayout());

    // Add Headline
    JLabel h1 = new JLabel("Welcome to " + Constants.APP_NAME, SwingConstants.CENTER);
    h1.setFont(new Font(DefaultStyling.FONT_FAMILY, Font.BOLD, DefaultStyling.H1_FONT_SIZE));
    frame.add(h1, BorderLayout.NORTH);

    JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
    frame.add(separator);

    // Add Paragraph Below Headline
    JLabel paragraph = new JLabel(startingPageModel.getDynamicParagraphText(), SwingConstants.CENTER);
    paragraph.setFont(new Font(DefaultStyling.FONT_FAMILY, Font.PLAIN, DefaultStyling.BODY_FONT_SIZE));
    frame.add(paragraph);

    // Add Panel to Frame
    frame.add(createButtonPanel(), BorderLayout.CENTER);

    // Set Frame Visible
    frame.setVisible(true);

    frame.setLocationRelativeTo(null);
  }

  private JPanel createButtonPanel(){
    // Panel for Buttons
    JPanel buttonPanel = new JPanel(new GridLayout(2, 2));

    // Create Buttons
    JButton button1 = new JButton("Fetch meterdata from yesterday!");
    button1.setActionCommand(FetchMeterDataActionType.FETCH_METER_DATA_FROM_YESTERDAY.toString());
    JButton button2 = new JButton("Fetch meterdata from a period you define!");
    button2.setActionCommand(FetchMeterDataActionType.FETCH_METER_DATA_CUSTOM_PERIOD.toString());
    JButton button3 = new JButton("Fetch meterdata based on the latest fetch date!");
    button3.setActionCommand(FetchMeterDataActionType.FETCH_METER_DATA_BASED_ON_LAST_FETCH_TIME.toString());
    JButton button4 = new JButton("Toggle CSV file data format for meter data (Currently: )!");

    button1.addActionListener(model.getFetchMeterDataAction());
    button2.addActionListener(model.getFetchMeterDataAction());
    button3.addActionListener(model.getFetchMeterDataAction());

    // Add Buttons to Panel
    buttonPanel.add(button1);
    buttonPanel.add(button2);
    buttonPanel.add(button3);
    buttonPanel.add(button4);

    return buttonPanel;
  }
}
