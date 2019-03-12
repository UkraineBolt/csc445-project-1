/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author alex
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String host = "pi.cs.oswego.edu";//change to server that client will run in.
        int PortNumber = 2691;

        Scanner s = new Scanner(System.in);
        System.out.println("UDP or TCP or quit");
        String input = s.nextLine();

        Socket tSocket = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        DatagramSocket dSocket = null;
        InetAddress address;
        boolean tcp = true;

        if (input.toUpperCase().contains("TCP")) {
            System.out.println("Running TCP");
            try {
                tSocket = new Socket(host, PortNumber);
                out = new DataOutputStream(tSocket.getOutputStream());
                in = new DataInputStream(tSocket.getInputStream());

            } catch (UnknownHostException e) {
                System.err.println("Don't know about host " + host);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection.");
                System.exit(1);
            }
        } else if (input.toUpperCase().contains("UDP")) {
            System.out.println("Running UDP");
            tcp = false;
            try {
                dSocket = new DatagramSocket();
            } catch (SocketException ex) {
                System.err.println("SocektException");
                System.exit(1);
            }
        } else {
            System.exit(1);
        }

        System.out.println("a1, a2 or a3");
        input = s.nextLine();
        if (tcp && input.contains("a1")) {

            System.out.println("Size of data");
            int dlen = s.nextInt();
            s.nextLine();
            byte[] data = new byte[dlen];
            try {
                out.writeInt(data.length);
                out.flush();
            } catch (IOException ex) {
            }

            System.out.println("trials");
            int len = s.nextInt();
            s.nextLine();

            try {
                out.writeInt(len);
                out.flush();
            } catch (IOException ex) {
            }

            System.out.println("time for x byte");
            for (int i = 0; i < len; i++) {//a1
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    out.flush();
                    long startTime = System.nanoTime();
                    out.write(data);
                    while (baos.size() < dlen) {
                        baos.write(in.readByte());
                    }
                    long endTime = System.nanoTime();
                    long rtt = endTime - startTime;
                    System.out.println(rtt);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("io error");
                }
            }

        } else if (tcp && input.contains("a3")) {
            try {
                System.out.println("Size: 1024, 512, 256");
                int size = s.nextInt();
                s.nextLine();
                int amount;
                if (size == 1024) {
                    amount = 1024;
                } else if (size == 512) {
                    amount = 2048;
                } else {//size==256
                    amount = 4096;
                }
                out.writeInt(size);
                out.flush();
                out.writeInt(amount);
                out.flush();

                System.out.println("trials:");
                int trials = s.nextInt();
                s.nextLine();

                out.writeInt(trials);
                out.flush();

                for (int i = 0; i < trials; i++) {
                    long startTime = System.nanoTime();
                    for (int j = 0; j < amount; j++) {
                        out.write(new byte[size]);
                        out.flush();
                    }
                    in.readByte();
                    long endTime = System.nanoTime();
                    System.out.println(endTime - startTime);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("stuff didnt close");
            }

        } else if (!tcp && input.contains("a1")) {
            try {
                address = InetAddress.getByName(host);

                System.out.println("data length: 1 64 1024");
                int len = s.nextInt();
                s.nextLine();
                
                System.out.println("trials");
                int trials = s.nextInt();
                s.nextLine();

                System.out.println("time for " + len + " byte");
                byte[] data = new byte[len];
                DatagramPacket spacket = new DatagramPacket(data, data.length, address, PortNumber);
                DatagramPacket rpacket = new DatagramPacket(data, data.length);
                for (int i = 0; i < trials; i++) {
                    long startTime = System.nanoTime();
                    dSocket.send(spacket);
                    dSocket.receive(rpacket);
                    long endTime = System.nanoTime();
                    long time = endTime - startTime;
                    System.out.print(time);
                    System.out.println("");
                }

            } catch (UnknownHostException ex) {
                System.out.println("unknown host");
            } catch (IOException ex) {

            }
        } else if (!tcp && input.contains("a3")) {
            try {
                address = InetAddress.getByName(host);
                
                System.out.println("data length: 1024 512 256");
                int len = s.nextInt();
                s.nextLine();
                
                int amount;
                if(len==1024){
                    amount=1024;
                }else if(len==512){ 
                    amount=2048;
                }else{//len==256
                    amount=4096;
                }
                
                System.out.println("trials");
                int trials = s.nextInt();
                s.nextLine();
                byte[] data = new byte[len];
                DatagramPacket spacket = new DatagramPacket(data, data.length, address, PortNumber);
                DatagramPacket rpacket = new DatagramPacket(data, data.length);
                for(int i=0;i<trials;i++){
                    long startTime = System.nanoTime();
                    for(int j=0;j<amount;j++){
                        dSocket.send(spacket);
                    }
                    dSocket.receive(rpacket);
                    long endTime = System.nanoTime();
                    long time = endTime - startTime;
                    System.out.print(time);
                    System.out.println("");
                }
                
                
            } catch (UnknownHostException ex) {
                System.out.println("unknown host");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("running a2");
            //1000,16000,64000,256000,1000000
            System.out.println("Size of data: 1000,16000,64000,256000 or 1000000");
            int dlen = s.nextInt();
            s.nextLine();
            byte[] data = new byte[dlen];
            try {
                out.writeInt(data.length);
                out.flush();
            } catch (IOException ex) {
            }

            System.out.println("trials");
            int len = s.nextInt();
            s.nextLine();

            try {
                out.writeInt(len);
                out.flush();
            } catch (IOException ex) {
            }

            System.out.println("throughput for " + dlen + " KBpns");
            for (int i = 0; i < len; i++) {//a2
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    out.flush();
                    long startTime = System.nanoTime();
                    out.write(data);
                    while (baos.size() < dlen) {
                        baos.write(in.readByte());
                    }
                    long endTime = System.nanoTime();
                    long rtt = endTime - startTime;
                    double sec = ((double) rtt * .000000001)/2.0;
                    double size = ((double) data.length * 8.0) / 1000000.0;

                    System.out.println(size / sec);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("io error");
                }
            }

        }
        try {
            System.out.println("done");
            if (tcp) {
                in.close();
                out.close();
                tSocket.close();
            } else {//udp
                dSocket.close();
            }
        } catch (IOException e) {
            System.out.println("stuff didnt close");
        }

    }

}
