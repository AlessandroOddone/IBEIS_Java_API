package edu.uic.ibeis_java_api.api;

import com.google.gson.JsonElement;
import edu.uic.ibeis_java_api.api.encounter.EncounterNotes;
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
 * An encounter is a collection of images (IbeisImage) in Ibeis database
 */
public class IbeisEncounter {

    private long id;

    public IbeisEncounter(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    /**
     * Get the title of the collection (Encounter)
     * @return Encounter title
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public String getName() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.ENCOUNTER_NAME.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.EID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess() || response.getContent().getAsJsonArray().get(0).isJsonNull()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            return response.getContent().getAsJsonArray().get(0).getAsString();

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
     * Get notes associated to the encounter. Notes are additional information about the encounter.
     * @return EncounterNotes object
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public EncounterNotes getNotes() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.ENCOUNTER_NOTES.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.ENCOUNTER_ROWID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess() || response.getContent().getAsJsonArray().get(0).isJsonNull()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            return EncounterNotes.fromJsonString(response.getContent().getAsJsonArray().get(0).getAsString());

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
     * Get all the images in the collection (Encounter)
     * @return List of IbeisImage elements
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisImage> getImages() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.ENCOUNTER_IMAGES.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.EID_LIST.getValue(), id))).execute();

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
     * Get all the individuals that appear in the images of the collection (Encounter)
     * @return List of IbeisIndividual elements
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisIndividual> getIndividuals() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.ENCOUNTER_INDIVIDUALS.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.EID_LIST.getValue(), id))).execute();

            if (response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            List<IbeisIndividual> individuals = new ArrayList<>();
            for (JsonElement individualIdJson : response.getContent().getAsJsonArray().get(0).getAsJsonArray()) {
                individuals.add(new IbeisIndividual(individualIdJson.getAsLong()));
            }
            return individuals;

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
     * Set the name of the encounter
     * @param name
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public void setName(String name) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.PUT, CallPath.ENCOUNTER_NAME.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.EID_LIST.getValue(), id))
                            .addParameter(new Parameter(ParamName.ENCOUNTER_TEXT_LIST.getValue(), name))).execute();

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
     * Set an EncounterNotes object associated to the encounter
     * @param encounterNotes
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public void setNotes(EncounterNotes encounterNotes) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.PUT, CallPath.ENCOUNTER_NOTES.getValue(),
                    new ParametersList().addParameter(new Parameter(ParamName.ENCOUNTER_ROWID_LIST.getValue(), id))
                            .addParameter(new Parameter(ParamName.ENCOUNTER_NOTES_LIST.getValue(), encounterNotes.toJsonString()))).execute();

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
     * Returns true if the encounter is in Ibeis database, false otherwise
     * @return
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public boolean isValiEncounter() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException
    {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.ENCOUNTERS.getValue()).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            for (JsonElement encounterIdJson : response.getContent().getAsJsonArray()) {
                if(this.id == encounterIdJson.getAsLong()) {
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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof IbeisEncounter) {
            if(((IbeisEncounter) obj).getId() == this.getId()) return true;
        }
        return false;
    }
}
