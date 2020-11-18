import java.io.*;

public class ClientUDPRun implements Runnable {

  public ClientUDPRun(){}

  public void run() {

    Process proc = null;
    try {
      String[] cmdss= {"gnome-terminal","-e", "java ClientUDP"};
      //String[] cmdss= {"ttab", "java ClientUDP"};
      proc = Runtime.getRuntime().exec(cmdss);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
