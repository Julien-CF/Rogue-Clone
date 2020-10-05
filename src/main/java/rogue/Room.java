package rogue;
import java.util.ArrayList;
import java.util.Map;
import java.awt.Point;
import java.util.HashMap;


/**
 * A room within the dungeon - contains monsters, treasure,
 * doors out, etc.
 */
public class Room  {
  int id;
  int height;
  int width;
  HashMap<String,Integer> door = new HashMap<>();
  HashMap<String,String> symbols = new HashMap<>();
  boolean in_room = false;

    // Default constructor
 public Room() {

 }


   // Required getter and setters below

  public void setSymbol(HashMap<String,String> symbol){
    this.symbols = symbol;
  }

  public void setIn_room(boolean newIn_room){
    this.in_room = newIn_room;
  }

  public boolean getIn_Room(){
    return (this.in_room);
  }

 public int getWidth() {
   return this.width;
 }


 public void setWidth(int newWidth) {
   this.width = newWidth;
 }


 public int getHeight() {
   return this.height;
 }


 public void setHeight(int newHeight) {
   this.height = newHeight;
 }

 public int getId() {
    return this.id;
 }


 public void setId(int newId) {
   this.id = newId;
 }


 public ArrayList<Item> getRoomItems() {
    return null;

 }


 public void setRoomItems(ArrayList<Item> newRoomItems) {

 }


 public Player getPlayer() {
    return null;

 }


 public void setPlayer(Player newPlayer) {

 }
// BROKEN NEEDS FIXING ASAP ROCKY TOMORROW
 public int getDoor(String direction){
   int i;
   if(this.door.get(direction) == null){
     i = -1;
   }
   else{
     i = this.door.get(direction);
   }
    return (i) ;
 }

/*
direction is one of NSEW
location is a number between 0 and the length of the wall
*/

public void setDoor(String direction, int location){
  this.door.put(direction, location);
}


public boolean isPlayerInRoom() {
  return (this.in_room);
}


public String createNS(String dir){
  int test = getDoor(dir);
  String North = "";
  String roof = this.symbols.get("NS_WALL");
  for(int i = 0; i < getWidth(); i++){
    if(i == test){
      North = North + this.symbols.get("DOOR");
    }
    else{
      North = North + roof;
    }
  }
  North = North + "\n";
  return(North);
}

public String createContent(){
  String content = "";
  String wall = this.symbols.get("EW_WALL");
  String floor = this.symbols.get("FLOOR");

  for(int i = 0; i < getHeight() - 2; i++){
    content = content + wall;
    for(int j = 0; j < getWidth() - 2; j++){
      content = content + floor;
    }
    content = content + wall;
    content = content + "\n";
  }
  return(content);
}


   /**
    * Produces a string that can be printed to produce an ascii rendering of the room and all of its contents
    * @return (String) String representation of how the room looks
    */
   public String displayRoom() {
     String map = "";
     map = map + createNS("N");
     map = map + createContent();
     map = map + createNS("S");
     map = map + "\n";

    return (map);
   }
}
