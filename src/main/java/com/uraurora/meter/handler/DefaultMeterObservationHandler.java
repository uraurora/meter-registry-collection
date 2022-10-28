package com.uraurora.meter.handler;

import io.micrometer.core.instrument.*;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;

import java.util.stream.Collectors;

/**
 * Handler for {@link Timer.Sample} and {@link Counter}.
 *
 * WARNING: Since the {@link LongTaskTimer} needs to be created in the {@code onStart}
 * method, it can only contain tags that are available by that time. This means that if
 * you add a {@code lowCardinalityKeyValue} after calling {@code start} on the
 * {@link Observation}, that {@code KeyValue} will not be translated as a {@link Tag} on
 * the {@link LongTaskTimer}. Likewise, since the {@code KeyValuesProvider} is evaluated
 * in the {@code stop} method of the {@link Observation} (after start), those
 * {@code KeyValue} instances will not be used for the {@link LongTaskTimer}.
 * @author uraurora
 */
public class DefaultMeterObservationHandler implements ObservationHandler<Observation.Context> {

    private final MeterRegistry meterRegistry;

    public DefaultMeterObservationHandler(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return true;
    }

    @Override
    public void onStart(Observation.Context context) {
        LongTaskTimer.Sample longTaskSample = LongTaskTimer
                .builder(context.getName() + ".active")
                .tags(createTags(context))
                .register(meterRegistry)
                .start();
        context.put(LongTaskTimer.Sample.class, longTaskSample);

        Timer.Sample sample = Timer.start(meterRegistry);
        context.put(Timer.Sample.class, sample);
    }

    @Override
    public void onStop(Observation.Context context) {
        Timer.Sample sample = context.getRequired(Timer.Sample.class);
        sample.stop(Timer.builder(context.getName())
                .tags(createErrorTags(context))
                .tags(createTags(context))
                .register(this.meterRegistry));

        LongTaskTimer.Sample longTaskSample = context.getRequired(LongTaskTimer.Sample.class);
        longTaskSample.stop();
    }


    private Tags createErrorTags(Observation.Context context) {
        return Tags.of(
                "error",
                context.getError()
                        .map(throwable -> throwable.getClass().getSimpleName())
                        .orElse("none")
        );
    }

    private Tags createTags(Observation.Context context) {
        return Tags.of(context.getLowCardinalityKeyValues().stream()
                .map(tag -> Tag.of(tag.getKey(), tag.getValue()))
                .collect(Collectors.toList())
        );
    }

}
