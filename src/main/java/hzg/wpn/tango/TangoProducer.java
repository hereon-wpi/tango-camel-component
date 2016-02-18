package hzg.wpn.tango;

import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoApi.DevicePipe;
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
        DevicePipe body = exchange.getIn().getBody(DevicePipe.class);

        //TODO transform body

        getEndpoint().getProxy().toDeviceProxy().writePipe(body);
    }


    @Override
    public TangoEndpoint getEndpoint(){
        return (TangoEndpoint) super.getEndpoint();
    }

}
