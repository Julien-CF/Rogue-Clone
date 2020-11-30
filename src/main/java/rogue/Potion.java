package rogue;

public class Potion extends Magic implements Edible, Tossable {
  public Potion() {

  }
  public String eat() {
    return(getDescription());
  }
  public String toss() {
    return(getDescription());
  }
}
