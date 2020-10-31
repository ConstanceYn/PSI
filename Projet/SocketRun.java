import java.net.*;
import java.io.*;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class SocketRun implements Runnable {
  private Socket connection;
  private Serveur serveur;

  public SocketRun(Socket connection, Serveur s){
    this.connection = connection;
    this.serveur = s;
  }


  public void run(){
    try{
      BufferedReader networkIn = null;
      PrintWriter writer = null;

      boolean continuer = true;
       try{
        // le serveur envoie un message de bienvenue au client
        writer = new PrintWriter(connection.getOutputStream());

        String str = "Bonjour !! Bienvenue sur Good Duck";
        writer.println(str);
        writer.flush();


        String intro = "Entrez le nombre de la requete que vous voulez executer :\n";
        String un = "> 1 : Connection\n";
        String deux = "> 2 : Poster une annonce\n";
        String trois = "> 3 : Modifier une annonce \n";
        String quatre = "> 4 : Suppimer une annonce \n";
        String cing = "> 5 : Afficher les domaines \n";
        String six = "> 6 : Afficher les annonces d'un domaine \n";
        String sept = "> 7 : Afficher ses annonces \n";
        String cmd = "\n" +intro + deux + six + ".\n";

        writer.write(cmd);
        writer.flush();
        System.out.println("on a envoye les commandes");
        // writer.println("");
        // writer.flush();

        // provisoir, une sorte de pansement quoi parce que sinon il veut pas flush
        // writer.close();

      } catch (IOException e) {
       System.err.println("Echec connection write 1");
      }

      while(continuer){
        try {
          // on écoute le message du client
          networkIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
          System.out.println("test read please");

          String content = networkIn.readLine();
          String msg = "";
          while(content != null){
            msg += content + "\n";
            content = networkIn.readLine();
            //System.out.println(content);
          }
          System.out.println(msg);
          continuer = parse(msg, serveur, connection);

          // 1 minute off
          Thread.sleep(60000);

        } catch (IOException e) {
          //System.err.println("Echec connection read");
          //continuer = false;
        }
      }
    }catch(InterruptedException e){

    }
  }

  static public boolean parse(String cmd, Serveur s, Socket c) {
    Message message = Message.strToMessage(cmd);
    String commande = message.getType();
    switch(commande) {
      case "CONNECT":
        break;
      case "DISCONNECT":
        return false;
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
        req_annonce(message, s);
        break;
      case "REQUEST_OWN_ANC":
        break;
      case "REQUEST_IP":
        break;
      default :
        // UNKNOWN_REQUEST
        break ;
    }
    return true;

  }

  public static void annonce(Message msg, Serveur s)
  {
    Annonce a = new Annonce(msg);
    a.setId(s.get_nbAnn());
    s.add_Annonce(a);
    System.out.println("annonce ok");
  }

  public static void req_annonce(Message m, Serveur s)
  {
    System.out.println("req annonce ?");
    System.out.println(s.get_Ann().size());
    for (int i = 0; i< s.get_Ann().size();i++ )
    {
      Annonce ann = s.get_Ann().get(i);
      if (ann.is_domaine(m.getArgs()[0]))
      {
        String str = ann.Annonce_to_Client();
        System.out.println(str);
      }
    }
  }
}
