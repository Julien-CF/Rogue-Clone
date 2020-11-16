package rogue;

import java.util.ArrayList;
import java.awt.Point;
import java.util.Map;
import java.util.HashMap;



public class Rogue {

    private ArrayList<Room> roomList = new ArrayList<Room>();
    private ArrayList<Item> itemList = new ArrayList<Item>();
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
        while((itemMap = theDungeonInfo.nextItem()) != null){
          addItem(itemMap);
        }
        // for(int i = 0; i < itemList.size(); i++){
        //   System.out.println(itemList.get(i).getName());
        // }
        displayAll();
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
      addDoors(toAdd, newRoom);
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
    * create the rooms parsed from a specific file.
    */
    public void createRooms() {

    }

    void addDoors(Map<String, String> doors, Room newRoom) {
      int id = Integer.parseInt(doors.get("N"));
        newRoom.setDoor("N", id);
      id = Integer.parseInt(doors.get("W"));
        newRoom.setDoor("W", id);
      id = Integer.parseInt(doors.get("E"));
        newRoom.setDoor("E", id);
      id = Integer.parseInt(doors.get("S"));
        newRoom.setDoor("S", id);
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
