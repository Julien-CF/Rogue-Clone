package rogue;

public class SmallFood extends Food implements Tossable {
  /**
  * Constructor initializes object.
  */
  public SmallFood() {

  }
  /**
  * calls to description.
  * @return (String)
  */
  public String toss() {
    return (getDescription());
  }
  /**
  * calls to description.
  * @return (String)
  */
  public String eat() {
    return (getDescription());
  }
}
