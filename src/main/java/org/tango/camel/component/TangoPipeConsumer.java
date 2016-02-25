package org.tango.camel.component;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DevicePipe;
import fr.esrf.TangoApi.PipeBlob;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.tango.client.ez.proxy.TangoProxy;
import org.tango.client.ez.util.TangoUtils;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 2/25/16
 */
public class TangoPipeConsumer extends DefaultConsumer {
    public TangoPipeConsumer(TangoEndpoint tangoEndpoint, Processor processor) {
        super(tangoEndpoint, processor);
    }

    public TangoEndpoint getEndpoint(){
        return (TangoEndpoint) super.getEndpoint();
    }

    @Override
    protected void doStart() throws Exception {
        DevicePipe pipe = null;
        String pipeName = getEndpoint().getPipe();
        TangoProxy proxy = getEndpoint().getProxy();
        try {
            log.debug("Reading pipe={}/{}",proxy.getName(),pipeName);
            pipe = proxy.toDeviceProxy().readPipe(pipeName);
        } catch (DevFailed devFailed) {
            throw TangoUtils.convertDevFailedToException(devFailed);
        }

        PipeBlob value = null;
        Exchange exchange = getEndpoint().createExchange();
        value = pipe.getPipeBlob();
        exchange.getIn().setBody(value);


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
}

