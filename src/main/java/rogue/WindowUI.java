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

import javax.swing.JLabel;
import javax.swing.JPanel;


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



import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;



public class WindowUI extends JFrame implements ActionListener {


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
   private JList inventory;
   private int indexOfItem = -1;
   private JFrame promptFrame = new JFrame();
   private JLabel playerName = new JLabel();
   private JFileChooser fc = new JFileChooser();
   private Rogue theGame = new Rogue();
   private Player thePlayer = new Player();

   private static final int ONE = 1;
   private static final int ZERO = 0;
   private static final int TWO = 2;
   private static final int ONEHUNDREDTHIRTYFIVE = 135;
   private static final int TWOHUNDREDTWENTYFIVE = 225;
   private static final int ONEHUNDREDFIFTY = 150;
   private static final int ONEHUNDRED = 100;
   private static final int THIRTYFIVE = 35;
   private static final int FOURHUNDRED = 400;
   private static final int TWENTYFIVE = 25;
   private static final int TWOHUNDRED = 200;
   private static final int FIFTYFIVE = 55;
   private static final int TEN = 10;
   private static final int ONEHUNDREDTWENTYFIVE = 125;
   private static final int TWENTY = 20;


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

    private void setWindowDefaults(Container container) {
        setTitle("Rogue!");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        container.setLayout(new GridBagLayout());

    }

    private void setTerminal() {
        JPanel terminalPanel = new JPanel();
        terminal = new SwingTerminal();
        terminalPanel.add(terminal);
        contentPane.add(terminalPanel, constraints(ZERO, ONE, TWO, TWO));
    }

    private void setUpPanels() {
        JPanel labelPanel = new JPanel();
        setUpLabelPanel(labelPanel);
        setTerminal();
        initiateInventory();
        initiateEquipped();
        setMenu();
        setPlayerPanel();
    }

    private Player getPlayer() {
      return (this.thePlayer);
    }

    private void setPlayer(Player newPlayer) {
      this.thePlayer = newPlayer;
    }

