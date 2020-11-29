package rogue;

public class Food extends Item implements Edible {
  public Food() {

  }
  public String eat() {
    System.out.println("baby food");
    return(null);
  }
}
