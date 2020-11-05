


package eu.innorenew;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class Node implements Runnable {
    private Socket connection;
    BufferedWriter out;
    BufferedReader in;
    Gson gson;



    String pub_key;
    String address;
    String nickname;
    boolean is_running = true;

    public Node(Socket client) {
        this.connection = client;
        gson = new Gson();
        try {
            out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HandShakeProtocol.sendHandShake(this);
    }

    @Override
    public void run() {
        String line;
        try {
            while (is_running && (line = in.readLine()) != null) {
                //System.out.println(line);
                Message m = gson.fromJson(line, Message.class);
                if(CryptoUtil.verfyMessage(m)) {
                    switch (m.getHeader()) {
                        case "handshake":
                            System.out.println("Handshake");
                            this.pub_key = HandShakeProtocol.digestHandshake(m, this);
                            PeerDiscoveryProtol.requestPeers(this);
                            Main.peerSet.put(this.pub_key, this);
                            if (this.getPub_key() == null) {
                                HandShakeProtocol.sendHandShake(this);
                            }
                            break;

                        case "peer_discovery":
                            PeerDiscoveryProtol.digest(m, this);
                            break;

                        case "chat_protocol":
                            ChatProtocol.digest(m, this);
                            break;
                        default:
                            System.out.println("Received message from: " + m.getPub_key());
                            System.out.println("Signature: " + m.getSignature());
                            break;
                    }
                } else {
                    System.out.println("MITM attack: Forged message!");
                }
            }
        } catch (IOException e) {
            System.out.println("Socket broke");
        }
    }
    public void send(Message m){
        try {
            out.write(gson.toJson(m));
            out.newLine();
            out.flush();
        } catch (IOException e) {
            try {
                out.close();
                in.close();
                connection.close();
                Main.peerSet.remove(this.getPub_key());
                this.is_running = false;
                System.out.println("Connection broken! Peer removed!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public String getPub_key() {
        return pub_key;
    }

    public void setPub_key(String pub_key) {
        this.pub_key = pub_key;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}






