package org.tango.camel.component;

import fr.esrf.Tango.DevFailed;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.tango.client.ez.proxy.TangoProxy;
import org.tango.client.ez.util.TangoUtils;

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
        TangoProxy proxy = getEndpoint().getProxy();
        String pipeName = getEndpoint().getPipe();
        log.debug("Polling pipe={}/{}",proxy.getName(),pipeName);
        Exchange exchange = getEndpoint().createExchange();

        Object message = proxy.toDeviceProxy().readPipe(pipeName).getPipeBlob();

        //TODO transform body

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
                     exception = TangoUtils.convertDevFailedToException((DevFailed) exception);
                getExceptionHandler().handleException("Error processing exchange", exchange, exception);
            }
        }
    }
}
