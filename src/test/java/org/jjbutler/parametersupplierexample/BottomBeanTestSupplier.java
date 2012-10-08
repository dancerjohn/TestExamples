package org.jjbutler.parametersupplierexample;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import org.junit.Ignore;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.ParametersSuppliedBy;
import org.junit.experimental.theories.PotentialAssignment;

import com.google.common.base.Function;

@Ignore
public class BottomBeanTestSupplier extends ParameterSupplier {

	@Retention(RetentionPolicy.RUNTIME)
	@ParametersSuppliedBy(BottomBeanTestSupplier.class)
	public static @interface SingleValidBottomBean {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@ParametersSuppliedBy(BottomBeanTestSupplier.class)
	public static @interface MultipleValidBottomBean {
		int count() default 2;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@ParametersSuppliedBy(BottomBeanTestSupplier.class)
	public static @interface InvalidBottomBean {
	}

	public static final String DEFAULT_FIELD1 = "defField1";
	public static final int DEFAULT_VALUE = 123;

	public static BottomBean createBottomBean() {
		return createBottomBean(DEFAULT_FIELD1, DEFAULT_VALUE);
	}

	public static BottomBean createBottomBean(String field1, int value) {
		return createBottomBeanLocal(field1, value);
	}

	private static BottomBean createBottomBeanLocal(String field1, int value) {
		BottomBean bean = new BottomBean();
		bean.setField1(field1);
		bean.setValue(value);
		return bean;
	}

	public static List<BottomBean> createBottomBeans(ParameterSignature sig) {
		List<BottomBean> beans = newArrayList();

		if (sig.getAnnotation(InvalidBottomBean.class) != null) {
			beans.add(createBottomBean("blah", -123));
		} else {
			int count = 1;

			if (sig.getAnnotation(MultipleValidBottomBean.class) != null) {
				MultipleValidBottomBean multipleValidBottomBean = sig.getAnnotation(MultipleValidBottomBean.class);
				count = multipleValidBottomBean.count();
			}

			for (int i = 0; i < count; i++) {
				beans.add(createBottomBean(DEFAULT_FIELD1 + i, DEFAULT_VALUE + i));
			}
		}

		return beans;
	}

	private static final Function<BottomBean, PotentialAssignment> toPotentialAssignment =
			new Function<BottomBean, PotentialAssignment>() {
				@Override
				public PotentialAssignment apply(BottomBean bean) {
					return PotentialAssignment.forValue(bean.getField1(), bean);
				}
			};

	@Override
	public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
		List<BottomBean> beans = createBottomBeans(sig);
		return newArrayList(transform(beans, toPotentialAssignment));
	}

}
