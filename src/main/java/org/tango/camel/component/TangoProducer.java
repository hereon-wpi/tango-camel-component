package org.tango.camel.component;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.PipeBlob;
import fr.soleil.tango.clientapi.TangoDevice;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tango.utils.DevFailedUtils;

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

        try {
            TangoDevice proxy = getEndpoint().getProxy();
            String pipeName = getEndpoint().getPipe();
            log.debug("Writing to pipe={}/{}",proxy.getDeviceProxy().get_name(),pipeName);
            proxy.getDeviceProxy().writePipe(pipeName, body);
        } catch (DevFailed devFailed) {
            DevFailedUtils.logDevFailed(devFailed, log);
            throw devFailed;
        }
    }


    @Override
    public TangoEndpoint getEndpoint(){
        return (TangoEndpoint) super.getEndpoint();
    }

}
