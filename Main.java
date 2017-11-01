import java.util.Scanner;
public class Main {
   public static void main(String[] args) {
      CubbyHole c = new CubbyHole();
      Client p1 = new Client(c, 2);
      Server c1 = new Server(c, 2);
      p1.start(); 
      c1.start();
   }
}
class CubbyHole {
   private char contents;
   private boolean available = false;
   
   public synchronized char get() {
      while (available == false) {
         try {
            wait();
         } catch (InterruptedException e) {}
      }
      available = false;
      notifyAll();
      return contents;
   }
   public synchronized void put(char value) {
      while (available == true) {
         try {
            wait();
         } catch (InterruptedException e) { } 
      }
      contents = value;
      available = true;
      notifyAll();
   }
}
class Server extends Thread {
   private CubbyHole cubbyhole;
   private int number;
   
   public Server(CubbyHole c, int number) {
      cubbyhole = c;
      this.number = number;
   }
   public void run() {
      char value = 0;
      for (int i = 0; i < 10; i++) {
         value = cubbyhole.get();
         System.out.println("Server #" + this.number + " got: " + value);
      }
   }
}
class Client extends Thread {
   private CubbyHole cubbyhole;
   Scanner scanner = new Scanner(System.in);
   private int number;
   public Client(CubbyHole c, int number) {
      cubbyhole = c;
      this.number = number;
   } 
   public void run() {
      System.out.println("Enter stream of numbers");
      String data = scanner.next();
      char[] a = data.toCharArray();
      int i=0;
      /*while(number!=0)
      {
          int r = number%10;
          a[i++]=r;
          number=number/10;
      }*/
      int l = data.length();
      for (i = 0; i < l; i++) {
          char temp = a[i];
         cubbyhole.put(temp);
         System.out.println("Client #" + this.number + " put: " + temp);
         try {
            sleep((int)(Math.random() * 100));
         } catch (InterruptedException e) { }
      } 
   }
}
