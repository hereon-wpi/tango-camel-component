package org.tango.camel.component;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.PipeBlob;
import fr.esrf.TangoApi.events.ITangoPipeListener;
import fr.esrf.TangoApi.events.TangoPipeEvent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.tango.client.ez.proxy.TangoProxy;
import org.tango.client.ez.util.TangoUtils;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 2/18/16
 */
public class TangoEventConsumer extends DefaultConsumer {

    private ITangoPipeListener listener;

    public TangoEventConsumer(TangoEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

    @Override
    public TangoEndpoint getEndpoint() {
        return (TangoEndpoint) super.getEndpoint();
    }

    /**
     * Subscribes to pipe event
     *
     * @throws Exception
     */
    @Override
    protected void doStart() throws Exception {
        listener = new ITangoPipeListener() {
            @Override
            public void pipe(TangoPipeEvent event) {
                PipeBlob value = null;
                Exchange exchange = getEndpoint().createExchange();
                try {
                    value = event.getValue().getPipeBlob();
                    exchange.getIn().setBody(value);
                } catch (DevFailed e) {
                    Exception e0 = TangoUtils.convertDevFailedToException(e);
                    exchange.setException(e0);
                }


                try {
                    // send message to next processor in the route
                    getProcessor().process(exchange);
                } catch (Exception e) {
                    getExceptionHandler().handleException("Error processing exchange", exchange, e);
                } finally {
                    // log exception if an exception occurred and was not handled
                    if (exchange.getException() != null) {
                        getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
                    }
                }
            }
        };
        TangoProxy proxy = getEndpoint().getProxy();
        String pipeName = getEndpoint().getPipe();
        log.debug("Subscribing to pipe={}/{}", proxy.getName(), pipeName);
        proxy.toTangoEventsAdapter().addTangoPipeListener(listener, pipeName, true);
    }

    @Override
    protected void doStop() throws Exception {
        getEndpoint().getProxy().toTangoEventsAdapter().removeTangoPipeListener(listener, getEndpoint().getPipe());
    }
}
