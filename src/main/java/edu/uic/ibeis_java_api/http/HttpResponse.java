package edu.uic.ibeis_java_api.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpResponse {
    private boolean success;
    private JsonElement content;

    public HttpResponse() {}

    public HttpResponse(String jsonResponse) {
        // debug: print response
        System.out.println("Http Response: " + jsonResponse);

        JsonObject response = new JsonParser().parse(jsonResponse).getAsJsonObject();
        content = response.get("response");
        JsonObject status = response.getAsJsonObject("status");
        success = status.get("success").getAsBoolean();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public JsonElement getContent() {
        return content;
    }

    public void setContent(JsonElement content) {
        this.content = content;
    }
}
