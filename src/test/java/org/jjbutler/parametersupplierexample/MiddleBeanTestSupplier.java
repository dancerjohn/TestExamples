package org.jjbutler.parametersupplierexample;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

import org.jjbutler.parametersupplierexample.MiddleBean.State;
import org.junit.Ignore;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.ParametersSuppliedBy;
import org.junit.experimental.theories.PotentialAssignment;

import com.google.common.base.Function;

@Ignore
public class MiddleBeanTestSupplier extends ParameterSupplier {

	@Retention(RetentionPolicy.RUNTIME)
	@ParametersSuppliedBy(MiddleBeanTestSupplier.class)
	public static @interface SingleMiddleBean {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@ParametersSuppliedBy(MiddleBeanTestSupplier.class)
	public static @interface MultipleMiddleBeans {
		int count() default 2;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@ParametersSuppliedBy(MiddleBeanTestSupplier.class)
	public static @interface MiddleBeansWithState {
		State[] states() default { State.State1 };
	}

	public static final MiddleBean.State DEFAULT_STATE = MiddleBean.State.State1;

	public static MiddleBean createMiddleBean() {
		return createMiddleBean(DEFAULT_STATE, BottomBeanTestSupplier.createBottomBean());
	}

	public static MiddleBean createMiddleBean(State state, BottomBean... beans) {
		return createMiddleBeanLocal(state, Arrays.asList(beans));
	}

	private static MiddleBean createMiddleBeanLocal(State state, List<BottomBean> bottomBeans) {
		MiddleBean bean = new MiddleBean();
		bean.setState(state);
		bean.setBottomBeans(bottomBeans);
		return bean;
	}

	public static List<MiddleBean> createMiddleBeans(ParameterSignature sig) {
		List<MiddleBean> beans = newArrayList();
		List<BottomBean> bottomBeans = BottomBeanTestSupplier.createBottomBeans(sig);

		if (sig.getAnnotation(MiddleBeansWithState.class) != null) {
			MiddleBeansWithState middleBeansWithState = sig.getAnnotation(MiddleBeansWithState.class);
			for (State state : middleBeansWithState.states()) {
				beans.add(createMiddleBeanLocal(state, bottomBeans));
			}
		} else {
			int count = 1;

			if (sig.getAnnotation(MultipleMiddleBeans.class) != null) {
				MultipleMiddleBeans multipleMiddleBean = sig.getAnnotation(MultipleMiddleBeans.class);
				count = multipleMiddleBean.count();
			}

			for (int i = 0; i < count; i++) {
				beans.add(createMiddleBeanLocal(DEFAULT_STATE, bottomBeans));
			}
		}

		return beans;
	}

	private static final Function<MiddleBean, PotentialAssignment> toPotentialAssignment =
			new Function<MiddleBean, PotentialAssignment>() {
				@Override
				public PotentialAssignment apply(MiddleBean bean) {
					return PotentialAssignment.forValue(bean.getState().name(), bean);
				}
			};

	@Override
	public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
		List<MiddleBean> beans = createMiddleBeans(sig);
		return newArrayList(transform(beans, toPotentialAssignment));
	}

}
