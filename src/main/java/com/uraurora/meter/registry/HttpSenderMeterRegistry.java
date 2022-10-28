package com.uraurora.meter.registry;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.step.StepRegistryConfig;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import io.micrometer.core.ipc.http.HttpSender;
import io.micrometer.core.ipc.http.HttpUrlConnectionSender;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author : uraurora
 * @date : 2022-09-13 16:22
 * @description : http sender meter registry
 */
public class HttpSenderMeterRegistry extends ObservableStepMeterRegistry {

    private final Logger logger = LoggerFactory.getLogger(HttpSenderMeterRegistry.class);

    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new NamedThreadFactory("step-http-sender-metrics-publisher");

    /**
     * internal http invoke
     */
    protected HttpSender httpClient;

    public HttpSenderMeterRegistry(StepRegistryConfig config, Clock clock, HttpSender httpClient) {
        super(config, clock);
        this.httpClient = httpClient;
    }

    public HttpSenderMeterRegistry(StepRegistryConfig config, Clock clock) {
        this(config, clock, new HttpUrlConnectionSender());
    }

    @Override
    protected void publish() {
        // todo: meter publish
    }

    @Override
    protected TimeUnit getBaseTimeUnit() {
        return TimeUnit.SECONDS;
    }

    @Override
    public void start(ThreadFactory threadFactory) {
        ThreadFactory tf = threadFactory == null ? DEFAULT_THREAD_FACTORY : threadFactory;
        if (config.enabled()) {
            super.start(tf);
        }
    }

}
