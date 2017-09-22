package org.jjbutler.buildersupplierexample;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.stream.Collectors;

import org.jjbutler.parametersupplierexample.MiddleBean;
import org.jjbutler.parametersupplierexample.MiddleBean.State;
import org.junit.Ignore;

import lombok.Setter;
import lombok.experimental.Accessors;

@Ignore
@Setter
@Accessors(chain=true, fluent=true)
public class MiddleBeanTestSupplier {

	public static final MiddleBean.State DEFAULT_STATE = MiddleBean.State.State1;

	public static MiddleBeanTestSupplier emptyMiddleBean() {
		return new MiddleBeanTestSupplier();
	}
	public static MiddleBeanTestSupplier populatedMiddleBean() {
		return new MiddleBeanTestSupplier()
				.state(DEFAULT_STATE)
				.bottomBeans(newArrayList(BottomBeanTestSupplier.populatedBottomBean()));
	}

	private State state;
	private List<BottomBeanTestSupplier> bottomBeans;

	public MiddleBean build() {
		MiddleBean bean = new MiddleBean();
		bean.setState(state);
		bean.setBottomBeans(bottomBeans.stream()
				.map(BottomBeanTestSupplier::build)
				.collect(Collectors.toList()));
		return bean;
	}
}
