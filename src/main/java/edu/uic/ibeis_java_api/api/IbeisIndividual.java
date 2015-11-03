package edu.uic.ibeis_java_api.api;

import com.google.gson.JsonElement;
import edu.uic.ibeis_java_api.api.individual.IndividualNotes;
import edu.uic.ibeis_java_api.exceptions.AuthorizationHeaderException;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.InvalidHttpMethodException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.http.*;
import edu.uic.ibeis_java_api.values.CallPath;
import edu.uic.ibeis_java_api.values.ParamName;
import edu.uic.ibeis_java_api.values.Sex;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * An individual on Ibeis server
 */
public class IbeisIndividual {

    private long id;

    protected IbeisIndividual(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    /**
     * Get the name of the individual
     * @return Name
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public String getName() throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.GET, CallPath.INDIVIDUAL_NAME.getValue(),
                    new HttpParametersList().addParameter(new HttpParameter(ParamName.NAME_ROWID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }
            return response.getContent().getAsJsonArray().get(0).getAsString();

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
     * Get the sex of the individual
     * @return Sex
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public Sex getSex() throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.GET, CallPath.INDIVIDUAL_SEX.getValue(),
                    new HttpParametersList().addParameter(new HttpParameter(ParamName.NAME_ROWID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            String sexString = response.getContent().getAsJsonArray().get(0).getAsString();
            if(sexString.equals(Integer.toString(Sex.MALE.getValue()))) {
                return Sex.MALE;
            }
            else if(sexString.equals(Integer.toString(Sex.FEMALE.getValue()))) {
                return Sex.FEMALE;
            }
            else {
                return Sex.UNKNOWN;
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
     * Get notes associated to the individual. Notes are additional information about the individual.
     * @return IndividualNotes object
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public IndividualNotes getNotes() throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.GET, CallPath.INDIVIDUAL_NOTES.getValue(),
                    new HttpParametersList().addParameter(new HttpParameter(ParamName.NAME_ROWID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess() || response.getContent().getAsJsonArray().get(0).isJsonNull()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }
            return IndividualNotes.fromJsonString(response.getContent().getAsJsonArray().get(0).getAsString());

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
     * Get all the images in which the individual appears
     * @return List of IbeisImage elements
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisImage> getImages() throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.GET, CallPath.INDIVIDUAL_IMAGES.getValue(),
                    new HttpParametersList().addParameter(new HttpParameter(ParamName.NID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            List<IbeisImage> images = new ArrayList<>();
            for (JsonElement imageIdJson : response.getContent().getAsJsonArray().get(0).getAsJsonArray()) {
                images.add(new IbeisImage(imageIdJson.getAsLong()));
            }
            return images;

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
     * Get all the annotations in which the individual appears
     * @return List of IbeisAnnotation elements
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisAnnotation> getAnnotations() throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.GET, CallPath.INDIVIDUAL_ANNOTATIONS.getValue(),
                    new HttpParametersList().addParameter(new HttpParameter(ParamName.NID_LIST.getValue(), id))).execute();

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
     * Set the name of the individual
     * @param name
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public void setName(String name) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.PUT, CallPath.INDIVIDUAL_NAME.getValue(),
                    new HttpParametersList().addParameter(new HttpParameter(ParamName.NAME_ROWID_LIST.getValue(), id))
                            .addParameter(new HttpParameter(ParamName.NAME_TEXT_LIST.getValue(), name))).execute();

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
     * Set the sex of the individual
     * @param sex
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public void setSex(Sex sex) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.PUT, CallPath.INDIVIDUAL_SEX.getValue(),
                    new HttpParametersList().addParameter(new HttpParameter(ParamName.NAME_ROWID_LIST.getValue(), id))
                            .addParameter(new HttpParameter(ParamName.NAME_SEX_LIST.getValue(), sex.getValue()))).execute();

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
     * Set an IndividualNotes object associated to the individual
     * @param individualNotes
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public void setIndividualNotes(IndividualNotes individualNotes) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.PUT, CallPath.INDIVIDUAL_NOTES.getValue(),
                    new HttpParametersList().addParameter(new HttpParameter(ParamName.NAME_ROWID_LIST.getValue(), id))
                            .addParameter(new HttpParameter(ParamName.NOTES_LIST.getValue(), individualNotes.toJsonString()))).execute();

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
     * Returns true if the individual is on Ibeis server, false otherwise
     * @return
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public boolean isValidIndividual() throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException
    {
        try {
            HttpResponse response = new HttpRequest(HttpRequestMethod.GET, CallPath.INDIVIDUALS.getValue()).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            for (JsonElement individualIdJson : response.getContent().getAsJsonArray()) {
                if(this.id == individualIdJson.getAsLong()) {
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
        if(obj instanceof IbeisIndividual) {
            if(((IbeisIndividual) obj).getId() == this.getId()) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }
}
