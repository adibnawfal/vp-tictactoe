import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Settings extends Config implements ActionListener, ItemListener {

  // declare the instance variables
  private JFrame frame;
  private JLabel settingsBackground, lblHeader, lblGamemode, lblDifficulty, lblBoard, lblMusic, lblMatchTimer, lblSpotsTaken, lblWinnerCounter;
  private JPanel pnlGamemode, pnlDifficulty, pnlBoard, pnlMusic, pnlMatchTimer, pnlSpotsTaken, pnlWinnerCounter;
  private JButton btnBack, btnResetDefault;
  private JToggleButton btnMatchTimer, btnSpotsTaken, btnWinnerCounter;
  private JComboBox<String> cbGamemode, cbDifficulty, cbBoard, cbMusic;
  private String gamemode, difficulty, music, optGamemode[], optDifficulty[], optBoard[], optMusic[];
  private int board;
  private boolean matchTimer, spotsTaken, winnerCounter;

  public Settings() {
    // initialize frame
    frame = new JFrame();

    // retrieve data from the database and
    // run the next code after retrieval is finished
    try {
      Thread newThread = new Thread(() -> {
        getData();
      });
      newThread.start();
      newThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // call showGUI() method to display
    // the gui for the settings screen
    showGUI();

    // set selected gamemode based on the database
    if (gamemode.equals("Singleplayer")) {
      cbGamemode.setSelectedItem("Singleplayer");
    } else {
      cbGamemode.setSelectedItem("Multiplayer");
    }

    // set selected difficulty based on the database
    if (difficulty.equals("Easy")) {
      cbDifficulty.setSelectedItem("Easy");
    } else {
      cbDifficulty.setSelectedItem("Hard");
    }

    // set selected board based on the database
    switch (board) {
      case 4:
        cbBoard.setSelectedItem("4x4");
        break;
      case 5:
        cbBoard.setSelectedItem("5x5");
        break;
      case 6:
        cbBoard.setSelectedItem("6x6");
        break;
      default:
        cbBoard.setSelectedItem("Default");
        break;
    }

    // set selected music based on the database
    if (music.equals("Angry Remix")) {
      cbMusic.setSelectedItem("Angry Music");
    } else {
      cbMusic.setSelectedItem("Cat");
    }

    // set selected match timer based on the database
    if (matchTimer) {
      btnMatchTimer.setSelected(true);
      btnMatchTimer.setIcon(icToggleOn);
    } else {
      btnMatchTimer.setSelected(false);
      btnMatchTimer.setIcon(icToggleOff);
    }

    // set selected board based on the database
    if (spotsTaken) {
      btnSpotsTaken.setSelected(true);
      btnSpotsTaken.setIcon(icToggleOn);
    } else {
      btnSpotsTaken.setSelected(false);
      btnSpotsTaken.setIcon(icToggleOff);
    }

    // set selected player counter based on the database
    if (winnerCounter) {
      btnWinnerCounter.setSelected(true);
      btnWinnerCounter.setIcon(icToggleOn);
    } else {
      btnWinnerCounter.setSelected(false);
      btnWinnerCounter.setIcon(icToggleOff);
    }

    // show the frame
    frame.setSize(1132, 660);
    frame.setUndecorated(true);
    // frame.setLayout(null);
    frame.setVisible(true);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.getContentPane().setBackground(clrMaroon);
  }

  // method to get the settings
  // data from the database
  public void getData() {
    newConn();

    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT * FROM settings");

      while (rs.next()) {
        gamemode = rs.getString("gamemode");
        difficulty = rs.getString("difficulty");
        board = rs.getInt("board");
        music = rs.getString("music");
        matchTimer = rs.getBoolean("match_timer");
        spotsTaken = rs.getBoolean("spots_taken");
        winnerCounter = rs.getBoolean("winner_counter");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    closeConn();
  }

  // method to design the
  // gui for the settings screen
  public void showGUI() {
    // background image
    settingsBackground = new JLabel(new ImageIcon("images/settings-bg.png"));
    settingsBackground.setLayout(null);

    // button to go back to the main screen
    btnBack = new JButton(icBack);
    btnBack.addActionListener(this);
    btnBack.setBounds(60, 40, 48, 48);
    btnBack.setFocusPainted(false);
    btnBack.setBorderPainted(false);
    btnBack.setContentAreaFilled(false);

    // header title
    lblHeader = new JLabel("SETTINGS");
    lblHeader.setBounds(68, 139, 400, 56);
    lblHeader.setFont(new Font("Roboto", Font.BOLD, 48));
    lblHeader.setForeground(clrWhite);

    // panel to show gamemode settings
    pnlGamemode = new JPanel(new GridLayout(1, 2));
    lblGamemode = new JLabel("Gamemode", icGamemode, SwingConstants.LEFT);
    lblGamemode.setIconTextGap(20);
    lblGamemode.setFont(new Font("Roboto", Font.BOLD, 18));
    lblGamemode.setForeground(clrWhite);
    optGamemode = new String[] { "Singleplayer", "Multiplayer" };
    cbGamemode = new JComboBox<>(optGamemode);
    cbGamemode.addItemListener(this);
    cbGamemode.setFont(new Font("Roboto", Font.BOLD, 14));
    cbGamemode.setForeground(clrWhite);
    cbGamemode.setBackground(clrMaroon);
    pnlGamemode.setBounds(68, 226, 492, 60);
    pnlGamemode.setBackground(clrMaroon);
    pnlGamemode.add(lblGamemode);
    pnlGamemode.add(cbGamemode);
    pnlGamemode.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(clrWhite, styBorder),
        BorderFactory.createEmptyBorder(0, 26, 0, 0)
      )
    );

    // panel to show difficutly settings
    pnlDifficulty = new JPanel(new GridLayout(1, 2));
    lblDifficulty = new JLabel("Difficulty", icDifficulty, SwingConstants.LEFT);
    lblDifficulty.setIconTextGap(20);
    lblDifficulty.setFont(new Font("Roboto", Font.BOLD, 18));
    lblDifficulty.setForeground(clrWhite);
    optDifficulty = new String[] { "Easy", "Hard" };
    cbDifficulty = new JComboBox<>(optDifficulty);
    cbDifficulty.addItemListener(this);
    cbDifficulty.setFont(new Font("Roboto", Font.BOLD, 14));
    cbDifficulty.setForeground(clrWhite);
    cbDifficulty.setBackground(clrMaroon);
    pnlDifficulty.setBounds(68, 296, 492, 60);
    pnlDifficulty.setBackground(clrMaroon);
    pnlDifficulty.add(lblDifficulty);
    pnlDifficulty.add(cbDifficulty);
    pnlDifficulty.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(clrWhite, styBorder),
        BorderFactory.createEmptyBorder(0, 26, 0, 0)
      )
    );

    // panel to show board settings
    pnlBoard = new JPanel(new GridLayout(1, 2));
    lblBoard = new JLabel("Board", icBoard, SwingConstants.LEFT);
    lblBoard.setIconTextGap(20);
    lblBoard.setFont(new Font("Roboto", Font.BOLD, 18));
    lblBoard.setForeground(clrWhite);
    optBoard = new String[] { "Default", "4x4", "5x5", "6x6" };
    cbBoard = new JComboBox<>(optBoard);
    cbBoard.addItemListener(this);
    cbBoard.setFont(new Font("Roboto", Font.BOLD, 14));
    cbBoard.setForeground(clrWhite);
    cbBoard.setBackground(clrMaroon);
    pnlBoard.setBounds(68, 366, 492, 60);
    pnlBoard.setBackground(clrMaroon);
    pnlBoard.add(lblBoard);
    pnlBoard.add(cbBoard);
    pnlBoard.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(clrWhite, styBorder),
        BorderFactory.createEmptyBorder(0, 26, 0, 0)
      )
    );

    // panel to show music settings
    pnlMusic = new JPanel(new GridLayout(1, 2));
    lblMusic = new JLabel("Music", icMusic, SwingConstants.LEFT);
    lblMusic.setIconTextGap(20);
    lblMusic.setFont(new Font("Roboto", Font.BOLD, 18));
    lblMusic.setForeground(clrWhite);
    optMusic = new String[] { "Angry Remix", "Cat" };
    cbMusic = new JComboBox<>(optMusic);
    cbMusic.addItemListener(this);
    cbMusic.setFont(new Font("Roboto", Font.BOLD, 14));
    cbMusic.setForeground(clrWhite);
    cbMusic.setBackground(clrMaroon);
    pnlMusic.setBounds(68, 436, 492, 60);
    pnlMusic.setBackground(clrMaroon);
    pnlMusic.add(lblMusic);
    pnlMusic.add(cbMusic);
    pnlMusic.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(clrWhite, styBorder),
        BorderFactory.createEmptyBorder(0, 26, 0, 0)
      )
    );

    // panel to show match timer settings
    pnlMatchTimer = new JPanel(new GridLayout(1, 2));
    lblMatchTimer = new JLabel("Match Timer", icTimer, SwingConstants.LEFT);
    lblMatchTimer.setIconTextGap(20);
    lblMatchTimer.setFont(new Font("Roboto", Font.BOLD, 18));
    lblMatchTimer.setForeground(clrWhite);
    btnMatchTimer = new JToggleButton();
    btnMatchTimer.addItemListener(this);
    btnMatchTimer.setBorderPainted(false);
    btnMatchTimer.setFocusPainted(false);
    btnMatchTimer.setContentAreaFilled(false);
    pnlMatchTimer.setBounds(580, 226, 492, 60);
    pnlMatchTimer.setBackground(clrMaroon);
    pnlMatchTimer.add(lblMatchTimer);
    pnlMatchTimer.add(btnMatchTimer);
    pnlMatchTimer.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(clrWhite, styBorder),
        BorderFactory.createEmptyBorder(0, 26, 0, 0)
      )
    );

    // panel to show spots taken settings
    pnlSpotsTaken = new JPanel(new GridLayout(1, 2));
    lblSpotsTaken = new JLabel("Spots Taken", icSpots, SwingConstants.LEFT);
    lblSpotsTaken.setIconTextGap(20);
    lblSpotsTaken.setFont(new Font("Roboto", Font.BOLD, 18));
    lblSpotsTaken.setForeground(clrWhite);
    btnSpotsTaken = new JToggleButton();
    btnSpotsTaken.addItemListener(this);
    btnSpotsTaken.setBorderPainted(false);
    btnSpotsTaken.setFocusPainted(false);
    btnSpotsTaken.setContentAreaFilled(false);
    pnlSpotsTaken.setBounds(580, 296, 492, 60);
    pnlSpotsTaken.setBackground(clrMaroon);
    pnlSpotsTaken.add(lblSpotsTaken);
    pnlSpotsTaken.add(btnSpotsTaken);
    pnlSpotsTaken.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(clrWhite, styBorder),
        BorderFactory.createEmptyBorder(0, 26, 0, 0)
      )
    );

    // panel to show winner counter settings
    pnlWinnerCounter = new JPanel(new GridLayout(1, 2));
    lblWinnerCounter =
      new JLabel("Winner Counter", icWinnerCounter, SwingConstants.LEFT);
    lblWinnerCounter.setIconTextGap(20);
    lblWinnerCounter.setFont(new Font("Roboto", Font.BOLD, 18));
    lblWinnerCounter.setForeground(clrWhite);
    btnWinnerCounter = new JToggleButton();
    btnWinnerCounter.addItemListener(this);
    btnWinnerCounter.setBorderPainted(false);
    btnWinnerCounter.setFocusPainted(false);
    btnWinnerCounter.setContentAreaFilled(false);
    pnlWinnerCounter.setBounds(580, 366, 492, 60);
    pnlWinnerCounter.setBackground(clrMaroon);
    pnlWinnerCounter.add(lblWinnerCounter);
    pnlWinnerCounter.add(btnWinnerCounter);
    pnlWinnerCounter.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(clrWhite, styBorder),
        BorderFactory.createEmptyBorder(0, 26, 0, 0)
      )
    );

    // button to reset all the settings to default
    btnResetDefault = new JButton("Reset To Default", icReset);
    btnResetDefault.addActionListener(this);
    btnResetDefault.setBounds(825, 526, 247, 60);
    btnResetDefault.setFont(new Font("Roboto", Font.BOLD, 18));
    btnResetDefault.setForeground(clrWhite);
    btnResetDefault.setBackground(clrMaroon);
    btnResetDefault.setFocusPainted(false);
    btnResetDefault.setIconTextGap(20);
    btnResetDefault.setBorder(
      BorderFactory.createLineBorder(clrWhite, styBorder)
    );

    // add the component to the frame
    settingsBackground.add(btnBack);
    settingsBackground.add(lblHeader);
    settingsBackground.add(pnlGamemode);
    settingsBackground.add(pnlDifficulty);
    settingsBackground.add(pnlBoard);
    settingsBackground.add(pnlMusic);
    settingsBackground.add(pnlMatchTimer);
    settingsBackground.add(pnlSpotsTaken);
    settingsBackground.add(pnlWinnerCounter);
    settingsBackground.add(btnResetDefault);
    frame.add(settingsBackground);
  }

  // method to handle the button component
  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(btnBack)) {
      frame.dispose(); // dispose this frame
    } else if (e.getSource().equals(btnResetDefault)) {
      // reset all settings to default
      cbGamemode.setSelectedItem("Singleplayer");
      cbDifficulty.setSelectedItem("Hard");
      cbBoard.setSelectedItem("Default");
      cbMusic.setSelectedItem("Angry Remix");
      btnMatchTimer.setSelected(true);
      btnSpotsTaken.setSelected(true);
      btnWinnerCounter.setSelected(true);
    }
  }

  // method to handle the combo box component
  public void itemStateChanged(ItemEvent ie) {
    newConn();

    if (ie.getSource().equals(cbGamemode)) {
      try { // update gamemode selection
        stmt = conn.createStatement();
        stmt.executeUpdate(
          "UPDATE settings SET gamemode = '" +
          cbGamemode.getSelectedItem() +
          "'"
        );
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (ie.getSource().equals(cbDifficulty)) {
      try { // update difficulty selection
        stmt = conn.createStatement();
        stmt.executeUpdate(
          "UPDATE settings SET difficulty = '" +
          cbDifficulty.getSelectedItem() +
          "'"
        );
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (ie.getSource().equals(cbBoard)) {
      int tempBoard;

      // assign board value to store in database
      if (cbBoard.getSelectedItem().equals("4x4")) {
        tempBoard = 4;
      } else if (cbBoard.getSelectedItem().equals("5x5")) {
        tempBoard = 5;
      } else if (cbBoard.getSelectedItem().equals("6x6")) {
        tempBoard = 6;
      } else {
        tempBoard = 3;
      }

      try { // update difficulty selection
        stmt = conn.createStatement();
        stmt.executeUpdate("UPDATE settings SET board = " + tempBoard);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (ie.getSource().equals(cbMusic)) {
      try { // update music selection
        stmt = conn.createStatement();
        stmt.executeUpdate(
          "UPDATE settings SET music = '" + cbMusic.getSelectedItem() + "'"
        );
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (ie.getSource().equals(btnMatchTimer)) {
      // toggle on and off button
      boolean tempMatchTimer;
      if (btnMatchTimer.isSelected()) {
        btnMatchTimer.setIcon(icToggleOn);
        tempMatchTimer = true;
      } else {
        btnMatchTimer.setIcon(icToggleOff);
        tempMatchTimer = false;
      }

      try { // update match timer selection
        stmt = conn.createStatement();
        stmt.executeUpdate(
          "UPDATE settings SET match_timer = " + tempMatchTimer
        );
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (ie.getSource().equals(btnSpotsTaken)) {
      // toggle on and off button
      boolean tempSpotsTaken;
      if (btnSpotsTaken.isSelected()) {
        btnSpotsTaken.setIcon(icToggleOn);
        tempSpotsTaken = true;
      } else {
        btnSpotsTaken.setIcon(icToggleOff);
        tempSpotsTaken = false;
      }

      try { // update spots taken selection
        stmt = conn.createStatement();
        stmt.executeUpdate(
          "UPDATE settings SET spots_taken = " + tempSpotsTaken
        );
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (ie.getSource().equals(btnWinnerCounter)) {
      // toggle on and off button
      boolean tempWinnerCounter;
      if (btnWinnerCounter.isSelected()) {
        btnWinnerCounter.setIcon(icToggleOn);
        tempWinnerCounter = true;
      } else {
        btnWinnerCounter.setIcon(icToggleOff);
        tempWinnerCounter = false;
      }

      try { // update winner counter selection
        stmt = conn.createStatement();
        stmt.executeUpdate(
          "UPDATE settings SET winner_counter = " + tempWinnerCounter
        );
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    closeConn();
  }
}
