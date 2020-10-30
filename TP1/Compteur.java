public class Compteur {
  int cmp;

  public Compteur(){
    cmp = 0;
  }
  public void incr(){
    cmp++;
  }
  public void decr(){
    cmp--;
  }
  public String Cmp_to_String(){
    String str = Integer.toString(cmp) + "\n";
    return str;
  }
}
