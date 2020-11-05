


package eu.innorenew;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static HashMap<String,Node> peerSet = new HashMap<String, Node>();
    static ExecutorService executor = Executors.newCachedThreadPool();
    public static String nickname = "Domen";

    public static void main(String[] args) {
        CryptoUtil.generatePubPvtKeyPair();
        NetworkUtil.my_ip = NetworkUtil.getLocalAddress();
        System.out.println("Discovering local ip: " + NetworkUtil.my_ip);
        executor.submit(new InputHandler());
        try {
            Socket first = new Socket("192.168.43.65", 5000);
            Node n = new Node(first);
            executor.submit(n);
            System.out.println("Successfuly connected to initial node!");
        }catch (Exception e) {
            e.printStackTrace();
        }
        ServerSocket server = null;

        try {
            server = new ServerSocket(5000);

            while (true) {
                Socket client = server.accept();
                System.out.println("New connection accepted...");
                Node new_node = new Node(client);
                executor.submit(new_node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void addNode(Node node) {
        executor.submit(node);
    }
}
