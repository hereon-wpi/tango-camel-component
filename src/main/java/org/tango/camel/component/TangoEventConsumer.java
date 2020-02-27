package org.tango.camel.component;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.PipeBlob;
import fr.esrf.TangoApi.events.ITangoPipeListener;
import fr.esrf.TangoApi.events.TangoPipe;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.tango.clientapi.TangoDevice;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.support.DefaultConsumer;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 2/18/16
 */
public class TangoEventConsumer extends DefaultConsumer {

    private ITangoPipeListener listener;
    private TangoPipe tangoPipe;
    private int eventId;

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
        listener = event -> {
            PipeBlob value = null;
            Exchange exchange = getEndpoint().createExchange();
            try {
                value = event.getValue().getPipeBlob();
                exchange.getIn().setBody(value);
            } catch (DevFailed e) {
                exchange.setException(e);
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
        };

        TangoDevice device = getEndpoint().getProxy();
        String pipeName = getEndpoint().getPipe();

        tangoPipe = new TangoPipe(device.getDeviceProxy(),pipeName,new String[0]);
        tangoPipe.addTangoPipeListener(listener, true);
        log.debug("Subscribing to pipe={}/{}", device.getDeviceProxy().get_name(), pipeName);
        eventId = device.getDeviceProxy().subscribe_event(pipeName, TangoConst.PIPE_EVENT, tangoPipe, new String[0]);
    }

    @Override
    protected void doStop() throws Exception {
        tangoPipe.removeTangoPipeListener(listener);
        getEndpoint().getProxy().getDeviceProxy().unsubscribe_event(eventId);
    }
}
