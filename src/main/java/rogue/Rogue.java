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

    public ArrayList<Room> getRooms(){
        return this.room_list;
    }

    public ArrayList<Item> getItems(){
        return null;

    }
    public Player getPlayer(){
        return null;

    }


    public void createRooms(String filename){

      try {

        JSONParser parser = new JSONParser();
        Object obj2 = parser.parse(new FileReader(filename));
        JSONObject room_object = (JSONObject) obj2;

        JSONArray room_array = (JSONArray) room_object.get("room");
        Iterator room_iterator = room_array.iterator();

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
