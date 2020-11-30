package rogue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.Serializable;

public class RogueParser implements Serializable {

    private ArrayList<Map<String, String>> rooms = new ArrayList<>();
    private ArrayList<Map<String, String>> items = new ArrayList<>();
    private ArrayList<Map<String, String>> itemLocations = new ArrayList<>();
    private ArrayList<Map<String, String>> doorList = new ArrayList<>();
    private HashMap<String, Character> symbols = new HashMap<>();

    private Iterator<Map<String, String>> roomIterator;
    private Iterator<Map<String, String>> itemIterator;

    private int numOfRooms = 0;
    private int numOfItems = 0;

    /**
     * Default constructor.
     */
    public RogueParser() {


    }

    /**
     * Constructor that takes filename and sets up the object.
     * @param filename  (String) name of file that contains file location for rooms and symbols
     */
    public RogueParser(String filename) {
        parse(filename);
    }


    /**
     * Return the array of doors.
     * @return (Array) array of doors
     */
    public ArrayList<Map<String, String>> getDoors() {
      return (this.doorList);
    }

    /**
     * Return the next room.
     * @return (Map) Information about a room
     */
    public Map nextRoom() {

        if (roomIterator.hasNext()) {
            return roomIterator.next();
        } else {
            return null;
        }
    }

    /**
     * Returns the next item.
     * @return (Map) Information about an item
     */
    public Map nextItem() {

        if (itemIterator.hasNext()) {
            return itemIterator.next();
        } else {
          itemIterator = itemLocations.iterator();
          return null;
        }

    }

    /**
     * Get the character for a symbol.
     * @param symbolName (String) Symbol Name
     * @return (Character) Display character for the symbol
     */
    public Character getSymbol(String symbolName) {

        if (symbols.containsKey(symbolName)) {
            return symbols.get(symbolName);
        }
        // Does not contain the key
        return null;
    }

    /**
     * Get the number of items.
     * @return (int) Number of items
     */
    public int getNumOfItems() {

        return numOfItems;
    }

    /**
     * Get the number of rooms.
     * @return (int) Number of rooms
     */
    public int getNumOfRooms() {

        return numOfRooms;
    }

    /**
     * Read the file containing the file locations.
     * @param filename (String) Name of the file
     */
    private void parse(String filename) {
            String roomsFileLocation = getFileLocation(filename, "Rooms");
            String symbolsFileLocation = getFileLocation(filename, "Symbols");
            JSONObject roomsJSON = createObject(roomsFileLocation);
            JSONObject symbolsJSON = createObject(symbolsFileLocation);
            extractRoomInfo(roomsJSON);
            extractItemInfo(roomsJSON);
            extractSymbolInfo(symbolsJSON);
            roomIterator = rooms.iterator();
            itemIterator = itemLocations.iterator();
    }

    /**
    * creates a JSON object.
    * @param file
    * @return roomsJSON
    */
    private JSONObject createObject(String file) {
      Object roomsObj = null;
      JSONObject roomsJSON = null;
      try {
      JSONParser parser = new JSONParser();
      roomsObj = parser.parse(new FileReader(file));
      roomsJSON = (JSONObject) roomsObj;
      } catch (FileNotFoundException e) {
          System.out.println("Cannot find file named: " + file);
      } catch (IOException e) {
          e.printStackTrace();
      } catch (ParseException e) {
          System.out.println("Error parsing JSON file");
      }
      return (roomsJSON);
    }

    /**
    * grabs the file locations.
    * @param filename
    * @param type
    * @return fileLocation
    */
    private String getFileLocation(String filename, String type) {
      String fileLocation = null;
      JSONParser parser = new JSONParser();
      try {
        Object obj = parser.parse(new FileReader(filename));
        JSONObject configurationJSON = (JSONObject) obj;
        fileLocation = (String) configurationJSON.get(type);
      } catch (FileNotFoundException e) {
          System.out.println("Cannot find file named: " + filename);
      } catch (IOException e) {
          e.printStackTrace();
      } catch (ParseException e) {
          System.out.println("Error parsing JSON file");
      }
      return (fileLocation);
    }

