package edu.uic.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Response {
    private boolean success;
    private JsonElement content;

    public Response(String jsonResponse) {
        // debug: print response
        System.out.println("Response Message: " + jsonResponse);

        JsonObject response = new JsonParser().parse(jsonResponse).getAsJsonObject();
        content = response.get("response");
        JsonObject status = response.getAsJsonObject("status");
        success = status.get("success").getAsBoolean();
    }

    public boolean isSuccess() {
        return success;
    }

    public JsonElement getContent() {
        return content;
    }
}
