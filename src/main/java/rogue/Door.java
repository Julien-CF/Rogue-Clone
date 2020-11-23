package rogue;
import java.util.ArrayList;


public class Door {

  private ArrayList<Room> roomList = new ArrayList<Room>();
  private int wallLoc;
  private int roomIndex;
  private int conRoomIndex;
  private String wall;

  private Door exitDoor;

  /**
  * Constructor for Door.
  */
  public Door() {

  }

  /**
  * Connects a room to the door.
  * @param r room to be connected
  */
  public void connectRoom(Room r) {
    this.roomList.add(r);
  }

  /**
  * get the list of rooms connected to the door.
  * @return arrayList containing rooms
  */
  public ArrayList<Room> getConnectedRooms() {
    return (this.roomList);
  }

  /**
  * returns the other room in the list of rooms connected.
  * @param currentRoom room used to diferenciate what the other room is
  * @return r the other room opposite currentRoom
  */
  public Room getOtherRoom(Room currentRoom) {
    Room r = new Room();
    for (int i = 0; i < roomList.size(); i++) {
      if (!(this.roomList.get(i).equals(currentRoom))) {
        r = roomList.get(i);
        break;
      }
    }
    return (r);
  }

  /**
  * setter for the exit door.
  * @param door
  */
  public void setExitDoor(Door door) {
    this.exitDoor = door;
  }

  /**
  * setter for the doors location on the wall.
  * @param newWallLoc
  */
  public void setWallLoc(int newWallLoc) {
    this.wallLoc = newWallLoc;
  }

  /**
  * setter for the new wall that the door is located on.
  * @param newWall
  */
  public void setWall(String newWall) {
    this.wall = newWall;
  }

  /**
  * setter for the new current room that this door sits in.
  * @param newCurRoom
  */
  public void setCurRoom(int newCurRoom) {
    this.roomIndex = newCurRoom;
  }

  /**
  * setter for the new connected room.
  * @param newConRoom
  */
  public void setConRoom(int newConRoom) {
    this.conRoomIndex = newConRoom;
  }

  /**
  * getter for the wall location of the door.
  * @return wall location
  */
  public int getWallLoc() {
    return (this.wallLoc);
  }

  /**
  * getter for the wall the door sits on.
  * @return wall
  */
  public String getWall() {
    return (this.wall);
  }

  /**
  * getter for the current room of the door.
  * @return roomIndex
  */
  public int getCurRoom() {
    return (this.roomIndex);
  }

  /**
  * getter for the connected room.
  * @return conRoomIndex
  */
  public int getConRoom() {
    return (this.conRoomIndex);
  }

  /**
  * getter for the exit door associated with this door.
  * @return exitdoor
  */
  public Door getExitDoor() {
    return this.exitDoor;
  }
}
