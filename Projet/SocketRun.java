import java.net.*;
import java.io.*;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class SocketRun implements Runnable {
  private Socket connection;
  private Serveur serveur;
  private int token; // pour retenir le token du client qu'on traite sans le chercher à chaque fois

  public SocketRun(Socket connection, Serveur s){
    this.connection = connection;
    this.serveur = s;
    this.token = 0;
  }


  public void run(){
    try {
    BufferedReader networkIn = null;
    PrintWriter writer = null;

    boolean continuer = true;
     try{
      // le serveur envoie un message de bienvenue au client
        writer = new PrintWriter(connection.getOutputStream());
        networkIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));

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
        String huit = "> 8 : deconnection \n";
        String cmd = "\n" +intro +un+ deux + six + huit + ".\n";

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
          System.out.println("test read please");

          String content = "";
          String msg = "";
          while(!content.equals(".")){
            content = networkIn.readLine();
            msg += content + "\n";
            System.out.println("content" + content);
          }
          //content = networkIn.readLine();
          System.out.println("msg \n" + msg);
          continuer = parse(msg, serveur);
          System.out.println("sortie du parse");
          System.out.println();

          // 10 secondes off
          Thread.sleep(1000);

        } catch (IOException e) {
          System.err.println("Echec connection read");
          continuer = false;
        }
      }
      System.out.println("fin du client " + token);
    }catch(InterruptedException e){

    }
  }

  public boolean parse(String cmd, Serveur s) {
    Message message = Message.strToMessage(cmd);
    String commande = message.getType();

    Message reponse = null;
    switch(commande) {
      case "CONNECT":
        reponse = this.connect(message, s);
        break;
      case "DISCONNECT":
        User u = s.getUser(token);
        u.set_disconnect();
        System.out.println( u.getConnected());
        return false;
      case "POST_ANC":
        reponse = this.annonce(message, s);
        break;
      case "MAJ_ANC":
        break;
      case "DELETE_ANC":
        break;
      case "REQUEST_DOMAIN":
        break;
      case "REQUEST_ANC":
        reponse = this.req_annonce(message, s);
        break;
      case "REQUEST_OWN_ANC":
        break;
      case "REQUEST_IP":
        break;
      default :
        // UNKNOWN_REQUEST
        break ;
    }
    String rep = reponse.messageToStr();
    System.out.println(rep);
    try {
      System.out.println("on envoie la réponse");
      PrintWriter writer = new PrintWriter(connection.getOutputStream());
      writer.println(rep);
      writer.flush();
      writer.close();
      System.out.println("réponse envoyé");
    } catch (IOException e) {
      System.err.println("Echec write");
    }

    return true;

  }

  public Message connect(Message msg, Serveur s)
  {
    String arg0 = msg.getArgs()[0];
    try {
      // if args[0] == int
      //alors déjà un user dans la BDD
      int i = Integer.parseInt(arg0);
      User u = s.getUser(i);
      if (u == null)
        return Message.connectKo();

      if (u.getConnected())
        return Message.connectKo();

      u.set_connect();
      u.setIp(connection.getInetAddress());
      return Message.connectOk();

    } catch (Exception e) { // le client a envoyé un nom
      // regarder si l'user éxiste deja :
      User u = s.getUser(arg0);
      if (u!=null){
        if (u.getConnected())
          return Message.connectKo();
        u.set_connect();
        u.setIp(connection.getInetAddress());
        return Message.connectOk();
      }
      // sinon
      System.out.println("nouvel utilisateur");
      u  = new User(arg0, connection.getInetAddress());
      int token = u.getToken();
      this.token = token;
      s.add_User(u);
      return Message.connectNewUserOk(Integer.toString(token));
    }




    // if args[0] == int
    //alors déjà un user dans la BDD

  }

  public Message annonce(Message msg, Serveur s)
  {
    Annonce a = new Annonce(msg);
    int id = s.get_nbAnn();
    a.setId(id);
    s.add_Annonce(a);
    System.out.println(a.Annonce_from_Serveur());
    Message reponse = Message.postAncOk(id);
    return reponse;

  }

  public Message req_annonce(Message m, Serveur s)
  {
    //String str;
    // A AMELIORER BEAUCOUP
    Annonce[] req = new Annonce[s.get_Ann().size()];
    for (int i = 0; i< s.get_Ann().size();i++ )
    {
      //System.out.println("dans le for");

      Annonce ann = s.get_Ann().get(i);
      req[i] = ann;
      // str = ann.Annonce_to_Client();
      // System.out.println(str);


      // if (ann.is_domaine(m.getArgs()[0]))
      // {
      //   String str = ann.Annonce_to_Client();
      //   System.out.println(str);
      // }
    }
    Message reponse = Message.sendAncOk(req, req.length);
    return reponse;

  }
}
