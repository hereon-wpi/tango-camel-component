package org.tango;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

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

        Exchange exchange = getEndpoint().createExchange();


        Object message = getEndpoint().getProxy().toDeviceProxy().readPipe(getEndpoint().getPipe()).getPipeBlob();

        //TODO transform body

        exchange.getIn().setBody(message);

        try {
            // send message to next processor in the route
            getProcessor().process(exchange);
            return 1; // number of messages polled
        } finally {
            // log exception if an exception occurred and was not handled
            if (exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }
}
