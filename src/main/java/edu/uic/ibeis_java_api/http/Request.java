package edu.uic.ibeis_java_api.http;

import android.org.apache.http.HttpEntity;
import android.org.apache.http.HttpResponse;
import android.org.apache.http.NameValuePair;
import android.org.apache.http.client.HttpClient;
import android.org.apache.http.client.entity.UrlEncodedFormEntity;
import android.org.apache.http.client.methods.HttpDelete;
import android.org.apache.http.client.methods.HttpGet;
import android.org.apache.http.client.methods.HttpPost;
import android.org.apache.http.client.methods.HttpPut;
import android.org.apache.http.entity.mime.MultipartEntityBuilder;
import android.org.apache.http.entity.mime.content.FileBody;
import android.org.apache.http.impl.client.DefaultHttpClient;
import android.org.apache.http.util.EntityUtils;
import edu.uic.ibeis_java_api.exceptions.AuthorizationHeaderException;
import edu.uic.ibeis_java_api.exceptions.InvalidHttpMethodException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Request {
    private static final String API_URL = "http://pachy.cs.uic.edu:5005/api";

    private URL url;
    private RequestMethod httpRequestMethod;
    private ParametersList parametersList;

    public Request(RequestMethod httpRequestMethod, String callPath, ParametersList parametersList) throws MalformedURLException {
        this.httpRequestMethod = httpRequestMethod;
        this.url = new URL(API_URL + callPath);
        this.parametersList = parametersList;

        //debug: print request
        System.out.println(this.toString());
    }

    public Request(RequestMethod httpRequestMethod, String callPath) throws MalformedURLException{
        this(httpRequestMethod, callPath, new ParametersList());
    }

    public Response execute() throws AuthorizationHeaderException, URISyntaxException, IOException, InvalidHttpMethodException {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;

        switch (httpRequestMethod) {
            case GET: {
                HttpGet request = new HttpGet();

                // add parameters to url
                List<Parameter> params = parametersList.getParameters();
                StringBuilder urlParams = new StringBuilder("");
                if (params.size() > 0) {
                    for (int i=0; i< params.size(); i++) {
                        if (i == 0) {
                            urlParams.append("?");
                        }
                        else {
                            urlParams.append("&");
                        }
                        urlParams.append(params.get(i).encodeInUrl());
                    }
                }

                URL urlWithParams = new URL(url.toString() + urlParams.toString());
                request.setURI(urlWithParams.toURI());
                request.setHeader("Authorization", Auth.getAuthorizationHeader(urlWithParams));
                response = client.execute(request);
                break;
            }
            case POST: {
                HttpPost request = new HttpPost();

                // add parameters to POST request message
                List<Parameter> params = parametersList.getParameters();
                if(params.size() > 0) {
                    if(parametersList.containsFile()) {
                        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                        for (Parameter p : params) {
                            if(p.isFile()) {
                                builder.addPart(p.getName(), new FileBody(new File(p.getValue())));
                            }
                            else {
                                builder.addTextBody(p.getName(), p.getValue());
                            }
                        }
                        request.setEntity(builder.build());
                    }
                    else {
                        List<NameValuePair> nameValuePairParams = new ArrayList<>();
                        for(Parameter p : params) {
                            nameValuePairParams.add(p.toNameValuePair());
                        }
                        System.out.println(new UrlEncodedFormEntity(nameValuePairParams, "utf-8").toString());
                        request.setEntity(new UrlEncodedFormEntity(nameValuePairParams, "utf-8"));
                    }
                }
                request.setURI(url.toURI());
                request.setHeader("Authorization", Auth.getAuthorizationHeader(url));
                response = client.execute(request);
                break;
            }
            case PUT: {
                HttpPut request = new HttpPut();

                // add parameters to POST request message
                List<Parameter> params = parametersList.getParameters();
                if(params.size() > 0) {
                    List<NameValuePair> nameValuePairParams = new ArrayList<>();
                    for(Parameter p : params) {
                        nameValuePairParams.add(p.toNameValuePair());
                    }
                    request.setEntity(new UrlEncodedFormEntity(nameValuePairParams, "utf-8"));
                }
                request.setURI(url.toURI());
                request.setHeader("Authorization", Auth.getAuthorizationHeader(url));
                response = client.execute(request);
                break;
            }
            case DELETE: {
                HttpDelete request = new HttpDelete();

                // add parameters to url
                List<Parameter> params = parametersList.getParameters();
                StringBuilder urlParams = new StringBuilder("");
                if (params.size() > 0) {
                    for (int i=0; i< params.size(); i++) {
                        if (i == 0) {
                            urlParams.append("?");
                        }
                        else {
                            urlParams.append("&");
                        }
                        urlParams.append(params.get(i).encodeInUrl());
                    }
                }

                URL urlWithParams = new URL(url.toString() + urlParams.toString());
                request.setURI(urlWithParams.toURI());
                request.setHeader("Authorization", Auth.getAuthorizationHeader(urlWithParams));
                response = client.execute(request);
                request.releaseConnection();
                break;
            }
            default:
                throw new InvalidHttpMethodException();
        }
        HttpEntity responseEntity = response.getEntity();
        if (responseEntity != null) {
            return new Response(EntityUtils.toString(responseEntity));
        }
        else {// no response (treat as an unsuccessful response)
            Response noResponse = new Response();
            noResponse.setSuccess(false);
            return noResponse;
        }
    }

    @Override
    public String toString() {
        StringBuilder formattedRequest = new StringBuilder("");

        formattedRequest.append("Http Request:\n");
        formattedRequest.append("- request type: " + this.httpRequestMethod.toString() + "\n");
        formattedRequest.append("- url: " + this.url + "\n");
        formattedRequest.append("- parameters: " + this.parametersList.toString());

        return formattedRequest.toString();
    }
}
