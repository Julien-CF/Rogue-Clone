package rogue;
import java.util.ArrayList;


public class Door{

  private ArrayList<Room> roomList = new ArrayList<Room>();
  private int wallLoc;
  private int roomIndex;
  private int conRoomIndex;
  private String wall;

  private Door exitDoor;

  public Door(){

  }
  public void ConnectRoom(Room r){
    this.roomList.add(r);
  }

  public ArrayList<Room> getConnectedRooms(){
    return(this.roomList);
  }

  public Room getOtherRoom(Room currentRoom){
    Room r = new Room();
    for(int i = 0; i < roomList.size(); i++){
      if(!(this.roomList.get(i).equals(currentRoom))){
        r = roomList.get(i);
        break;
      }
    }
    return(r);
  }

  public void setExitDoor(Door door){
    this.exitDoor = door;
  }

  public void setWallLoc(int newWallLoc){
    this.wallLoc = newWallLoc;
  }

  public void setWall(String newWall){
    this.wall = newWall;
  }

  public void setCurRoom(int newCurRoom){
    this.roomIndex = newCurRoom;
  }

  public void setConRoom(int newConRoom){
    this.conRoomIndex = newConRoom;
  }

  public int getWallLoc(){
    return(this.wallLoc);
  }

  public String getWall(){
    return(this.wall);
  }

  public int getCurRoom(){
    return(this.roomIndex);
  }

  public int getConRoom(){
    return(this.conRoomIndex);
  }

  public Door getExitDoor(){
    return this.exitDoor;
  }



}
