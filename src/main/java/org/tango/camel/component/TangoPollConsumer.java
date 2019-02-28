package org.tango.camel.component;

import fr.esrf.Tango.DevFailed;
import fr.soleil.tango.clientapi.TangoDevice;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.tango.utils.DevFailedUtils;

/**
 * The Tango consumer.
 */
public class TangoPollConsumer extends ScheduledPollConsumer {
    public TangoPollConsumer(TangoEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

    @Override
    public TangoEndpoint getEndpoint() {
        return (TangoEndpoint) super.getEndpoint();
    }

    @Override
    protected int poll() throws Exception {
        TangoDevice device = getEndpoint().getProxy();
        String pipeName = getEndpoint().getPipe();
        log.debug("Polling pipe={}/{}",device.getDeviceProxy().get_name(),pipeName);
        Exchange exchange = getEndpoint().createExchange();

        Object message = device.getDeviceProxy().readPipe(pipeName).getPipeBlob();

        exchange.getIn().setBody(message);

        try {
            // send message to next processor in the route
            getProcessor().process(exchange);
            return 1; // number of messages polled
        } finally {
            // log exception if a/n exception occurred and was not handled
            Exception exception = exchange.getException();
            if (exception != null) {
                if(exception.getClass().isAssignableFrom(DevFailed.class))
                    DevFailedUtils.logDevFailed((DevFailed) exception, log);
                else
                    getExceptionHandler().handleException("Error processing exchange", exchange, exception);
            }
        }
    }
}
