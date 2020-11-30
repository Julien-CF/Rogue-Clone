package rogue;

import java.util.ArrayList;
import java.awt.Point;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;


public class Rogue implements Serializable {

    public static final char UP = 'y';
    public static final char DOWN = 'h';
    public static final char LEFT = 'g';
    public static final char RIGHT = 'j';

    private ArrayList<Room> roomList = new ArrayList<Room>();
    private ArrayList<Item> itemList = new ArrayList<Item>();
    private ArrayList<Door> doorList = new ArrayList<Door>();
    private RogueParser rogueParser;
    private Player player = new Player("Default");
    private HashMap<String, Character> symbols = new HashMap<>();

    /**
    * initializes rogueParser.
    */
    public Rogue() {

    }

    /**
    * initializes rogueParser.
    * @param theDungeonInfo contains the parse information
    */
    public Rogue(RogueParser theDungeonInfo) {
        Map<String, String> roomMap;
        Map<String, String> itemMap;
        setSymbols(theDungeonInfo);
        while ((roomMap = theDungeonInfo.nextRoom()) != null) {
          addRoom(roomMap);
        }
        addDoors(theDungeonInfo);
        placeDoors();
        for (int i = 0; i < roomList.size(); i++) {
          checkRoom(roomList.get(i), i);
        }
        while ((itemMap = theDungeonInfo.nextItem()) != null) {
          addItem(itemMap);
        }
        updPlayerRoom();
    }

    /**
    * Check if room is valid if not fix it.
    * @param r room to be verified
    * @param index
    */
    public void checkRoom(Room r, int index) {
      try {
        for (int i = 0; i < roomList.size(); i++) {
          r.verifyRoom();
        }
      } catch (NotEnoughDoorsException e) {
        fixRoom(r, index);
      }
    }

    /**
    * fix the room given by creating a new door and linking it.
    * @param r room to be fixed
    * @param index
    * @return (int) -1 if there is no way of fixing room
    */
    public int fixRoom(Room r, int index) {
      for (int i = 0; i < roomList.size(); i++) {
        String wall = openWall(roomList.get(i));
        if (wall != null) {
          String oppWall = oppositeWall(wall);
          newConnection(r, roomList.get(i), oppWall, wall);
          return (1);
        }
      }
      System.out.println("dungeon file cannot be used");
      System.exit(-1);
      return (-1);
    }

    /**
    * connect the new door to a room with available space.
    * @param cur
    * @param con
    * @param currentRoomWall
    * @param connectedRoomWall
    */
    public void newConnection(Room cur, Room con, String currentRoomWall, String connectedRoomWall) {
      Door currentRoomDoor = new Door();
      Door connectedRoomDoor = new Door();
      if (connectedRoomWall.equals("E") || connectedRoomWall.equals("W")) {
        newConnectionWallPlace(true, currentRoomDoor, connectedRoomDoor, cur, con);
      } else {
        newConnectionWallPlace(true, currentRoomDoor, connectedRoomDoor, cur, con);
      }
      currentRoomDoor.setWall(currentRoomWall);
      connectedRoomDoor.setWall(connectedRoomWall);
      currentRoomDoor.connectRoom(cur);
      currentRoomDoor.connectRoom(con);
      connectedRoomDoor.connectRoom(con);
      connectedRoomDoor.connectRoom(cur);
      currentRoomDoor.setExitDoor(connectedRoomDoor);
      connectedRoomDoor.setExitDoor(currentRoomDoor);
      addNewDoor(currentRoomDoor, connectedRoomDoor);
      cur.setDoor(currentRoomWall, currentRoomDoor);
      con.setDoor(connectedRoomWall, connectedRoomDoor);
    }

    /**
    * add the new connection doors.
    * @param a
    * @param b
    */
    public void addNewDoor(Door a, Door b) {
      doorList.add(a);
      doorList.add(b);
    }

    /**
    * give the door its position on the wall.
    * @param cur
    * @param con
    * @param curD
    * @param conD
    * @param check
    */
    public void newConnectionWallPlace(boolean check, Door curD, Door conD, Room cur, Room con) {
      if (check) {
        curD.setWallLoc(cur.getHeight() / 2);
        conD.setWallLoc(con.getHeight() / 2);
      } else {
        curD.setWallLoc(cur.getHeight() / 2);
        conD.setWallLoc(con.getHeight() / 2);
      }
    }

