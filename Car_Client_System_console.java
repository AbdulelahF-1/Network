
package finalProject370;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Car_Client_System_console {

	DataInputStream in;//
	DataOutputStream out;//
	Socket s;//

	public Car_Client_System_console() {
		try {
			s = new Socket("localhost", 5544);//
			in = new DataInputStream(s.getInputStream()); // to read
			out = new DataOutputStream(s.getOutputStream()); // to send
			String sn = in.readUTF();
			System.out.println("Welcome in " + sn);
			System.out.println("============================================================");
			System.out.println("Available Services");
			String req = "DISPLAY";
			try {
				out.writeUTF(req);//
				out.flush();//
				String res = in.readUTF();//
				String[] spliter = res.split("#");
				for (String s : spliter) {
					if (!s.equals("display"))
						System.out.println(s);
				}
			} catch (IOException ex) {
				Logger.getLogger(Car_Client_System_console.class.getName()).log(Level.SEVERE, null, ex);
			}
		} catch (UnknownHostException ex) {
			Logger.getLogger(Car_Client_System_console.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Car_Client_System_console.class.getName()).log(Level.SEVERE, null, ex);
		}

		while (true) {

			System.out.println("==================================================================");
			System.out.println("Enter the sevice you want please : buy  [OR]  check .. (exit to stop):");
			
			Scanner sc = new Scanner(System.in);

			String serv = sc.next();
			if (serv.equals("exit")) {
				break;
			}
			if (serv.equalsIgnoreCase("buy")) {
				System.out.println("Enter the item name: Tires || Oil || Spare ...");
				Scanner sc2 = new Scanner(System.in);
				String itName = sc2.next();
				System.out.println("Enter the quantity:");
				String q = sc2.next();
				if (!"".equals(itName) && !"".equals(q)) {
					try {
						int x = Integer.parseInt(q);
						if (x < 0){
							System.out.println("Quantity should be a positive number : ");
							
						}else{
						String bReq = "buy#" + itName + "#" + q;
						try {
							out.writeUTF(bReq);//
							out.flush();//
							String bRes = in.readUTF();//
							if (!bRes.contains("Sorry")) {
								System.out.println("Quantity " + q + " from item \"" + itName
										+ "\" is available. Total cost = " + bRes.split("#")[1] + "SR");

							} else {
								System.out.println(bRes);

							}
						} catch (IOException ex) {
							Logger.getLogger(Car_Client_System_console.class.getName()).log(Level.SEVERE, null, ex);
						}
						}
					} catch (NumberFormatException ex) {
						System.out.println("Quantity Should by a number");
					} catch (IllegalArgumentException ex) {
						System.out.println("Quantity Should by a number");
					}
					
				} else {
					System.out.println("You can not send empty values");
				}
			} else if (serv.equals("check")) {
				try {
					String cReq = "check";
					out.writeUTF(cReq);//
					out.flush();//
					String chRes = in.readUTF();//

					String[] iS = chRes.split("#");
					String[] pS = iS[iS.length - 1].split("@");
					String[] qS = pS[pS.length - 1].split("!");
					ArrayList<String> iL = new ArrayList<String>();
					ArrayList<Double> pL = new ArrayList<Double>();
					ArrayList<Integer> qL = new ArrayList<Integer>();
					for (int i = 1; i < iS.length - 1; i++) {
						iL.add(iS[i]);
					}
					for (int i = 0; i < pS.length - 1; i++) {
						pL.add(Double.parseDouble(pS[i]));

					}
					for (int i = 0; i < qS.length; i++) {
						qL.add(Integer.parseInt(qS[i]));

					}

					System.out.println("Items\tPrice\tQuantity\n");
					for (int i = 0; i < iL.size(); i++) {
						System.out.print(iL.get(i) + "\t" + pL.get(i) + "\t" + qL.get(i) + "\n");
					}
					System.out.println("========================================================");
				} catch (IOException ex) {
					Logger.getLogger(Car_Client_System_console.class.getName()).log(Level.SEVERE, null, ex);
				}
			} else {
				System.out.println("Sorry this service does not existed");
			}
		}
	}

	public static void main(String[] args) {
		new Car_Client_System_console();
	}
}
