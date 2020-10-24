import java.net.*;
import java.util.Scanner;


public class UDPClient {
    public static void main(String[] args) throws Exception {
        // se connecter
        Scanner inFromUser = new Scanner(System.in);
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");

        // envoyer un message au serveur
        byte[] sendData = new byte[1024];
        String sentence = inFromUser.nextLine();
        sendData = sentence.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        clientSocket.send(sendPacket);

        // recevoir un message du serveur
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        // afficher le message
        String reponse = new String(receivePacket.getData());
        System.out.println(reponse);
    }
}
