package eu.innorenew;

import java.util.HashMap;

public class ChatProtocol {
    static HashMap<String, Message> messageLog = new HashMap<>();

    public static synchronized void digest(Message message, Node node) {
        if (!messageLog.containsKey(message.getSignature())) {
            Node origin = Main.peerSet.get(message.getPub_key());
            System.out.println("\033[42m   \033[0m " + origin.getNickname() + ": \033[1;35m" + message.getBody() + "\033[0m");
            broadcast(message, node);
        }

    }



    public static void broadcast(Message message, Node node) {
        messageLog.put(message.getSignature(), message);
        for (Node n : Main.peerSet.values()) {
            if(!n.equals(node)) {
                n.send(message);
            }
        }
    }

}
