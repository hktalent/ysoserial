package ysoserial.test.util;

import ysoserial.payloads.util.Gadgets;

public class Test1 {
    public static void main(String[] args) {
        try {
            Class<?> clazz = Class.forName("ysoserial.payloads.templates.TomcatCmdEcho", false, Gadgets.class.getClassLoader());
        }catch (Exception e){e.printStackTrace();}
    }
}
