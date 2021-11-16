package model.decoder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public LocalDate getValidFrom(){
        return LocalDate.parse(certificateValidFrom, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public LocalDate getValidUntil(){
        return LocalDate.parse(certificateValidUntil, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
