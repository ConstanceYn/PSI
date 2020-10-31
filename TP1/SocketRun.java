import java.net.*;
import java.io.*;
import java.io.BufferedInputStream;

public class SocketRun implements Runnable {
  private Socket connection;
  private Compteur cmp;

  public SocketRun(Socket connection, Compteur c){
    this.connection = connection;
    cmp = c;
  }
  public void run(){
    try{
      cmp.incr();
      boolean continuer = true;
      while(continuer){
        try {
          PrintWriter pw = new PrintWriter(connection.getOutputStream() );
          while(true)
          {
            String content = cmp.Cmp_to_String();
            System.out.print(content);
            pw.print(content);
            pw.flush();
            Thread.sleep(10000);
            //System.out.println("socket closed : " + connection.isBound());
          }
          // BON En gros on arrive bien à incrémenter le compteur
          // mais quand le client quitte le serveur ne le sait pas
          // du coup on arrive pas à décrementer le compteur :(


          // System.out.println("deco");
          //pw.close();

        } catch (IOException e) {
          System.err.println("Echec connection write");
          continuer = false;
          cmp.decr();

        }
      }
    }catch(InterruptedException e){
    }
  }
}
