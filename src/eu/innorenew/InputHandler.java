package eu.innorenew;

import java.util.Scanner;
import java.util.StringTokenizer;

public class InputHandler implements Runnable {

    Scanner input;

    @Override
    public void run() {
        input = new Scanner(System.in);
        String line;
        while((line = input.next()) != null) {
            if (line.startsWith("|")) {
                StringTokenizer st = new StringTokenizer(line);
                String command = st.nextToken();
                //String param = st.nextToken();
                if (command.equalsIgnoreCase("|list_nodes")) {
                    for (Node n : Main.peerSet.values()) {
                        System.out.println("Node: " + n.getPub_key().substring(10,20));
                        System.out.println("Address: " + n.getAddress());
                        System.out.println();
                    }
                }
            } else {
                //normal message broadcast
                Message message = new Message(
                        System.currentTimeMillis(),
                        "chat_protocol",
                        "text",
                        line
                );
                ChatProtocol.broadcast(CryptoUtil.signMessage(message), null);
            }
        }
    }
}