    /**
     * Get the symbol information.
     * @param symbolsJSON  (JSONObject) Contains information about the symbols
     */
    private void extractSymbolInfo(JSONObject symbolsJSON) {


        JSONArray symbolsJSONArray = (JSONArray) symbolsJSON.get("symbols");

        // Make an array list of room information as maps
        for (int i = 0; i < symbolsJSONArray.size(); i++) {
            JSONObject symbolObj = (JSONObject) symbolsJSONArray.get(i);
            symbols.put(symbolObj.get("name").toString(), String.valueOf(symbolObj.get("symbol")).charAt(0));
        }
    }

    /**
     * Get the room information.
     * @param roomsJSON (JSONObject) Contains information about the rooms
     */
    private void extractRoomInfo(JSONObject roomsJSON) {


        JSONArray roomsJSONArray = (JSONArray) roomsJSON.get("room");

        // Make an array list of room information as maps
        for (int i = 0; i < roomsJSONArray.size(); i++) {
            rooms.add(singleRoom((JSONObject) roomsJSONArray.get(i)));
            numOfRooms += 1;
        }
    }

    /**
     * Get a room's information.
     * @param roomJSON (JSONObject) Contains information about one room
     * @return (Map<String, String>) Contains key/values that has information about the room
     */
    private Map<String, String> singleRoom(JSONObject roomJSON) {
        HashMap<String, String> room = new HashMap<>();
        room.put("id", roomJSON.get("id").toString());
        room.put("start", roomJSON.get("start").toString());
        room.put("height", roomJSON.get("height").toString());
        room.put("width", roomJSON.get("width").toString());
        JSONArray doorArray = (JSONArray) roomJSON.get("doors");
        for (int j = 0; j < doorArray.size(); j++) {
            HashMap<String, String> newDoor = new HashMap<>();
            JSONObject doorObj = (JSONObject) doorArray.get(j);
            newDoor = createDoor(newDoor, doorObj, roomJSON);
            doorList.add(newDoor);
        }
        JSONArray lootArray = (JSONArray) roomJSON.get("loot");
        for (int j = 0; j < lootArray.size(); j++) {
            itemLocations.add(itemPosition((JSONObject) lootArray.get(j), roomJSON.get("id").toString()));
            numOfItems += 1;
        }
        return room;
    }

    /**
    * fill a hashmap with information needed for a door.
    * @param newDoor
    * @param door
    * @param roomJSON
    * @return newDoor
    */
    private HashMap<String, String> createDoor(HashMap<String, String> newDoor, JSONObject door, JSONObject roomJSON) {
      newDoor.put("dir", String.valueOf(door.get("dir")));
      newDoor.put("curRoom", roomJSON.get("id").toString());
      newDoor.put("con_room", door.get("con_room").toString());
      newDoor.put("wall_pos", door.get("wall_pos").toString());
      return (newDoor);
    }


    /**
     * Create a map for information about an item in a room.
     * @param lootJSON (JSONObject) Loot key from the rooms file
     * @param roomID (String) Room id value
     * @return (Map<String, String>) Contains information about the item, where it is and what room
     */
    private Map<String, String>  itemPosition(JSONObject lootJSON, String roomID) {

        HashMap<String, String> loot = new HashMap<>();

        loot.put("room", roomID);
        loot.put("id", lootJSON.get("id").toString());
        loot.put("x", lootJSON.get("x").toString());
        loot.put("y", lootJSON.get("y").toString());

        return loot;
    }

    /**
     * Get the Item information from the Item key.
     * @param roomsJSON (JSONObject) The entire JSON file that contains keys for room and items
     */
    private void extractItemInfo(JSONObject roomsJSON) {

        JSONArray itemsJSONArray = (JSONArray) roomsJSON.get("items");
        for (int j = 0; j < itemLocations.size(); j++) {
          for (int i = 0; i < itemsJSONArray.size(); i++) {
              JSONObject itemsJSON = (JSONObject) itemsJSONArray.get(i);
              if (itemLocations.get(j).get("id").toString().equals(itemsJSON.get("id").toString())) {
                itemLocations.get(j).put("name", itemsJSON.get("name").toString());
                itemLocations.get(j).put("type", itemsJSON.get("type").toString());
                itemLocations.get(j).put("description", itemsJSON.get("description").toString());
              }
          }
        }
    }
}
