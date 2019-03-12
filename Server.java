/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//THIS IS THE DAMN SERVER REMEMBER THIS.
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author alex
 */
public class Server {

    static final int PORT = 2691;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Boolean tcp = true;
        try {
            ServerSocket serverSocket = null;
            Scanner s = new Scanner(System.in);
            Socket client = null;
            DataInputStream in = null;
            DataOutputStream out = null;
            DatagramSocket dSocket = null;

            System.out.println("UDP or TCP or quit");
            String input = s.nextLine();

            if (input.toUpperCase().contains("TCP")) {
                tcp = true;
                System.out.println("running TCP");
                serverSocket = new ServerSocket(PORT);
                client = serverSocket.accept();
                in = new DataInputStream(client.getInputStream());
                out = new DataOutputStream(client.getOutputStream());
            } else if (input.toUpperCase().contains("UDP")) {
                tcp = false;
                System.out.println("Running UDP");
                dSocket = new DatagramSocket(PORT);
            } else {
                System.exit(1);
            }

            System.out.println("a1, a2, a3");
            input = s.nextLine();
            if (tcp && input.contains("a1")) {
                System.out.println("running a1 with tcp");
                int length = 0;
                for (;;) {
                    length = in.readInt();
                    System.out.println(length);
                    if (length != 0) {
                        break;
                    }
                }
                int trials = 0;
                int temp = 0;
                for (;;) {
                    trials = in.readInt();
                    System.out.println(trials);
                    if (trials != 0) {
                        break;
                    }
                }

                for (;;) {
                    if (trials == temp) {
                        break;
                    } else {
                        temp++;
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while (baos.size() < length) {
                        baos.write(in.readByte());
                    }
                    out.write(baos.toByteArray(), 0, length);
                }

                in.close();
                out.close();
                serverSocket.close();

            } else if (tcp && input.contains("a3")) {
                System.out.println("running a3 with tcp");
                ArrayList<ByteArrayOutputStream> data = new ArrayList();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int size;
                for (;;) {
                    size = in.readInt();
                    System.out.println(size);
                    if (size != 0) {
                        break;
                    }
                }

                int amount;
                for (;;) {
                    amount = in.readInt();
                    System.out.println(amount);
                    if (amount != 0) {
                        break;
                    }
                }

                int trials;
                for (;;) {
                    trials = in.readInt();
                    System.out.println(trials);
                    if (amount != 0) {
                        break;
                    }
                }
                int temp = 0;
                for (;;) {
                    out.flush();
                    if (trials == temp) {
                        break;
                    } else {
                        temp++;
                    }

                    for (int i = 0; i < amount; i++) {
                        while (baos.size() < size) {
                            baos.write(in.readByte());
                        }
                        data.add(baos);
                        baos.reset();
                    }
                    out.write(new byte[1]);
                    out.flush();

                }

                in.close();
                out.close();
                serverSocket.close();

            } else if (!tcp && input.contains("a1")) {
                System.out.println("running a1 with udp");
                System.out.println("data length: 1 64 1024");
                int len = s.nextInt();
                s.nextLine();

                System.out.println("Trials");
                int trials = s.nextInt();
                s.nextLine();

                for (int i = 0; i < trials; i++) {
                    byte[] data = new byte[len];
                    DatagramPacket receiver = new DatagramPacket(data, data.length);
                    dSocket.receive(receiver);
                    DatagramPacket sender = new DatagramPacket(receiver.getData(), receiver.getLength(), receiver.getAddress(), receiver.getPort());
                    dSocket.send(sender);
                }
                dSocket.close();

            } else if (!tcp && input.contains("a3")) {
                System.out.println("running a3 with udp");

                System.out.println("data length: 1024 512 256");
                int len = s.nextInt();
                s.nextLine();

                int amount;
                if (len == 1024) {
                    amount = 1024;
                } else if (len == 512) {
                    amount = 2048;
                } else {//len==256
                    amount = 4096;
                }

                System.out.println("trials");
                int trials = s.nextInt();
                s.nextLine();
                int temp = 0;
                for (;;) {
                    if (temp == trials) {
                        break;
                    } else {
                        temp++;
                    }
                    byte[] data = new byte[len];
                    byte[] com = new byte[1];
                    DatagramPacket receiver = new DatagramPacket(data, data.length);
                    for (int i = 0; i < amount; i++) {
                        dSocket.receive(receiver);
                    }
                    DatagramPacket sender = new DatagramPacket(com, com.length, receiver.getAddress(), receiver.getPort());
                    dSocket.send(sender);
                }
                dSocket.close();

            } else {
                System.out.println("Running A2 with tcp");
                int length = 0;
                for (;;) {
                    length = in.readInt();
                    System.out.println(length);
                    if (length != 0) {
                        break;
                    }
                }
                int trials = 0;
                int temp = 0;
                for (;;) {
                    trials = in.readInt();
                    System.out.println(trials);
                    if (trials != 0) {
                        break;
                    }
                }

                for (;;) {
                    out.flush();
                    if (trials == temp) {
                        break;
                    } else {
                        temp++;
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while (baos.size() < length) {
                        baos.write(in.readByte());
                    }
                    out.write(baos.toByteArray(), 0, length);
                }

                in.close();
                out.close();
                serverSocket.close();
            }

        } catch (EOFException ef) {
            ef.printStackTrace();
            System.out.println("client is assumed to be disconnected");

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("IO Exception???");
        }

    }

}
