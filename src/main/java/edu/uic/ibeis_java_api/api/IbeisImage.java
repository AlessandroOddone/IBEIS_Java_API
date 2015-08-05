package edu.uic.ibeis_java_api.api;

import android.org.apache.commons.codec.binary.Base64;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import edu.uic.ibeis_java_api.api.data.GeoCoordinates;
import edu.uic.ibeis_java_api.api.data.image.ImageNotes;
import edu.uic.ibeis_java_api.api.data.image.ImageSize;
import edu.uic.ibeis_java_api.api.data.image.RawImage;
import edu.uic.ibeis_java_api.exceptions.*;
import edu.uic.ibeis_java_api.http.*;
import edu.uic.ibeis_java_api.values.CallPath;
import edu.uic.ibeis_java_api.values.ParamName;
import edu.uic.ibeis_java_api.values.Species;
import edu.uic.ibeis_java_api.values.SupportedImageFileType;

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

    private long id;

    protected IbeisImage(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    /**
     * Get the raw image associated to the IbeisImage object
     * @return RawImage object, containing image bytes and file type information
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public RawImage getRawImage() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException, UnsupportedImageFileTypeException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.IMAGE.getValue() + id + "/").execute();

            if (response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            String responseString = response.getContent().getAsString();
            String responseFileTypeString = responseString.substring(responseString.indexOf("/") + 1, responseString.indexOf(";"));
            String responseBase64EncodedImageString = responseString.substring(responseString.indexOf(",") + 1);

            SupportedImageFileType imageFileType = null;
            for (SupportedImageFileType ft : SupportedImageFileType.values()) {
                if (ft.getValues().contains(responseFileTypeString)) {
                    imageFileType = ft;
                    break;
                }
            }

            if(imageFileType == null) {
                throw new UnsupportedImageFileTypeException();
            }

            return new RawImage(imageFileType, Base64.decodeBase64(responseBase64EncodedImageString));

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
     * Get the GPS position (geographic coordinates) of the image
     * @return (latitude,longitude) pair
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public GeoCoordinates getGpsPosition() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.IMAGE_GPS.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess() || response.getContent().getAsJsonArray().get(0).isJsonNull()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            JsonArray geoCoordinatesParamsJson = response.getContent().getAsJsonArray().get(0).getAsJsonArray();
            return new GeoCoordinates(geoCoordinatesParamsJson.get(0).getAsDouble(), geoCoordinatesParamsJson.get(1).getAsDouble());

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
     * Get the datetime associated to the image
     * @return Datetime in which the image was created
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public GregorianCalendar getDatetime() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.IMAGE_UNIXTIME.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess() || response.getContent().getAsJsonArray().get(0).isJsonNull()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            GregorianCalendar datetime = new GregorianCalendar();
            datetime.setTimeInMillis(response.getContent().getAsJsonArray().get(0).getAsLong() * 1000);
            return datetime;

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
     * Get the size of the image as its width and length
     * @return (width,length) pair
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public ImageSize getSize() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.IMAGE_SIZE.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess() || response.getContent().getAsJsonArray().get(0).isJsonNull()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            JsonArray sizeParamsJson = response.getContent().getAsJsonArray().get(0).getAsJsonArray();
            return new ImageSize(sizeParamsJson.get(0).getAsInt(), sizeParamsJson.get(1).getAsInt());

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
     * Get notes associated to the image. Notes are additional information about the image.
     * @return ImageNotes object
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public ImageNotes getNotes() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.IMAGE_NOTES.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess() || response.getContent().getAsJsonArray().get(0).isJsonNull()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            return ImageNotes.fromJsonString(response.getContent().getAsJsonArray().get(0).getAsString());

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
     * Get all the annotations in Ibeis database that are associated to the image
     * @return List of all the annotations associated to the image
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisAnnotation> getAnnotations() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.IMAGE_ANNOTATIONS.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            List<IbeisAnnotation> annotations = new ArrayList<>();
            for (JsonElement annotationIdJson : response.getContent().getAsJsonArray().get(0).getAsJsonArray()) {
                annotations.add(new IbeisAnnotation(annotationIdJson.getAsLong()));
            }
            return annotations;

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
     * Get all the annotations in Ibeis database, corresponding to a certain species, that are associated to the image
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
                annotationsOfSpecies.add(new IbeisAnnotation(annotationIdJson.getAsLong()));
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
     * Get all the encounters the image is included in
     * @return List of all the encounters associated to the image
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisEncounter> getEncounters() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.IMAGE_EIDS.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            List<IbeisEncounter> encounters = new ArrayList<>();
            for (JsonElement encounterIdJson : response.getContent().getAsJsonArray().get(0).getAsJsonArray()) {
                encounters.add(new IbeisEncounter(encounterIdJson.getAsLong()));
            }
            return encounters;

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
     * Set the GPS position of the image
     * @param location
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public void setGpsPosition(GeoCoordinates location) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.PUT, CallPath.IMAGE_GPS.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))
                            .addParameter(new Parameter(ParamName.LAT_LIST.getValue(), location.getLatitude()))
                            .addParameter(new Parameter(ParamName.LON_LIST.getValue(), location.getLongitude()))).execute();

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
     * Set the datetime of the image
     * @param datetime
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public void setDatetime(GregorianCalendar datetime) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
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
     * Set an ImageNotes object associated to the image
     * @param imageNotes
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public void setNotes(ImageNotes imageNotes) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.PUT, CallPath.IMAGE_NOTES.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))
                            .addParameter(new Parameter(ParamName.NOTES_LIST.getValue(), imageNotes.toJsonString()))).execute();

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
     * Add the image to an Encounter
     * @param encounter
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public void addToEncounter(IbeisEncounter encounter) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.PUT, CallPath.IMAGE_EIDS.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.GID_LIST.getValue(), id))
                            .addParameter(new Parameter(ParamName.EID_LIST.getValue(), encounter.getId()))).execute();

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
     * Returns true if the image is in Ibeis database, false otherwise
     * @return
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public boolean isValidImage() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException
    {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.IMAGE.getValue()).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            for (JsonElement imageIdJson : response.getContent().getAsJsonArray()) {
                if(this.id == imageIdJson.getAsLong()) {
                    return true;
                }
            }
            return false;

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
