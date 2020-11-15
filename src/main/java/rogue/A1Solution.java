package rogue;





import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;




import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class A1Solution {
  /**
  * The main designed to parse the file location file and call to the classes to build the room.
  * @param args arguments from terminal
  */
    public static void main(String[] args) {

        // Hardcoded configuration file location/name
        String configurationFileLocation = "fileLocations.json";
        String roomLoc = "";
        String symbolLoc = "";

        // reading the input file locations using the configuration file
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader(configurationFileLocation));
            JSONObject configurationJSON = (JSONObject) obj;

            // Extract the Rooms value from the file to get the file location for rooms
            roomLoc = (String) configurationJSON.get("Rooms");

            // Extract the Symbols value from the file to get the file location for symbols-map
            symbolLoc = (String) configurationJSON.get("Symbols");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // instantiate a new Rogue object and call methods to do the required things
        Rogue test = new Rogue();
        test.parse(configurationFileLocation);
        test.createRooms();
        test.displayAll();
    }
}
