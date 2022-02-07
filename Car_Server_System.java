
package finalProject370;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Car_Server_System {


    public static void main(String[] args) {
        try { 
            String serviceName = "Car Service";
            System.out.println("Please enter the available services(exit to stop)");
            ArrayList<String> services = new ArrayList<String>(); //
            Scanner sc = new Scanner(System.in); //
            while(true){
                String serv = sc.next();
                serv.toUpperCase();
                if(serv.equalsIgnoreCase("exit")){
                    break;
                }
                services.add(serv);
            }
            System.out.println("Please enter the available Items(exit to stop)");
            ArrayList<String> items = new ArrayList<String>();
            Scanner sc2 = new Scanner(System.in);
            while(true){
                String it = sc2.nextLine();
                it.toUpperCase();
                if(it.equalsIgnoreCase("exit")){
                    break;
                }
                items.add(it);
            }
             System.out.println("Please enter the price of Items(0 to stop)");
            ArrayList<Double> prices = new ArrayList<Double>();
            Scanner sc3 = new Scanner(System.in);
            while(true){
                double pr = sc3.nextDouble();
                if(pr == 0){
                    break;
                }
                prices.add(pr);
            }
            
            
            System.out.println("Please enter the available quantities of Items(-1 to stop)");
            ArrayList<Integer> q = new ArrayList<Integer>();
            Scanner sc4 = new Scanner(System.in);
            while(true){
                int qu = sc4.nextInt();
                if(qu == -1){
                    break;
                }
                q.add(qu);
            }
            
            ServerSocket ss = new ServerSocket(5544); //start
            System.out.println("Server is running...");
            while(true){
                Socket s = ss.accept();//
                
                DataInputStream in = new DataInputStream(s.getInputStream()); // to receive 
                DataOutputStream out = new DataOutputStream(s.getOutputStream()); // to send
                out.writeUTF("Welcome in our service: " + serviceName);//
                while(true){
                    String request = in.readUTF();//
                    if(request.equals("DISPLAY")){
                        String dResponse = "display#";
                        for(String service:services){
                            dResponse += service + "#";
                        }
                        out.writeUTF(dResponse);//
                        out.flush();//
                    }
                    else if(request.equalsIgnoreCase("check")){
                        String chRequest = "ch#";
                        for(String item:items){
                            chRequest += item + "#";
                        }
                        for(double pr:prices){
                            chRequest += pr + "@";
                        }
                        for(int quan:q){
                            chRequest += quan + "!";
                        }
                        out.writeUTF(chRequest);//
                        out.flush();//
                    }
                    else if(request.contains("buy")){
                        String itemName = request.split("#")[1];
                        int itemQ = Integer.parseInt(request.split("#")[2]);
                        int itemIndex = 0;
                        for(String item:items){
                            if(item.equals(itemName)){
                                break;
                            }
                            itemIndex++;
                        }
                        if(!items.contains(itemName)){
                            out.writeUTF("Sorry Enter an existed item");//
                            out.flush();//
                        }
                        else{
                        double iPrice = prices.get(itemIndex);
                        if(itemQ>q.get(itemIndex)){
                             out.writeUTF("Sorry this quantity is not available");//
                              out.flush();//
                        }
                        else{
                        double totalPrice = iPrice * itemQ;
                        out.writeUTF("buy#"+totalPrice);//
                        out.flush();//
                        out.flush();//
                        int qq = q.get(itemIndex);
                        q.remove(itemIndex);
                        q.add(itemIndex, qq-itemQ);
                        }
                        }
                        
                    }
                    
                }
            }
          
        } catch (IOException ex) {
           System.out.println(ex.getMessage());
        }
    }
    
}
