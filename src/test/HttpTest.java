package test;

import edu.uic.http.Parameter;

import java.util.List;

public interface HttpTest extends Test {

    String getCallPath();

    List<Parameter> getParams();
}