    /**
    * look for an open wall in the room.
    * @param r room to check
    * @return (string) the wall that is open
    */
    public String openWall(Room r) {
      int check = r.getDoor("N");
      if (check == -1) {
        return ("N");
      }
      check = r.getDoor("E");
      if (check == -1) {
        return ("E");
      }
      check = r.getDoor("S");
      if (check == -1) {
        return ("S");
      }
      check = r.getDoor("W");
      if (check == -1) {
        return ("W");
      }
      return (null);
    }

    /**
    * create all doors that will populate the world.
    * @param theDungeonInfo contains the information for the doors
    */
    public void addDoors(RogueParser theDungeonInfo) {
      ArrayList<Map<String, String>> door = theDungeonInfo.getDoors();
      for (int i = 0; i < door.size(); i++) {
        Map<String, String> currentDoor = door.get(i);
        Door newDoor = buildDoor(currentDoor);
        doorList.add(newDoor);
      }
      linkDoors();
    }

    /**
    * add the doors to their corresponding rooms.
    */
    public void placeDoors() {
      for (int i = 0; i < roomList.size(); i++) {
        for (int j = 0; j < doorList.size(); j++) {
          if (roomList.get(i).getId() == doorList.get(j).getCurRoom()) {
            roomList.get(i).setDoor(doorList.get(j).getWall(), doorList.get(j));
          }
        }
      }
    }

    /**
    * link the doors to their exit door.
    */
    public void linkDoors() {
      for (int i = 0; i < doorList.size(); i++) {
        for (int j = 0; j < doorList.size(); j++) {
          String oppWall = oppositeWall(doorList.get(i).getWall());
          String curWall = doorList.get(j).getWall();
          if ((doorList.get(i).getConRoom() == doorList.get(j).getCurRoom()) && (oppWall.equals(curWall))) {
            doorList.get(i).setExitDoor(doorList.get(j));
          }
        }
      }
    }

    /**
    * find the opposite wall.
    * @param wall the wall to find its opposite
    * @return (String) the opposite wall
    */
    public String oppositeWall(String wall) {
      if (wall.equals("N")) {
        return ("S");
      } else if (wall.equals("S")) {
        return ("N");
      } else if (wall.equals("E")) {
        return ("W");
      } else if (wall.equals("W")) {
        return ("E");
      }
      return (null);
    }

    /**
    * build a new door.
    * @param door HashMap containing the informationused to construct the door object
    * @return newDoor the newly constructed door
    */
    public Door buildDoor(Map<String, String> door) {
      Door newDoor = new Door();
      newDoor.setCurRoom(Integer.parseInt(door.get("curRoom")));
      newDoor.setConRoom(Integer.parseInt(door.get("con_room")));
      newDoor.setWall(door.get("dir"));
      newDoor.setWallLoc(Integer.parseInt(door.get("wall_pos")));

      for (int i = 0; i < roomList.size(); i++) {
        if (roomList.get(i).getId() == newDoor.getCurRoom()) {
          newDoor.connectRoom(roomList.get(i));
        } else if (roomList.get(i).getId() == newDoor.getConRoom()) {
          newDoor.connectRoom(roomList.get(i));
        }
      }
      return (newDoor);
    }

    /**
    * adds an item to the list of items that populate the game.
    * @param toAdd HashMap containing the information to create an item
    */
    public void addItem(Map<String, String> toAdd) {
      int roomIndex = Integer.parseInt(toAdd.get("room")) - 1;
      Item newItem = checkType(toAdd);
      newItem = buildItem(toAdd, roomList.get(roomIndex), newItem);
      newItem = verifyItem(newItem, roomList.get(roomIndex));
      if (newItem != null) {
        itemList.add(newItem);
        roomList.get(roomIndex).addNewItem(newItem);
      }
    }

    public Item checkType(Map<String, String> toAdd){
      if("Food".equals(toAdd.get("type"))){
        return (new Food());
      } else if ("SmallFood".equals(toAdd.get("type"))) {
        return (new SmallFood());
      } else if ("Clothing".equals(toAdd.get("type"))){
        return (new Clothing());
      } else if ("Magic".equals(toAdd.get("type"))){
        return (new Magic());
      } else if ("Potion".equals(toAdd.get("type"))){
        return (new Potion());
      } else if ("Ring".equals(toAdd.get("type"))){
        return (new Ring());
      } else {
        return(new Item());
      }
    }

