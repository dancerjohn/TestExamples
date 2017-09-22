package org.jjbutler.buildersupplierexample;

import org.jjbutler.parametersupplierexample.BottomBean;
import org.junit.Ignore;

import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This pattern is primarily for immutable classes
 */
@Ignore
@Setter
@Accessors(chain=true, fluent=true)
public class BottomBeanTestSupplier  {

	public static final String DEFAULT_FIELD1 = "defField1";
	public static final int DEFAULT_VALUE = 123;
	
	public static BottomBeanTestSupplier emptyBottomBean(){
		return new BottomBeanTestSupplier();
	}

	public static BottomBeanTestSupplier populatedBottomBean() {
		return createBottomBean(DEFAULT_FIELD1, DEFAULT_VALUE);
	}

	public static BottomBeanTestSupplier createBottomBean(String field1, int value) {
		return new BottomBeanTestSupplier()
				.field1(field1)
				.value(value);
	}

	private String field1;
	private int value;

	public BottomBean build() {
		BottomBean bean = new BottomBean();
		bean.setField1(field1);
		bean.setValue(value);
		return bean;
	}
}
