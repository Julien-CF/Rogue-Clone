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
      

        // instantiate a new Rogue object and call methods to do the required things
      RogueParser parser = new RogueParser(configurationFileLocation);
      Rogue theGame = new Rogue(parser);

    }
}
