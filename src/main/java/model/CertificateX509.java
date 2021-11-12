package model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CertificateX509 implements Serializable {
    private String kid;
    private String certificate;
    private String resumeToken;
}
