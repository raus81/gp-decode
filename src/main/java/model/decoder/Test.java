package model.decoder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Test {

    @JsonProperty("tg")
    String disease;

    @JsonProperty("tt")
    String typeOfTest;

    @JsonProperty("nm")
    String testName;

    @JsonProperty("ma")
    String testNameAndManufacturer;

    @JsonProperty("sc")
    String dateTimeOfCollection;

    @JsonProperty("dr")
    String dateTimeOfTestResult;

    @JsonProperty("tr")
    String testResult;

    @JsonProperty("tc")
    String testingCentre;

    @JsonProperty("co")
    String countryOfVaccination;

    @JsonProperty("is")
    String certificateIssuer;

    @JsonProperty("ci")
    String certificateIdentifier;
}