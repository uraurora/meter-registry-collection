package com.uraurora.meter.enums;

/**
 * tag{@link io.micrometer.core.instrument.Meter}scope
 * @author gaoxiaodong
 */
public enum MeterScope {

    /**
     * docker or other container scope
     */
    CONTAINER,

    /**
     * java virtual machine scope
     */
    JVM,

    /**
     * thread scope
     */
    THREAD,

    /**
     * all
     */
    ALL
    ;
}

