package rogue;

import java.awt.Point;
/*
 * The player character
 */
public class Player {

private Room currentRoom;
private String name;
private Point position;

    // Default constructor
    /**
    * Constructor for player. Sets current room to null and name to empty
    */
    public Player() {
        this.currentRoom = null;
        this.name = "";
    }

    /**
    * constructor for player. initializes current room and sets the name
    * @param newName name of the player
    */
    public Player(String newName) {
        this.name = name;
    }

    /**
    * getter for the name of the player.
    * @return the name of the player
    */
    public String getName() {
        return (this.name);
    }

    /**
    * setter for the name of the player.
    * @param newName new name to set to the player
    */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
    * getter for the xyLocation of the player.
    * @return the xy location of the player
    */
    public Point getXyLocation() {
        return (this.position);
    }

    /**
    * setter for the xy location of the player.
    * @param newXyLocation new xy location to set to the player
    */
    public void setXyLocation(Point newXyLocation) {
        this.position = newXyLocation;
    }

    /**
    * getter for the current room of the player of the player.
    * @return the current room of the player
    */
    public Room getCurrentRoom() {
        return (this.currentRoom);
    }

    /**
    * setter for the current room of the player.
    * @param newRoom new current room to set to the player
    */
    public void setCurrentRoom(Room newRoom) {
        this.currentRoom = newRoom;
    }
}
