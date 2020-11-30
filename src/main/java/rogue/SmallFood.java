package rogue;

public class SmallFood extends Food implements Tossable {
  public SmallFood() {

  }
  public String toss() {
    return(getDescription());
  }
  public String eat(){
    return(getDescription());
  }
}
