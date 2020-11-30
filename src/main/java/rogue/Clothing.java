package rogue;

public class Clothing extends Item implements Wearable {
  /**
  * Constructor initializes item.
  */
  public Clothing() {

  }
  /**
  * calls to descrption of item.
  * @return (String)
  */
  public String wear() {
    return (getDescription());
  }
}
