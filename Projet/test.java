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
import java.lang.Runtime;


public class test{

    public static void main(String[] args) {
      // test ouverture d'un terminal

      Process proc = null;
      try {
        String[] cmdss= {"gnome-terminal","-e", "java Envoi"};
        // String[] cmdss= {"ttab", "java Envoi"};
        proc = Runtime.getRuntime().exec(cmdss);
      } catch (IOException e) {
        e.printStackTrace();
      }

    }

}
