package org.tango;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

import org.apache.camel.impl.UriEndpointComponent;
import org.tango.client.ez.proxy.TangoProxies;
import org.tango.client.ez.proxy.TangoProxy;

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
        //TODO load from cache
        TangoProxy proxy = TangoProxies.newDeviceProxyWrapper(remaining);

        Endpoint endpoint = new TangoEndpoint(uri, this, proxy);
        setProperties(endpoint, parameters);

        return endpoint;
    }
}
