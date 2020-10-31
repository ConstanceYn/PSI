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
      String content = "";
      String requetes = "";
      while( !content.equals(".") ){
        content = networkIn.readLine();
        requetes += content + "\n";
      }
      System.out.println("sorti du while de ces morts");
      //System.out.println(requetes);

      writer = new PrintWriter(soc.getOutputStream());

      // on envoie des requetes au serveur
      boolean continuer = true;
      while(continuer)
      {
        System.out.println(requetes);
        int action = userIn.read()-'0';
        //System.out.println(action);
        switch(action) {
          case 1: // Connection
            break;

          case 2: // poster une annonce
            String message = Message.postAnc().messageToStr();
            System.out.println(message);
            writer.println(message);
            writer.flush();

            break;
          // case 3: //modifier une annonce
          //   break;
          // case 4: // Suppimer une annonce
          //   break;
          // case 5: // Afficher les domaines
          //   break;
          case 6: // Afficher les annonces d'un domaine (version provisoire, toutes les annonces)
            writer.println("REQUEST_ANC\n.\n");
            writer.flush();

            break;
          // case 7: // Afficher ses annones
          //   break;

          default :
            System.out.println("Action inconnue");
            break ;
        }


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



      //System.out.println("message du serveur = " + content);
      //writer.close();
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
