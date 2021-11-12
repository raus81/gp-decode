package model.decoder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Person {

    @JsonProperty("fnt")
    String standardisedFamilyName;

    @JsonProperty("fn")
    String familyName;

    @JsonProperty("gnt")
    String standardisedGivenName;

    @JsonProperty("gn")
    String givenName;

    public String getFullName() {
        return givenName + " " + familyName;
    }

}