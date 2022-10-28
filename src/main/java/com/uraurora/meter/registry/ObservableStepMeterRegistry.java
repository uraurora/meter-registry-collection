package com.uraurora.meter.registry;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.core.instrument.step.StepRegistryConfig;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

import java.util.concurrent.ThreadFactory;
import java.util.function.Supplier;

/**
 * @author : uraurora
 * @program : meter-registry-collection
 * @date : 2022-10-27 17:32
 * @description :
 */
public abstract class ObservableStepMeterRegistry extends StepMeterRegistry {

    private final Logger logger = LoggerFactory.getLogger(ObservableStepMeterRegistry.class);

    /**
     * observable registry
     */
    protected final ObservationRegistry observationRegistry = ObservationRegistry.create();

    /**
     * config
     */
    protected final StepRegistryConfig config;

    /**
     * stepping thread factory
     */
    protected static final ThreadFactory DEFAULT_THREAD_FACTORY = new NamedThreadFactory("observable-metrics-publisher");

    public ObservableStepMeterRegistry(StepRegistryConfig config, Clock clock) {
        super(config, clock);
        this.config = config;
    }

    public ObservationRegistry getObservationRegistry() {
        return observationRegistry;
    }


    @Override
    public void start(ThreadFactory threadFactory) {
        ThreadFactory tf = threadFactory == null ? DEFAULT_THREAD_FACTORY : threadFactory;
        if (config.enabled()) {
            super.start(tf);
        }
    }

    public void start0() {
        start(DEFAULT_THREAD_FACTORY);
    }

    protected  <T> T observableAction(String observationName, Supplier<T> supplier) {
        final Observation.Context context = new Observation.Context();
        final Observation observation = Observation.start(observationName, context, observationRegistry);
        return observation.observe(supplier);
    }


}
