package edu.uic.ibeis_java_api.http;

import edu.uic.ibeis_java_api.exceptions.AuthorizationHeaderException;
import edu.uic.ibeis_java_api.exceptions.InvalidHttpMethodException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

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
    }

    public Request(RequestMethod httpRequestMethod, String callPath) throws MalformedURLException{
        this(httpRequestMethod, callPath, new ParametersList());
    }

    public Response execute() throws AuthorizationHeaderException, URISyntaxException, IOException, InvalidHttpMethodException {
        HttpClient client = HttpClientBuilder.create().build();
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
                        request.setEntity(new UrlEncodedFormEntity(nameValuePairParams));
                    }
                }
                request.setURI(url.toURI());
                request.setHeader("Authorization", Auth.getAuthorizationHeader(url));
                response = client.execute(request);
                break;
            }
            case PUT: {
                HttpPut request = new HttpPut();

            }
            case DELETE: {
                HttpDelete request = new HttpDelete();

            }
            default:
                throw new InvalidHttpMethodException();
        }
        HttpEntity entity = response.getEntity();
        return new Response(EntityUtils.toString(entity));
    }
}
