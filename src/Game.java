import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;

public class Game extends JFrame implements ActionListener {

  // declare the JFrame component
  JLabel lblHeader, lblIconTimer, lblTimer, lblIconSpotsTaken, lblSpotsTaken, lblIconPlayer, lblPlayer, lblIconBot, lblBot;
  JButton btnBox[], btnMusic, btnReset, btnAudio;
  JPanel pnlInfo, pnlBox, pnlFooter, pnlAudio, pnlReset, pnlSettings;
  ImageIcon icO, icX, icTimer, icTimerOff, icSpotsTaken, icSpotsTakenOff, icPlayer, icPlayerOff, icPlayerOne, icPlayerTwo, icBot, icBotOff, icMusic, icMusicOff, icVolumeUp, icVolumeDown, icVolumeOff, icReset, icSettings;
  Clip cMusic, cClick1, cClick2;
  Timer timer;

  // declare the instance variables
  // needed to use in different methods
  Connection conn = null;
  Statement stmt = null;
  ResultSet rs = null;
  String gamemode, icon;
  int board, loop, validateLane, width, height, leftMargin, lblHeaderSize, lblInfoSize, icSize, index, wins =
    0, losses = 0, draws = 0, grid[][];
  boolean player = true, turn = true, gameStart =
    false, soundLoaded, matchTimer, boardInfo, playerCounter;
  char BLANK = ' ', O = 'O', X = 'X', cells[];
  long start, finish, timeElapsed;

