package javax.jcr;

import java.util.HashMap;

public final class SimpleCredentials implements javax.jcr.Credentials {
    private  String userID;
    private  char[] password;
    private  HashMap attributes = new HashMap();

    public SimpleCredentials(){}
    public SimpleCredentials(String userID, char[] password) {
        this.userID = userID;
        this.password = (char[])((char[])password.clone());
    }
    public void setAttribute(String name, Object value) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        } else if (value == null) {
        } else {
            synchronized(this.attributes) {
                this.attributes.put(name, value);
            }
        }
    }
}
