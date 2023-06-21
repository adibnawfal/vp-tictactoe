import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class App extends JFrame implements ActionListener {

  // declare instance variable
  private JLabel mainBackground, lblHeader, lblAbout, lblAboutDesc;
  private JButton btnPlay, btnSettings, btnExit;
  private ImageIcon icPlay, icSettings, icExit;
  private Color clrWhite, clrMaroon;
  private int styBorder;

  public App() {
    // create icon with specific size
    icPlay = resizeImg(new ImageIcon("images/play.png"), 32);
    icSettings = resizeImg(new ImageIcon("images/settings.png"), 32);
    icExit = resizeImg(new ImageIcon("images/exit.png"), 32);

    // create color and border thickness for styling
    clrWhite = new Color(250, 250, 250);
    clrMaroon = new Color(99, 8, 70);
    styBorder = 2;

    // call showGUI() method to display
    // the gui for the main screen
    showGUI();

    // show the frame
    setSize(1132, 660);
    setUndecorated(true);
    setVisible(true);
    setResizable(false);
    setLocationRelativeTo(null);
    getContentPane().setBackground(clrMaroon);
  }

  // method to design the
  // gui for the main screen
  public void showGUI() {
    // background image
    mainBackground = new JLabel(new ImageIcon("images/main-bg.gif"));
    mainBackground.setLayout(null);

    // header title
    lblHeader = new JLabel("TIC-TAC-TOE");
    lblHeader.setBounds(519, 109, 400, 56);
    lblHeader.setFont(new Font("Roboto", Font.BOLD, 48));
    lblHeader.setForeground(clrWhite);

    // button to play the game
    btnPlay = new JButton("Play", icPlay);
    btnPlay.addActionListener(this);
    btnPlay.setBounds(520, 195, 180, 60);
    btnPlay.setFont(new Font("Roboto", Font.BOLD, 18));
    btnPlay.setForeground(clrWhite);
    btnPlay.setBackground(clrMaroon);
    btnPlay.setFocusPainted(false);
    btnPlay.setIconTextGap(20);
    btnPlay.setHorizontalAlignment(SwingConstants.LEFT);
    btnPlay.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(clrWhite, styBorder),
        BorderFactory.createEmptyBorder(0, 26, 0, 0)
      )
    );

    // button to go to the settings
    btnSettings = new JButton("Settings", icSettings);
    btnSettings.addActionListener(this);
    btnSettings.setBounds(520, 270, 180, 60);
    btnSettings.setFont(new Font("Roboto", Font.BOLD, 18));
    btnSettings.setForeground(clrWhite);
    btnSettings.setBackground(clrMaroon);
    btnSettings.setFocusPainted(false);
    btnSettings.setIconTextGap(20);
    btnSettings.setHorizontalAlignment(SwingConstants.LEFT);
    btnSettings.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(clrWhite, styBorder),
        BorderFactory.createEmptyBorder(0, 26, 0, 0)
      )
    );

    // button to exit the game
    btnExit = new JButton("Exit", icExit);
    btnExit.addActionListener(this);
    btnExit.setBounds(520, 345, 180, 60);
    btnExit.setFont(new Font("Roboto", Font.BOLD, 18));
    btnExit.setForeground(clrWhite);
    btnExit.setBackground(clrMaroon);
    btnExit.setFocusPainted(false);
    btnExit.setIconTextGap(20);
    btnExit.setHorizontalAlignment(SwingConstants.LEFT);
    btnExit.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(clrWhite, styBorder),
        BorderFactory.createEmptyBorder(0, 26, 0, 0)
      )
    );

    // label for about header
    lblAbout = new JLabel("About");
    lblAbout.setBounds(519, 434, 100, 30);
    lblAbout.setFont(new Font("Roboto", Font.PLAIN, 24));
    lblAbout.setForeground(clrWhite);

    // label for about description
    String aboutDesc =
      "Tic-tac-toe, also known as noughts and crosses or Xs and Os, is a classic pencil-and-paper game played on a grid of 3x3 squares. The game involves two players who take turns marking their respective symbols (traditionally \"X\" and \"O\") in empty squares on the grid.";
    lblAboutDesc = new JLabel();
    lblAboutDesc.setText("<html>" + aboutDesc + "</html>");
    lblAboutDesc.setBounds(519, 484, 530, 70);
    lblAboutDesc.setFont(new Font("Times New Roman", Font.PLAIN, 15));
    lblAboutDesc.setForeground(clrWhite);

    // add the component to the frame
    mainBackground.add(lblHeader);
    mainBackground.add(btnPlay);
    mainBackground.add(btnSettings);
    mainBackground.add(btnExit);
    mainBackground.add(lblAbout);
    mainBackground.add(lblAboutDesc);
    add(mainBackground);
  }

  // method to resize the image
  public ImageIcon resizeImg(ImageIcon srcIcon, int icSize) {
    Image image = srcIcon.getImage();
    image =
      image.getScaledInstance(icSize, icSize, java.awt.Image.SCALE_SMOOTH);
    ImageIcon newIcon = new ImageIcon(image);
    return newIcon;
  }

  // method to handle the button component
  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(btnPlay)) {
      new Game();
    } else if (e.getSource().equals(btnSettings)) {
      new Settings();
    } else if (e.getSource().equals(btnExit)) {
      System.exit(0);
    }
  }

  public static void main(String[] args) {
    new App();
  }
}