  public Game() {
    // rename the frame title
    // that will show on the windows bar
    super("Tic-Tac-Toe");
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

    // initialize style, grid, settings for each grid.
    // by default the board layout will be 3x3
    // user can select up to 6x6
    switch (board) {
      case 4:
        loop = 16;
        validateLane = 10;
        width = 350;
        height = 520;
        leftMargin = 20;
        lblHeaderSize = 24;
        lblInfoSize = 16;
        icSize = 24;
        btnBox = new JButton[16];
        cells = new char[16];
        for (int i = 0; i < 16; i++) cells[i] = BLANK;
        grid =
          new int[][] {
            { 0, 1, 2, 3 },
            { 4, 5, 6, 7 },
            { 8, 9, 10, 11 },
            { 12, 13, 14, 15 },
            { 0, 4, 8, 12 },
            { 1, 5, 9, 13 },
            { 2, 6, 10, 14 },
            { 3, 7, 11, 15 },
            { 0, 5, 10, 15 },
            { 3, 6, 9, 12 },
          };
        break;
      case 5:
        loop = 25;
        validateLane = 12;
        width = 450;
        height = 610;
        leftMargin = 30;
        lblHeaderSize = 28;
        lblInfoSize = 18;
        icSize = 30;
        btnBox = new JButton[25];
        cells = new char[25];
        for (int i = 0; i < 25; i++) cells[i] = BLANK;
        grid =
          new int[][] {
            { 0, 1, 2, 3, 4 },
            { 5, 6, 7, 8, 9 },
            { 10, 11, 12, 13, 14 },
            { 15, 16, 17, 18, 19 },
            { 20, 21, 22, 23, 24 },
            { 0, 5, 10, 15, 20 },
            { 1, 6, 11, 16, 21 },
            { 2, 7, 12, 17, 22 },
            { 3, 8, 13, 18, 23 },
            { 4, 9, 14, 19, 24 },
            { 0, 6, 12, 18, 24 },
            { 4, 8, 12, 16, 20 },
          };
        break;
      case 6:
        loop = 36;
        validateLane = 14;
        width = 500;
        height = 700;
        leftMargin = 40;
        lblHeaderSize = 32;
        lblInfoSize = 20;
        icSize = 34;
        btnBox = new JButton[36];
        cells = new char[36];
        for (int i = 0; i < 36; i++) cells[i] = BLANK;
        grid =
          new int[][] {
            { 0, 1, 2, 3, 4, 5 },
            { 6, 7, 8, 9, 10, 11 },
            { 12, 13, 14, 15, 16, 17 },
            { 18, 19, 20, 21, 22, 23 },
            { 24, 25, 26, 27, 28, 29 },
            { 30, 31, 32, 33, 34, 35 },
            { 0, 6, 12, 18, 24, 30 },
            { 1, 7, 13, 19, 25, 31 },
            { 2, 8, 14, 20, 26, 32 },
            { 3, 9, 15, 21, 27, 33 },
            { 4, 10, 16, 22, 28, 34 },
            { 5, 11, 17, 23, 29, 35 },
            { 0, 7, 14, 21, 28, 35 },
            { 5, 10, 15, 20, 25, 30 },
          };
        break;
      default:
        loop = 9;
        validateLane = 8;
        width = 300;
        height = 450;
        leftMargin = 10;
        lblHeaderSize = 22;
        lblInfoSize = 14;
        icSize = 22;
        btnBox = new JButton[9];
        cells = new char[9];
        for (int i = 0; i < 9; i++) cells[i] = BLANK;
        grid =
          new int[][] {
            { 0, 1, 2 },
            { 3, 4, 5 },
            { 6, 7, 8 },
            { 0, 3, 6 },
            { 1, 4, 7 },
            { 2, 5, 8 },
            { 0, 4, 8 },
            { 2, 4, 6 },
          };
    }

    // initialize the icon image
    icO = new ImageIcon("images/o.png");
    icX = new ImageIcon("images/x.png");
    icTimer = resizeImg(new ImageIcon("images/timer.png"), icSize);
    icTimerOff = resizeImg(new ImageIcon("images/timer-off.png"), icSize);
    icSpotsTaken = resizeImg(new ImageIcon("images/spots-taken.png"), icSize);
    icSpotsTakenOff =
      resizeImg(new ImageIcon("images/spots-taken-off.png"), icSize);
    icPlayer = resizeImg(new ImageIcon("images/player.png"), icSize);
    icPlayerOff = resizeImg(new ImageIcon("images/player-off.png"), icSize);
    icPlayerOne = resizeImg(new ImageIcon("images/player-1.png"), icSize);
    icPlayerTwo = resizeImg(new ImageIcon("images/player-2.png"), icSize);
    icBot = resizeImg(new ImageIcon("images/bot.png"), icSize);
    icBotOff = resizeImg(new ImageIcon("images/bot-off.png"), icSize);
    icMusic = resizeImg(new ImageIcon("images/music.png"), icSize);
    icMusicOff = resizeImg(new ImageIcon("images/music-off.png"), icSize);
    icVolumeUp = resizeImg(new ImageIcon("images/volume-up.png"), icSize);
    icVolumeDown = resizeImg(new ImageIcon("images/volume-down.png"), icSize);
    icVolumeOff = resizeImg(new ImageIcon("images/volume-off.png"), icSize);
    icReset = resizeImg(new ImageIcon("images/reset.png"), icSize);
    icSettings = resizeImg(new ImageIcon("images/settings.png"), icSize);

    // call showGUI() method to display
    // the gui for the Game
    showGUI();

    // set selected match timer from the database
    if (matchTimer) lblIconTimer.setIcon(icTimer); else lblIconTimer.setIcon(
      icTimerOff
    );

    // set selected board info from the database
    if (boardInfo) lblIconSpotsTaken.setIcon(
      icSpotsTaken
    ); else lblIconSpotsTaken.setIcon(icSpotsTakenOff);

    // set selected player counter from the database
    if (playerCounter) {
      if (gamemode.equals("Singleplayer")) {
        lblIconPlayer.setIcon(icPlayer);
        lblIconBot.setIcon(icBot);
      } else {
        lblIconPlayer.setIcon(icPlayerOne);
        lblIconBot.setIcon(icPlayerTwo);
      }
    } else {
      lblIconPlayer.setIcon(icPlayerOff);
      lblIconBot.setIcon(icBotOff);
    }

    // needed method to show the frame
    setSize(width, height);
    setVisible(true);
    setResizable(false);
    setLocationRelativeTo(null);
    // getContentPane().setBackground(new Color(54, 69, 79));

    // dispose current frame when close and
    // stop the background music
    WindowListener windowExit = new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        cMusic.stop();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      }
    };
    addWindowListener(windowExit);
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

    // display the header text based
    // on the gamemode selection
    // JOptionPane.showMessageDialog(this, gamemode);

    if (gamemode.equals("Singleplayer")) lblHeader =
      new JLabel("Tic-Tac-Toe", JLabel.CENTER); else lblHeader =
      new JLabel("Player 1 Turn!", JLabel.CENTER);
    gbc.gridx = 0; // specify the row position
    gbc.gridy = 0; // specify the column position
    gbc.gridwidth = board; // specify the width of the row
    gbc.gridheight = 1; // specify the height of the column
    add(lblHeader, gbc); // add the component to the frame

    // define the margin of the component
    // with the following arguments rules
    // (top, left, bottom, right)
    gbc.insets = new Insets(20, leftMargin, 20, 0);

    // panel info to display the timer,
    // spots taken, count player win,
    // and count bot win
    lblIconTimer = new JLabel();
    lblTimer = new JLabel("0");
    lblIconSpotsTaken = new JLabel();
    lblSpotsTaken = new JLabel("0");
    lblIconPlayer = new JLabel();
    lblPlayer = new JLabel("0");
    lblIconBot = new JLabel();
    lblBot = new JLabel("0");
    pnlInfo = new JPanel();
    pnlInfo.setLayout(new GridLayout(1, 4));
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = board;
    gbc.gridheight = 1;
    pnlInfo.add(lblIconTimer);
    pnlInfo.add(lblTimer);
    pnlInfo.add(lblIconSpotsTaken);
    pnlInfo.add(lblSpotsTaken);
    pnlInfo.add(lblIconPlayer);
    pnlInfo.add(lblPlayer);
    pnlInfo.add(lblIconBot);
    pnlInfo.add(lblBot);
    add(pnlInfo, gbc);

    gbc.insets = new Insets(0, 0, 0, 0);

    // display the grid button based on the
    // board layout selected on the settings
    int index = 0;
    for (int row = 1; row <= board; row++) {
      for (int column = 0; column < board; column++) {
        btnBox[index] = new JButton();
        pnlBox = new JPanel();
        gbc.gridx = column;
        gbc.gridy = row + 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        btnBox[index].setPreferredSize(new Dimension(60, 60));
        btnBox[index].addActionListener(this);
        pnlBox.add(btnBox[index]);
        add(pnlBox, gbc);
        index++;
      }
    }

    gbc.insets = new Insets(10, 0, 0, 0);

    // try to load the sound from the file
    try {
      // open an audio input stream
      File fMusic = new File("audio/angrybird.wav");
      File fClick1 = new File("audio/pew.wav");
      File fClick2 = new File("audio/paww.wav");

      AudioInputStream adMusic = AudioSystem.getAudioInputStream(fMusic);
      AudioInputStream adClick1 = AudioSystem.getAudioInputStream(fClick1);
      AudioInputStream adClick2 = AudioSystem.getAudioInputStream(fClick2);

      // get a sound clip resource, open audio clip, and
      // load samples from the audio input stream
      cMusic = AudioSystem.getClip();
      cClick1 = AudioSystem.getClip();
      cClick2 = AudioSystem.getClip();

      cMusic.open(adMusic);
      cClick1.open(adClick1);
      cClick2.open(adClick2);

      soundLoaded = true;
    } catch (UnsupportedAudioFileException e) {
      soundLoaded = false;
      e.printStackTrace();
    } catch (IOException e) {
      soundLoaded = false;
      e.printStackTrace();
    } catch (LineUnavailableException e) {
      soundLoaded = false;
      e.printStackTrace();
    }

    // start background music and loop
    cMusic.start();
    cMusic.loop(Clip.LOOP_CONTINUOUSLY);

    // panel footer to display the audio,
    // reset, and settings button
    btnMusic = new JButton(icMusic);
    btnMusic.setPreferredSize(new Dimension(40, 40));
    btnMusic.addActionListener(this);
    btnMusic.setBorderPainted(false);
    btnMusic.setFocusPainted(false);
    btnMusic.setContentAreaFilled(false);
    btnReset = new JButton(icReset);
    btnReset.setText("Reset");
    btnReset.setPreferredSize(new Dimension(board * 60 - 80, 40));
    btnReset.addActionListener(this);
    btnAudio = new JButton(icVolumeUp);
    btnAudio.setPreferredSize(new Dimension(40, 40));
    btnAudio.addActionListener(this);
    btnAudio.setBorderPainted(false);
    btnAudio.setFocusPainted(false);
    btnAudio.setContentAreaFilled(false);
    pnlFooter = new JPanel();
    gbc.gridx = 0;
    gbc.gridy = board + 2;
    gbc.gridwidth = board;
    gbc.gridheight = 1;
    pnlFooter.add(btnMusic);
    pnlFooter.add(btnReset);
    pnlFooter.add(btnAudio);
    add(pnlFooter, gbc);

    // styling the font-family, font-weight,
    // and font-size of the label component
    lblHeader.setFont(new Font("Open Sans", Font.BOLD, lblHeaderSize));
    lblTimer.setFont(new Font("Open Sans", Font.BOLD, lblInfoSize));
    lblSpotsTaken.setFont(new Font("Open Sans", Font.BOLD, lblInfoSize));
    lblPlayer.setFont(new Font("Open Sans", Font.BOLD, lblInfoSize));
    lblBot.setFont(new Font("Open Sans", Font.BOLD, lblInfoSize));
  }

  // method to handle the button component
  public void actionPerformed(ActionEvent e) {
    // handle the button based on the
    // gamemode selected on the settings
    if (gamemode.equals("Singleplayer")) { // singleplayer
      for (int i = 0; i < loop; i++) {
        if (e.getSource() == btnBox[i]) {
          // start timer
          if (matchTimer && !gameStart) {
            start = System.currentTimeMillis();
            gameStart = true;

            timer =
              new Timer(
                1000,
                new ActionListener() {
                  public void actionPerformed(ActionEvent e) {
                    float seconds =
                      (System.currentTimeMillis() - start) / 1000F;
                    lblTimer.setText(String.valueOf(Math.round(seconds)));
                  }
                }
              );
            timer.start();
          }

          if (cells[i] == BLANK) {
            cells[i] = O;
            btnBox[i].setIcon(icO);
            cClick1.start();
            cClick1.setFramePosition(0);
            if (isWin(O)) finish(O); else if (isDraw()) finish(BLANK); else {
              compTurn();
              if (isWin(X)) finish(X); else if (isDraw()) finish(BLANK);
            }
          }
        }
      }
    } else { // multiplayer
      for (int i = 0; i < loop; i++) {
        if (e.getSource() == btnBox[i]) {
          // start timer
          if (matchTimer && !gameStart) {
            start = System.currentTimeMillis();
            gameStart = true;

            timer =
              new Timer(
                1000,
                new ActionListener() {
                  public void actionPerformed(ActionEvent e) {
                    float seconds =
                      (System.currentTimeMillis() - start) / 1000F;
                    lblTimer.setText(String.valueOf(Math.round(seconds)));
                  }
                }
              );
            timer.start();
          }

          if (cells[i] == BLANK) {
            if (player == true) {
              cells[i] = O;
              btnBox[i].setIcon(icO);
              lblHeader.setText("Player 2 Turn!");
              cClick1.start();
              cClick1.setFramePosition(0);
              player = false;
              if (isWin(O)) finish(O); else if (isDraw()) finish(BLANK);
            } else {
              cells[i] = X;
              btnBox[i].setIcon(icX);
              lblHeader.setText("Player 1 Turn!");
              cClick2.start();
              cClick2.setFramePosition(0);
              player = true;
              if (isWin(X)) finish(X); else if (isDraw()) finish(BLANK);
            }
          }
        }
      }
    }

    // calculate how many cells spots have been taken
    if (boardInfo) {
      int calcSpots = 0;
      for (int i = 0; i < loop; i++) {
        if (cells[i] != BLANK) {
          ++calcSpots;
        }
      }
      lblSpotsTaken.setText(String.valueOf(calcSpots));
    }

    if (e.getSource() == btnReset) {
      // clear the board to start a new game
      for (int i = 0; i < loop; i++) {
        cells[i] = BLANK;
        btnBox[i].setIcon(null);
        btnBox[i].setEnabled(true);
      }

      // reset the header test back to default based
      // on the gamemode selected on the settings
      if (gamemode.equals("Singleplayer")) lblHeader.setText(
        "Tic-Tac-Toe"
      ); else {
        if (turn) {
          lblHeader.setText("Player 2 Turn!");
          player = false;
          turn = !turn;
        } else {
          lblHeader.setText("Player 1 Turn!");
          player = true;
          turn = !turn;
        }
      }

      if (matchTimer) lblTimer.setText("0");
      if (boardInfo) lblSpotsTaken.setText("0");
    } else if (e.getSource() == btnMusic) {
      if (soundLoaded) {
        if (cMusic.isRunning()) {
          cMusic.stop(); // Stop the player if it is still running
          btnMusic.setIcon(icMusicOff);
        } else {
          cMusic.start(); // Start playing if audio is paused
          btnMusic.setIcon(icMusic);
        }
      }
    } else if (e.getSource() == btnAudio) {
      // volume control for background music
      FloatControl volumeControl = (FloatControl) cMusic.getControl(
        FloatControl.Type.MASTER_GAIN
      );

      if (btnAudio.getIcon() == icVolumeUp) {
        volumeControl.setValue(-20.0f); // value to set the volume
        btnAudio.setIcon(icVolumeDown);
      } else if (btnAudio.getIcon() == icVolumeDown) {
        volumeControl.setValue(-80.0f); // value to set the volume
        btnAudio.setIcon(icVolumeOff);
      } else {
        volumeControl.setValue(0.0f); // value to set the volume
        btnAudio.setIcon(icVolumeUp);
      }
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

  // method for the computer
  // to decide on its next move
  public void compTurn() {
    Random random = new Random();
    int idx = searchRow(X); // complete a row of X and win if possible
    if (idx < 0) idx = searchRow(O); // or try to block O from winning
    if (idx < 0) do idx = random.nextInt(loop); while (cells[idx] != BLANK); // otherwise move randomly

    cells[idx] = X;
    btnBox[idx].setIcon(icX);
    cClick2.start();
    cClick2.setFramePosition(0);
  }

  // method for searching the final cells
  // that must be clicked in order to win
  public int searchRow(char player) {
    // the rows to be searched will be different
    // based on the board layout selected in the settings
    switch (board) {
      case 4:
        for (int i = 0; i < validateLane; i++) {
          int result = validate(
            player,
            grid[i][0],
            grid[i][1],
            grid[i][2],
            grid[i][3],
            0,
            0
          );
          if (result >= 0) return result;
        }
        break;
      case 5:
        for (int i = 0; i < validateLane; i++) {
          int result = validate(
            player,
            grid[i][0],
            grid[i][1],
            grid[i][2],
            grid[i][3],
            grid[i][4],
            0
          );
          if (result >= 0) return result;
        }
        break;
      case 6:
        for (int i = 0; i < validateLane; i++) {
          int result = validate(
            player,
            grid[i][0],
            grid[i][1],
            grid[i][2],
            grid[i][3],
            grid[i][4],
            grid[i][5]
          );
          if (result >= 0) return result;
        }
        break;
      default:
        for (int i = 0; i < validateLane; i++) {
          int result = validate(
            player,
            grid[i][0],
            grid[i][1],
            grid[i][2],
            0,
            0,
            0
          );
          if (result >= 0) return result;
        }
    }
    return -1;
  }

  // method to validate and return the last blank cell
  public int validate(char player, int a, int b, int c, int d, int e, int f) {
    // the cells validation be different based
    // on the board layout selected in the settings
    switch (board) {
      case 4:
        if (
          cells[a] == player &&
          cells[b] == player &&
          cells[c] == player &&
          cells[d] == BLANK
        ) return d;
        if (
          cells[a] == player &&
          cells[b] == player &&
          cells[d] == player &&
          cells[c] == BLANK
        ) return c;
        if (
          cells[a] == player &&
          cells[c] == player &&
          cells[d] == player &&
          cells[b] == BLANK
        ) return b;
        if (
          cells[b] == player &&
          cells[c] == player &&
          cells[d] == player &&
          cells[a] == BLANK
        ) return a;
        break;
      case 5:
        if (
          cells[a] == player &&
          cells[b] == player &&
          cells[c] == player &&
          cells[d] == player &&
          cells[e] == BLANK
        ) return e;
        if (
          cells[a] == player &&
          cells[b] == player &&
          cells[c] == player &&
          cells[e] == player &&
          cells[d] == BLANK
        ) return d;
        if (
          cells[a] == player &&
          cells[b] == player &&
          cells[d] == player &&
          cells[e] == player &&
          cells[c] == BLANK
        ) return c;
        if (
          cells[a] == player &&
          cells[c] == player &&
          cells[d] == player &&
          cells[e] == player &&
          cells[b] == BLANK
        ) return b;
        if (
          cells[b] == player &&
          cells[c] == player &&
          cells[d] == player &&
          cells[e] == player &&
          cells[a] == BLANK
        ) return a;
        break;
      case 6:
        if (
          cells[a] == player &&
          cells[b] == player &&
          cells[c] == player &&
          cells[d] == player &&
          cells[e] == player &&
          cells[f] == BLANK
        ) return f;
        if (
          cells[a] == player &&
          cells[b] == player &&
          cells[c] == player &&
          cells[d] == player &&
          cells[f] == player &&
          cells[e] == BLANK
        ) return e;
        if (
          cells[a] == player &&
          cells[b] == player &&
          cells[c] == player &&
          cells[e] == player &&
          cells[f] == player &&
          cells[d] == BLANK
        ) return d;
        if (
          cells[a] == player &&
          cells[b] == player &&
          cells[d] == player &&
          cells[e] == player &&
          cells[f] == player &&
          cells[c] == BLANK
        ) return c;
        if (
          cells[a] == player &&
          cells[c] == player &&
          cells[d] == player &&
          cells[e] == player &&
          cells[f] == player &&
          cells[b] == BLANK
        ) return b;
        if (
          cells[b] == player &&
          cells[c] == player &&
          cells[d] == player &&
          cells[e] == player &&
          cells[f] == player &&
          cells[a] == BLANK
        ) return a;
        break;
      default:
        if (
          cells[a] == player && cells[b] == player && cells[c] == BLANK
        ) return c;
        if (
          cells[a] == player && cells[c] == player && cells[b] == BLANK
        ) return b;
        if (
          cells[b] == player && cells[c] == player && cells[a] == BLANK
        ) return a;
    }
    return -1;
  }

  // method to verify if the selected
  // move will result in winning the game
  boolean isWin(char player) {
    switch (board) {
      case 4:
        for (int i = 0; i < validateLane; i++) {
          if (
            cells[grid[i][0]] == player &&
            cells[grid[i][1]] == player &&
            cells[grid[i][2]] == player &&
            cells[grid[i][3]] == player
          ) return true;
        }
        break;
      case 5:
        for (int i = 0; i < validateLane; i++) {
          if (
            cells[grid[i][0]] == player &&
            cells[grid[i][1]] == player &&
            cells[grid[i][2]] == player &&
            cells[grid[i][3]] == player &&
            cells[grid[i][4]] == player
          ) return true;
        }
        break;
      case 6:
        for (int i = 0; i < validateLane; i++) {
          if (
            cells[grid[i][0]] == player &&
            cells[grid[i][1]] == player &&
            cells[grid[i][2]] == player &&
            cells[grid[i][3]] == player &&
            cells[grid[i][4]] == player &&
            cells[grid[i][5]] == player
          ) return true;
        }
        break;
      default:
        for (int i = 0; i < validateLane; i++) {
          if (
            cells[grid[i][0]] == player &&
            cells[grid[i][1]] == player &&
            cells[grid[i][2]] == player
          ) return true;
        }
        break;
    }
    return false;
  }

  // method to verify if the selected
  // move will result in draw the game
  boolean isDraw() {
    for (int i = 0; i < loop; i++) if (cells[i] == BLANK) return false;
    return true;
  }

  // method for handling the actions
  // required once the game is finished
  void finish(char winner) {
    // finish timer
    if (matchTimer && gameStart) {
      gameStart = false;
      timer.stop();
    }

    // announce result of last game
    if (gamemode.equals("Singleplayer")) {
      if (winner == O) {
        lblHeader.setText("You Win!");
        ++wins;
      } else if (winner == X) {
        lblHeader.setText("Computer Win!");
        ++losses;
      } else {
        lblHeader.setText("Tie!");
        ++draws;
      }
    } else {
      if (winner == O) {
        lblHeader.setText("Player 1 Win!");
        ++wins;
      } else if (winner == X) {
        lblHeader.setText("Player 2 Win!");
        ++losses;
      } else {
        lblHeader.setText("Tie!");
        ++draws;
      }
    }

    // disable grid cells button
    for (int i = 0; i < loop; i++) btnBox[i].setEnabled(false);
  }

  public static void main(String[] args) {
    new Game();
  }
}
