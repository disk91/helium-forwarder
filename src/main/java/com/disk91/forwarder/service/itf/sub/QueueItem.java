package com.disk91.forwarder.service.itf.sub;

import com.disk91.forwarder.api.interfaces.sub.KeyValue;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Enqueue downlink", description = "Add a message in downlink queue")
public class QueueItem {

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

    protected boolean confirmed;
    protected String data; // b64 encoded
    protected int fPort;

  // Following fields are more a problem as they are set by chirpstack and not
  // set by requester. The "object" field will take over the data field and
  // make data invalid
  //
  //  protected int fCntDown;
  //  protected String id;
  //  protected boolean isPending;
  //  protected KeyValue object;

    // ---


    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getfPort() {
        return fPort;
    }

    public void setfPort(int fPort) {
        this.fPort = fPort;
    }

}
