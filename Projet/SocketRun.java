import java.net.*;
import java.io.*;
import java.io.BufferedInputStream;
import java.util.ArrayList;

public class SocketRun implements Runnable {
  private Socket connection;
  private Serveur serveur;

  public SocketRun(Socket connection, Serveur s){
    this.connection = connection;
    this.serveur = s;
  }


  public void run(){
    try{
      for(;;){
        try {
          // le serveur envoie un message au client
          BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
          //serveur.add();
          String str = "bonjour nouveau client.";
          bos.write(str.getBytes());
          bos.flush();

          // le serveur reçoit un message du client
          BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
          String content = "";
          char stream;
          while((stream = (char)bis.read()) != '.'){
            content += (char)stream;
          }
          content += ".\n";
          System.out.println(content);
          //System.out.print((char)stream);
          parse(content, serveur);

          // 1 minute off
          //Thread.sleep(60000);
          Thread.sleep(60);

        } catch (IOException e) {
          //System.err.println("Echec connection write");
        }
      }
    }catch(InterruptedException e){

    }
  }

  static public void parse(String cmd, Serveur s) {
    Message message = Message.strToMessage(cmd);
    String commande = message.getType();
    System.out.println(commande);
    switch(commande) {
      case "CONNECT":
        break;
      case "DISCONNECT":
        break;
      case "POST_ANC":
        annonce(message, s);
        break;
      case "MAJ_ANC":
        break;
      case "DELETE_ANC":
        break;
      case "REQUEST_DOMAIN":
        break;
      case "REQUEST_ANC":
        req_annonce(s);
        break;
      case "REQUEST_OWN_ANC":
        break;
      case "REQUEST_IP":
        break;
      default :
        // UNKNOWN_REQUEST
        break ;
    }

  }

  public static void annonce(Message msg, Serveur s)
  {
    Annonce a = new Annonce(msg);
    a.setId(s.get_nbAnn());
    s.add_Annonce(a);
    System.out.println("annonce ok");
  }

  public static void req_annonce(Serveur s)
  {
    for (int i = 0; i< s.get_Ann().size();i++ )
    {
      Annonce ann = s.get_Ann().get(i);
      String str = ann.Annonce_to_Client();
      System.out.println(str);
    }
  }
}
