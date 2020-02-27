package org.tango.camel.component;

import fr.esrf.TangoApi.DeviceProxyFactory;
import fr.soleil.tango.clientapi.TangoDevice;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.support.DefaultComponent;

import java.util.Map;

/**
 * Represents the component that manages {@link TangoEndpoint}.
 */
public class TangoComponent extends DefaultComponent {
    
    public TangoComponent(CamelContext context) {
        super(context);
    }

    protected Endpoint createEndpoint(String rawURI, String remaining, Map<String, Object> parameters) throws Exception {
        TangoDevice proxy = new TangoDevice(DeviceProxyFactory.get("tango://" + remaining));

        Endpoint endpoint = new TangoEndpoint(rawURI, this, proxy);
        setProperties(endpoint, parameters);

        return endpoint;
    }
}
