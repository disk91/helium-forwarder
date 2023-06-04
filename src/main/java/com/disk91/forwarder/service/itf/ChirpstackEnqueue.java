package com.disk91.forwarder.service.itf;

import com.disk91.forwarder.service.itf.sub.QueueItem;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Enqueue downlink", description = "Add a message in downlink queue")
public class ChirpstackEnqueue {

    /*

        {
          "queueItem": {
            "confirmed": true,
            "data": "string",
            "fCntDown": 0,
            "fPort": 0,
            "id": "string",
            "isPending": true,
            "object": {}
          }
        }

     */

    protected QueueItem queueItem;

    // ---


    public QueueItem getQueueItem() {
        return queueItem;
    }

    public void setQueueItem(QueueItem queueItem) {
        this.queueItem = queueItem;
    }
}
