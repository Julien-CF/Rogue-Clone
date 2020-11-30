package rogue;

public class Potion extends Magic implements Edible, Tossable {
  /**
  * constructor initializes item.
  */
  public Potion() {

  }
  /**
  * gets first half of description.
  * @return (String)
  */
  public String eat() {
    return (getDescription());
  }
  /**
  * gets second half of description.
  * @return (String)
  */
  public String toss() {
    return (getDescription());
  }
}
