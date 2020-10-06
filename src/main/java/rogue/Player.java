package rogue;
import java.util.ArrayList;
import java.awt.Point;
/**
 * The player character
 */
public class Player {

Room current_room;
String name;
Point position;

    // Default constructor
    public Player() {
      this.current_room = null;
      this.name = "";
    }


    public Player(String name) {
      this.name = name;
    }


    public String getName() {
        return (this.name);
    }


    public void setName(String newName) {
      this.name = newName;
    }

    public Point getXyLocation() {
      return(this.position);
    }


    public void setXyLocation(Point newXyLocation) {
      this.position = newXyLocation;
    }


    public Room getCurrentRoom() {
        return (this.current_room);
    }


    public void setCurrentRoom(Room newRoom) {
      this.current_room = newRoom;
    }
}
