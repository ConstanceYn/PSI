import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
// la partie du client qui attend les communications
// comme MainServeur.java mais en udp
public class ClientUDP implements Runnable {
  private ArrayList<Connexion> contacts;
  private ArrayList<Message> messages;
  private boolean run;
  private DatagramSocket serveurSocket;

  public ClientUDP(DatagramSocket s) {
    contacts = new ArrayList<Connexion>();
    messages = new ArrayList<Message>(); // liste des messages dont on attend un ack
          // de la forme : MSG \n emetteur \n timestamp \n msg \n.
    run = true;
    serveurSocket = s;
  }


  public void stop(){
    System.out.println("j'ai dit stop");
    this.run = false;
  }

  // return la position dans l'array
  public boolean isContact(String nom){
    for (int i =0; i<contacts.size(); i++ ) {
      if(contacts.get(i).getNom().equals(nom))
      return true;
    }
    return false;
  }

  public InetAddress getIp(String nom){
    for (int i =0; i<contacts.size(); i++ ) {
      if(contacts.get(i).getNom().equals(nom))
      return contacts.get(i).getIp();
    }
    return null;
  }

  public void addContact(Connexion c){
    contacts.add(c);
  }


  public boolean waitAck(String timestamp){
    for (int i=0;i<messages.size() ;i++ ) {
      if(messages.get(i).getArgs()[1].equals(timestamp))
        return true;
    }
    return false;
  }

  public Message RemoveMessage(String timestamp){
    for (int i=0;i<messages.size() ;i++ ) {
      if(messages.get(i).getArgs()[1].equals(timestamp)){
        return( messages.remove(i) );
      }
    }
    return null;
  }

  public void addMessage(Message m){
    messages.add(m);
  }

  public void run() {
    int port = 7201;

    //DatagramSocket serveurSocket = new DatagramSocket(port);
    byte[] receiveData = new byte[1024];

    while(this.run){
      // attend des messages et les affiches

      // recevoir un message du client
      try {
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serveurSocket.receive(receivePacket);

        String sentence = new String(receivePacket.getData());
        System.out.println(sentence);

        // Adresse et port du contact
        InetAddress addr = receivePacket.getAddress();
        int portCo = receivePacket.getPort();
        Message message = Message.strToMessage(sentence);

        if (message.getType().equals("MSG")){
          // message[0] = emmetteur
          // message[1] = timestamp
          // message[2] = msg
          String nom = message.getArgs()[0];
          if( !this.isContact(nom))
          {
            //System.out.println(nom);
            Connexion newCo = new Connexion(nom, addr, portCo);
            this.addContact(newCo);
          }

          try{
            String nomFile = nom + ".txt";
            FileWriter fis = new FileWriter(nomFile, true);
            String str = nom + " : " +message.getArgs()[2] + "\n";
            fis.write(str);
            fis.close();

          }catch(Exception e){}

            // on construit l'ack avec le timestamp
            String ack = Message.msgAck(message.getArgs()[0], message.getArgs()[1]).messageToStr();
            byte[] sendData = ack.getBytes();
            System.out.println(ack);
            // et on l'envoie
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, addr, portCo);
            serveurSocket.send(sendPacket);
          }
          else if(message.getType().equals("MSG_ACK")){
            Message m = this.RemoveMessage(message.getArgs()[1]);
            String nom = m.getArgs()[0];

            if( !this.isContact(nom))
            {
              //System.out.println(nom);
              Connexion newCo = new Connexion(nom, addr, portCo);
              this.addContact(newCo);
            }

            try{
              String nomFile = nom + ".txt";
              FileWriter fis = new FileWriter(nomFile, true);
              String str ="moi : " +message.getArgs()[2] + "\n";
              fis.write(str);
              fis.close();

            }catch(Exception e){}

            }


      }catch(Exception e){
        //e.printStackTrace();
      }


    }
    System.out.println("fini ici aussi !!");
    serveurSocket.close();

  }

}
