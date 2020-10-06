package rogue;
import java.util.ArrayList;
import java.awt.Point;
/**
 * The player character
 */
public class Player {

private Room currentRoom;
private String name;
private Point position;

    // Default constructor
    public Player() {
        this.currentRoom = null;
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
        return (this.currentRoom);
    }


    public void setCurrentRoom(Room newRoom) {
        this.currentRoom = newRoom;
    }
}
