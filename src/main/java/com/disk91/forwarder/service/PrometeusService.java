package com.disk91.forwarder.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PrometeusService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // ---- API Metrics

    // =============================================================
    // Prometheus interface



    public PrometeusService(MeterRegistry registry) {


    }

}