    private void initiateInventory() {
      JPanel inventoryPanel = new JPanel();
      BoxLayout layout = new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS);
      inventoryPanel.setLayout(layout);
      inventoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
      inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));
      inventoryPanel.setPreferredSize(new Dimension(ONEHUNDREDTHIRTYFIVE, TWOHUNDREDTWENTYFIVE));
      inventoryPanel.setBackground(Color.LIGHT_GRAY);
      contentPane.add(inventoryPanel, constraints(TWO, ONE, ONE, ONE));
    }

    private void initiateEquipped() {
      JPanel equippedPanel = new JPanel();
      BoxLayout layout = new BoxLayout(equippedPanel, BoxLayout.Y_AXIS);
      equippedPanel.setLayout(layout);
      equippedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
      equippedPanel.setBorder(BorderFactory.createTitledBorder("Equipped"));
      equippedPanel.setPreferredSize(new Dimension(ONEHUNDREDTHIRTYFIVE, ONEHUNDREDFIFTY));
      equippedPanel.setBackground(Color.LIGHT_GRAY);
      contentPane.add(equippedPanel, constraints(TWO, TWO, ONE, ONE));
    }

    private void setUpLabelPanel(JPanel thePanel) {
        Border prettyLine = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        thePanel.setBorder(prettyLine);
        thePanel.setPreferredSize(new Dimension(ONEHUNDRED, THIRTYFIVE));
        messageLabel.setBackground(Color.LIGHT_GRAY);
        messageLabel.setPreferredSize(new Dimension(FOURHUNDRED, TWENTYFIVE));
        thePanel.add(messageLabel);

        contentPane.add(thePanel, constraints(ONE, ZERO, ONE, ONE));
    }

    /**
    * Set the panel up displaying the player name.
    */
    public void setPlayerPanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Player : ");
        panel.setLayout(new BorderLayout());
        panel.add(playerName, BorderLayout.CENTER);
        panel.add(label, BorderLayout.WEST);
        contentPane.add(panel, constraints(TWO, ZERO, ONE, ONE));

    }

    private void setPlayerName(String newName) {
      this.playerName.setText(newName);
    }

    private String getPlayerName() {
      return (this.playerName.getText());
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

    private GridBagConstraints constraints(int x, int y, int width, int height) {
      GridBagConstraints c = new GridBagConstraints();
      c.gridx = x;
      c.gridy = y;
      c.gridwidth = width;
      c.gridheight = height;
      c.fill = GridBagConstraints.BOTH;
      return (c);
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
        * checks what action is to be made dependent on the input.
        * @param input
        * @param message
        * @return (boolean)
        */
        public boolean checkAction(char input, String message) {
          if (input == 'w' || input == 'e' || input == 't') {
            if (theGame.getPlayer().getInventory().size() == 0) {
              message = "Inventory empty";
              return (false);
            }
            promptUser();
            return (true);
          }
          return (false);
        }

        /**
        * prompt the user to use an item.
        */
        public void promptUser() {
          int index = -1;
          promptFrame = new JFrame();
          promptFrame.setSize(TWOHUNDRED, FOURHUNDRED);
          promptFrame.setLocationRelativeTo(contentPane);
          JLabel label = new JLabel("Choose an Item");
          JButton button = new JButton("Okay");
          JPanel panel = new JPanel();
          BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
          panel.setLayout(layout);
          button.addActionListener(this);
          panel.add(label);
          panel.add(inventory);
          panel.add(button);
          promptFrame.add(panel);
          promptFrame.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
          indexOfItem = inventory.getSelectedIndex();
          promptFrame.setVisible(false);
        }

        private int getIndexOfItem() {
          return (this.indexOfItem);
        }

        private void setIndexOfItem(int i) {
            this.indexOfItem = i;
        }

        private Rogue getRogue() {
          return (this.theGame);
        }

        private void setRogue(Rogue game) {
          this.theGame = game;
        }
/**
Changes the message at the top of the screen for the user.
@param msg the message to be displayed
**/
            private void setMessage(String msg) {
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
            * sets up the drop down menu.
            */
            public void setMenu() {
              JPanel panel = new JPanel();
              panel.setLayout(new BorderLayout());
              JMenuBar menuBar = new JMenuBar();
              menuBar.setPreferredSize(new Dimension(FIFTYFIVE, TEN));
              JMenu menu = new JMenu("Options");
              JMenuItem name = new JMenuItem("Change player name");
              name.addActionListener(ev -> changeName());
              JMenuItem json = new JMenuItem("Load JSON file");
              json.addActionListener(ev -> chooseFile());
              JMenuItem game = new JMenuItem("Load saved game");
              game.addActionListener(ev -> loadGame());
              JMenuItem save = new JMenuItem("Save game");
              save.addActionListener(ev -> save());
              addMenus(name, json, game, save, menu);
              menuBar.add(menu);
              panel.add(menuBar, BorderLayout.CENTER);
              contentPane.add(panel, constraints(ZERO, ZERO, ONE, ONE));
            }

            private void addMenus(JMenuItem name, JMenuItem json, JMenuItem game, JMenuItem save, JMenu m) {
              m.add(name);
              m.add(json);
              m.add(game);
              m.add(save);
            }

            /**
            * change the players name.
            */
            public void changeName() {
              JFrame f = new JFrame();
              f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
              JOptionPane pane = new JOptionPane();
              String check = JOptionPane.showInputDialog(f, "Enter new name");
              if (check != null) {
                setPlayerName(check);
              }
            }

            /**
            * save the game using Serialization.
            */
            public void save() {
              if (fc.showSaveDialog(this.contentPane) == JFileChooser.APPROVE_OPTION) {
                try {
                  FileOutputStream outPutStream = new FileOutputStream(fc.getSelectedFile());
                  ObjectOutputStream outPutDest = new ObjectOutputStream(outPutStream);
                  outPutDest.writeObject(theGame);
                  outPutDest.close();
                  outPutStream.close();
                  JFrame f = new JFrame();
                  JOptionPane.showMessageDialog(f, "Succesfully saved to:" + fc.getSelectedFile());
                } catch (IOException e) {
                  JFrame f = new JFrame();
                  JOptionPane.showMessageDialog(f, "Failed to saved to:" + fc.getSelectedFile());
                }
              }
            }

            private void chooseFile() {
              if (fc.showOpenDialog(this.contentPane) == JFileChooser.APPROVE_OPTION) {
                String filename = fc.getSelectedFile().toString();
                System.out.println(filename);
                loadAdventure(filename);
              }
            }

            /**
            * load a new room json file to play on.
            * @param filename
            */
            public void loadAdventure(String filename) {
              try {
                RogueParser newGame = new RogueParser(filename, true);
                Rogue game = new Rogue(newGame);
                System.out.println(game.getException());
                if (game.getException() == 0) {
                  this.theGame = game;
                  this.thePlayer.emptyInventory();
                  this.theGame.setPlayer(this.thePlayer);
                  draw("Adventure changed", this.theGame.getNextDisplay());
                } else {
                  JFrame frame = new JFrame();
                  JOptionPane.showMessageDialog(frame, "Adventure file unusable", "Alert", JOptionPane.WARNING_MESSAGE);
                }
              } catch (Exception e) {
                  JFrame frame = new JFrame();
                  JOptionPane.showMessageDialog(frame, "File incompatible", "Alert", JOptionPane.WARNING_MESSAGE);
              }
            }

            /**
            * load a saved game using Serialization.
            */
            public void loadGame() {
              if (fc.showOpenDialog(this.contentPane) == JFileChooser.APPROVE_OPTION) {
                try {
                  ObjectInputStream inPutDest = new ObjectInputStream(new FileInputStream(fc.getSelectedFile()));
                  Rogue loadedGame = (Rogue) inPutDest.readObject();
                  this.theGame = loadedGame;
                  this.thePlayer = loadedGame.getPlayer();
                  JFrame f = new JFrame();
                  JOptionPane.showMessageDialog(f, "Succesfully opened:" + fc.getSelectedFile());
                  draw("Game opened", theGame.getNextDisplay());
                } catch (IOException e) {
                  JFrame f = new JFrame();
                  JOptionPane.showMessageDialog(f, "Failed to open:" + fc.getSelectedFile());
                } catch (ClassNotFoundException e) {
                  JFrame f = new JFrame();
                  JOptionPane.showMessageDialog(f, "Failed to open:" + fc.getSelectedFile());
                }
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
            returnChar = checkKeyStroke(keyStroke);
        return returnChar;
    }

    private char checkKeyStroke(KeyStroke keyStroke) {
      if (keyStroke.getKeyType() == KeyType.ArrowDown) {
         return (Rogue.DOWN);  //constant defined in rogue
     } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
         return (Rogue.UP);
     } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
         return (Rogue.LEFT);
     } else if (keyStroke.getKeyType() == KeyType.ArrowRight) {
         return (Rogue.RIGHT);
     } else {
         return (keyStroke.getCharacter());
     }
    }

    /**
    * sets the inventory panel for the player.
    * @param list
    */
    public void setInventory(String[] list) {
        JPanel inventoryPanel = new JPanel();
        BoxLayout layout = new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS);
        inventoryPanel.setLayout(layout);
        inventoryPanel.setPreferredSize(new Dimension(ONEHUNDREDTHIRTYFIVE, TWOHUNDREDTWENTYFIVE));
        inventory = new JList(list);
        inventory.setFixedCellWidth(ONEHUNDREDTWENTYFIVE);
        inventory.setFixedCellHeight(TWENTY);
        inventory.setBackground(Color.LIGHT_GRAY);
        inventoryPanel.add(inventory);
        inventoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));
        inventoryPanel.setBackground(Color.LIGHT_GRAY);
        contentPane.add(inventoryPanel, constraints(TWO, ONE, ONE, ONE));
    }

    /**
    * setup the equipped panel for the player.
    * @param list
    */
    public void setEquipped(String[] list) {
        JPanel equippedPanel = new JPanel();
        BoxLayout layout = new BoxLayout(equippedPanel, BoxLayout.Y_AXIS);
        equippedPanel.setLayout(layout);
        equippedPanel.setPreferredSize(new Dimension(ONEHUNDREDTHIRTYFIVE, ONEHUNDREDFIFTY));
        JList equipped = new JList(list);
        equipped.setFixedCellWidth(ONEHUNDREDTWENTYFIVE);
        equipped.setFixedCellHeight(TWENTY);
        equipped.setBackground(Color.LIGHT_GRAY);
        equippedPanel.add(equipped);
        equippedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        equippedPanel.setBorder(BorderFactory.createTitledBorder("Equipped"));
        equippedPanel.setBackground(Color.LIGHT_GRAY);
        contentPane.add(equippedPanel, constraints(TWO, TWO, ONE, ONE));
    }

    private void initializeWindow(WindowUI theGameUI, String filename, String message, Player player) {
      RogueParser parser = new RogueParser(filename);
      // allocate memory for the game and set it up
      Rogue initialGame = new Rogue(parser);
      theGameUI.setRogue(initialGame);
     //set up the initial game display
      theGameUI.setPlayer(player);
      theGameUI.getRogue().setPlayer(player);
      theGameUI.setPlayerName(theGameUI.getRogue().getPlayer().getName());
      theGameUI.draw(message, theGameUI.getRogue().getNextDisplay());
      theGameUI.setInventory(player.getInventoryStrings());
      theGameUI.setEquipped(player.getEquippedStrings());
      theGameUI.setVisible(true);
    }

    private void itemUsage(WindowUI theGameUI, String message, Player player, char userInput) {
      if (theGameUI.checkAction(userInput, message)) {
        message = "Input c on keyboard to apply changes";
        theGameUI.draw(message, theGameUI.getRogue().getNextDisplay());
        while (theGameUI.getInput() != 'c') {
          message = "Input c on keyboard to apply changes";
          theGameUI.draw(message, theGameUI.getRogue().getNextDisplay());
        }
        if (theGameUI.getIndexOfItem() != -1) {
          theGameUI.getRogue().useItem(userInput, theGameUI.getIndexOfItem());
          theGameUI.setInventory(player.getInventoryStrings());
          theGameUI.setEquipped(player.getEquippedStrings());
          theGameUI.setIndexOfItem(-1);
        }
      }
    }

    private void updateRoom(String message, WindowUI theGameUI, char userInput, Player player) {
      try {
          message = theGameUI.getRogue().makeMove(userInput);
          theGameUI.setInventory(player.getInventoryStrings());
          theGameUI.setEquipped(player.getEquippedStrings());
          theGameUI.draw(message, theGameUI.getRogue().getNextDisplay());
          theGameUI.setPlayerPanel();
      } catch (InvalidMoveException badMove) {
          message = "I didn't understand what you meant, please enter a command";
          theGameUI.draw(message, theGameUI.getRogue().getNextDisplay());
      }
    }

/**
The controller method for making the game logic work.
@param args command line parameters
**/
    public static void main(String[] args) {
    char userInput = 'h';
    String message;
    String configurationFileLocation = "fileLocations.json";
    WindowUI theGameUI = new WindowUI();
    Player thePlayer = new Player("Judi");
    message = "Welcome to my Rogue game";
    theGameUI.initializeWindow(theGameUI, configurationFileLocation, message, thePlayer);
    while (userInput != 'q') {
    theGameUI.draw(message, theGameUI.getRogue().getNextDisplay());
    theGameUI.getRogue().getPlayer().setName(theGameUI.getPlayerName());
    userInput = theGameUI.getInput();
    thePlayer = theGameUI.getPlayer();
    theGameUI.itemUsage(theGameUI, message, thePlayer, userInput);
    theGameUI.updateRoom(message, theGameUI, userInput, thePlayer);
    theGameUI.setPlayer(thePlayer);
    }
    }
}
