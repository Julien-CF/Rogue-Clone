package rogue;

import java.awt.Point;
import java.util.ArrayList;
import java.io.Serializable;
/*
 * The player character
 */
public class Player implements Serializable {

private Room currentRoom;
private String name;
private Point position;
private ArrayList<Item> inventory = new ArrayList<Item>();
private ArrayList<Item> equipped = new ArrayList<Item>();
private String message = null;


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
        this.name = newName;
    }

    public void addItem(Item newItem){
      this.inventory.add(newItem);
    }

    public void equipItem(int index){
      System.out.println(index);
      this.equipped.add(this.inventory.get(index));
      this.inventory.remove(index);
    }

    public String[] getInventoryStrings(){
      String list[] = new String[this.inventory.size()];
      for (int i = 0; i < this.inventory.size(); i++){
        list[i] = this.inventory.get(i).getName();
      }
      return (list);
    }

    public String[] getEquippedStrings(){
      String list[] = new String[this.equipped.size()];
      for (int i = 0; i < this.equipped.size(); i++){
        list[i] = this.equipped.get(i).getName();
      }
      return (list);
    }

    public ArrayList<Item> getInventory(){
      return(this.inventory);
    }

    public void setMessage(String newMessage){
        this.message = newMessage;
    }

    public String getMessage(){
      return(this.message);
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
