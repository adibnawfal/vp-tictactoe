import java.awt.*;
import java.sql.*;
import javax.swing.*;

public abstract class Config {

  // declare the instance variables
  protected Connection conn = null;
  protected Statement stmt = null;
  protected ResultSet rs = null;
  protected ImageIcon icBack, icMusic, icMusicOff, icTimer, icTimerOff, icSpots, icSpotsOff, icPlayer, icPlayerOff, icPlayerOne, icPlayerOneOff, icPlayerTwo, icPlayerTwoOff, icBot, icBotOff, icNewGame, icReset, icGamemode, icDifficulty, icBoard, icWinnerCounter, icToggleOn, icToggleOff;
  protected Color clrWhite, clrMaroon, clrMaroonDark, clrMaroonBg, clrYellow, clrYellowBg, clrRed, clrRedBg, tempColor;
  protected int styBorder;

  public Config() {
    // create icon with specific size
    icBack = resizeImg(new ImageIcon("images/back.png"), 48);
    icMusic = resizeImg(new ImageIcon("images/music.png"), 48);
    icMusicOff = resizeImg(new ImageIcon("images/music-off.png"), 48);
    icTimer = resizeImg(new ImageIcon("images/timer.png"), 32);
    icTimerOff = resizeImg(new ImageIcon("images/timer-off.png"), 32);
    icSpots = resizeImg(new ImageIcon("images/spots.png"), 32);
    icSpotsOff = resizeImg(new ImageIcon("images/spots-off.png"), 32);
    icPlayer = resizeImg(new ImageIcon("images/player.png"), 32);
    icPlayerOff = resizeImg(new ImageIcon("images/player-off.png"), 32);
    icPlayerOne = resizeImg(new ImageIcon("images/player-1.png"), 32);
    icPlayerOneOff = resizeImg(new ImageIcon("images/player-1-off.png"), 32);
    icPlayerTwo = resizeImg(new ImageIcon("images/player-2.png"), 32);
    icPlayerTwoOff = resizeImg(new ImageIcon("images/player-2-off.png"), 32);
    icBot = resizeImg(new ImageIcon("images/bot.png"), 32);
    icBotOff = resizeImg(new ImageIcon("images/bot-off.png"), 32);
    icNewGame = resizeImg(new ImageIcon("images/play.png"), 32);
    icReset = resizeImg(new ImageIcon("images/reset.png"), 32);
    icGamemode = resizeImg(new ImageIcon("images/play.png"), 32);
    icDifficulty = resizeImg(new ImageIcon("images/difficulty.png"), 32);
    icBoard = resizeImg(new ImageIcon("images/board.png"), 32);
    icWinnerCounter = resizeImg(new ImageIcon("images/winner.png"), 32);
    icToggleOn = resizeImg(new ImageIcon("images/toggle-on.png"), 48);
    icToggleOff = resizeImg(new ImageIcon("images/toggle-off.png"), 48);

    // create color and border thickness for styling
    clrWhite = new Color(250, 250, 250);
    clrMaroon = new Color(99, 8, 70);
    clrMaroonDark = new Color(65, 7, 46);
    clrMaroonBg = new Color(143, 80, 123);
    clrYellow = new Color(255, 153, 51);
    clrYellowBg = new Color(177, 80, 60);
    clrRed = new Color(246, 1, 92);
    clrRedBg = new Color(173, 4, 81);
    styBorder = 2;
  }

  // method to create connection to the database
  protected void newConn() {
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
  protected void closeConn() {
    try {
      if (rs != null) rs.close();
      if (stmt != null) stmt.close();
      if (conn != null) conn.close();
    } catch (Exception e) {
      e.printStackTrace();
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

  // abstract method to get the
  // settings data from the database
  public abstract void getData();

  // abstract method to design the gui
  public abstract void showGUI();
}
