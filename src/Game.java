import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;

public class Game extends Config implements ActionListener {

  // declare the instance variables
  private JFrame frame;
  private JLabel gameBackground, lblIconTimer, lblTimer, lblIconSpots, lblSpots, lblIconPlayerWin, lblPlayerWin, lblIconBotWin, lblBotWin, lblPlayer, lblBot, lblPlayerTurn, lblBotTurn;
  private JPanel pnlBox;
  private JButton btnBack, btnMusic, btnBox[], btnResetScore, btnNewGame;
  private File fMusic, fClickOne, fClickTwo;
  private AudioInputStream adMusic, adClickOne, adClickTwo;
  private Clip cMusic, cClickOne, cClickTwo;
  private Timer timer;
  private String gamemode, difficulty, music, pathName;
  private int winPlayer, winBot, winPlayerOne, winPlayerTwo, board, grid[][], validateLane;
  private boolean isMusic, matchTimer, spotsTaken, winnerCounter, soundLoaded, player =
    true, turn = true, gameStart = false, isButtonEnabled = true;
  private char BLANK = ' ', O = 'O', X = 'X', cells[];
  private long start;

  public Game() {
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

    // initialize grid, row validation, settings for
    // each grid. by default the board layout will
    // be 3x3, user can select up to 6x6
    switch (board) {
      case 4:
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
        validateLane = 10;
        break;
      case 5:
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
        validateLane = 12;
        break;
      case 6:
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
        validateLane = 14;
        break;
      default:
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
        validateLane = 8;
    }

    // call showGUI() method to display
    // the gui for the game screen
    showGUI();

    // show turn text if playing multiplayer
    if (gamemode.equals("Multiplayer")) {
      lblPlayer.setText("Player 1");
      lblBot.setText("Player 2");
      lblPlayer.setIcon(icPlayer);
      lblBot.setIcon(icPlayer);
      lblPlayer.setBounds(120, 187, 150, 32);
      lblBot.setBounds(880, 187, 150, 32);
      lblPlayerTurn.setText("Player 1 Turn");
      lblBotTurn.setText("");
    }

    // show match timer if selected in the settings
    if (matchTimer) {
      lblIconTimer.setIcon(icTimer);
    } else {
      lblIconTimer.setIcon(icTimerOff);
    }

    // show spots taken if selected in the settings
    if (spotsTaken) {
      lblIconSpots.setIcon(icSpots);
    } else {
      lblIconSpots.setIcon(icSpotsOff);
    }

    // show winner counter if selected in the settings
    if (winnerCounter) {
      if (gamemode.equals("Singleplayer")) {
        lblIconPlayerWin.setIcon(icPlayer);
        lblIconBotWin.setIcon(icBot);
        lblPlayerWin.setText(String.valueOf(winPlayer));
        lblBotWin.setText(String.valueOf(winBot));
      } else {
        lblIconPlayerWin.setIcon(icPlayerOne);
        lblIconBotWin.setIcon(icPlayerTwo);
        lblPlayerWin.setText(String.valueOf(winPlayerOne));
        lblBotWin.setText(String.valueOf(winPlayerTwo));
      }
    } else {
      if (gamemode.equals("Singleplayer")) {
        lblIconPlayerWin.setIcon(icPlayerOff);
        lblIconBotWin.setIcon(icBotOff);
      } else {
        lblIconPlayerWin.setIcon(icPlayerOneOff);
        lblIconBotWin.setIcon(icPlayerTwoOff);
      }
    }

    // try to load the sound from the file
    try {
      // set music based on selection from the settings
      if (music.equals("Angry Remix")) {
        pathName = "audio/angry-remix.wav";
      } else {
        pathName = "audio/cat.wav";
      }

      // open an audio input stream
      fMusic = new File(pathName);
      fClickOne = new File("audio/pew.wav");
      fClickTwo = new File("audio/paww.wav");

      adMusic = AudioSystem.getAudioInputStream(fMusic);
      adClickOne = AudioSystem.getAudioInputStream(fClickOne);
      adClickTwo = AudioSystem.getAudioInputStream(fClickTwo);

      // get a sound clip resource, open audio clip, and
      // load samples from the audio input stream
      cMusic = AudioSystem.getClip();
      cClickOne = AudioSystem.getClip();
      cClickTwo = AudioSystem.getClip();

      cMusic.open(adMusic);
      cClickOne.open(adClickOne);
      cClickTwo.open(adClickTwo);

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
    // if the user turn on the music
    if (isMusic) {
      cMusic.start();
      cMusic.loop(Clip.LOOP_CONTINUOUSLY);
    } else {
      btnMusic.setIcon(icMusicOff);
    }

    // volume control for background music
    // FloatControl volumeControl = (FloatControl) cMusic.getControl(
    //   FloatControl.Type.MASTER_GAIN
    // );
    // volumeControl.setValue(-20.0f); // value to set the volume

    // show the frame
    frame.setSize(1132, 660);
    frame.setUndecorated(true);
    frame.setVisible(true);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.getContentPane().setBackground(clrMaroon);
  }

  // method to get the settings and
  // score data from the database
  public void getData() {
    newConn();

    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT * FROM score a, settings b");

      while (rs.next()) {
        winPlayer = rs.getInt("player");
        winBot = rs.getInt("bot");
        winPlayerOne = rs.getInt("player_one");
        winPlayerTwo = rs.getInt("player_two");
        gamemode = rs.getString("gamemode");
        difficulty = rs.getString("difficulty");
        board = rs.getInt("board");
        music = rs.getString("music");
        isMusic = rs.getBoolean("is_music");
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
  // gui for the game screen
  public void showGUI() {
    // background image
    gameBackground = new JLabel(new ImageIcon("images/game-bg.png"));
    gameBackground.setLayout(null);

    // button to go back to the main screen
    btnBack = new JButton(icBack);
    btnBack.addActionListener(this);
    btnBack.setBounds(60, 40, 48, 48);
    btnBack.setFocusPainted(false);
    btnBack.setBorderPainted(false);
    btnBack.setContentAreaFilled(false);

    // button to turn on or off the music
    btnMusic = new JButton(icMusic);
    btnMusic.addActionListener(this);
    btnMusic.setBounds(1024, 40, 48, 48);
    btnMusic.setFocusPainted(false);
    btnMusic.setBorderPainted(false);
    btnMusic.setContentAreaFilled(false);

    // match timer information
    lblIconTimer = new JLabel(icTimer);
    lblTimer = new JLabel("0");
    lblIconTimer.setBounds(432, 61, 32, 32);
    lblTimer.setBounds(473, 59, 32, 32);
    lblTimer.setFont(new Font("Roboto", Font.BOLD, 21));
    lblTimer.setForeground(clrWhite);

    // spots taken information
    lblIconSpots = new JLabel(icSpots);
    lblSpots = new JLabel("0");
    lblIconSpots.setBounds(504, 61, 32, 32);
    lblSpots.setBounds(545, 59, 32, 32);
    lblSpots.setFont(new Font("Roboto", Font.BOLD, 21));
    lblSpots.setForeground(clrWhite);

    // player or player 1 win information
    lblIconPlayerWin = new JLabel(icPlayer);
    lblPlayerWin = new JLabel("0");
    lblIconPlayerWin.setBounds(576, 61, 32, 32);
    lblPlayerWin.setBounds(617, 59, 32, 32);
    lblPlayerWin.setFont(new Font("Roboto", Font.BOLD, 21));
    lblPlayerWin.setForeground(clrWhite);

    // bot or player 2 win information
    lblIconBotWin = new JLabel(icBot);
    lblBotWin = new JLabel("0");
    lblIconBotWin.setBounds(648, 61, 32, 32);
    lblBotWin.setBounds(689, 59, 32, 32);
    lblBotWin.setFont(new Font("Roboto", Font.BOLD, 21));
    lblBotWin.setForeground(clrWhite);

    // display the grid button based on the
    // board layout selected on the settings
    pnlBox = new JPanel(new GridLayout(board, board, 5, 5));
    pnlBox.setBounds(360, 124, 413, 413);
    pnlBox.setBackground(clrMaroon);
    for (int i = 0; i < (board * board); i++) {
      btnBox[i] = new JButton();
      btnBox[i].addActionListener(this);
      btnBox[i].setFont(new Font("Roboto", Font.BOLD, 64));
      btnBox[i].setBackground(clrMaroonBg);
      btnBox[i].setFocusPainted(false);
      btnBox[i].setBorder(BorderFactory.createLineBorder(clrMaroonDark, 5));
      pnlBox.add(btnBox[i]);
    }

    // player or player 1 title header
    lblPlayer = new JLabel("Player", icPlayer, SwingConstants.LEFT);
    lblPlayer.setBounds(130, 187, 150, 32);
    lblPlayer.setIconTextGap(10);
    lblPlayer.setFont(new Font("Open Sans", Font.BOLD, 24));
    lblPlayer.setForeground(clrWhite);

    // bot or player 2 title header
    lblBot = new JLabel("Bot", icBot, SwingConstants.LEFT);
    lblBot.setBounds(907, 187, 150, 32);
    lblBot.setIconTextGap(10);
    lblBot.setFont(new Font("Roboto", Font.BOLD, 24));
    lblBot.setForeground(clrWhite);

    // showing turn information for player or player 1
    lblPlayerTurn = new JLabel("Your Turn", SwingConstants.CENTER);
    lblPlayerTurn.setBounds(85, 438, 199, 58);
    lblPlayerTurn.setFont(new Font("Roboto", Font.BOLD, 20));
    lblPlayerTurn.setBorder(new MatteBorder(styBorder, 0, 0, 0, clrWhite));
    lblPlayerTurn.setForeground(clrWhite);

    // showing turn information for bot or player 2
    lblBotTurn = new JLabel("", SwingConstants.CENTER);
    lblBotTurn.setBounds(847, 438, 199, 58);
    lblBotTurn.setFont(new Font("Roboto", Font.BOLD, 20));
    lblBotTurn.setBorder(new MatteBorder(styBorder, 0, 0, 0, clrWhite));
    lblBotTurn.setForeground(clrWhite);

    // button to reset the score
    btnResetScore = new JButton("Reset Score", icReset);
    btnResetScore.addActionListener(this);
    btnResetScore.setBounds(360, 568, 199, 60);
    btnResetScore.setFont(new Font("Roboto", Font.BOLD, 18));
    btnResetScore.setForeground(clrWhite);
    btnResetScore.setBackground(clrMaroon);
    btnResetScore.setFocusPainted(false);
    btnResetScore.setIconTextGap(20);
    btnResetScore.setBorder(
      BorderFactory.createLineBorder(clrWhite, styBorder)
    );

    // button to start a new game
    btnNewGame = new JButton("New Game", icNewGame);
    btnNewGame.addActionListener(this);
    btnNewGame.setBounds(574, 568, 199, 60);
    btnNewGame.setFont(new Font("Roboto", Font.BOLD, 18));
    btnNewGame.setForeground(clrWhite);
    btnNewGame.setBackground(clrMaroon);
    btnNewGame.setFocusPainted(false);
    btnNewGame.setIconTextGap(20);
    btnNewGame.setBorder(BorderFactory.createLineBorder(clrWhite, styBorder));

    // add the component to the frame
    gameBackground.add(btnBack);
    gameBackground.add(btnMusic);
    gameBackground.add(lblIconTimer);
    gameBackground.add(lblTimer);
    gameBackground.add(lblIconSpots);
    gameBackground.add(lblSpots);
    gameBackground.add(lblIconPlayerWin);
    gameBackground.add(lblPlayerWin);
    gameBackground.add(lblIconBotWin);
    gameBackground.add(lblBotWin);
    gameBackground.add(pnlBox);
    gameBackground.add(lblPlayer);
    gameBackground.add(lblBot);
    gameBackground.add(lblPlayerTurn);
    gameBackground.add(lblBotTurn);
    gameBackground.add(btnResetScore);
    gameBackground.add(btnNewGame);
    frame.add(gameBackground);
  }

  // method to handle the button component
  public void actionPerformed(ActionEvent e) {
    // handle the grid button based on the
    // gamemode selected on the settings
    if (isButtonEnabled) {
      if (gamemode.equals("Singleplayer")) { // singleplayer
        for (int i = 0; i < (board * board); i++) {
          if (e.getSource().equals(btnBox[i])) {
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
              btnBox[i].setText("O");
              btnBox[i].setForeground(clrYellow);
              btnBox[i].setBackground(clrYellowBg);
              btnBox[i].setBorder(BorderFactory.createLineBorder(clrYellow, 5));
              cClickOne.start();
              cClickOne.setFramePosition(0);
              if (isWin(O)) finish(O); else if (isDraw()) finish(BLANK); else {
                compTurn();
                if (isWin(X)) finish(X); else if (isDraw()) finish(BLANK);
              }
            }
          }
        }
      } else { // multiplayer
        for (int i = 0; i < (board * board); i++) {
          if (e.getSource().equals(btnBox[i])) {
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
                btnBox[i].setText("O");
                btnBox[i].setForeground(clrYellow);
                btnBox[i].setBackground(clrYellowBg);
                btnBox[i].setBorder(
                    BorderFactory.createLineBorder(clrYellow, 5)
                  );
                lblBotTurn.setText("Player 2 Turn!");
                lblPlayerTurn.setText("");
                cClickOne.start();
                cClickOne.setFramePosition(0);
                player = false;
                if (isWin(O)) finish(O); else if (isDraw()) finish(BLANK);
              } else {
                cells[i] = X;
                btnBox[i].setText("X");
                btnBox[i].setForeground(clrRed);
                btnBox[i].setBackground(clrRedBg);
                btnBox[i].setBorder(BorderFactory.createLineBorder(clrRed, 5));
                lblPlayerTurn.setText("Player 1 Turn!");
                lblBotTurn.setText("");
                cClickTwo.start();
                cClickTwo.setFramePosition(0);
                player = true;
                if (isWin(X)) finish(X); else if (isDraw()) finish(BLANK);
              }
            }
          }
        }
      }
    }

    // calculate how many grid spots have been taken
    if (spotsTaken) {
      int calcSpots = 0;
      for (int i = 0; i < (board * board); i++) {
        if (cells[i] != BLANK) {
          ++calcSpots;
        }
      }
      lblSpots.setText(String.valueOf(calcSpots));
    }

    if (e.getSource().equals(btnBack)) { // handle back button
      if (cMusic.isRunning()) cMusic.stop(); // stop music
      frame.dispose(); // dispose this frame
    } else if (e.getSource().equals(btnMusic)) {
      newConn();

      if (soundLoaded) {
        if (cMusic.isRunning()) { // stop music
          cMusic.stop(); // stop the player if it is still running
          btnMusic.setIcon(icMusicOff);
          try {
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE settings SET is_music = " + false);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        } else {
          cMusic.start(); // start playing if music is stop
          btnMusic.setIcon(icMusic);
          try {
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE settings SET is_music = " + true);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }

      closeConn();
    } else if (e.getSource().equals(btnResetScore)) { // handle reset score button
      if (winnerCounter) {
        // ask user for confirmation
        int result = JOptionPane.showConfirmDialog(
          frame,
          "Are you sure you want to reset the score?",
          "Reset Score",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE
        );

        // reset the score if user select yes
        if (result == JOptionPane.YES_OPTION) {
          if (gamemode.equals("Singleplayer")) {
            winPlayer = 0;
            winBot = 0;
          } else {
            winPlayerOne = 0;
            winPlayerTwo = 0;
          }
          lblPlayerWin.setText("0");
          lblBotWin.setText("0");

          // reset the score on database
          newConn();

          try {
            stmt = conn.createStatement();
            stmt.executeUpdate(
              "UPDATE score SET player = " +
              winPlayer +
              ", bot = " +
              winBot +
              ", player_one = " +
              winPlayerOne +
              ", player_two = " +
              winPlayerTwo
            );
          } catch (Exception ex) {
            ex.printStackTrace();
          }

          closeConn();

          // start a new game after resetting the score
          btnNewGame.doClick();
        }
      } else {
        // reset score is not available if user
        // turn off the reset score settings
        JOptionPane.showMessageDialog(
          frame,
          "Resetting the score is not available if you have disabled the winner counter.",
          "Reset Score",
          JOptionPane.WARNING_MESSAGE
        );
      }
    } else if (e.getSource().equals(btnNewGame)) { // handle new game button
      // finish timer
      if (matchTimer && gameStart) {
        gameStart = false;
        timer.stop();
      }

      // enable grid cells button
      isButtonEnabled = true;

      // clear the board to start a new game
      for (int i = 0; i < (board * board); i++) {
        cells[i] = BLANK;
        btnBox[i].setText(null);
        btnBox[i].setBackground(clrMaroonBg);
        btnBox[i].setBorder(BorderFactory.createLineBorder(clrMaroonDark, 5));
      }

      // reset text back to default based
      // on the gamemode selection
      if (gamemode.equals("Singleplayer")) {
        lblPlayerTurn.setText("Your Turn");
        lblBotTurn.setText("");
      } else {
        if (turn) {
          lblBotTurn.setText("Player 2 Turn!");
          lblPlayerTurn.setText("");
          player = false;
          turn = !turn;
        } else {
          lblPlayerTurn.setText("Player 1 Turn!");
          lblBotTurn.setText("");
          player = true;
          turn = !turn;
        }
      }

      if (matchTimer) lblTimer.setText("0");
      if (spotsTaken) lblSpots.setText("0");
    }
  }

  // method for the computer
  // to decide on its next move
  public void compTurn() {
    Random random = new Random();

    // complete a row of X and win if possible
    int idx = searchRow(X);

    // if mode is easy, comp will
    // not try to block the user
    if (difficulty.equals("Hard")) {
      if (idx < 0) idx = searchRow(O); // or try to block O from winning
    }

    // otherwise move randomly
    if (idx < 0) do idx = random.nextInt(board * board); while (
      cells[idx] != BLANK
    );

    cells[idx] = X;
    btnBox[idx].setText("X");
    btnBox[idx].setForeground(clrRed);
    btnBox[idx].setBackground(clrRedBg);
    btnBox[idx].setBorder(BorderFactory.createLineBorder(clrRed, 5));
    cClickTwo.start();
    cClickTwo.setFramePosition(0);
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
    // assign color for styling purpose
    if (player == O) tempColor = clrYellow; else tempColor = clrRed;

    switch (board) {
      case 4:
        for (int i = 0; i < validateLane; i++) {
          if (
            cells[grid[i][0]] == player &&
            cells[grid[i][1]] == player &&
            cells[grid[i][2]] == player &&
            cells[grid[i][3]] == player
          ) {
            for (int j = 0; j < board; j++) {
              btnBox[grid[i][j]].setBackground(tempColor);
              btnBox[grid[i][j]].setForeground(clrMaroon);
            }
            return true;
          }
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
          ) {
            for (int j = 0; j < board; j++) {
              btnBox[grid[i][j]].setBackground(tempColor);
              btnBox[grid[i][j]].setForeground(clrMaroon);
            }
            return true;
          }
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
          ) {
            for (int j = 0; j < board; j++) {
              btnBox[grid[i][j]].setBackground(tempColor);
              btnBox[grid[i][j]].setForeground(clrMaroon);
            }
            return true;
          }
        }
        break;
      default:
        for (int i = 0; i < validateLane; i++) {
          if (
            cells[grid[i][0]] == player &&
            cells[grid[i][1]] == player &&
            cells[grid[i][2]] == player
          ) {
            for (int j = 0; j < board; j++) {
              btnBox[grid[i][j]].setBackground(tempColor);
              btnBox[grid[i][j]].setForeground(clrMaroon);
            }
            return true;
          }
        }
        break;
    }
    return false;
  }

  // method to verify if the selected
  // move will result in draw the game
  boolean isDraw() {
    for (int i = 0; i < (board * board); i++) {
      if (cells[i] == BLANK) {
        return false;
      }
    }

    return true;
  }

  // method for handling the actions
  // required once the game is finished
  public void finish(char winner) {
    // finish timer
    if (matchTimer && gameStart) {
      gameStart = false;
      timer.stop();
    }

    // announce result of last game
    if (gamemode.equals("Singleplayer")) {
      if (winner == O) {
        ++winPlayer;
        if (winnerCounter) lblPlayerWin.setText(String.valueOf(winPlayer));
        lblPlayerTurn.setText("You Win!");
        lblBotTurn.setText("");
      } else if (winner == X) {
        ++winBot;
        if (winnerCounter) lblBotWin.setText(String.valueOf(winBot));
        lblBotTurn.setText("Computer Win!");
        lblPlayerTurn.setText("");
      } else {
        lblPlayerTurn.setText("Tie!");
        lblBotTurn.setText("Tie!");
      }
    } else {
      if (winner == O) {
        ++winPlayerOne;
        if (winnerCounter) lblPlayerWin.setText(String.valueOf(winPlayerOne));
        lblPlayerTurn.setText("Player 1 Win!");
        lblBotTurn.setText("");
      } else if (winner == X) {
        ++winPlayerTwo;
        if (winnerCounter) lblBotWin.setText(String.valueOf(winPlayerTwo));
        lblBotTurn.setText("Player 2 Win!");
        lblPlayerTurn.setText("");
      } else {
        lblPlayerTurn.setText("Tie!");
        lblBotTurn.setText("Tie!");
      }
    }

    // update win score on the database
    if (winnerCounter) {
      newConn();

      try {
        stmt = conn.createStatement();
        stmt.executeUpdate(
          "UPDATE score SET player = " +
          winPlayer +
          ", bot = " +
          winBot +
          ", player_one = " +
          winPlayerOne +
          ", player_two = " +
          winPlayerTwo
        );
      } catch (Exception e) {
        e.printStackTrace();
      }

      closeConn();
    }

    // disable grid cells button
    isButtonEnabled = false;
  }
}
