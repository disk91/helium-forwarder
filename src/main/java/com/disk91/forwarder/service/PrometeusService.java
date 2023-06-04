package com.disk91.forwarder.service;

import fr.ingeniousthings.tools.Now;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class PrometeusService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // ---- API Metrics

    private int uplinkQueueSize = 0;
    private long totalUplinkRetry = 0;
    private long totalUplinkFailure = 0;

    synchronized public void addUplinkInQueue() {
        uplinkQueueSize++;
    }

    synchronized public void remUplinkInQueue() {
        uplinkQueueSize--;
    }

    synchronized public void addUplinkRetry() {
        totalUplinkRetry++;
    }

    synchronized public void addUplinkFailure() {
        totalUplinkFailure++;
    }

    private long downlinkCacheSize = 0;

    private int downlinkQueueSize = 0;

    private long totalDownlinkRetry = 0;

    private long totalDownlinkFailure = 0;

    synchronized public void updateDownlinkCacheSize(long sz) {
        downlinkCacheSize = sz;
    }

    synchronized public void addDownlinkInQueue() {
        downlinkQueueSize++;
    }

    synchronized public void remDownlinkInQueue() {
        downlinkQueueSize--;
    }

    synchronized public void addDownlinkRetry() {
        totalDownlinkRetry++;
    }

    synchronized public void addDownlinkFailure() {
        totalDownlinkFailure++;
    }

    // =============================================================
    // Prometheus interface

    protected Supplier<Number> getUplinkQueueSize() {
        return () -> uplinkQueueSize;
    }

    protected Supplier<Number> getTotalUplinkRetry() {
        return () -> totalUplinkRetry;
    }

    protected Supplier<Number> getTotalUplinkFailure() {
        return () -> totalUplinkFailure;
    }

    protected Supplier<Number> getDownlinkCacheSize() {
        return () -> downlinkCacheSize;
    }

    protected Supplier<Number> getDownlinkQueueSize() {
        return () -> downlinkQueueSize;
    }

    protected Supplier<Number> getTotalDownlinkRetry() {
        return () -> totalDownlinkRetry;
    }
    protected Supplier<Number> getTotalDownlinkFailure() {
        return () -> totalDownlinkFailure;
    }


    public PrometeusService(MeterRegistry registry) {

        Gauge.builder("fwder.uplink.queue.sz", getUplinkQueueSize())
                .description("Pending uplink in queue")
                .register(registry);
        Gauge.builder("fwder.uplink.retry.total", getTotalUplinkRetry())
                .description("Total number of uplink retry")
                .register(registry);
        Gauge.builder("fwder.uplink.failure.total", getTotalUplinkFailure())
                .description("Total number of failure after max retry")
                .register(registry);
        Gauge.builder("fwder.downlink.cache.sz", getDownlinkCacheSize())
                .description("Size of downlink session cache")
                .register(registry);
        Gauge.builder("fwder.downlink.queue.sz", getDownlinkQueueSize())
                .description("Pending downlink in queue")
                .register(registry);
        Gauge.builder("fwder.downlink.retry.total", getTotalDownlinkRetry())
                .description("Total number of downlink retry")
                .register(registry);
        Gauge.builder("fwder.downlink.failure.total", getTotalDownlinkFailure())
                .description("Total number of downlink failure")
                .register(registry);

    }

/*
    @Scheduled(fixedRateString = "${helium.prometeus.logPeriod}", initialDelay = 30_000)
    protected void backgroundLogStatsUpdate() {


    }
*/
}