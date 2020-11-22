package rogue;

import java.util.ArrayList;
import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

import java.lang.NullPointerException;



public class Rogue {

    public static final char UP = 'w';
    public static final char DOWN = 's';
    public static final char LEFT = 'a';
    public static final char RIGHT = 'd';

    private ArrayList<Room> roomList = new ArrayList<Room>();
    private ArrayList<Item> itemList = new ArrayList<Item>();
    private ArrayList<Door> doorList = new ArrayList<Door>();
    private RogueParser rogueParser;
    private Player player = new Player("Default");
    private HashMap<String, Character> symbols = new HashMap<>();
    /**
    * initializes rogueParser.
    * @param filename the name of the file tp parse from
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
        for(int i = 0; i < roomList.size(); i++){
          checkRoom(roomList.get(i), i);
        }
        while((itemMap = theDungeonInfo.nextItem()) != null){
          addItem(itemMap);
        }
        updPlayerRoom();
    }

    public void checkRoom(Room r, int index){
      try{
        for(int i = 0; i < roomList.size(); i++){
          r.verifyRoom();
        }
      } catch (NotEnoughDoorsException e) {
        fixRoom(r, index);
      }
    }

    public int fixRoom(Room r, int index){
      for(int i = 0; i < roomList.size(); i++){
        String wall = openWall(roomList.get(i));
        if(wall != null){
          String oppWall = oppositeWall(wall);
          newConnection(r, roomList.get(i), oppWall, wall, index, i);
          return(1);
        }
      }
      System.out.println("dungeon file cannot be used");
      System.exit(-1);
      return(-1);
    }

    public void newConnection(Room currentRoom, Room conRoom, String currentRoomWall, String connectedRoomWall, int currentRoomIndex, int connectedRoomIndex){
      Door currentRoomDoor = new Door();
      Door connectedRoomDoor = new Door();
      if(connectedRoomWall.equals("E") || connectedRoomWall.equals("W")){
        currentRoomDoor.setWallLoc(currentRoom.getHeight()/2);
        connectedRoomDoor.setWallLoc(conRoom.getHeight()/2);
      }
      else{
        currentRoomDoor.setWallLoc(currentRoom.getWidth()/2);
        connectedRoomDoor.setWallLoc(conRoom.getWidth()/2);
      }
      currentRoomDoor.setWall(currentRoomWall);
      connectedRoomDoor.setWall(connectedRoomWall);
      currentRoomDoor.setCurRoom(currentRoomIndex);
      currentRoomDoor.setConRoom(connectedRoomIndex);
      connectedRoomDoor.setCurRoom(connectedRoomIndex);
      connectedRoomDoor.setConRoom(currentRoomIndex);
      currentRoomDoor.ConnectRoom(currentRoom);
      currentRoomDoor.ConnectRoom(conRoom);
      connectedRoomDoor.ConnectRoom(conRoom);
      connectedRoomDoor.ConnectRoom(currentRoom);
      currentRoomDoor.setExitDoor(connectedRoomDoor);
      connectedRoomDoor.setExitDoor(currentRoomDoor);

      doorList.add(currentRoomDoor);
      doorList.add(connectedRoomDoor);

      currentRoom.setDoor(currentRoomWall, currentRoomDoor);
      conRoom.setDoor(connectedRoomWall, connectedRoomDoor);
    }

    public String openWall(Room r){
      int check = r.getDoor("N");
      if(check == -1){
        return("N");
      }
      check = r.getDoor("E");
      if(check == -1){
        return("E");
      }
      check = r.getDoor("S");
      if(check == -1){
        return("S");
      }
      check = r.getDoor("W");
      if(check == -1){
        return("W");
      }
      return (null);
    }

    public void addDoors(RogueParser theDungeonInfo){
      ArrayList<Map<String, String>> door = theDungeonInfo.getDoors();
      for(int i = 0; i < door.size(); i++){
        Map<String, String> currentDoor = door.get(i);
        Door newDoor = buildDoor(currentDoor);
        doorList.add(newDoor);
      }
      linkDoors();
    }

    public void placeDoors(){
      for(int i = 0; i < roomList.size(); i++){
        for(int j = 0; j < doorList.size(); j++){
          if(roomList.get(i).getId() == doorList.get(j).getCurRoom()){
            roomList.get(i).setDoor(doorList.get(j).getWall(), doorList.get(j));
          }
        }
      }
    }

    public void linkDoors(){
      for(int i = 0; i < doorList.size(); i++){
        for(int j = 0; j < doorList.size(); j++){
          String oppWall = oppositeWall(doorList.get(i).getWall());
          if((doorList.get(i).getConRoom() == doorList.get(j).getCurRoom()) && (oppWall.equals(doorList.get(j).getWall()))){
            doorList.get(i).setExitDoor(doorList.get(j));
          }
        }
      }
    }

    public String oppositeWall(String wall){
      if(wall.equals("N")){
        return("S");
      } else if (wall.equals("S")){
        return("N");
      } else if(wall.equals("E")){
        return("W");
      } else if(wall.equals("W")){
        return("E");
      }
      return(null);
    }

    public Door buildDoor(Map<String, String> door){
      Door newDoor = new Door();
      newDoor.setCurRoom(Integer.parseInt(door.get("curRoom")));
      newDoor.setConRoom(Integer.parseInt(door.get("con_room")));
      newDoor.setWall(door.get("dir"));
      newDoor.setWallLoc(Integer.parseInt(door.get("wall_pos")));

      for(int i = 0; i < roomList.size(); i++){
        if(roomList.get(i).getId() == newDoor.getCurRoom()){
          newDoor.ConnectRoom(roomList.get(i));
        } else if (roomList.get(i).getId() == newDoor.getConRoom()) {
          newDoor.ConnectRoom(roomList.get(i));
        }
      }
      return(newDoor);
    }

    public void addItem(Map<String, String> toAdd){
      int roomIndex = Integer.parseInt(toAdd.get("room"))-1;
      Item newItem = buildItem(toAdd, roomList.get(roomIndex));
      newItem = verifyItem(newItem, roomList.get(roomIndex));
      if(newItem != null){
        itemList.add(newItem);
        roomList.get(roomIndex).addNewItem(newItem);
      }
    }
    /**
    * parse the symbols to be used in the program from a specified file.
    * @return Hashmap containing the symbols
    */
    public void setSymbols(RogueParser rogueParser) {
      HashMap<String, Character> symbols = new HashMap<>();

      this.symbols.put("PASSAGE", rogueParser.getSymbol("PASSAGE"));
      this.symbols.put("DOOR", rogueParser.getSymbol("DOOR"));
      this.symbols.put("FLOOR", rogueParser.getSymbol("FLOOR"));
      this.symbols.put("PLAYER", rogueParser.getSymbol("PLAYER"));
      this.symbols.put("GOLD", rogueParser.getSymbol("GOLD"));
      this.symbols.put("NS_WALL", rogueParser.getSymbol("NS_WALL"));
      this.symbols.put("EW_WALL", rogueParser.getSymbol("EW_WALL"));
      this.symbols.put("POTION", rogueParser.getSymbol("POTION"));
      this.symbols.put("SCROLL", rogueParser.getSymbol("SCROLL"));
      this.symbols.put("ARMOR", rogueParser.getSymbol("ARMOR"));
      this.symbols.put("FOOD", rogueParser.getSymbol("FOOD"));
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


    public Item verifyItem(Item item, Room room){
      try{
        room.addItem(item);
      } catch(NoSuchItemException a){
        item = null;
        return(item);
      } catch(ImpossiblePositionException e){
        item = rebuildItem(item, room);
      }

      return(item);
    }

    public Item rebuildItem(Item item, Room room){
      for(int i = 1; i < room.getHeight() - 1; i++){
        for(int j = 1; j < room.getWidth() - 1; j++){
          Point newPoint = new Point(j, i);
          item.setXyLocation(newPoint);
          try{
            room.addItem(item);
            i = room.getHeight();
            break;

          } catch (ImpossiblePositionException e){
          } catch (NoSuchItemException a){
          }
        }
      }
      return(item);
    }

    /**
    * Function used to build an item with all the elements it needs.
    * @param itemMap a map that contains all the information to be stored
    * @param currentRoom the room the item will be put in
    * @return newItem retuns the newly created item
    */
    public Item buildItem(Map<String, String> itemMap, Room currentRoom) {
      Item newItem = new Item();
      Point xylocation = new Point(Integer.parseInt(itemMap.get("x")), Integer.parseInt(itemMap.get("y")));
      newItem.setXyLocation(xylocation);
      newItem.setCurrentRoom(currentRoom);
      newItem.setId(Integer.parseInt(itemMap.get("id")));
      newItem.setName(itemMap.get("name"));
      newItem.setType(itemMap.get("type"));
      newItem.setDescription(itemMap.get("description"));

      return (newItem);
    }

    public void addRoom(Map<String, String> toAdd){
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

    boolean checkTrue(String str) {
      if (str.equals("true")) {
        return (true);
      }
      return (false);
    }

    public String makeMove(char input) throws InvalidMoveException{
      String message = "Move made";
      if(input != 'w' && input != 'a' && input != 's' && input != 'd'){
        throw new InvalidMoveException();
      }
      message = updateRoom(input, player.getCurrentRoom(), message);

      updPlayerRoom();
      return(message);
    }

    public String updateRoom(char input, Room r, String message){
      int x = (int) r.getPlayer().getXyLocation().getX();
      int y = (int) r.getPlayer().getXyLocation().getY();
      if(input == 'w' && r.getPlayer().getXyLocation().getY()-1 > 0){
        Point newPoint = new Point(x, y-1);
        r.getPlayer().setXyLocation(newPoint);
      } else if (input == 's' && r.getPlayer().getXyLocation().getY()+1 < r.getHeight() - 1){
        Point newPoint = new Point(x, y+1);
        r.getPlayer().setXyLocation(newPoint);
      } else if (input == 'a' && r.getPlayer().getXyLocation().getX()-1 > 0){
        Point newPoint = new Point(x-1, y);
        r.getPlayer().setXyLocation(newPoint);
      } else if (input == 'd' && r.getPlayer().getXyLocation().getX()+1 < r.getWidth() - 1){
        Point newPoint = new Point(x+1, y);
        r.getPlayer().setXyLocation(newPoint);
      } else{
        message = "Can't move there, wall in way";
        message = checkDoor(r, input, message);
        return(message);
      }
      message = grabItem(r, message);
      return(message);
    }

    public String checkDoor(Room r, char input,String message){
      if(input == 'w' && r.getPlayer().getXyLocation().getX() == r.getDoor("N")){
        goThroughDoor(r.getFullDoor("N"), r);
        return("Gone through Door");
      } else if(input == 's' && r.getPlayer().getXyLocation().getX() == r.getDoor("S")){
        goThroughDoor(r.getFullDoor("S"), r);
        return("Gone through door");
      } else if(input == 'a' && r.getPlayer().getXyLocation().getY() == r.getDoor("W")){
        goThroughDoor(r.getFullDoor("W"), r);
        return("Gone through door");
      } else if(input == 'd' && r.getPlayer().getXyLocation().getY() == r.getDoor("E")){
        goThroughDoor(r.getFullDoor("E"), r);
        return("Gone through door");
      }
      return(message);
    }

    public void goThroughDoor(Door entrance, Room r){
      Room exit = entrance.getOtherRoom(r);
      Door exitDoor = entrance.getExitDoor();

      exit.setInRoom(true);
      exit.setPlayer(r.getPlayer());
      exit.getPlayer().setCurrentRoom(exit);
      placePlayer(exitDoor, exit);

      r.setInRoom(false);
      r.setPlayer(null);

    }

    public void placePlayer(Door d, Room r){
      if(d.getWall().equals("N")){
        System.out.println("N");
        Point newPoint = new Point(d.getWallLoc(),1);
        r.getPlayer().setXyLocation(newPoint);
      } else if(d.getWall().equals("S")){
        System.out.println("S");
        Point newPoint = new Point(d.getWallLoc(),r.getHeight()-2);
        r.getPlayer().setXyLocation(newPoint);
      } else if(d.getWall().equals("W")){
        System.out.println("E");
        Point newPoint = new Point(1,d.getWallLoc());
        r.getPlayer().setXyLocation(newPoint);
      } else if(d.getWall().equals("E")){
        System.out.println("W");
        Point newPoint = new Point(r.getWidth()-2, d.getWallLoc());
        r.getPlayer().setXyLocation(newPoint);
      }
    }

    public String grabItem(Room r, String message){
      for(int i = 0; i < r.getRoomItems().size(); i++){
        if(r.getPlayer().getXyLocation().equals(r.getRoomItems().get(i).getXyLocation())){
          r.getRoomItems().remove(i);
           return("Item grabbed");
        }
      }
      return(message);
    }


    public String getNextDisplay(){
      updPlayerRoom();
      Room displayRoom = new Room();
      displayRoom = player.getCurrentRoom();
      System.out.println(displayRoom.displayRoom());
      return(displayRoom.displayRoom());
    }

    public void updPlayerRoom(){
      for(int i = 0; i < roomList.size(); i++){
        if(roomList.get(i).isPlayerInRoom()){
          player.setCurrentRoom(roomList.get(i));
          player.setXyLocation(roomList.get(i).getPlayer().getXyLocation());
        }
      }
    }

}
