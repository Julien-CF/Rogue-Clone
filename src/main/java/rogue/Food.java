package rogue;

public class Food extends Item implements Edible {
  /**
  * Constructor to initiate object.
  */
  public Food() {

  }
  /**
  * calls to description of item.
  * @return (String)
  */
  public String eat() {
    return (getDescription());
  }
}
