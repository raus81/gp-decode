package model.decoder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * CBOR structure of the certificate
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GreenCertificate {


    @JsonProperty("ver")
    String schemaVersion;

    @JsonProperty("dob")  @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;

    @JsonProperty("nam")
    Person person;

    @JsonProperty("v")
    List<Vaccination> vaccinations;

    @JsonProperty("t")
    List<Test> tests;

    @JsonProperty("r")
    List<RecoveryStatement> recoveryStatements;


   public  CertificateType getType() {
       if( vaccinations != null && !vaccinations.isEmpty()){
           return CertificateType.VACCINATION;
       }
       if( tests != null && !tests.isEmpty()){
           return CertificateType.TEST;
       }
       if( recoveryStatements != null && !recoveryStatements.isEmpty()){
           return CertificateType.RECOVERY;
       }
       return CertificateType.UNKNOWN;
    }

}
