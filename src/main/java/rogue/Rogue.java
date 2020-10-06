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

  ArrayList<Room> room_list = new ArrayList<Room>();
  ArrayList<Item> item_list = new ArrayList<Item>();
  HashMap<String,String> loot = new HashMap<>();


    public void setPlayer(Player thePlayer){

    }


    public void setSymbols(String filename){


      try {

        JSONParser parser = new JSONParser();
        Object sym_obj = parser.parse(new FileReader(filename));
        JSONObject symbol_object = (JSONObject) sym_obj;

        JSONArray symbol_array = (JSONArray) symbol_object.get("symbols");
        Iterator symbol_iterator = symbol_array.iterator();

        while(symbol_iterator.hasNext()) {

          JSONObject current_symbol = (JSONObject) symbol_iterator.next();

          String name = (String) current_symbol.get("name");
          String symbol = (String) current_symbol.get("symbol");

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
        Object item_obj = parser.parse(new FileReader(filename));
        JSONObject item_object = (JSONObject) item_obj;

        JSONArray item_array = (JSONArray) item_object.get("items");
        Iterator item_iterator = item_array.iterator();

        while(item_iterator.hasNext()) {


          JSONObject current_item = (JSONObject) item_iterator.next();

          Item current_Item = new Item();

          current_Item.setId( (int)(long) current_item.get("id") );
          current_Item.setType( (String) current_item.get("type") );
          current_Item.setName( (String) current_item.get("name") );

          this.item_list.add(current_Item);
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
        return this.room_list;
    }

    public ArrayList<Item> getItems(){
        return (this.item_list);

    }
    public Player getPlayer(){
        return null;

    }


    public ArrayList<Item> createLootList(JSONArray loot_array){

      ArrayList<Item> loot_list = new ArrayList<Item>();
      Iterator loot_iterator = loot_array.iterator();

      while(loot_iterator.hasNext()){
        JSONObject current_loot = (JSONObject) loot_iterator.next();

        Item current_Item = new Item();

        current_Item.setId( (int)(long) current_loot.get("id") );
        Point loot_pos = new Point( (int)(long) current_loot.get("x"), (int)(long) current_loot.get("y") );
        current_Item.setXyLocation(loot_pos);

        for(int i = 0; i < this.item_list.size(); i++){
          Item temp = (Item) this.item_list.get(i);
          if(temp.getId() == current_Item.getId()){
            current_Item.setName(temp.getName());
            current_Item.setType(temp.getType());
          }
        }
        loot_list.add(current_Item);


      }
      return(loot_list);
    }

    public void createRooms(String filename){

      try {

        setItems(filename);

        JSONParser parser = new JSONParser();
        Object obj2 = parser.parse(new FileReader(filename));
        JSONObject room_object = (JSONObject) obj2;

        JSONArray room_array = (JSONArray) room_object.get("room");
        Iterator room_iterator = room_array.iterator();

        Player player = new Player("Default");

        while(room_iterator.hasNext()) {
          JSONObject current_room = (JSONObject) room_iterator.next();

          Room newRoom = new Room();

          int id = (int)(long) current_room.get("id");
          newRoom.setId(id);

          int height = (int)(long) current_room.get("height");
          newRoom.setHeight(height);

          int width = (int)(long) current_room.get("width");
          newRoom.setWidth(width);

          boolean inRoom = (boolean) current_room.get("start");
          newRoom.setIn_room(inRoom);

          JSONArray door_array = (JSONArray) current_room.get("doors");
          Iterator door_iterator = door_array.iterator();

          while(door_iterator.hasNext()){
            JSONObject current_door = (JSONObject) door_iterator.next();

            String direction = (String) current_door.get("dir");


            int location = (int)(long) current_door.get("id");

            newRoom.setDoor(direction,location);

          }

          newRoom.setSymbol(this.loot);

          if(newRoom.isPlayerInRoom() == true){
            Point player_location = new Point(1,1);
            player.setXyLocation(player_location);
            player.setCurrentRoom(newRoom);
            newRoom.setPlayer(player);
          }

          JSONArray loot_array = (JSONArray) current_room.get("loot");
          ArrayList<Item> final_loot_list = createLootList(loot_array);
          newRoom.setRoomItems(final_loot_list);

          this.room_list.add(newRoom);


        }

      } catch(FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      } catch (ParseException e) {
          e.printStackTrace();
      }
    }
    public String displayAll(){
        //creates a string that displays all the rooms in the dungeon
        String rooms = "";
        for(int i = 0; i < this.room_list.size(); i++){
          rooms = rooms + this.room_list.get(i).displayRoom();
        }
        System.out.println(rooms);
        return rooms;
    }
}
