package model.decoder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Vaccination {

    @JsonProperty("tg")
    String disease;

    @JsonProperty("vp")
    String vaccine;

    @JsonProperty("mp")
    String medicinalProduct;

    @JsonProperty("ma")
    String manufacturer;

    @JsonProperty("dn")
    Integer doseNumber;

    @JsonProperty("sd")
    Integer totalSeriesOfDoses;

    @JsonProperty("dt")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfVaccination;

    @JsonProperty("co")
    String countryOfVaccination;

    @JsonProperty("is")
    String certificateIssuer;

    @JsonProperty("ci")
    String certificateIdentifier;

}