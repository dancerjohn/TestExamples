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
public class TopBeanTestSupplier extends ParameterSupplier {

	@Retention(RetentionPolicy.RUNTIME)
	@ParametersSuppliedBy(TopBeanTestSupplier.class)
	public static @interface DefaultTopBean {
	}

	public static final String DEFAULT_FIELD1 = "topField1";

	public static TopBean createTopBean() {
		return createTopBeanLocal(DEFAULT_FIELD1, MiddleBeanTestSupplier.createMiddleBean());
	}

	public static TopBean createTopBean(String field1, MiddleBean middleBean) {
		return createTopBeanLocal(field1, middleBean);
	}

	private static TopBean createTopBeanLocal(String field1, MiddleBean middleBean) {
		TopBean bean = new TopBean();
		bean.setField1(field1);
		bean.setMiddleBean(middleBean);
		return bean;
	}

	public static List<TopBean> createTopBeans(ParameterSignature sig) {
		List<TopBean> beans = newArrayList();
		List<MiddleBean> middleBeans = MiddleBeanTestSupplier.createMiddleBeans(sig);

		for (MiddleBean middleBean : middleBeans) {
			beans.add(createTopBeanLocal(DEFAULT_FIELD1 + middleBean.getState().name(), middleBean));
		}

		return beans;
	}

	private static final Function<TopBean, PotentialAssignment> toPotentialAssignment =
			new Function<TopBean, PotentialAssignment>() {
				@Override
				public PotentialAssignment apply(TopBean bean) {
					return PotentialAssignment.forValue(bean.getField1(), bean);
				}
			};

	@Override
	public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
		List<TopBean> beans = createTopBeans(sig);
		return newArrayList(transform(beans, toPotentialAssignment));
	}

}
