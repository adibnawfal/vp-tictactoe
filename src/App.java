import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class App extends JFrame implements ActionListener {

  // declare the JFrame component
  JLabel lblHeader;
  JButton btnPlay, btnSettings;
  JPanel pnlPlay, pnlSettings;
  ImageIcon icPlay, icSettings;

  public App() {
    // rename the frame title
    // that will show on the windows bar
    super("Tic-Tac-Toe");
    // initialize the icon image
    icPlay = resizeImg(new ImageIcon("images/play.png"), 22);
    icSettings = resizeImg(new ImageIcon("images/settings.png"), 22);

    // call showGUI() method to display
    // the gui for the Home
    showGUI();

    // needed method to show the frame
    setSize(250, 230);
    setVisible(true);
    setResizable(false);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    lblHeader = new JLabel("Tic-Tac-Toe", JLabel.CENTER);
    gbc.gridx = 0; // specify the row position
    gbc.gridy = 0; // specify the column position
    gbc.gridwidth = 1; // specify the width of the row
    gbc.gridheight = 1; // specify the height of the column
    add(lblHeader, gbc); // add the component to the frame

    gbc.insets = new Insets(0, 0, 0, 0);

    btnPlay = new JButton(icPlay);
    pnlPlay = new JPanel();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    btnPlay.setText("Play");
    btnPlay.setPreferredSize(new Dimension(150, 40));
    btnPlay.addActionListener(this);
    pnlPlay.add(btnPlay);
    add(pnlPlay, gbc);

    btnSettings = new JButton(icSettings);
    pnlSettings = new JPanel();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    btnSettings.setText("Settings");
    btnSettings.setPreferredSize(new Dimension(150, 40));
    btnSettings.addActionListener(this);
    pnlSettings.add(btnSettings);
    add(pnlSettings, gbc);

    // styling the font-family, font-weight,
    // and font-size of the label component
    lblHeader.setFont(new Font("Open Sans", Font.BOLD, 22));
  }

  // method to handle the button component
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btnPlay) {
      new Game();
    } else if (e.getSource() == btnSettings) {
      new Settings();
    }
  }

  // method to resize the image
  public ImageIcon resizeImg(ImageIcon srcIcon, int icSize) {
    Image image = srcIcon.getImage();
    image =
      image.getScaledInstance(icSize, icSize, java.awt.Image.SCALE_SMOOTH);
    ImageIcon newIcon = new ImageIcon(image);
    return newIcon;
  }

  public static void main(String[] args) {
    new App();
  }
}
