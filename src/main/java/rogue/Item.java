package rogue;
import java.awt.Point;

/**
 * A basic Item class; basic functionality for both consumables and equipment.
 */

public class Item  {

private int id;
private String name;
private String type;
private String description;
private Point xyLocation;
private Room currentRoom;
private Character character;


    /**
    * Constructor for item.
    */
    public Item() {

    }

    /**
    * Constructor for item.
    * @param newId id if the item
    * @param newName name of the item
    * @param newType the type of item
    * @param newXyLocation the location of the item in the room
    */
    public Item(int newId, String newName, String newType, Point newXyLocation) {
        this.id = newId;
        this.name = newName;
        this.type = newType;
        this.xyLocation = newXyLocation;
    }

    // Getters and setters

    /**
    * getter for the Id of the item.
    * @return the idea of the item
    */
    public int getId() {
        return (this.id);
    }

    /**
    * setter for the id of the item.
    * @param newId the new id for the item
    */
    public void setId(int newId) {
        this.id = newId;
    }

    /**
    * getter for the name of the item.
    * @return the name of the item
    */
    public String getName() {
        return this.name;
    }

    /**
    * setter for the name of the item.
    * @param newName the new name of the item
    */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
    * getter for the type of the item.
    * @return the type of item
    */
    public String getType() {
        return (this.type);
    }

    /**
    * setter for the type of the item.
    * @param newType the new type of the item
    */
    public void setType(String newType) {
        this.type = newType;
    }

    /**
    * getter for the display character of the item.
    * @return the display character of the item
    */
    public Character getDisplayCharacter() {
        return this.character;
    }

    /**
    * setter for the display character of the item.
    * @param newDisplayCharacter the new display character of the item
    */
    public void setDisplayCharacter(Character newDisplayCharacter) {
        this.character = newDisplayCharacter;
    }

    /**
    * getter for the description of the item.
    * @return the description of the item
    */
    public String getDescription() {
        return (this.description);
    }

    /**
    * setter for the description of the item.
    * @param newDescription the new description of the item
    */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
    * getter for the xy locations of the item.
    * @return the xy locations of the item
    */
    public Point getXyLocation() {
        return this.xyLocation;
    }

    /**
    * setter for the xy locations of the item.
    * @param newXyLocation the new xy locations of the item
    */
    public void setXyLocation(Point newXyLocation) {
        this.xyLocation = newXyLocation;
    }

    /**
    * getter for the current room of the item.
    * @return the current room of the item
    */
    public Room getCurrentRoom() {
        return (this.currentRoom);
    }

    /**
    * setter for the current room of the item.
    * @param newCurrentRoom the new room location of the item
    */
    public void setCurrentRoom(Room newCurrentRoom) {
        this.currentRoom = newCurrentRoom;
    }
}
