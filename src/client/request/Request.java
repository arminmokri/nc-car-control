/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.request;

import client.Header;
import client.transfer.TransferProtocol;
import client.parameters.Parameters;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import org.json.simple.parser.ParseException;

/**
 *
 * @author armin
 */
public class Request {

    //
    private TransferProtocol transferProtocol;
    private Header header;
    private Parameters requestParameters;
    private Parameters responseParameters;
    //
    private long ResponseTimeout;

    public Request(TransferProtocol transferProtocol, Parameters parameters) {
        this.transferProtocol = transferProtocol;
        this.header = new Header(Header.REQUEST);
        this.requestParameters = parameters;
        this.ResponseTimeout = 5000;
    }

    public Request(TransferProtocol transferProtocol, byte[] bytes) throws ParseException {
        // transfer protocol
        this.transferProtocol = transferProtocol;
        // header
        byte[] header = Arrays.copyOfRange(bytes, 0, 11);
        this.header = new Header(Header.REQUEST, header);
        // requestParameters
        byte[] parmeters = Arrays.copyOfRange(bytes, 11, bytes.length);
        this.requestParameters = new Parameters(parmeters);
    }

    public long getResponseTimeout() {
        return ResponseTimeout;
    }

    public Parameters getResponseParameters() {
        try {
            long now = System.currentTimeMillis();
            while (responseParameters == null && (System.currentTimeMillis() <= now + ResponseTimeout)) {
                Thread.sleep(25);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return responseParameters;
    }

    public void setResponseParameters(Parameters responseParameters) {
        this.responseParameters = responseParameters;
    }

    public Parameters getRequestParameters() {
        return requestParameters;
    }

    public Header getHeader() {
        return header;
    }

    public TransferProtocol getTransferProtocol() {
        return transferProtocol;
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
        byteArrayOutputStream.write(header.getBytes());
        byteArrayOutputStream.write(requestParameters.getJsonBytes());
        return byteArrayOutputStream.toByteArray();
    }

}
