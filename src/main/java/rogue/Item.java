package rogue;
import java.awt.Point;

/**
 * A basic Item class; basic functionality for both consumables and equipment
 */
public class Item  {

private int id;
private String name;
private String type;
private String description;
private Point xyLocation;
private Room currentRoom;
private Character character;

    //Constructors
    public Item() {

    }

    public Item(int id, String name, String type, Point xyLocation) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.xyLocation = xyLocation;
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
        return this.character;
    }


    public void setDisplayCharacter(Character newDisplayCharacter) {
        this.character = newDisplayCharacter;
    }


    public String getDescription() {
        return (this.description);
    }


    public void setDescription(String newDescription) {
        this.description = newDescription;
    }


    public Point getXyLocation() {
        return this.xyLocation;
    }


    public void setXyLocation(Point newXyLocation) {
        this.xyLocation = newXyLocation;
    }


    public Room getCurrentRoom() {
        return (this.currentRoom);

    }


    public void setCurrentRoom(Room newCurrentRoom) {
        this.currentRoom = newCurrentRoom;
    }
}
