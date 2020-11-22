package rogue;
import java.util.ArrayList;

import java.awt.Point;
import java.util.HashMap;


/**
 * A room within the dungeon - contains monsters, treasure,
 * doors out, etc.
 */
public class Room  {

    private int id;
    private int height;
    private int width;
    private ArrayList<Item> lootList = new ArrayList<Item>();
    private HashMap<String, Door> door = new HashMap<>();
    private HashMap<String, Character> symbols = new HashMap<>();
    private boolean inRoom = false;
    private Player player;

    // Default constructor
    /**
    * constructor for the door initializes a null door object.
    */
    public Room() {

    }


    // Required getter and setters below
    /**
    * setter for the symbol map.
    * @param symbol hashmap for the symbols in the room
    */
    public void setSymbol(HashMap<String, Character> symbol) {
        this.symbols = symbol;
    }

    public void addNewItem(Item newItem){
      lootList.add(newItem);
    }

    /**
    * setter for if the player is in the room.
    * @param newInRoom boolean for if player is in room or not
    */
    public void setInRoom(boolean newInRoom) {
        this.inRoom = newInRoom;
    }

    /**
    * getter for if the player is in the room.
    * @return boolean of if player is in the room
    */
    public boolean getInRoom() {
        return (this.inRoom);
    }

    /**
    * getter for the width of the room.
    * @return width of the room
    */
    public int getWidth() {
        return (this.width);
    }

    /**
    * setter for the width of the map.
    * @param newWidth the new width for the room
    */
    public void setWidth(int newWidth) {
        this.width = newWidth;
    }

    /**
    * getter for the height of the room.
    * @return height of the room
    */
    public int getHeight() {
        return (this.height);
    }

    /**
    * setter for the height of the room.
    * @param newHeight new height for the room
    */
    public void setHeight(int newHeight) {
        this.height = newHeight;
    }

    /**
    * getter for the id of the room.
    * @return the id of the room
    */
    public int getId() {
        return (this.id);
    }

    /**
    * setter for the id of the room.
    * @param newId new id of the room
    */
    public void setId(int newId) {
        this.id = newId;
    }

    /**
    * getter for the list of items in the room.
    * @return ArrayList of the items in the room
    */
    public ArrayList<Item> getRoomItems() {
        return (this.lootList);
    }

    /**
    * setter for the list of items in the room.
    * @param newRoomItems array list of items to be placed in the room
    */
    public void setRoomItems(ArrayList<Item> newRoomItems) {
        this.lootList = newRoomItems;
    }

    /**
    * getter for the player..
    * @return the player
    */
    public Player getPlayer() {
        return this.player;
    }

    /**
    * setter for the player.
    * @param newPlayer the player object
    */
    public void setPlayer(Player newPlayer) {
        this.player = newPlayer;
    }

    /**
    * getter for a door specified by the directon if it does not exist return -1.
    * @param direction direction of the door we are looking for
    * @return an the id of the door
    */
    public int getDoor(String direction) {
        int i;
        if (this.door.get(direction) == null) {
            i = -1;
        } else {
            Door temp = this.door.get(direction);
            i = temp.getWallLoc();
        }
        return (i);
    }

    public void finishDoor(String dir, Room conRoom){

    }

    /**
    * @param direction is one of NSEW.
    * @param location is a number between 0 and the length of the wall
    */
    public void setDoor(String direction, Door newDoor) {
        this.door.put(direction, newDoor);
    }

    /**
    * verify if the player is in the room.
    * @return boolean if the player is in the room or not
    */
    public boolean isPlayerInRoom() {
        return (this.inRoom);
    }

    /**
    * creates the roof or floor dependent on the direction given.
    * @param dir either N for north or S for south
    * @return a string of either the roof or the floor
    */
    public String createNS(String dir) {
        int doorPos = getDoor(dir);
        String north = "";
        String roof = Character.toString(this.symbols.get("NS_WALL"));

        for (int i = 0; i < getWidth(); i++) {
            if (i == doorPos) {
                north = north + this.symbols.get("DOOR");
            } else {
                north = north + roof;
            }
        }
        north = north + "\n";
        return (north);
    }

    /**
    * verifies if a specific item is in the position of the room.
    * @param i the y axis of the room
    * @param j the x axis of the room
    * @return boolean if the item exists in the room
    */
    public String itemIsThere(int i, int j) {
        ArrayList<Item> temp = getRoomItems();
        for (int t = 0; t < temp.size(); t++) {

            Item tempI = temp.get(t);
            Point tempP = tempI.getXyLocation();
            int x = (int) tempP.getX();
            int y = (int) tempP.getY();
            if (j == x && i == y) {
                return(tempI.getType().toUpperCase());
            }

        }
        return ("FLOOR");
    }

    /**
    * creates the content of the room everything but the roof and the floor.
    * @return a string containing the content of the room
    */
    public String createContent() {

        String content = "";
        String wall = Character.toString(this.symbols.get("EW_WALL"));
        String floor = Character.toString(this.symbols.get("FLOOR"));
        int doorPosW = getDoor("W");
        int doorPosE = getDoor("E");
        Point playerPos = new Point(-1, -1);

        if (isPlayerInRoom()) {
            playerPos = this.player.getXyLocation();
        }

        for (int i = 1; i < getHeight() - 1; i++) {

            if (i == doorPosW) {
                content = content + this.symbols.get("DOOR");
            } else {
                content = content + wall;
            }
            for (int j = 1; j < getWidth() - 1; j++) {
                if (j == playerPos.getX() && i == playerPos.getY()) {
                    content = content + this.symbols.get("PLAYER");
                } else {
                  // System.out.println(itemIsThere(i,j));
                  content = content + this.symbols.get(itemIsThere(i , j));
                }
            }
            if (i == doorPosE) {
                content = content + this.symbols.get("DOOR");
            } else {
                content = content + wall;
            }

            content = content + "\n";
        }
        return (content);
    }

    /**
    * Produces a string that can be printed to produce an ascii rendering of the room and all of its contents.
    * @return map, String representation of how the room looks
    */
    public String displayRoom() {
        String map = "";
        map = map + createNS("N");
        map = map + createContent();
        map = map + createNS("S");
        map = map + "\n";

        return (map);
    }

    public void addItem(Item toAdd) throws ImpossiblePositionException, NoSuchItemException{
      if(toAdd.getName() == null){
        throw new NoSuchItemException();
      }
      if(toAdd.getXyLocation().getX() <= 0 || toAdd.getXyLocation().getY() <= 0 || toAdd.getXyLocation().getY() >= getHeight() - 1 || toAdd.getXyLocation().getX() >= getWidth() - 1 ){
        throw new ImpossiblePositionException();
      } else if(isPlayerInRoom() && (toAdd.getXyLocation().getX() == player.getXyLocation().getX() && toAdd.getXyLocation().getY() == player.getXyLocation().getY())){
        throw new ImpossiblePositionException();
      } else{
        for(int i = 0; i < lootList.size(); i++){
          if(toAdd.getXyLocation().getX() == lootList.get(i).getXyLocation().getX() && toAdd.getXyLocation().getY() == lootList.get(i).getXyLocation().getY()){
            throw new ImpossiblePositionException();
          }
        }
      }
    }

    public boolean verifyRoom() throws NotEnoughDoorsException{
      if(door.isEmpty()){
        throw new NotEnoughDoorsException();
      }
      return(true);
    }

}
