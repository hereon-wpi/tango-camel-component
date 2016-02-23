package org.tango;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DevicePipe;
import fr.esrf.TangoApi.PipeBlob;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tango.client.ez.util.TangoUtils;

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

        try {
            getEndpoint().getProxy().toDeviceProxy().writePipe(getEndpoint().getPipe(), body);
        } catch (DevFailed devFailed) {
            throw TangoUtils.convertDevFailedToException(devFailed);
        }
    }


    @Override
    public TangoEndpoint getEndpoint(){
        return (TangoEndpoint) super.getEndpoint();
    }

}
