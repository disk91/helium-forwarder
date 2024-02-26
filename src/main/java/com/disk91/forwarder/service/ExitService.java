/*
 * Copyright (c) - Paul Pinault (aka disk91) - 2020.
 *
 *    Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 *    and associated documentation files (the "Software"), to deal in the Software without restriction,
 *    including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *    sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *    furnished to do so, subject to the following conditions:
 *
 *    The above copyright notice and this permission notice shall be included in all copies or
 *    substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *    FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *    OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *    WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 *    IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.disk91.forwarder.service;


import com.disk91.forwarder.ForwarderApplication;
import fr.ingeniousthings.tools.Now;
import fr.ingeniousthings.tools.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;

@Service
public class ExitService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private boolean exiting = false;

    /**
     * Return true when exiting - do not start any async process
     * @return
     */
    public boolean isExiting() {
        return this.exiting;
    }

    @Autowired
    protected PayloadService payloadService;

    @PreDestroy
    public void onCallExit() {

        if (this.exiting) return;

        // ------------------------------------------------
        log.info("Exit - stop listeners");
        long s = Now.NowUtcMs();

        // ------------------------------------------------
        log.info("Exit - stop services");
        payloadService.closeService();

        /*
        int services = 0;
        s = Now.NowUtcMs();
        long d = s;
        do {
            services = 0;

            if ( (Now.NowUtcMs() - d) > 1000 ) {
                log.error("Waiting for "+services+" services to stop");
                d+=1000;
            }
            if ( (Now.NowUtcMs() - s) > 30_000 ) {
                log.error("Services not stopping, force stop");
                break;
            }
        } while (services > 0);
        */

        // ------------------------------------------------
        //log.info("Exit - commit caches");

        log.info("Exit - completed");
        this.exiting = true;
        Tools.sleep(500);
        ForwarderApplication.exit();
    }


}

