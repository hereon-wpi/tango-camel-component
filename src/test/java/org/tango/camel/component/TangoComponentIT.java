package org.tango.camel.component;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class TangoComponentIT extends CamelTestSupport {

    @Test
    public void testTango() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);       
        
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("tango:tango://hzgxenvtest:10000/development/pipe/0?pipe=pipe&poll=true")
                  .to("tango:development/pipe/0?pipe=pipe")
                  .to("mock:result");
            }
        };
    }
}
