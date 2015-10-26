package edu.uic.ibeis_java_api.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import edu.uic.ibeis_java_api.api.annotation.BoundingBox;
import edu.uic.ibeis_java_api.exceptions.AuthorizationHeaderException;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.InvalidHttpMethodException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.http.*;
import edu.uic.ibeis_java_api.values.CallPath;
import edu.uic.ibeis_java_api.values.ParamName;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * An annotation is a specific sighting of an animal within an image
 */
public class IbeisAnnotation {

    private long id;

    protected IbeisAnnotation(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    /**
     * Get the bounding box corresponding to the annotation
     * @return Bounding box
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public BoundingBox getBoundingBox() throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.GET, CallPath.ANNOTATION_BOUNDING_BOX.getValue(),
                    new HttpParametersList().addParameter(new HttpParameter(ParamName.AID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            JsonArray boundingBoxParamsJson = response.getContent().getAsJsonArray().get(0).getAsJsonArray();
            BoundingBox boundingBox = new BoundingBox();
            boundingBox.setX(boundingBoxParamsJson.get(0).getAsInt());
            boundingBox.setY(boundingBoxParamsJson.get(1).getAsInt());
            boundingBox.setW(boundingBoxParamsJson.get(2).getAsInt());
            boundingBox.setH(boundingBoxParamsJson.get(3).getAsInt());
            return boundingBox;

        } catch (AuthorizationHeaderException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("error in authorization header");
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("invalid url");
        } catch (InvalidHttpMethodException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("invalid http method");
        }
    }

    /**
     * Get the image corresponding to the annotation
     * @return IbeisImage
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public IbeisImage getImage() throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.GET, CallPath.ANNOTATION_IMAGE.getValue(),
                    new HttpParametersList().addParameter(new HttpParameter(ParamName.AID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            return new IbeisImage(response.getContent().getAsJsonArray().get(0).getAsLong());

        } catch (AuthorizationHeaderException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("error in authorization header");
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("invalid url");
        } catch (InvalidHttpMethodException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("invalid http method");
        }
    }

    /**
     * Get the individual corresponding to the annotation
     * @return IbeisIndividual
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public IbeisIndividual getIndividual() throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.GET, CallPath.ANNOTATION_INDIVIDUAL.getValue(),
                    new HttpParametersList().addParameter(new HttpParameter(ParamName.AID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            return new IbeisIndividual(response.getContent().getAsJsonArray().get(0).getAsLong());

        } catch (AuthorizationHeaderException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("error in authorization header");
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("invalid url");
        } catch (InvalidHttpMethodException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("invalid http method");
        }
    }

    /**
     * Get the annotations that have been found in the same image
     * @return List of IbeisAnnotation elements
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisAnnotation> getNeighborAnnotations() throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.GET, CallPath.ANNOTATION_NEIGHBORS.getValue(),
                    new HttpParametersList().addParameter(new HttpParameter(ParamName.AID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            List<IbeisAnnotation> neighborAnnotations = new ArrayList<>();
            for (JsonElement annotationIdJson : response.getContent().getAsJsonArray().get(0).getAsJsonArray()) {
                neighborAnnotations.add(new IbeisAnnotation(annotationIdJson.getAsLong()));
            }
            return neighborAnnotations;

        } catch (AuthorizationHeaderException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("error in authorization header");
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("invalid url");
        } catch (InvalidHttpMethodException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("invalid http method");
        }
    }

    /**
     * Set the individual associated to the annotation
     * @param individual
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public void setIndividual(IbeisIndividual individual) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.PUT, CallPath.ANNOTATION_INDIVIDUAL.getValue(),
                    new HttpParametersList().addParameter(new HttpParameter(ParamName.AID_LIST.getValue(), id))
                            .addParameter(new HttpParameter(ParamName.NAME_ROWID_LIST.getValue(), individual.getId()))).execute();

            if (response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

        } catch (AuthorizationHeaderException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("error in authorization header");
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("invalid url");
        } catch (InvalidHttpMethodException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("invalid http method");
        }
    }

    /**
     * Returns true if the annotation is in Ibeis database, false otherwise
     * @return
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public boolean isValidAnnotation() throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException
    {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.GET, CallPath.ANNOTATIONS.getValue()).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            for (JsonElement annotationIdJson : response.getContent().getAsJsonArray()) {
                if(this.id == annotationIdJson.getAsLong()) {
                    return true;
                }
            }
            return false;

        } catch (AuthorizationHeaderException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("error in authorization header");
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("invalid url");
        } catch (InvalidHttpMethodException e) {
            e.printStackTrace();
            throw new MalformedHttpRequestException("invalid http method");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof IbeisAnnotation) {
            if(((IbeisAnnotation) obj).getId() == this.getId()) return true;
        }
        return false;
    }
}
