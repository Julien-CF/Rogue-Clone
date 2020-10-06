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

    private int id;
    private int height;
    private int width;
    private ArrayList<Item> lootList = new ArrayList<Item>();
    private HashMap<String,Integer> door = new HashMap<>();
    private HashMap<String,String> symbols = new HashMap<>();
    private boolean inRoom = false;
    private Player player;

    // Default constructor
    public Room() {

    }


    // Required getter and setters below
    public void setSymbol(HashMap<String,String> symbol){
        this.symbols = symbol;
    }

    public void setIn_room(boolean newInRoom){
        this.inRoom = newInRoom;
    }

    public boolean getIn_Room(){
        return (this.inRoom);
    }

    public int getWidth() {
        return (this.width);
    }

    public void setWidth(int newWidth) {
        this.width = newWidth;
    }

    public int getHeight() {
        return (this.height);
    }

    public void setHeight(int newHeight) {
        this.height = newHeight;
    }

    public int getId() {
        return (this.id);
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public ArrayList<Item> getRoomItems() {
        return (this.lootList);
    }

    public void setRoomItems(ArrayList<Item> newRoomItems) {
        this.lootList = newRoomItems;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player newPlayer) {
        this.player = newPlayer;
    }

    public int getDoor(String direction){
        int i;
        if(this.door.get(direction) == null){
            i = -1;
        }
        else{
            i = this.door.get(direction);
        }
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
        return (this.inRoom);
    }

    public String createNS(String dir){
        int doorPos = getDoor(dir);
        String North = "";
        String roof = this.symbols.get("NS_WALL");

        for(int i = 0; i < getWidth(); i++){
            if(i == doorPos){
                North = North + this.symbols.get("DOOR");
            }
            else{
                North = North + roof;
            }
        }
        North = North + "\n";
        return(North);
    }

    public boolean itemIsThere( int i, int j){
        boolean test = false;
        ArrayList<Item> temp = getRoomItems();
        for(int t = 0; t < temp.size(); t++){

            Item tempI = temp.get(t);
            Point tempP = tempI.getXyLocation();
            int x = (int) tempP.getX();
            int y = (int) tempP.getY();
            if(j == x && i == y){
                test = true;
            }

        }
        return(test);
    }

    public String createContent(){

        String content = "";
        String wall = this.symbols.get("EW_WALL");
        String floor = this.symbols.get("FLOOR");
        int doorPosW = getDoor("W");
        int doorPosE = getDoor("E");
        Point playerPos = new Point(-1,-1);

        if(isPlayerInRoom() == true){
            playerPos = this.player.getXyLocation();
        }

        for(int i = 1; i < getHeight() - 1; i++){

            if(i == doorPosW){
                content = content + this.symbols.get("DOOR");
            }
            else{
                content = content + wall;
            }
            for(int j = 1; j < getWidth() -1; j++){
                if(j == playerPos.getX() && i == playerPos.getY()){
                    content = content + this.symbols.get("PLAYER");
                }
                else if(itemIsThere(i, j) == true ){
                    content = content + this.symbols.get("ITEM");
                }
                else{
                    content = content + floor;
                }
            }
            if(i == doorPosE){
                content = content + this.symbols.get("DOOR");
            }
            else{
                content = content + wall;
            }

            content = content + "\n";
        }
        return(content);
    }
    /**
    * Produces a string that can be printed to produce an ascii rendering of the room and all of its contents
    * @return (String) String representation of how the room looks
    */
    public String displayRoom() {
        String map = "";
        map = map + createNS("N");
        map = map + createContent();
        map = map + createNS("S");
        map = map + "\n";

        return (map);
    }
}
