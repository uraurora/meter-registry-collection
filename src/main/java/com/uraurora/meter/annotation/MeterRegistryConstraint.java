package com.uraurora.meter.annotation;

import com.uraurora.meter.enums.MeterPublishType;
import com.uraurora.meter.enums.MeterScope;

import java.lang.annotation.*;

/**
 * @author gaoxiaodong
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface MeterRegistryConstraint {

    /**
     * Meter-Registry Constraint
     * @return scope
     */
    MeterScope[] scope() default {MeterScope.ALL};

    /**
     * publish type,see{@link MeterPublishType}
     * @return publish type
     */
    MeterPublishType publishType() default MeterPublishType.PUSH;

}
