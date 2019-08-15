package com.starnetsecurity.common.spring;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by 宏炜 on 2017-02-09.
 */
public class MAPIHttpServletRequestWrapper extends HttpServletRequestWrapper  {

    private byte[] body;
    private final Map<String, String> customHeaders;
    private Map<String , String[]> parameters;
    AesCipherService aesCipherService;
    private String aesKey;

    public MAPIHttpServletRequestWrapper(HttpServletRequest request,String aesKey) throws IOException {
        super(request);
        this.customHeaders = new HashMap();
        this.aesCipherService = new AesCipherService();
        this.aesKey = aesKey;
        if("application/json".equals(request.getContentType())){
            byte[] params = IOUtils.toByteArray(request.getInputStream());
            ByteSource byteSource = this.aesCipherService.decrypt(Hex.decode(params) , Hex.decode(this.aesKey));
            String bodyStr = new String(byteSource.getBytes());
            this.body = bodyStr.getBytes("utf-8");
        }else{
            this.parameters = request.getParameterMap();
        }
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    public void putHeader(String name, String value){
        this.customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = customHeaders.get(name);
        if (headerValue != null){
            return headerValue;
        }
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<String>(customHeaders.keySet());

        @SuppressWarnings("unchecked")
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            String n = e.nextElement();
            set.add(n);
        }
        return Collections.enumeration(set);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = this.parameters.get(name);
        if(values == null || values.length == 0) {
            return super.getParameterValues(name);
        }
        for(int i = 0; i < values.length; i++){
            String value = values[i];
            ByteSource byteSource = aesCipherService.decrypt(Hex.decode(value),Hex.decode(this.aesKey));
            values[i] = new String(byteSource.getBytes());
        }
        return values;
    }

    @Override
    public String getParameter(String name) {
        String[] values = this.parameters.get(name);
        if(values == null || values.length == 0) {
            return null;
        }
        String value = values[0];
        ByteSource byteSource = aesCipherService.decrypt(Hex.decode(value),Hex.decode(this.aesKey));
        value = new String(byteSource.getBytes());
        return value;
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        String headerValue = customHeaders.get(name);
        Set<String> set = new HashSet<String>();
        if(headerValue != null){
            set.add(headerValue);
        }else{
            Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaders(name);
            while (e.hasMoreElements()) {
                String n = e.nextElement();
                set.add(n);
            }
        }
        return Collections.enumeration(set);
    }
}