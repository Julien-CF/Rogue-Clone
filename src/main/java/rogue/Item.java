package rogue;
import java.awt.Point;

/**
 * A basic Item class; basic functionality for both consumables and equipment
 */
public class Item  {

int id;
String name;
String type;
Point Xylocation;
Room current_room;

    //Constructors
    public Item() {

    }

    public Item(int id, String name, String type, Point xyLocation) {
      this.id = id;
      this.name = name;
      this.type = type;
      this.Xylocation = xyLocation;
    }

    // Getters and setters


    public int getId() {
        return (this.id);
    }


    public void setId(int id) {
      this.id = id;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
      this.name = name;
    }


    public String getType() {
        return (this.type);
    }


    public void setType(String type) {
      this.type = type;
    }


    public Character getDisplayCharacter() {
        return null;

    }


    public void setDisplayCharacter(Character newDisplayCharacter) {

    }


    public String getDescription() {
        return null;

    }


    public void setDescription(String newDescription) {

    }


    public Point getXyLocation() {
        return this.Xylocation;
    }


    public void setXyLocation(Point newXyLocation) {
      this.Xylocation = newXyLocation;
    }


    public Room getCurrentRoom() {
        return (this.current_room);

    }


    public void setCurrentRoom(Room newCurrentRoom) {
      this.current_room = newCurrentRoom;
    }
}