    /**
    * parse the symbols to be used in the program from a specified file.
    * @param parser parsed information
    */
    public void setSymbols(RogueParser parser) {
      this.symbols.put("PASSAGE", parser.getSymbol("PASSAGE"));
      this.symbols.put("DOOR", parser.getSymbol("DOOR"));
      this.symbols.put("FLOOR", parser.getSymbol("FLOOR"));
      this.symbols.put("PLAYER", parser.getSymbol("PLAYER"));
      this.symbols.put("GOLD", parser.getSymbol("GOLD"));
      this.symbols.put("NS_WALL", parser.getSymbol("NS_WALL"));
      this.symbols.put("EW_WALL", parser.getSymbol("EW_WALL"));
      this.symbols.put("POTION", parser.getSymbol("POTION"));
      this.symbols.put("SCROLL", parser.getSymbol("SCROLL"));
      this.symbols.put("ARMOR", parser.getSymbol("ARMOR"));
      this.symbols.put("FOOD", parser.getSymbol("FOOD"));
      this.symbols.put("RING", parser.getSymbol("RING"));
      this.symbols.put("SMALLFOOD", parser.getSymbol("SMALLFOOD"));
      this.symbols.put("CLOTHING", parser.getSymbol("CLOTHING"));
    }

    /**
    * getter for the list of rooms stored in the array list.
    * @return returns the list of rooms
    */
    public ArrayList<Room> getRooms() {
        return this.roomList;
    }

    /**
    * getter for the list of items.
    * @return the list of items ot be placed in the rooms
    */
    public ArrayList<Item> getItems() {
        return (this.itemList);

    }

    /**
    * getter for the player.
    * @return the player
    */
    public Player getPlayer() {
        return (this.player);
    }

    /**
    * setter for the player in the rogue.
    * @param thePlayer
    */
    public void setPlayer(Player thePlayer) {
      this.player = thePlayer;
    }

    /**
    * verfies that an item is valid.
    * @param item item to be verified
    * @param room room where the item is placed
    * @return item the reconstructed item
    */
    public Item verifyItem(Item item, Room room) {
      try {
        room.addItem(item);
      } catch (NoSuchItemException a) {
        item = null;
        return (item);
      } catch (ImpossiblePositionException e) {
        item = rebuildItem(item, room);
      }

      return (item);
    }

    /**
    * rebuilds an item so that it is valid.
    * @param item the item to be rebuilt
    * @param room room where the item is placed
    * @return item reconstructed item
    */
    public Item rebuildItem(Item item, Room room) {
      for (int i = 1; i < room.getHeight() - 1; i++) {
        for (int j = 1; j < room.getWidth() - 1; j++) {
          Point newPoint = new Point(j, i);
          item.setXyLocation(newPoint);
          try {
            room.addItem(item);
            i = room.getHeight();
            break;

          } catch (ImpossiblePositionException e) {
          } catch (NoSuchItemException a) {
          }
        }
      }
      return (item);
    }

    /**
    * Function used to build an item with all the elements it needs.
    * @param itemMap a map that contains all the information to be stored
    * @param currentRoom the room the item will be put in
    * @return newItem retuns the newly created item
    */
    public Item buildItem(Map<String, String> itemMap, Room currentRoom, Item newItem) {
      Point xylocation = new Point(Integer.parseInt(itemMap.get("x")), Integer.parseInt(itemMap.get("y")));
      newItem.setXyLocation(xylocation);
      newItem.setCurrentRoom(currentRoom);
      newItem.setId(Integer.parseInt(itemMap.get("id")));
      newItem.setName(itemMap.get("name"));
      newItem.setType(itemMap.get("type"));
      newItem.setDescription(itemMap.get("description"));

      return (newItem);
    }

    /**
    * construct a new room to be added to the list of rooms.
    * @param toAdd hashmap containing the information needed to construct the room
    */
    public void addRoom(Map<String, String> toAdd) {
      Room newRoom = new Room();
      int id = Integer.parseInt(toAdd.get("id"));
      newRoom.setId(id);
      int height = Integer.parseInt(toAdd.get("height"));
      newRoom.setHeight(height);
      int width = Integer.parseInt(toAdd.get("width"));
      newRoom.setWidth(width);
      boolean inRoom = checkTrue(toAdd.get("start"));
      newRoom.setInRoom(inRoom);
      newRoom.setSymbol(this.symbols);
      if (newRoom.isPlayerInRoom()) {
          Point playerLocation = new Point(1, 1);
          player.setXyLocation(playerLocation);
          player.setCurrentRoom(newRoom);
          newRoom.setPlayer(player);
      }
      this.roomList.add(newRoom);
    }

