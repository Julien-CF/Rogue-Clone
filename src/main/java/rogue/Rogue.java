package rogue;

import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.awt.Point;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Iterator;
import java.util.HashMap;



public class Rogue{

    private ArrayList<Room> roomList = new ArrayList<Room>();
    private ArrayList<Item> itemList = new ArrayList<Item>();
    private HashMap<String,String> loot = new HashMap<>();


    public void setPlayer(Player thePlayer){

    }


    public void setSymbols(String filename){


        try {

            JSONParser parser = new JSONParser();
            Object symObj = parser.parse(new FileReader(filename));
            JSONObject symbolObject = (JSONObject) symObj;

            JSONArray symbolArray = (JSONArray) symbolObject.get("symbols");
            Iterator symbolIterator = symbolArray.iterator();

        while(symbolIterator.hasNext()) {

            JSONObject currentSymbol = (JSONObject) symbolIterator.next();

            String name = (String) currentSymbol.get("name");
            String symbol = (String) currentSymbol.get("symbol");

            this.loot.put(name,symbol);

        }

      } catch(FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      } catch (ParseException e) {
          e.printStackTrace();
      }

    }

    public void setItems(String filename){
        try {

            JSONParser parser = new JSONParser();
            Object itemObj = parser.parse(new FileReader(filename));
            JSONObject itemObject = (JSONObject) itemObj;

            JSONArray itemArray = (JSONArray) itemObject.get("items");
            Iterator itemIterator = itemArray.iterator();

            while(itemIterator.hasNext()) {

                JSONObject currentObject = (JSONObject) itemIterator.next();

                Item currentItem = new Item();

                currentItem.setId( (int)(long) currentObject.get("id") );
                currentItem.setType( (String) currentObject.get("type") );
                currentItem.setName( (String) currentObject.get("name") );

                this.itemList.add(currentItem);
            }

        } catch(FileNotFoundException e) {
              e.printStackTrace();
        } catch (IOException e) {
              e.printStackTrace();
        } catch (ParseException e) {
              e.printStackTrace();
        }
    }

    public ArrayList<Room> getRooms(){
        return this.roomList;
    }

    public ArrayList<Item> getItems(){
        return (this.itemList);

    }
    public Player getPlayer(){
        return null;
    }


    public ArrayList<Item> createLootList(JSONArray lootArray){

        ArrayList<Item> lootList = new ArrayList<Item>();
        Iterator lootIterator = lootArray.iterator();

        while(lootIterator.hasNext()){
            JSONObject currentLoot = (JSONObject) lootIterator.next();

            Item currentItem = new Item();

            currentItem.setId( (int)(long) currentLoot.get("id") );
            Point loot_pos = new Point( (int)(long) currentLoot.get("x"), (int)(long) currentLoot.get("y") );
            currentItem.setXyLocation(loot_pos);

            for(int i = 0; i < this.itemList.size(); i++){
                Item temp = (Item) this.itemList.get(i);

                if(temp.getId() == currentItem.getId()){
                    currentItem.setName(temp.getName());
                    currentItem.setType(temp.getType());
                }
            }

            lootList.add(currentItem);
        }
        return(lootList);
    }

    public void createRooms(String filename){

        try {
            setItems(filename);

            JSONParser parser = new JSONParser();
            Object obj2 = parser.parse(new FileReader(filename));
            JSONObject roomObject = (JSONObject) obj2;

            JSONArray roomArray = (JSONArray) roomObject.get("room");
            Iterator roomIterator = roomArray.iterator();

            Player player = new Player("Default");

            while(roomIterator.hasNext()) {
                JSONObject currentRoom = (JSONObject) roomIterator.next();

                Room newRoom = new Room();

                int id = (int)(long) currentRoom.get("id");
                newRoom.setId(id);

                int height = (int)(long) currentRoom.get("height");
                newRoom.setHeight(height);

                int width = (int)(long) currentRoom.get("width");
                newRoom.setWidth(width);

                boolean inRoom = (boolean) currentRoom.get("start");
                newRoom.setIn_room(inRoom);

                JSONArray doorArray = (JSONArray) currentRoom.get("doors");
                Iterator doorIterator = doorArray.iterator();

                while(doorIterator.hasNext()){

                    JSONObject currentDoor = (JSONObject) doorIterator.next();

                    String direction = (String) currentDoor.get("dir");

                    int location = (int)(long) currentDoor.get("id");

                    newRoom.setDoor(direction,location);

                }

                newRoom.setSymbol(this.loot);

                if(newRoom.isPlayerInRoom() == true){
                    Point playerLocation = new Point(1,1);
                    player.setXyLocation(playerLocation);
                    player.setCurrentRoom(newRoom);
                    newRoom.setPlayer(player);
                }

                JSONArray lootArray = (JSONArray) currentRoom.get("loot");
                ArrayList<Item> finalLootList = createLootList(lootArray);
                newRoom.setRoomItems(finalLootList);

                this.roomList.add(newRoom);


            }

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //creates a string that displays all the rooms in the dungeon
    public String displayAll(){
        String rooms = "";
        for(int i = 0; i < this.roomList.size(); i++){
            rooms = rooms + this.roomList.get(i).displayRoom();
        }
        System.out.println(rooms);
        return (rooms);
    }
}
