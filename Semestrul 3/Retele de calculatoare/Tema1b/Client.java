import java.net.*;
import java.io.*;
 
public class Client {
 
public static void main(String args[]) throws Exception {
  if(args.length<2){
	  System.out.println("Introduceti portul si adresa ip a serverlui");
	  return;
  }

  Socket c = new Socket(args[1],Integer.parseInt(args[0]));
  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  int l,i;
  System.out.print("l = ");
  l = Integer.parseInt(reader.readLine());
  System.out.print("i = ");
  i = Integer.parseInt(reader.readLine());
  System.out.println("Introduceti un sir:");
  String text=reader.readLine();
  
  DataInputStream socketIn = new DataInputStream(c.getInputStream());
  DataOutputStream socketOut = new DataOutputStream(c.getOutputStream());
  
  byte[] textBytes=text.getBytes();
  int textLen=textBytes.length;

  socketOut.writeShort(textLen);
  socketOut.write(textBytes);
  socketOut.writeShort(l);
  socketOut.writeShort(i);
  socketOut.flush();

  byte[] result=new byte[100];
  int n = socketIn.read(result);
  if(n>0){
	  String response = new String(result,0,n);
  	  System.out.println(response);
  }
  reader.close();
  c.close();  
}
 
}
