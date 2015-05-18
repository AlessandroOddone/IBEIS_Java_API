package edu.uic.ibeis_java_api.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import edu.uic.ibeis_java_api.exceptions.AuthorizationHeaderException;
import edu.uic.ibeis_java_api.exceptions.BadHttpRequestException;
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

    private int id;

    private BoundingBox boundingBox;
    private IbeisIndividual individual;
    private List<IbeisAnnotation> neighborAnnotations;

    protected IbeisAnnotation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * Get the bounding box corresponding to the annotation (Http GET)
     * @return bounding box
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public BoundingBox getBoundingBox() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        if (boundingBox == null) {
            Response response;
            try {
                response = new Request(RequestMethod.GET, CallPath.ANNOTATION_BOUNDING_BOX.getValue(),
                        new ParametersList().addParameter(new Parameter(ParamName.AID_LIST.getValue(), id))).execute();

                if (response == null || !response.isSuccess()) {
                    System.out.println("Unsuccessful Request");
                    throw new UnsuccessfulHttpRequestException();
                }

            } catch (AuthorizationHeaderException e) {
                e.printStackTrace();
                throw new BadHttpRequestException("error in authorization header");
            } catch (URISyntaxException | MalformedURLException e) {
                e.printStackTrace();
                throw new BadHttpRequestException("invalid url");
            } catch (InvalidHttpMethodException e) {
                e.printStackTrace();
                throw new BadHttpRequestException("invalid http method");
            }

            JsonArray boundingBoxParams = response.getContent().getAsJsonArray().get(0).getAsJsonArray();
            boundingBox = new BoundingBox();
            boundingBox.setX(boundingBoxParams.get(0).getAsInt());
            boundingBox.setY(boundingBoxParams.get(1).getAsInt());
            boundingBox.setW(boundingBoxParams.get(2).getAsInt());
            boundingBox.setH(boundingBoxParams.get(3).getAsInt());
        }
        return boundingBox;
    }

    /**
     * Get the individual corresponding to the annotation (Http Get)
     * @return IbeisIndividual
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public IbeisIndividual getIndividual() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        if (individual == null) {
            Response response;
            try {
                response = new Request(RequestMethod.GET, CallPath.ANNOTATION_INDIVIDUAL.getValue(),
                        new ParametersList().addParameter(new Parameter(ParamName.AID_LIST.getValue(), id))).execute();

                if (response == null || !response.isSuccess()) {
                    System.out.println("Unsuccessful Request");
                    throw new UnsuccessfulHttpRequestException();
                }

                individual = new IbeisIndividual(response.getContent().getAsJsonArray().get(0).getAsInt());

            } catch (AuthorizationHeaderException e) {
                e.printStackTrace();
                throw new BadHttpRequestException("error in authorization header");
            } catch (URISyntaxException | MalformedURLException e) {
                e.printStackTrace();
                throw new BadHttpRequestException("invalid url");
            } catch (InvalidHttpMethodException e) {
                e.printStackTrace();
                throw new BadHttpRequestException("invalid http method");
            }
        }
        return individual;
    }

    /**
     * Get the annotations that have been found in the same image (Http Get)
     * @return list of IbeisAnnotation elements
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisAnnotation> getNeighborAnnotations() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        if (neighborAnnotations == null) {
            Response response;
            try {
                response = new Request(RequestMethod.GET, CallPath.ANNOTATION_NEIGHBORS.getValue(),
                        new ParametersList().addParameter(new Parameter(ParamName.AID_LIST.getValue(), id))).execute();

                if (response == null || !response.isSuccess()) {
                    System.out.println("Unsuccessful Request");
                    throw new UnsuccessfulHttpRequestException();
                }

            } catch (AuthorizationHeaderException e) {
                e.printStackTrace();
                throw new BadHttpRequestException("error in authorization header");
            } catch (URISyntaxException | MalformedURLException e) {
                e.printStackTrace();
                throw new BadHttpRequestException("invalid url");
            } catch (InvalidHttpMethodException e) {
                e.printStackTrace();
                throw new BadHttpRequestException("invalid http method");
            }
            neighborAnnotations = new ArrayList<>();
            for (JsonElement annotationId : response.getContent().getAsJsonArray().get(0).getAsJsonArray()) {
                neighborAnnotations.add(new IbeisAnnotation(annotationId.getAsInt()));
            }
        }
        return neighborAnnotations;
    }
}
