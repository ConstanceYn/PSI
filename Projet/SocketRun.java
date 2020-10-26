import java.net.*;
import java.io.*;
import java.io.BufferedInputStream;

public class SocketRun implements Runnable {
  private Socket connection;
  public SocketRun(Socket connection){
    this.connection = connection;
  }
  public void run(){
    try{
      for(;;){
        try {
          PrintWriter pw=new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
          while(true){
            pw.print("HI\n");
            pw.flush();
            Thread.sleep(10000);
          }
          //pw.close();
        } catch (IOException e) {
          System.err.println("Echec connection write");
        }
      }
    }catch(InterruptedException e){
    }
  }
}
