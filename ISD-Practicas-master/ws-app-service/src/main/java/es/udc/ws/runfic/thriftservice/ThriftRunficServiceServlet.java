package es.udc.ws.runfic.thriftservice;

import es.udc.ws.runfic.thrift.ThriftRunficService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;

public class ThriftRunficServiceServlet extends TServlet {

    public ThriftRunficServiceServlet() {
        super(createProcessor(), createProtocolFactory());
    }

    private static TProcessor createProcessor() {
        return new ThriftRunficService.Processor<ThriftRunficService.Iface>(
                new ThriftRunficServiceImpl()
        );
    }

    private static TProtocolFactory createProtocolFactory() {
        return new TBinaryProtocol.Factory();
    }
}
