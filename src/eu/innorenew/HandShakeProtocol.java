package eu.innorenew;

public class HandShakeProtocol {

    public static void sendHandShake(Node node) {
        //System.out.println("Requesting handshake!");
        Message message = new Message(
                System.currentTimeMillis(),
                "handshake",
                "text",
                NetworkUtil.getLocalAddress() + " " + Main.nickname
        );
        node.send(CryptoUtil.signMessage(message));
    }

    public static String digestHandshake(Message message, Node node){
        //System.out.println("Digesting handshake");
        String[] body = message.getBody().split(" ");
        node.setAddress(body[0]);
        node.setNickname(body[1]);
        node.setPub_key(message.getPub_key());
        return message.getPub_key();
    }
}
