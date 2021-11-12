package model.decoder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecoveryStatement {

    @JsonProperty("tg")
    String disease;

    @JsonProperty("fr")
    String dateOfFirstPositiveTest;

    @JsonProperty("co")
    String countryOfVaccination;

    @JsonProperty("is")
    String certificateIssuer;

    @JsonProperty("df")
    String certificateValidFrom;

    @JsonProperty("du")
    String certificateValidUntil;

    @JsonProperty("ci")
    String certificateIdentifier;
}