    /**
    * checks to see if a String is "true".
    * @param str the string to check
    * @return (boolean)
    */
    boolean checkTrue(String str) {
      if ("true".equals(str)) {
        return (true);
      }
      return (false);
    }

    /**
    * updates the displayed room dependent on the input.
    * @param input the input that the user puts
    * @return message description of the move that was made
    * @throws InvalidMoveException
    */
    public String makeMove(char input) throws InvalidMoveException {
      String message = "Move made";
      if (input != 'y' && input != 'g' && input != 'h' && input != 'j') {
        if(input == 'w' || input == 'e' || input == 't'){
          message = this.player.getMessage();
          return(message);
        } else {
          throw new InvalidMoveException();
        }
      }
      message = updateRoom(input, player.getCurrentRoom(), message);
      if (message.equals("Can't move there, wall in way")){
        throw new InvalidMoveException();
      }
      updPlayerRoom();
      return (message);
    }

    /**
    * updates the displayed room by verifying the players new position.
    * @param input players requested input
    * @param r room to be updated
    * @param message String to be updated dependent on the movement made
    * @return message descibes the move made
    */
    public String updateRoom(char input, Room r, String message) {
      int x = (int) r.getPlayer().getXyLocation().getX();
      int y = (int) r.getPlayer().getXyLocation().getY();
      if (!(updatePlayerPosition(x, y, input, r))) {
        message = "Can't move there, wall in way";
        message = checkDoor(r, input, message);
        return (message);
      }
      message = grabItem(r, message);
      return (message);
    }

    /**
    * updates the players position in the game.
    * @param input players requested input
    * @param r room to be updated
    * @param x
    * @param y
    * @return (boolean) returns true if player position gets updated if not return false
    */
    public boolean updatePlayerPosition(int x, int y, char input, Room r) {
      if (input == 'y' && r.getPlayer().getXyLocation().getY() - 1 > 0) {
        Point newPoint = new Point(x, y - 1);
        r.getPlayer().setXyLocation(newPoint);
      } else if (input == 'h' && r.getPlayer().getXyLocation().getY() + 1 < r.getHeight() - 1) {
        Point newPoint = new Point(x, y + 1);
        r.getPlayer().setXyLocation(newPoint);
      } else if (input == 'g' && r.getPlayer().getXyLocation().getX() - 1 > 0) {
        Point newPoint = new Point(x - 1, y);
        r.getPlayer().setXyLocation(newPoint);
      } else if (input == 'j' && r.getPlayer().getXyLocation().getX() + 1 < r.getWidth() - 1) {
        Point newPoint = new Point(x + 1, y);
        r.getPlayer().setXyLocation(newPoint);
      } else {
        return false;
      }
      return true;
    }

    /**
    * checks if next move leads to door.
    * @param r room to checks
    * @param input move to be made by player
    * @param message message describing next move made
    * @return message describes the next move
    */
    public String checkDoor(Room r, char input, String message) {
      if (input == 'y' && r.getPlayer().getXyLocation().getX() == r.getDoor("N")) {
        goThroughDoor(r.getFullDoor("N"), r);
        return ("Gone through Door");
      } else if (input == 'h' && r.getPlayer().getXyLocation().getX() == r.getDoor("S")) {
        goThroughDoor(r.getFullDoor("S"), r);
        return ("Gone through door");
      } else if (input == 'g' && r.getPlayer().getXyLocation().getY() == r.getDoor("W")) {
        goThroughDoor(r.getFullDoor("W"), r);
        return ("Gone through door");
      } else if (input == 'j' && r.getPlayer().getXyLocation().getY() == r.getDoor("E")) {
        goThroughDoor(r.getFullDoor("E"), r);
        return ("Gone through door");
      }
      return (message);
    }

    /**
    * moves a player through the door to the next room.
    * @param entrance door that you go through
    * @param r room currently in
    */
    public void goThroughDoor(Door entrance, Room r) {
      Room exit = entrance.getOtherRoom(r);
      Door exitDoor = entrance.getExitDoor();
      exit.setInRoom(true);
      exit.setPlayer(r.getPlayer());
      exit.getPlayer().setCurrentRoom(exit);
      placePlayer(exitDoor, exit);
      r.setInRoom(false);
      r.setPlayer(null);
      for(int i = 0; i < roomList.size(); i++){
        if(r.getId() == roomList.get(i).getId()){
          roomList.get(i).setInRoom(false);
        }
      }
    }

