package eu.innorenew;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class PeerDiscoveryProtol {

    public static void requestPeers(Node node) {
       // System.out.println("Requesting peers!");
        Message message = new Message(
                System.currentTimeMillis(),
                "peer_discovery",
                "text",
                "request"
        );
        node.send(CryptoUtil.signMessage(message));
    }

    public static void digest(Message message, Node node) {
        //System.out.println("Digesting peer request!");
        Gson g = new Gson();
        if(message.getBody().equals("request")) {
            //String body = g.toJson(Main.peerSet.values().stream().map(n -> n.getAddress()).collect(Collectors.toList()));
            String[] peers = new String[Main.peerSet.size()];
            int count = 0;
            for (Node n : Main.peerSet.values()) {
                peers[count] = n.getAddress() + " " + n.getPub_key();
                count++;
            }
            String body = g.toJson(peers);
            Message reply = new Message(
                    System.currentTimeMillis(),
                    "peer_discovery",
                    "json",
                    body
            );
            node.send(CryptoUtil.signMessage(message));
        } else {
            String[] newPeers = g.fromJson(message.getBody(), String[].class);
            //System.out.println("Recieved peer list!");
            for (int i = 0 ; i < newPeers.length ; i++) {
                String[] info = newPeers[i].split(" ");

                if(!Main.peerSet.containsKey(info[1]) && !NetworkUtil.my_ip.equals(info[0])) {
                    System.out.println(newPeers[i]);
                    System.out.println("new peer is: " + info[0]);
                    try {
                        Socket newPeer = new Socket(info[0], 5000);
                        Node newNode = new Node(newPeer);
                        Main.addNode(newNode);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
