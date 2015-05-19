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
import edu.uic.ibeis_java_api.values.Species;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * An image on Ibeis server
 */
public class IbeisImage {

    private int id;

    private String base64EncodedImage;
    private GeoCoordinates location;
    private GregorianCalendar datetime;
    private ImageSize size;
    private Note note;
    private List<IbeisAnnotation> annotations;

    protected IbeisImage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    /**
     * Get the raw image Base64 encoded
     * @return Base64 encoded image
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public String getBase64EncodedImage() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        if (base64EncodedImage == null) {
            try {
                Response response = new Request(RequestMethod.GET, CallPath.IMAGE.getValue() + id + "/").execute();

                if (response == null || !response.isSuccess()) {
                    System.out.println("Unsuccessful Request");
                    throw new UnsuccessfulHttpRequestException();
                }

                base64EncodedImage = response.getContent().getAsString();

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
        return base64EncodedImage;
    }

    /**
     * Get the location (geographic coordinates) of the image (Http GET)
     * @return (latitude,longitude) pair
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public GeoCoordinates getLocation() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        if (location == null) {
            try {
                Response response = new Request(RequestMethod.GET, CallPath.IMAGE_GPS.getValue(),
                        new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))).execute();

                if (response == null || !response.isSuccess()) {
                    System.out.println("Unsuccessful Request");
                    throw new UnsuccessfulHttpRequestException();
                }

                JsonArray geoCoordinatesParamsJson = response.getContent().getAsJsonArray().get(0).getAsJsonArray();
                location = new GeoCoordinates(geoCoordinatesParamsJson.get(0).getAsDouble(), geoCoordinatesParamsJson.get(1).getAsDouble());

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
        return location;
    }

    /**
     * Get the datetime associated to the image (Http GET)
     * @return Datetime in which the image was created
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public GregorianCalendar getDatetime() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        if (datetime == null) {
            try {
                Response response = new Request(RequestMethod.GET, CallPath.IMAGE_UNIXTIME.getValue(),
                        new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))).execute();

                if (response == null || !response.isSuccess()) {
                    System.out.println("Unsuccessful Request");
                    throw new UnsuccessfulHttpRequestException();
                }

                datetime = new GregorianCalendar();
                datetime.setTimeInMillis(response.getContent().getAsJsonArray().get(0).getAsLong() * 1000);

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
        return datetime;
    }

    /**
     * Get the size of the image as its width and length
     * @return (width,length) pair
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public ImageSize getSize() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        if (size == null) {
            try {
                Response response = new Request(RequestMethod.GET, CallPath.IMAGE_SIZE.getValue(),
                        new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))).execute();

                if (response == null || !response.isSuccess()) {
                    System.out.println("Unsuccessful Request");
                    throw new UnsuccessfulHttpRequestException();
                }

                JsonArray sizeParamsJson = response.getContent().getAsJsonArray().get(0).getAsJsonArray();
                size = new ImageSize(sizeParamsJson.get(0).getAsInt(), sizeParamsJson.get(1).getAsInt());

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
        return size;
    }

    /**
     * Get a note associated to the image. A note is an object containing a string in which it is possible to store
     * additional information about the image.
     * @return Note
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public Note getNote() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        if (note == null) {
            try {
                Response response = new Request(RequestMethod.GET, CallPath.IMAGE_NOTE.getValue(),
                        new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))).execute();

                if (response == null || !response.isSuccess()) {
                    System.out.println("Unsuccessful Request");
                    throw new UnsuccessfulHttpRequestException();
                }

                note = new Note(response.getContent().getAsJsonArray().get(0).getAsString());

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
        return note;
    }

    /**
     * Get all the annotations on Ibeis server that are associated to the image
     * @return List of all the annotations associated to the image
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisAnnotation> getAnnotations() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        if (annotations == null) {
            try {
                Response response = new Request(RequestMethod.GET, CallPath.IMAGE_ANNOTATIONS.getValue(),
                        new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))).execute();

                if (response == null || !response.isSuccess()) {
                    System.out.println("Unsuccessful Request");
                    throw new UnsuccessfulHttpRequestException();
                }

                annotations = new ArrayList<>();
                for (JsonElement annotationIdJson : response.getContent().getAsJsonArray().get(0).getAsJsonArray()) {
                    annotations.add(new IbeisAnnotation(annotationIdJson.getAsInt()));
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
        }
        return annotations;
    }

    /**
     * Get all the annotations on Ibeis server, corresponding to a certain species, that are associated to the image
     * @param species
     * @return List of all the annotations, corresponding to the species passed as parameter, associated to the image
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisAnnotation> getAnnotationsOfSpecies(Species species) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.IMAGE_ANNOTATIONS_OF_SPECIES.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))
                                        .addParameter(new Parameter(ParamName.SPECIES.getValue(), species.getValue()))).execute();

            if (response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            List<IbeisAnnotation> annotationsOfSpecies = new ArrayList<>();
            for (JsonElement annotationIdJson : response.getContent().getAsJsonArray().get(0).getAsJsonArray()) {
                annotationsOfSpecies.add(new IbeisAnnotation(annotationIdJson.getAsInt()));
            }
            return annotationsOfSpecies;

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

    /**
     * Set the location of the image (Http PUT)
     * @param location
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public void setLocation(GeoCoordinates location) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        this.location = location;

        //push to server
        try {
            Response response = new Request(RequestMethod.PUT, CallPath.IMAGE_GPS.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))
                            .addParameter(new Parameter(ParamName.LAT_LIST.getValue(), location.getLatitude()))
                            .addParameter(new Parameter(ParamName.LAT_LIST.getValue(), location.getLongitude()))).execute();

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
    }

    /**
     * Set the datetime of the image (Http PUT)
     * @param datetime
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public void setDatetime(GregorianCalendar datetime) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        this.datetime = datetime;

        //push to server
        try {
            Response response = new Request(RequestMethod.PUT, CallPath.IMAGE_UNIXTIME.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))
                            .addParameter(new Parameter(ParamName.UNIXTIME_LIST.getValue(), datetime.getTimeInMillis()  / 1000))).execute();

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
    }

    /**
     * Set a note associated to the image (Http PUT)
     * @param note
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public void setNote(Note note) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        this.note = note;

        //push to server
        try {
            Response response = new Request(RequestMethod.PUT, CallPath.IMAGE_NOTE.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))
                            .addParameter(new Parameter(ParamName.NOTES_LIST.getValue(), note.getText()))).execute();

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
    }
}
