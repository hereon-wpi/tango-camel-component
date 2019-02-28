package org.tango.camel.component;

import java.util.Map;

import fr.esrf.TangoApi.DeviceProxyFactory;
import fr.soleil.tango.clientapi.TangoDevice;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

import org.apache.camel.impl.UriEndpointComponent;

/**
 * Represents the component that manages {@link TangoEndpoint}.
 */
public class TangoComponent extends UriEndpointComponent {
    
    public TangoComponent() {
        super(TangoEndpoint.class);
    }

    public TangoComponent(CamelContext context) {
        super(context, TangoEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        TangoDevice proxy = new TangoDevice(DeviceProxyFactory.get(remaining));

        Endpoint endpoint = new TangoEndpoint(uri, this, proxy);
        setProperties(endpoint, parameters);

        return endpoint;
    }
}