    /**
    * put the player in its new position in the next room after walking through door.
    * @param d door being exitted out of
    * @param r room being entered
    */
    public void placePlayer(Door d, Room r) {
      if (d.getWall().equals("N")) {
        Point newPoint = new Point(d.getWallLoc(), 1);
        r.getPlayer().setXyLocation(newPoint);
      } else if (d.getWall().equals("S")) {
        Point newPoint = new Point(d.getWallLoc(), r.getHeight() - 2);
        r.getPlayer().setXyLocation(newPoint);
      } else if (d.getWall().equals("W")) {
        Point newPoint = new Point(1, d.getWallLoc());
        r.getPlayer().setXyLocation(newPoint);
      } else if (d.getWall().equals("E")) {
        Point newPoint = new Point(r.getWidth() - 2, d.getWallLoc());
        r.getPlayer().setXyLocation(newPoint);
      }
    }

    /**
    * if a player is on an item grab it.
    * @param r room currently in
    * @param message string describing the next move
    * @return message describes the next move
    */
    public String grabItem(Room r, String message) {
      for (int i = 0; i < r.getRoomItems().size(); i++) {
        if (r.getPlayer().getXyLocation().equals(r.getRoomItems().get(i).getXyLocation())) {
          this.player.addItem(r.getRoomItems().get(i));
          String item = r.getRoomItems().get(i).getName();
          r.getRoomItems().remove(i);
           return (item + " grabbed");
        }
      }
      return (message);
    }

    /**
    * gets the next display image.
    * @return (String) contains the next instance of the game to be displayed
    */
    public String getNextDisplay() {
      updPlayerRoom();
      Room displayRoom = new Room();
      displayRoom = player.getCurrentRoom();
      return (displayRoom.displayRoom());
    }

    /**
    * updates the active player to ensure it contains the correct information.
    */
    public void updPlayerRoom() {
      for (int i = 0; i < roomList.size(); i++) {
        if (roomList.get(i).isPlayerInRoom()) {
          player.setCurrentRoom(roomList.get(i));
          player.setXyLocation(roomList.get(i).getPlayer().getXyLocation());
        }
      }
    }

    public void useItem(char input, int indexOfItem){
      if(input == 'e'){
        eat(indexOfItem);
      } else if (input == 'w') {
        wear(indexOfItem);
      } else if (input == 't'){
        toss(indexOfItem);
      }
    }

    private void eat(int index){
      String message;
        if(player.getInventory().get(index) instanceof Potion){
           message = ((Potion) player.getInventory().get(index)).eat();
          player.getInventory().remove(player.getInventory().get(index));
        } else if (player.getInventory().get(index) instanceof Food){
           message = ((Food) player.getInventory().get(index)).eat();
          player.getInventory().remove(player.getInventory().get(index));
        } else if (player.getInventory().get(index) instanceof SmallFood){
          message = ((SmallFood) player.getInventory().get(index)).eat();
         player.getInventory().remove(player.getInventory().get(index));
        }
        else{
          message = "Although you probably can you shouldn't eat a " + player.getInventory().get(index).getName();
        }
        player.setMessage(message);
    }

    private void wear(int index){
      String message;
        if(player.getInventory().get(index) instanceof Clothing){
           message = ((Clothing) player.getInventory().get(index)).wear();
          player.equipItem(index);
        } else if (player.getInventory().get(index) instanceof Ring){
           message = ((Ring) player.getInventory().get(index)).wear();
          player.equipItem(index);
        } else{
          message = player.getInventory().get(index).getName() + " is not Clothing";
        }
        player.setMessage(message);
    }

    public void toss(int index){
      String message;
        if(player.getInventory().get(index) instanceof SmallFood){
           message = ((SmallFood) player.getInventory().get(index)).toss();
           throwItem(index);
        } else if (player.getInventory().get(index) instanceof Potion){
           message = ((Potion) player.getInventory().get(index)).toss();
           throwItem(index);
        } else{
          message = "Stop trying to throw the " + player.getInventory().get(index).getName();
        }
        player.setMessage(message);
    }

    public void throwItem(int index){
      player.getInventory().get(index).setXyLocation(player.getXyLocation());
      player.getCurrentRoom().addNewItem(player.getInventory().get(index));
      player.getInventory().remove(index);
    }

}
