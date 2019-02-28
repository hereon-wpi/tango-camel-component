package org.tango.camel.component;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DevicePipe;
import fr.esrf.TangoApi.PipeBlob;
import fr.soleil.tango.clientapi.TangoDevice;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.tango.utils.DevFailedUtils;

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
        TangoDevice device = getEndpoint().getProxy();
        try {
            log.debug("Reading pipe={}/{}",device.getDeviceProxy().get_name(),pipeName);
            pipe = device.getDeviceProxy().readPipe(pipeName);
        } catch (DevFailed devFailed) {
            DevFailedUtils.logDevFailed(devFailed, log);
            throw devFailed;
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

