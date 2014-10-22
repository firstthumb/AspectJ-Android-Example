package android.mobile.peakgames.net.aspectjandroid;


public class Session {

    private String name;

    private static final Session INSTANCE = new Session();

    private Session() {
    }

    public static Session getInstance() {
        return INSTANCE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
