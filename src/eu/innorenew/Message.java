

package eu.innorenew;

public class Message {
    private long timestamp;
    private String header;
    private String contentType;
    private String body;
    private String pub_key;
    private String signature;

    public Message(long timestamp, String header, String contentType, String body, String pub_key, String signature) {
        this.timestamp = timestamp;
        this.header = header;
        this.contentType = contentType;
        this.body = body;
        this.pub_key = pub_key;
        this.signature = signature;
    }

    public Message(long timestamp, String header, String contentType, String body) {
        this.timestamp = timestamp;
        this.header = header;
        this.contentType = contentType;
        this.body = body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPub_key() {
        return pub_key;
    }

    public void setPub_key(String pub_key) {
        this.pub_key = pub_key;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
