package org.jjbutler.buildersupplierexample;

import org.jjbutler.parametersupplierexample.TopBean;
import org.junit.Ignore;

import lombok.Setter;
import lombok.experimental.Accessors;

@Ignore
@Setter
@Accessors(chain=true, fluent=true)
public class TopBeanTestSupplier {

	public static final String DEFAULT_FIELD1 = "topField1";

	public static TopBeanTestSupplier emptyTopBean() {
		return new TopBeanTestSupplier();
	}

	public static TopBeanTestSupplier populatedTopBean() {
		return new TopBeanTestSupplier()
				.field1(DEFAULT_FIELD1)
				.middleBean(MiddleBeanTestSupplier.populatedMiddleBean());
	}

	private String field1;
	private MiddleBeanTestSupplier middleBean;

	public  TopBean build() {
		TopBean bean = new TopBean();
		bean.setField1(field1);
		bean.setMiddleBean(middleBean.build());
		return bean;
	}

}
