package rogue;

import java.util.ArrayList;
import java.awt.Point;
import java.util.Map;
import java.util.HashMap;



public class Rogue {

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
        while((itemMap = theDungeonInfo.nextItem()) != null){
          addItem(itemMap);
        }
        displayAll();
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
            System.out.println("this fits here good sir");
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
      this.symbols.put("ITEM", rogueParser.getSymbol("ITEM"));
      this.symbols.put("NS_WALL", rogueParser.getSymbol("NS_WALL"));
      this.symbols.put("EW_WALL", rogueParser.getSymbol("EW_WALL"));
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
        return null;
    }

    /**
    * setter for the player in the rogue.
    * @param thePlayer
    */
    public void setPlayer(Player thePlayer) {

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

    //creates a string that displays all the rooms in the dungeon
    /**
    * function to display the rooms as a string of characters.
    * @return the string of all the rooms
    */
    public String displayAll() {
        String rooms = "";
        for (int i = 0; i < this.roomList.size(); i++) {
            rooms = rooms + this.roomList.get(i).displayRoom();
        }
        System.out.println(rooms);
        return (rooms);
    }
}
