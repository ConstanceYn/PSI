import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.net.*;
import java.io.*;


public class Client{

  public static void main(String[] args)
  {
    Socket soc = null;
    try {

      soc = new Socket("localhost", 1027);
      System.out.println("Port de communication côté serveur : " + soc.getPort());


      PrintWriter writer = null; // pour écrire au serveur
      BufferedReader networkIn = new BufferedReader( new InputStreamReader(soc.getInputStream())); // pour lire le serveur
      // pour lire l'entrée user
      BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));


      // message de bienvenue du serveur
      String msg = networkIn.readLine();
      System.out.println(msg);


      // liste des requetes possibles :
      String content = null;
      String requetes = "";
      while( (content = networkIn.readLine()) != null){
        requetes += content + "\n";
      }


      // on envoie des requetes au serveur
      boolean continuer = true;
      while(continuer)
      {
        System.out.println(requetes);

        continuer = false;

      }

      // Demande de connection en bonne et due forme
      // BufferedOutputStream bos = new BufferedOutputStream(soc.getOutputStream());
      // String message;
      // Scanner inFromUser = new Scanner(System.in);
      // System.out.print("\nVeuillez indiquer votre nom d'utilisateur (première connection)\nOu votre #token (pensez au #) :\n");
      // String sentence = inFromUser.nextLine();
      // message = Message.connect(sentence).messageToStr();
      // System.out.println(message);
      // bos.write(message.getBytes());
      // bos.flush();
      //
      // System.out.println("\nQuevoulez-vous faire ? (1 -> envoyer annonce, q -> déconnexion)\n");
      // String action = inFromUser.nextLine();
      // switch(action) {
      //   case "1":
      //     message = Message.postAnc().messageToStr();
      //     bos.write(message.getBytes());
      //     bos.flush();
      //     break;
      //   case "q":
      //     break;
      //   default :
      //     System.out.println("Action inconnue");
      //     break ;
      // }


      //System.out.println("message du serveur = " + content);

      soc.close();
    }
    catch (UnknownHostException e){
      // pour la soc 3 car toto n'existe pas
      e.printStackTrace();
    }
    catch (IOException e){
      e.printStackTrace();
    }
    finally
    {
      if(soc != null){
        try {
          soc.close();
        }
        catch (IOException e) {
          e.printStackTrace();
          soc = null;
        }
      }
    }

  }

}
