import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Settings extends JFrame implements ActionListener, ItemListener {

  // declare the JFrame component
  JLabel lblHeader, lblIconGamemode, lblGamemode;
  JButton btnGamemode, test;
  JPanel pnlGamemode;
  ImageIcon icGamemode;
  JComboBox<String> cbGamemode;

  // declare the instance variables
  // needed to use in different methods
  Connection conn = null;
  Statement stmt = null;
  ResultSet rs = null;
  String gamemode;
  int board;

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

    // call showGUI() method to display
    // the gui for the Home
    showGUI();

    // needed method to show the frame
    setSize(300, 450);
    setVisible(true);
    setResizable(false);
    setLocationRelativeTo(null);
    // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    // show selected data from the database
    String optGamemode[];
    if (gamemode.equals("Singleplayer")) optGamemode =
      new String[] { "Singleplayer", "Multiplayer" }; else optGamemode =
      new String[] { "Multiplayer", "Singleplayer" };

    // example gui for the gamemode selection
    lblIconGamemode = new JLabel(icGamemode);
    lblGamemode = new JLabel("Gamemode");
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

    // styling the font-family, font-weight,
    // and font-size of the label component
    lblHeader.setFont(new Font("Open Sans", Font.BOLD, 22));
  }

  // method to handle the button component
  public void actionPerformed(ActionEvent e) {
    newConn();

    // if (e.getSource() == btn1) {
    //   // handlle the button 1 here ...
    // } else if (e.getSource() == btn2) {
    //   // handle the button 2 here ...
    // }

    closeConn();
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
