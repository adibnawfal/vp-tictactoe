import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Settings extends JFrame implements ActionListener, ItemListener {

  // declare the JFrame component
  JLabel lblHeader, lblIconGamemode, lblGamemode, lblIconBoard, lblBoard, lblIconMatchTimer, lblMatchTimer, lblIconBoardInfo, lblBoardInfo, lblIconPlayerCounter, lblPlayerCounter, lblIconResetDefault, lblResetDefault;
  JButton btnResetDefault;
  JToggleButton btnMatchTimer, btnBoardInfo, btnPlayerCounter;
  JPanel pnlGamemode, pnlBoard, pnlMatchTimer, pnlBoardInfo, pnlPlayerCounter, pnlResetDefault;
  ImageIcon icGamemode, icBoard, icMatchTimer, icBoardInfo, icPlayerCounter, icResetDefault, icToggleOn, icToggleOff;
  JComboBox<String> cbGamemode, cbBoard;

  // declare the instance variables
  // needed to use in different methods
  Connection conn = null;
  Statement stmt = null;
  ResultSet rs = null;
  String gamemode, optGamemode[], optBoard[];
  int board;
  boolean matchTimer, boardInfo, playerCounter;

  public Settings() {
    // rename the frame title
    // that will show on the windows bar
    super("Settings");
    // retrieve data from the database and
    // run the next code after retrieval is finished
    try {
      Thread newThread = new Thread(() -> {
        getData();
      });
      newThread.start();
      newThread.join();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // initialize the icon image
    icGamemode = resizeImg(new ImageIcon("images/play.png"), 22);
    icBoard = resizeImg(new ImageIcon("images/board.png"), 22);
    icMatchTimer = resizeImg(new ImageIcon("images/timer.png"), 22);
    icBoardInfo = resizeImg(new ImageIcon("images/spots-taken.png"), 22);
    icPlayerCounter = resizeImg(new ImageIcon("images/player.png"), 22);
    icResetDefault = resizeImg(new ImageIcon("images/reset.png"), 22);
    icToggleOn = resizeImg(new ImageIcon("images/toggle-on.png"), 32);
    icToggleOff = resizeImg(new ImageIcon("images/toggle-off.png"), 32);

    // call showGUI() method to display
    // the gui for the Home
    showGUI();

    // set selected gamemode from the database
    if (gamemode.equals("Singleplayer")) cbGamemode.setSelectedItem(
      "Singleplayer"
    ); else cbGamemode.setSelectedItem("Multiplayer");

    // set selected board from the database
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

    // set selected match timer from the database
    if (matchTimer) {
      btnMatchTimer.setSelected(true);
      btnMatchTimer.setIcon(icToggleOn);
    } else {
      btnMatchTimer.setSelected(false);
      btnMatchTimer.setIcon(icToggleOff);
    }

    // set selected board info from the database
    if (boardInfo) {
      btnBoardInfo.setSelected(true);
      btnBoardInfo.setIcon(icToggleOn);
    } else {
      btnBoardInfo.setSelected(false);
      btnBoardInfo.setIcon(icToggleOff);
    }

    // set selected player counter from the database
    if (playerCounter) {
      btnPlayerCounter.setSelected(true);
      btnPlayerCounter.setIcon(icToggleOn);
    } else {
      btnPlayerCounter.setSelected(false);
      btnPlayerCounter.setIcon(icToggleOff);
    }

    // needed method to show the frame
    setSize(300, 450);
    setVisible(true);
    setResizable(false);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  // method to design the
  // gui for the game
  public void showGUI() {
    // create a JFrame layout component
    // to use for arranging the JFrame component
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    setLayout(layout);

    // define the margin of the component
    // with the following arguments rules
    // (top, left, bottom, right)
    gbc.insets = new Insets(0, 0, 20, 0);

    lblHeader = new JLabel("Settings", JLabel.CENTER);
    gbc.gridx = 0; // specify the row position
    gbc.gridy = 0; // specify the column position
    gbc.gridwidth = 1; // specify the width of the row
    gbc.gridheight = 1; // specify the height of the column
    add(lblHeader, gbc); // add the component to the frame

    gbc.insets = new Insets(0, 0, 0, 0);

    // gui for gamemode selection
    lblIconGamemode = new JLabel(icGamemode);
    lblGamemode = new JLabel("Gamemode");
    optGamemode = new String[] { "Singleplayer", "Multiplayer" };
    cbGamemode = new JComboBox<>(optGamemode);
    pnlGamemode = new JPanel();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    cbGamemode.addItemListener(this);
    pnlGamemode.add(lblIconGamemode);
    pnlGamemode.add(lblGamemode);
    pnlGamemode.add(cbGamemode);
    add(pnlGamemode, gbc);

    // gui for board selection
    lblIconBoard = new JLabel(icBoard);
    lblBoard = new JLabel("Board");
    optBoard = new String[] { "Default", "4x4", "5x5", "6x6" };
    cbBoard = new JComboBox<>(optBoard);
    pnlBoard = new JPanel();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    cbBoard.addItemListener(this);
    pnlBoard.add(lblIconBoard);
    pnlBoard.add(lblBoard);
    pnlBoard.add(cbBoard);
    add(pnlBoard, gbc);

    // gui for match timer
    lblIconMatchTimer = new JLabel(icMatchTimer);
    lblMatchTimer = new JLabel("Match Timer");
    btnMatchTimer = new JToggleButton();
    pnlMatchTimer = new JPanel();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    btnMatchTimer.addItemListener(this);
    btnMatchTimer.setBorderPainted(false);
    btnMatchTimer.setFocusPainted(false);
    btnMatchTimer.setContentAreaFilled(false);
    btnMatchTimer.setBorder(null);
    pnlMatchTimer.add(lblIconMatchTimer);
    pnlMatchTimer.add(lblMatchTimer);
    pnlMatchTimer.add(btnMatchTimer);
    add(pnlMatchTimer, gbc);

    // gui for board info
    lblBoardInfo = new JLabel("Board Info");
    lblIconBoardInfo = new JLabel(icBoardInfo);
    btnBoardInfo = new JToggleButton(icToggleOn);
    pnlBoardInfo = new JPanel();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    btnBoardInfo.addItemListener(this);
    btnBoardInfo.setBorderPainted(false);
    btnBoardInfo.setFocusPainted(false);
    btnBoardInfo.setContentAreaFilled(false);
    btnBoardInfo.setBorder(null);
    pnlBoardInfo.add(lblIconBoardInfo);
    pnlBoardInfo.add(lblBoardInfo);
    pnlBoardInfo.add(btnBoardInfo);
    add(pnlBoardInfo, gbc);

    // gui for player counter
    lblPlayerCounter = new JLabel("Player Counter");
    lblIconPlayerCounter = new JLabel(icPlayerCounter);
    btnPlayerCounter = new JToggleButton(icToggleOn);
    pnlPlayerCounter = new JPanel();
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    btnPlayerCounter.addItemListener(this);
    btnPlayerCounter.setBorderPainted(false);
    btnPlayerCounter.setFocusPainted(false);
    btnPlayerCounter.setContentAreaFilled(false);
    btnPlayerCounter.setBorder(null);
    pnlPlayerCounter.add(lblIconPlayerCounter);
    pnlPlayerCounter.add(lblPlayerCounter);
    pnlPlayerCounter.add(btnPlayerCounter);
    add(pnlPlayerCounter, gbc);

    gbc.insets = new Insets(10, 0, 0, 0);

    // gui for reset to default
    btnResetDefault = new JButton(icResetDefault);
    pnlResetDefault = new JPanel();
    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    btnResetDefault.addActionListener(this);
    btnResetDefault.setText("Reset to Default");
    pnlResetDefault.add(btnResetDefault);
    add(pnlResetDefault, gbc);

    // styling the font-family, font-weight,
    // and font-size of the label component
    lblHeader.setFont(new Font("Open Sans", Font.BOLD, 22));
  }

  // method to handle the button component
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btnResetDefault) {
      cbGamemode.setSelectedItem("Singleplayer");
      cbBoard.setSelectedItem("Default");
      btnMatchTimer.setSelected(true);
      btnBoardInfo.setSelected(true);
      btnPlayerCounter.setSelected(true);
    }
  }

  // method to handle the combo box component
  public void itemStateChanged(ItemEvent ie) {
    newConn();

    // if the state combobox is changed
    if (ie.getSource() == cbGamemode) {
      try {
        stmt = conn.createStatement();
        stmt.executeUpdate(
          "UPDATE settings SET gamemode = '" +
          cbGamemode.getSelectedItem() +
          "'"
        );
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (ie.getSource() == cbBoard) {
      int tempBoard;

      if (cbBoard.getSelectedItem().equals("4x4")) {
        tempBoard = 4;
      } else if (cbBoard.getSelectedItem().equals("5x5")) {
        tempBoard = 5;
      } else if (cbBoard.getSelectedItem().equals("6x6")) {
        tempBoard = 6;
      } else {
        tempBoard = 3;
      }

      try {
        stmt = conn.createStatement();
        stmt.executeUpdate("UPDATE settings SET board = " + tempBoard);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (ie.getSource() == btnMatchTimer) {
      boolean tempMatchTimer;
      if (btnMatchTimer.isSelected()) {
        btnMatchTimer.setIcon(icToggleOn);
        tempMatchTimer = true;
      } else {
        btnMatchTimer.setIcon(icToggleOff);
        tempMatchTimer = false;
      }

      try {
        stmt = conn.createStatement();
        stmt.executeUpdate(
          "UPDATE settings SET match_timer = " + tempMatchTimer
        );
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (ie.getSource() == btnBoardInfo) {
      boolean tempBoardInfo;
      if (btnBoardInfo.isSelected()) {
        btnBoardInfo.setIcon(icToggleOn);
        tempBoardInfo = true;
      } else {
        btnBoardInfo.setIcon(icToggleOff);
        tempBoardInfo = false;
      }

      try {
        stmt = conn.createStatement();
        stmt.executeUpdate("UPDATE settings SET board_info = " + tempBoardInfo);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (ie.getSource() == btnPlayerCounter) {
      boolean tempPlayerCounter;
      if (btnPlayerCounter.isSelected()) {
        btnPlayerCounter.setIcon(icToggleOn);
        tempPlayerCounter = true;
      } else {
        btnPlayerCounter.setIcon(icToggleOff);
        tempPlayerCounter = false;
      }

      try {
        stmt = conn.createStatement();
        stmt.executeUpdate(
          "UPDATE settings SET player_counter = " + tempPlayerCounter
        );
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    closeConn();
  }

  // method to resize the image
  public ImageIcon resizeImg(ImageIcon srcIcon, int icSize) {
    Image image = srcIcon.getImage();
    image =
      image.getScaledInstance(icSize, icSize, java.awt.Image.SCALE_SMOOTH);
    ImageIcon newIcon = new ImageIcon(image);
    return newIcon;
  }

  // method to create connection to the database
  public void newConn() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      conn =
        DriverManager.getConnection(
          "jdbc:mysql://localhost/tictactoe",
          "visualprogramming",
          "vp12345"
        );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // method to close connection to the database
  public void closeConn() {
    try {
      if (rs != null) rs.close();
      if (stmt != null) stmt.close();
      if (conn != null) conn.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
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
        board = rs.getInt("board");
        matchTimer = rs.getBoolean("match_timer");
        boardInfo = rs.getBoolean("board_info");
        playerCounter = rs.getBoolean("player_counter");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    closeConn();
  }

  public static void main(String[] args) {
    new Settings();
  }
}
