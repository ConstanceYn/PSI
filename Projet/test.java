import java.util.Random;

public class test{

    public static void main(String[] args) {
      // TEST RANDOM

      // Random r = new Random();
      // int test = r.nextInt();
      // if(test < 0)
      // {
      //   System.out.println("negatif");
      //   test = -test;
      // }
      // System.out.println(test);


      // TEST IF STRING IS A INT
      String str = "ceci n'est pas un entier ";
      try {
        int i = Integer.parseInt(str);
        System.out.println("C'est un entier");
      } catch (Exception e) {
        System.out.println("Je ne suis pas un entier, et alors ca te derange ?");
      }

    }

}
