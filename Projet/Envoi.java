import java.net.*;
import java.io.*;
import java.util.Scanner;


public class Envoi {

  public static void main(String[] args) {
    System.out.println("bonjour sur cette nouvelle conversation");

    // création d'un objet connexion
    // lancement du thread pour écouter les messages

    // while()
    // on lit les données sur le terminal et on les envoie
    System.out.print(">> ");
    Scanner sc = new Scanner(System.in);
    String test = sc.nextLine();
    System.out.println(test);


    // provisoire pour qu'on voit l'affichage
    try {
      Thread.sleep(1000);
    }catch(InterruptedException e){
      // à cause du sleep
    }

  }
}
