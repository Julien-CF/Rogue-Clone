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

    // Default constructor
 public Room() {

 }




   // Required getter and setters below


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






 public int getDoor(String direction){
  int i = this.door.get(direction);
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

return true;
}




   /**
    * Produces a string that can be printed to produce an ascii rendering of the room and all of its contents
    * @return (String) String representation of how the room looks
    */
   public String displayRoom() {
    return null;


   }
}
