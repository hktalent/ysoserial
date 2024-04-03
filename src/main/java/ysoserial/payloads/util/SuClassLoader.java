package ysoserial.payloads.util;

/**
 * @author su18
 */
public class SuClassLoader extends ClassLoader {

	public SuClassLoader() {
		super(Thread.currentThread().getContextClassLoader());
	}
}
