package ir.urmia;

import java.util.concurrent.atomic.AtomicInteger;

public class Document {
    private static final AtomicInteger lastId = new AtomicInteger(0);

    private int docId;
    private String name;
    private String body;

    public Document( String name, String body) {
        this.docId = lastId.incrementAndGet();
        this.name = name;
        this.body = body;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
