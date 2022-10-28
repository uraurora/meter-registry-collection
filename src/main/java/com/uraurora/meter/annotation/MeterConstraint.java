package com.uraurora.meter.annotation;

import com.uraurora.meter.enums.MeterScope;

import java.lang.annotation.*;

/**
 * meter constraint
 * @author uraurora
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MeterConstraint {

    /**
     * 标记指标作用范围约束
     * @return 指标的作用范围
     */
    MeterScope scope() default MeterScope.JVM;

}
