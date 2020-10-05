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



public class Rogue{

  ArrayList<Room> room_list = new ArrayList<Room>();

    public void setPlayer(Player thePlayer){

    }


    public void setSymbols(String filename){

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
          int height = (int)(long) current_room.get("height");
          int width = (int)(long) current_room.get("width");

          JSONArray door_array = (JSONArray) current_room.get("doors");
          Iterator door_iterator = door_array.iterator();

          while(door_iterator.hasNext()){
            JSONObject current_door = (JSONObject) door_iterator.next();

            String direction = (String) current_door.get("dir");

            int location = (int)(long) current_door.get("id");

            newRoom.setDoor(direction,location);
          }

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
        return null;
    }
}
