import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

class Main extends JFrame implements ActionListener {
    private JButton[][] buttons;
    private boolean playerX; // Indicates whether it's Player X's turn or not
    private boolean singlePlayerMode; // Flag for single-player mode
    private int roundsPlayed;
    private int playerXWins;
    private int playerOWins;
    private int draws;
    private static final int MAX_ROUNDS = 3;
    private String playerXName = "Player X";
    private String playerOName = "Player O";
    private static final Color backgroundColor = new Color(240, 240, 240);
    private static final Color buttonColor = new Color(220, 220, 220);
    private static final Color controlPanelColor = new Color(200, 200, 200);
    private static final Color textColor = Color.BLACK;

    // Labels to display player scores
    private JLabel playerXScoreLabel;
    private JLabel playerOScoreLabel;

    public Main() {
        setTitle("Tic Tac Toe");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(backgroundColor);
        add(boardPanel, BorderLayout.CENTER);

        buttons = new JButton[3][3];
        playerX = true; // Player X starts first
        roundsPlayed = 0;
        playerXWins = 0;
        playerOWins = 0;
        draws = 0;

        // Initialize buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
                buttons[i][j].addActionListener(this);
                buttons[i][j].setBackground(buttonColor);
                boardPanel.add(buttons[i][j]);
            }
        }

        // Create control panel for symbol selection and scoreboard
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(4, 2));
        controlPanel.setBackground(controlPanelColor);

        JLabel playerXLabel = new JLabel("Player X:");
        JLabel playerOLabel = new JLabel("Player O:");
        playerXScoreLabel = new JLabel("0");
        playerOScoreLabel = new JLabel("0");
        JLabel playerNameLabel = new JLabel("Player Name:");
        JTextField playerNameField = new JTextField();
        JButton selectXButton = new JButton("Select X");
        JButton selectOButton = new JButton("Select O");
        JButton startSinglePlayerButton = new JButton("Single Player");
        JButton startMultiPlayerButton = new JButton("Multiplayer");

        playerXLabel.setForeground(textColor);
        playerOLabel.setForeground(textColor);
        playerXScoreLabel.setForeground(textColor);
        playerOScoreLabel.setForeground(textColor);
        playerNameLabel.setForeground(textColor);
        playerNameField.setBackground(buttonColor);
        selectXButton.setBackground(buttonColor);
        selectOButton.setBackground(buttonColor);
        startSinglePlayerButton.setBackground(buttonColor);
        startMultiPlayerButton.setBackground(buttonColor);

        controlPanel.add(playerXLabel);
        controlPanel.add(playerXScoreLabel);
        controlPanel.add(playerOLabel);
        controlPanel.add(playerOScoreLabel);
        controlPanel.add(playerNameLabel);
        controlPanel.add(playerNameField);
        controlPanel.add(selectXButton);
        controlPanel.add(selectOButton);
        controlPanel.add(startSinglePlayerButton);
        controlPanel.add(startMultiPlayerButton);

        add(controlPanel, BorderLayout.SOUTH);

        selectXButton.addActionListener(e -> {
            playerX = true;
            playerXName = playerNameField.getText();
            JOptionPane.showMessageDialog(this, playerXName + " selected X.");
        });

        selectOButton.addActionListener(e -> {
            playerX = false;
            playerOName = playerNameField.getText();
            JOptionPane.showMessageDialog(this, playerOName + " selected O.");
        });

        startSinglePlayerButton.addActionListener(e -> {
            singlePlayerMode = true;
            playerOName = "Computer";
            JOptionPane.showMessageDialog(this, "Single player mode activated.");
        });

        startMultiPlayerButton.addActionListener(e -> {
            singlePlayerMode = false;
            JOptionPane.showMessageDialog(this, "Multiplayer mode activated.");
        });
    }

    // Action performed when a button is clicked
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();

        if (clickedButton.getText().equals("")) {
            if (playerX) {
                clickedButton.setText("X");
            } else {
                clickedButton.setText("O");
            }
            playerX = !playerX; // Switch turn

            if (checkWin()) {
                if (playerX) {
                    playerOWins++;
                } else {
                    playerXWins++;
                }
                updateScoreboard();
                JOptionPane.showMessageDialog(this, (playerX ? playerOName : playerXName) + " wins this round!");
                roundsPlayed++;
                if (roundsPlayed == MAX_ROUNDS) {
                    JOptionPane.showMessageDialog(this, "Game Over! All rounds played.");
                    displayFinalScore();
                    System.exit(0);
                } else {
                    resetGame();
                }
            } else if (checkDraw()) {
                draws++;
                JOptionPane.showMessageDialog(this, "It's a draw this round!");
                roundsPlayed++;
                if (roundsPlayed == MAX_ROUNDS) {
                    JOptionPane.showMessageDialog(this, "Game Over! All rounds played.");
                    displayFinalScore();
                    System.exit(0);
                } else {
                    resetGame();
                }
            } else if (singlePlayerMode && !playerX) {
                // If in single player mode and it's the computer's turn
                makeComputerMove();
                if (checkWin()) {
                    playerOWins++;
                    updateScoreboard();
                    JOptionPane.showMessageDialog(this, playerOName + " wins this round!");
                    roundsPlayed++;
                    if (roundsPlayed == MAX_ROUNDS) {
                        JOptionPane.showMessageDialog(this, "Game Over! All rounds played.");
                        displayFinalScore();
                        System.exit(0);
                    } else {
                        resetGame();
                    }
                } else if (checkDraw()) {
                    draws++;
                    JOptionPane.showMessageDialog(this, "It's a draw this round!");
                    roundsPlayed++;
                    if (roundsPlayed == MAX_ROUNDS) {
                        JOptionPane.showMessageDialog(this, "Game Over! All rounds played.");
                        displayFinalScore();
                        System.exit(0);
                    } else {
                        resetGame();
                    }
                }
            }
        }
    }

    private void makeComputerMove() {
        // Simulate a simple AI move: randomly choose an empty button
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(3);
            col = rand.nextInt(3);
        } while (!buttons[row][col].getText().equals(""));
        buttons[row][col].setText("O");
        playerX = true; // Switch turn back to player X
    }

    private void resetGame() {
        // Clear the text of all buttons
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                button.setText("");
            }
        }
        playerX = true; // Player X starts first in the next round
    }

    // Check for a win condition
    private boolean checkWin() {
        String[][] board = new String[3][3];
        // Copy the text from buttons to the board array
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = buttons[i][j].getText();
            }
        }

        // Check rows, columns, and diagonals for a win
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]) && !board[i][0].equals("")) {
                return true; // Row win
            }
            if (board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i]) && !board[0][i].equals("")) {
                return true; // Column win
            }
        }
        if (board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && !board[0][0].equals("")) {
            return true; // Diagonal win
        }
        if (board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0]) && !board[0][2].equals("")) {
            return true; // Diagonal win
        }
        return false;
    }

    // Check for a draw condition
    private boolean checkDraw() {
        // Check if all buttons are filled
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                if (button.getText().equals("")) {
                    return false; // Not a draw
                }
            }
        }
        return true; // Draw
    }

    // Update the scoreboard with the current wins
    private void updateScoreboard() {
        playerXScoreLabel.setText(String.valueOf(playerXWins));
        playerOScoreLabel.setText(String.valueOf(playerOWins));
    }

    private void displayFinalScore() {
        JOptionPane.showMessageDialog(this,
                "Final Score:\n" +
                        playerXName + ": " + playerXWins + " wins\n" +
                        playerOName + ": " + playerOWins + " wins\n" +
                        "Draws: " + draws);
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}
