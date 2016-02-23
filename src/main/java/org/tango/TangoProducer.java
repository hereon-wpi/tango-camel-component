package org.tango;

import fr.esrf.TangoApi.DevicePipe;
import fr.esrf.TangoApi.PipeBlob;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Tango producer.
 */
public class TangoProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(TangoProducer.class);

    public TangoProducer(TangoEndpoint endpoint) {
        super(endpoint);
    }

    public void process(Exchange exchange) throws Exception {
        PipeBlob body = exchange.getIn().getBody(PipeBlob.class);

        //TODO transform body

        getEndpoint().getProxy().toDeviceProxy().writePipe(getEndpoint().getPipe(), body);
    }


    @Override
    public TangoEndpoint getEndpoint(){
        return (TangoEndpoint) super.getEndpoint();
    }

}
