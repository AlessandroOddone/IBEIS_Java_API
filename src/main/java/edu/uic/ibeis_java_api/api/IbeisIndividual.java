package edu.uic.ibeis_java_api.api;

import edu.uic.ibeis_java_api.exceptions.AuthorizationHeaderException;
import edu.uic.ibeis_java_api.exceptions.BadHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.InvalidHttpMethodException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.http.*;
import edu.uic.ibeis_java_api.values.CallPath;
import edu.uic.ibeis_java_api.values.ParamName;
import edu.uic.ibeis_java_api.values.Sex;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public class IbeisIndividual {

    private int id;

    private String name;
    private Sex sex;
    private List<IbeisAnnotation> annotations;

    protected IbeisIndividual(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * Get the name of the individual (Http GET)
     * @return Name
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public String getName() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        if (name == null) {
            Response response;
            try {
                response = new Request(RequestMethod.GET, CallPath.INDIVIDUAL_NAME.getValue(),
                        new ParametersList().addParameter(new Parameter(ParamName.NAME_ROWID_LIST.getValue(), id))).execute();

                if (response == null || !response.isSuccess()) {
                    System.out.println("Unsuccessful Request");
                    throw new UnsuccessfulHttpRequestException();
                }

                name = response.getContent().getAsJsonArray().get(0).getAsString();

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
        return name;
    }

    /**
     * Get the sex of the individual
     * @return Sex
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public Sex getSex() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        if (sex == null) {
            Response response;
            try {
                response = new Request(RequestMethod.GET, CallPath.INDIVIDUAL_SEX.getValue(),
                        new ParametersList().addParameter(new Parameter(ParamName.NAME_ROWID_LIST.getValue(), id))).execute();

                if (response == null || !response.isSuccess()) {
                    System.out.println("Unsuccessful Request");
                    throw new UnsuccessfulHttpRequestException();
                }

                String sexString = response.getContent().getAsJsonArray().get(0).getAsString();
                if(sexString.equals(Sex.MALE.getValue())) {
                    sex = Sex.MALE;
                }
                else if(sexString.equals(Sex.FEMALE.getValue())) {
                    sex = Sex.FEMALE;
                }
                else {
                    sex = Sex.UNKNOWN;
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
        return sex;
    }

    /**
     * Get all the annotations in which the individual appears
     * @return List of IbeisAnnotation elements
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisAnnotation> getAnnotations() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        if (annotations == null) {
            Response response;
            try {
                response = new Request(RequestMethod.GET, CallPath.INDIVIDUAL_ANNOTATIONS.getValue(),
                        new ParametersList().addParameter(new Parameter(ParamName.NID_LIST.getValue(), id))).execute();

                if (response == null || !response.isSuccess()) {
                    System.out.println("Unsuccessful Request");
                    throw new UnsuccessfulHttpRequestException();
                }

                name = response.getContent().getAsJsonArray().get(0).getAsString();

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
}
