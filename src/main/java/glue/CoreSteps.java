package glue;

import io.cucumber.java.en.When;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CoreSteps {

    CoreFunctions getCore(){
        return CoreFunctions.CoreFunctions;
    }

    @When("^I import elements from files (.*)$")
    public void when_I_import_elements_from_files(String filesPathWithName){
        List<String> files = Arrays.stream(filesPathWithName.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        getCore().when_I_import_elements(files);
    }

}
