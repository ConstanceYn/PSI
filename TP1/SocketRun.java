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
          BufferedReader read = new BufferedReader( new InputStreamReader(connection.getInputStream()));
          while(true)
          {
            String content = cmp.Cmp_to_String();
            System.out.print(content);
            // envoie du nombre de connections
            pw.print(content);
            pw.flush();
            // lit le message d'acquitement du client
            int co = read.read();
            // si pas d'acquitement, le client est considéré comme déconnecté
            if (co == -1){
              cmp.decr();
              connection.close();
            }
            Thread.sleep(10000);
          }
        } catch (IOException e) {
          // Client considérer déconnecté
          System.err.println("Echec connection write");
          continuer = false;
          cmp.decr();
        }
      }
    }catch(InterruptedException e){
    }
  }
}
