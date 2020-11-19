import java.io.*;

public class EnvoiRun implements Runnable {

  public EnvoiRun(){}

  public void run() {

    Process proc = null;
    try {
      String[] cmdss= {"gnome-terminal","-e", "java Envoi"};
      //String[] cmdss= {"ttab", "java Envoi"};
      proc = Runtime.getRuntime().exec(cmdss);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
