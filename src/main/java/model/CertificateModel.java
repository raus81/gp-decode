package model;


import lombok.Data;
import model.decoder.CertificateType;
import model.decoder.GreenCertificate;

@Data
public class CertificateModel {
    private GreenCertificate certificate;
    private boolean cborValid = false;
    private String status;
    private CertificateType type;
}
