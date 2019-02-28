package org.tango.camel.component;

import fr.soleil.tango.clientapi.TangoDevice;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a Tango endpoint.
 */
@UriEndpoint(scheme = "tango", title = "Tango", syntax="tango:name?[attr|pipe]", consumerClass = TangoPollConsumer.class, label = "Tango")
public class TangoEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = "true")
    private String name;
    @UriParam @Metadata(required = "true")
    private String pipe;
    @UriParam
    private boolean poll = false;
    @UriParam
    private boolean once = false;

    private final TangoDevice proxy;

    public TangoEndpoint(String uri, TangoComponent component, TangoDevice proxy) {
        super(uri, component);
        this.proxy = proxy;
    }

    public Producer createProducer() throws Exception {
        return new TangoProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        if(poll)
            return new TangoPollConsumer(this, processor);
        else if(once)
            return new TangoPipeConsumer(this, processor);
        else
            return new TangoEventConsumer(this, processor);
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * Tango device name, e.g. either tango://host:port/name or just name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TangoDevice getProxy() {
        return proxy;
    }

    public String getPipe() {
        return pipe;
    }

    /**
     * Pipe name to attach to
     *
     * @param pipe
     */
    public void setPipe(String pipe) {
        this.pipe = pipe;
    }

    public boolean getPoll() {
        return poll;
    }

    /**
     * Defines whether this Endpoint will create PollConsumers
     *
     * @param isPoll
     */
    public void setPoll(boolean isPoll) {
        this.poll = isPoll;
    }

    public boolean isOnce() {
        return once;
    }

    /**
     * If set to true. This end point will read corresponding pipe once during its lifetime
     *
     * @param once
     */
    public void setOnce(boolean once) {
        this.once = once;
    }
}
