import java.io.*;
import java.net.*;
import java.util.Scanner;


public class UDPServeur {

    public static void main(String args[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] receiveData = new byte[1024];

        while(true)
        {
            // recevoir un message du client
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData());
            System.out.println(sentence);

            // afficher le port et la source du message :
            InetAddress addr =  receivePacket.getAddress();
            int port = receivePacket.getPort();

            System.out.println(">> adresse du client : " + addr.getHostName() + "\n >> port du client : " + port);



            // envoyer un message au client
            Scanner inFromUser = new Scanner(System.in);
            byte[] sendData = new byte[1024];
            String reponse = inFromUser.nextLine();
            sendData = reponse.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, addr, port);
            serverSocket.send(sendPacket);

        }
    }

}

// Quand un client envoie un message long puis un court,
// le message court est écrit sur le message long

// Exemple :
// bonjour je suis un message long, très long
// et moi court !s un message long, très long
