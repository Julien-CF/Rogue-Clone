package rogue;

import java.util.ArrayList;
import java.awt.Point;
import java.util.Map;
import java.util.HashMap;



public class Rogue {

    private ArrayList<Room> roomList = new ArrayList<Room>();
    private ArrayList<Item> itemList = new ArrayList<Item>();
    private HashMap<String, String> loot = new HashMap<>();

    private RogueParser rogueParser;
    private Player player = new Player("Default");
    /**
    * initializes rogueParser.
    * @param filename the name of the file tp parse from
    */
    public void parse(String filename) {
      RogueParser temp = new RogueParser(filename);
      rogueParser = temp;
    }


    /**
    * parse the symbols to be used in the program from a specified file.
    * @return Hashmap containing the symbols
    */
    public HashMap<String, Character> setSymbols() {
      HashMap<String, Character> symbols = new HashMap<>();

      symbols.put("PASSAGE", rogueParser.getSymbol("PASSAGE"));
      symbols.put("DOOR", rogueParser.getSymbol("DOOR"));
      symbols.put("FLOOR", rogueParser.getSymbol("FLOOR"));
      symbols.put("PLAYER", rogueParser.getSymbol("PLAYER"));
      symbols.put("ITEM", rogueParser.getSymbol("ITEM"));
      symbols.put("NS_WALL", rogueParser.getSymbol("NS_WALL"));
      symbols.put("EW_WALL", rogueParser.getSymbol("EW_WALL"));

      return (symbols);
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

    /**
    * create all of the items that are to be stored in the rooms.
    * @param newRoom the room you want to create the list of loot for
    * @return a list of all the loot in the room
    */
    public void createLootList(Room newRoom) {

        ArrayList<Item> lootList = new ArrayList<Item>();
        Map<String, String> itemMap;
        for (int i = 0; i < rogueParser.getNumOfItems(); i++) {
          itemMap = rogueParser.nextItem();
          if (newRoom.getId() == Integer.parseInt(itemMap.get("room"))) {
            Item newItem = buildItem(itemMap, newRoom);
            newItem = verifyItem(newItem, newRoom);
            lootList.add(newItem);
            newRoom.setRoomItems(lootList);
          }
        }
        itemMap = rogueParser.nextItem();

    }

    public Item verifyItem(Item item, Room room){
      try{
        room.addItem(item);
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
            System.out.println("Item replaced at : " + j + " " + i);
            i = room.getHeight();
            break;

          } catch (ImpossiblePositionException e){
            System.out.println("Item cant be replaced at : " + j + " " + i);

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

    /**
    * create the rooms parsed from a specific file.
    */
    public void createRooms() {
            Map<String, String> roomMap;
            while ((roomMap = rogueParser.nextRoom()) != null) {
                Room newRoom = new Room();
                int id = Integer.parseInt(roomMap.get("id"));
                newRoom.setId(id);
                int height = Integer.parseInt(roomMap.get("height"));
                newRoom.setHeight(height);
                int width = Integer.parseInt(roomMap.get("width"));
                newRoom.setWidth(width);
                boolean inRoom = checkTrue(roomMap.get("start"));
                newRoom.setInRoom(inRoom);
                addDoors(roomMap, newRoom);

                newRoom.setSymbol(setSymbols());

                if (newRoom.isPlayerInRoom()) {
                    Point playerLocation = new Point(1, 1);
                    player.setXyLocation(playerLocation);
                    player.setCurrentRoom(newRoom);
                    newRoom.setPlayer(player);
                }

                createLootList(newRoom);
                this.roomList.add(newRoom);
            }
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
