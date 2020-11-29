package rogue;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TerminalPosition;

import javax.swing.JFrame;
import java.awt.Container;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.SwingConstants;
import java.util.ArrayList;
import javax.swing.ListModel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Component;
import javax.swing.BorderFactory;

public class WindowUI extends JFrame {


    private SwingTerminal terminal;
    private TerminalScreen screen;
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1200;
    // Screen buffer dimensions are different than terminal dimensions
    public static final int COLS = 80;
    public static final int ROWS = 24;
   private final char startCol = 0;
   private final char msgRow = 1;
   private final char roomRow = 3;
   private Container contentPane;
   private JLabel messageLabel = new JLabel("Welcome to ROGUE!!!!!!");


/**
Constructor.
**/

    public WindowUI() {
        super("my awesome game");
        contentPane = getContentPane();
        setWindowDefaults(getContentPane());
        setUpPanels();
        pack();
        start();
    }

    private void setWindowDefaults(Container contentPane) {
        setTitle("Rogue!");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        contentPane.setLayout(new GridBagLayout());

    }

    private void setTerminal() {
        JPanel terminalPanel = new JPanel();
        terminal = new SwingTerminal();
        terminalPanel.add(terminal);
        contentPane.add(terminalPanel,constraints(0,1,1,1));
    }

    private void setUpPanels(){
        JPanel labelPanel = new JPanel();
        setUpLabelPanel(labelPanel);
        setTerminal();
        initiateInventory();
    }

    private void initiateInventory(){
      JPanel inventoryPanel = new JPanel();
      BoxLayout layout = new BoxLayout(inventoryPanel,BoxLayout.Y_AXIS);
      inventoryPanel.setLayout(layout);
      inventoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
      inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));
      inventoryPanel.setPreferredSize(new Dimension(125, 300));
      contentPane.add(inventoryPanel, constraints(1,1,1,1));
    }

    private void setUpLabelPanel(JPanel thePanel){
        Border prettyLine = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        thePanel.setBorder(prettyLine);
        thePanel.setPreferredSize(new Dimension(100,35));
        messageLabel.setBackground(Color.LIGHT_GRAY);
        messageLabel.setPreferredSize(new Dimension(300, 27));
        thePanel.add(messageLabel);
        contentPane.add(thePanel, constraints(0,0,1,1));
    }

    private void start() {
        try {
            screen = new TerminalScreen(terminal);
            screen.setCursorPosition(TerminalPosition.TOP_LEFT_CORNER);
            screen.startScreen();
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GridBagConstraints constraints(int x, int y, int width, int height){
      GridBagConstraints c = new GridBagConstraints();
      c.gridx = x;
      c.gridy = y;
      c.gridwidth = width;
      c.gridheight = height;
      c.fill = GridBagConstraints.BOTH;
      return(c);
    }

    /**
Prints a string to the screen starting at the indicated column and row.
@param toDisplay the string to be printed
@param column the column in which to start the display
@param row the row in which to start the display
**/
        public void putString(String toDisplay, int column, int row) {

            Terminal t = screen.getTerminal();
            try {
                // t.setCursorPosition(column, row);
            for (char ch: toDisplay.toCharArray()) {
                t.putCharacter(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        }

/**
Changes the message at the top of the screen for the user.
@param msg the message to be displayed
**/
            public void setMessage(String msg) {
              messageLabel.setText(msg);
            }

/**
Redraws the whole screen including the room and the message.
@param message the message to be displayed at the top of the room
@param room the room map to be drawn
**/
            public void draw(String message, String room) {

                try {
                    terminal.clearScreen();
                    putString(room, startCol, roomRow);
                    setMessage(message);
                    screen.refresh();
                    // pack();
                } catch (IOException e) {

                }

        }

/**
Obtains input from the user and returns it as a char.  Converts arrow
keys to the equivalent movement keys in rogue.
@return the ascii value of the key pressed by the user
**/
        public char getInput() {
            KeyStroke keyStroke = null;
            char returnChar;
            while (keyStroke == null) {
            try {
                keyStroke = screen.pollInput();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
         if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            returnChar = Rogue.DOWN;  //constant defined in rogue
        } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            returnChar = Rogue.UP;
        } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            returnChar = Rogue.LEFT;
        } else if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            returnChar = Rogue.RIGHT;
        } else {
            returnChar = keyStroke.getCharacter();
        }
        return returnChar;
    }

    public void setInventory(String list[]){
        JPanel inventoryPanel = new JPanel();
        BoxLayout layout = new BoxLayout(inventoryPanel,BoxLayout.Y_AXIS);
        inventoryPanel.setLayout(layout);
        inventoryPanel.setPreferredSize(new Dimension(100, 300));
        JList inventory = new JList(list);
        inventory.setFixedCellWidth(125);
        inventory.setFixedCellHeight(20);
        inventory.setBackground(Color.LIGHT_GRAY);
        inventoryPanel.add(inventory);
        inventoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));
        contentPane.add(inventoryPanel, constraints(1,1,1,1));
    }

/**
The controller method for making the game logic work.
@param args command line parameters
**/
    public static void main(String[] args) {
       char userInput = 'h';
    String message;
    String configurationFileLocation = "fileLocations.json";
    //Parse the json files
    RogueParser parser = new RogueParser(configurationFileLocation);
    //allocate memory for the GUI
    WindowUI theGameUI = new WindowUI();
    // allocate memory for the game and set it up
    Rogue theGame = new Rogue(parser);
   //set up the initial game display
    Player thePlayer = new Player("Judi");
    theGame.setPlayer(thePlayer);
    System.out.println(theGame.getPlayer().getName());
    message = "Welcome to my Rogue game";
    theGameUI.draw(message, theGame.getNextDisplay());
    theGameUI.setInventory(thePlayer.getInventory());
    theGameUI.setVisible(true);

    while (userInput != 'q') {
    //get input from the user
    userInput = theGameUI.getInput();

    //ask the game if the user can move there
    try {
        message = theGame.makeMove(userInput);
        theGameUI.setInventory(thePlayer.getInventory());
        theGameUI.draw(message, theGame.getNextDisplay());
    } catch (InvalidMoveException badMove) {
        message = "I didn't understand what you meant, please enter a command";
        theGameUI.draw(message, theGame.getNextDisplay());
    }
    }


    }

}
