package rogue;

public class Ring extends Magic implements Wearable {
  /**
  * constructor initiates object.
  */
  public Ring() {

  }
  /**
  * calls to description of item.
  * @return (String)
  */
  public String wear() {
    return (getDescription());
  }
}
