package model;


import lombok.Data;
import model.decoder.CertificateType;
import model.decoder.GreenCertificate;

@Data
public class CertificateModel {
    private GreenCertificate certificate;
    private boolean cborValid = false;
    private GreenPassCertificateStatus status;
    private CertificateType type;

    public String getStatusMessage() {
        String message = "Non valido";
        switch (status) {
            case VALID:
                message = "Valido";
                break;
            case NOT_VALID:
                message = "Non valido";
                break;
            case NOT_VALID_YET:
                message = "Non ancora valido";
                break;
            case NOT_GREEN_PASS:
                message = "Non greenpass";
                break;
            case PARTIALLY_VALID:
                message = "Parzialmente valido";
                break;
        }
        return message;
    }
}
