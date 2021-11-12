package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.upokecenter.cbor.CBORObject;
import model.CertificateModel;
import model.CertificateX509;
import model.decoder.GreenCertificate;
import nl.minvws.encoding.Base45;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class DecodeService {

    private byte[] getBytes(String rawCose) throws Exception {
        rawCose = rawCose.replaceAll("^HC1:", "");
        byte[] data = null;

        try {
            byte[] bytecompressed = Base45.getDecoder().decode(rawCose);
            Inflater inflater = new Inflater();
            inflater.setInput(bytecompressed);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytecompressed.length);
            byte[] buffer = new byte[512];
            while (!inflater.finished()) {
                final int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            data = outputStream.toByteArray();
        } catch (DataFormatException e) {
            throw new Exception("Impossibile decodificare QR code");
        }

        return data;
    }

    public GreenCertificate decodeGreenPass(String rawCode) throws Exception {
        byte[] data = getBytes(rawCode);
        CBORObject messageObject = CBORObject.DecodeFromBytes(data);
        byte[] rawContent = messageObject.get(2).GetByteString();

        CBORObject cborContent = CBORObject.DecodeFromBytes(rawContent)
                .get(-260).get(1);

        String json = cborContent.ToJSONString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        return objectMapper.readValue(json, GreenCertificate.class);


        //return new HashMap<>();
        //return cborObject;
//        Message message = Sign1Message.DecodeFromBytes(outputStream.toByteArray());
//
//        Message a = Encrypt0Message.DecodeFromBytes(outputStream.toByteArray());
//        byte[] bytes = a.GetContent();
//        for( byte b: bytes){
//            System.out.print(b + ",");
//        }
//        System.out.println("");
//
//        CborMap cborMap = CborMap.createFromCborByteArray(a.GetContent());
//        //CborObject cborObject = CborObject.createFromCborByteArray(a.GetContent());
//        // CBORObject cborObject = CBORObject.DecodeFromBytes(a.GetContent());
//        System.out.println(cborMap.toString(2));
//        return cborMap.toJavaObject();

        // return "";
    }

    private String getKid(byte[] protectedHeader, CBORObject unprotectedHeader) {
        CBORObject kid;
        int key = 4;// HeaderKeys.KID.AsCBOR()
        if (protectedHeader.length > 0) {
            try {
                kid = CBORObject.DecodeFromBytes(protectedHeader).get(key);
                if (kid == null) {
                    kid = unprotectedHeader.get(key);
                }
            } catch (Exception ex) {
                kid = unprotectedHeader.get(key);
            }
        } else {
            kid = unprotectedHeader.get(key);
        }
        return Base64.encode(kid.GetByteString());
    }

    public PublicKey getCertificateFromKid(String kid) throws IOException {
        
        KeyService ks = new KeyService();
        CertificateX509 x509Cert = ks.getFromKid(kid);

       // String x509Cert = "MIIEDzCCAfegAwIBAgIURldu5rsfrDeZtDBxrJ+SujMr2IswDQYJKoZIhvcNAQELBQAwSTELMAkGA1UEBhMCSVQxHzAdBgNVBAoMFk1pbmlzdGVybyBkZWxsYSBTYWx1dGUxGTAXBgNVBAMMEEl0YWx5IERHQyBDU0NBIDEwHhcNMjEwNTEyMDgxODE3WhcNMjMwNTEyMDgxMTU5WjBIMQswCQYDVQQGEwJJVDEfMB0GA1UECgwWTWluaXN0ZXJvIGRlbGxhIFNhbHV0ZTEYMBYGA1UEAwwPSXRhbHkgREdDIERTQyAxMFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEnL9+WnIp9fvbcocZSGUFlSw9ffW/jbMONzcvm1X4c+pXOPEs7C4/83+PxS8Swea2hgm/tKt4PI0z8wgnIehoj6OBujCBtzAfBgNVHSMEGDAWgBS+VOVpXmeSQImXYEEAB/pLRVCw/zBlBgNVHR8EXjBcMFqgWKBWhlRsZGFwOi8vY2Fkcy5kZ2MuZ292Lml0L0NOPUl0YWx5JTIwREdDJTIwQ1NDQSUyMHhcMSxPPU1pbmlzdGVybyUyMGRlbGxhJTIwU2FsdXRlLEM9SVQwHQYDVR0OBBYEFC4bAbCvpArrgZ0E+RrqS8V7TNNIMA4GA1UdDwEB/wQEAwIHgDANBgkqhkiG9w0BAQsFAAOCAgEAjxTeF7yhKz/3PKZ9+WfgZPaIzZvnO/nmuUartgVd3xuTPNtd5tuYRNS/1B78HNNk7fXiq5hH2q8xHF9yxYxExov2qFrfUMD5HOZzYKHZcjcWFNHvH6jx7qDCtb5PrOgSK5QUQzycR7MgWIFinoWwsWIrA1AJOwfUoi7v1aoWNMK1eHZmR3Y9LQ84qeE2yDk3jqEGjlJVCbgBp7O8emzy2KhWv3JyRZgTmFz7p6eRXDzUYHtJaufveIhkNM/U8p3S7egQegliIFMmufvEyZemD2BMvb97H9PQpuzeMwB8zcFbuZmNl42AFMQ2PhQe27pU0wFsDEqLe0ETb5eR3T9L6zdSrWldw6UuXoYV0/5fvjA55qCjAaLJ0qi16Ca/jt6iKuws/KKh9yr+FqZMnZUH2D2j2i8LBA67Ie0JoZPSojr8cwSTxQBdJFI722uczCj/Rt69Y4sLdV3hNQ2A9hHrXesyQslr0ez3UHHzDRFMVlOXWCayj3LIgvtfTjKrT1J+/3Vu9fvs1+CCJELuC9gtVLxMsdRc/A6/bvW4mAsyY78ROX27Bi8CxPN5IZbtiyjpmdfr2bufDcwhwzdwsdQQDoSiIF1LZqCn7sHBmUhzoPcBJdXFET58EKow0BWcerZzpvsVHcMTE2uuAUr/JUh1SBpoJCiMIRSl+XPoEA2qqYU=";
        byte[] in = java.util.Base64.getDecoder().decode(x509Cert.getCertificate());
        InputStream inputStream = new ByteArrayInputStream(in);

        Certificate certificate = null;
        try {
            certificate = CertificateFactory.getInstance("X.509")
                    .generateCertificate(inputStream);
            return certificate.getPublicKey();
        } catch (CertificateException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] transcodeSignatureToDER(byte[] jwsSignature)
            throws Exception {

        // Adapted from org.apache.xml.security.algorithms.implementations.SignatureECDSA

        int rawLen = jwsSignature.length / 2;

        int i;

        for (i = rawLen; (i > 0) && (jwsSignature[rawLen - i] == 0); i--) {
            // do nothing
        }

        int j = i;

        if (jwsSignature[rawLen - i] < 0) {
            j += 1;
        }

        int k;

        for (k = rawLen; (k > 0) && (jwsSignature[2 * rawLen - k] == 0); k--) {
            // do nothing
        }

        int l = k;

        if (jwsSignature[2 * rawLen - k] < 0) {
            l += 1;
        }

        int len = 2 + j + 2 + l;

        if (len > 255) {
            throw new Exception("Invalid ECDSA signature format");
        }

        int offset;

        final byte[] derSignature;

        if (len < 128) {
            derSignature = new byte[2 + 2 + j + 2 + l];
            offset = 1;
        } else {
            derSignature = new byte[3 + 2 + j + 2 + l];
            derSignature[1] = (byte) 0x81;
            offset = 2;
        }

        derSignature[0] = 48;
        derSignature[offset++] = (byte) len;
        derSignature[offset++] = 2;
        derSignature[offset++] = (byte) j;

        System.arraycopy(jwsSignature, rawLen - i, derSignature, (offset + j) - i, i);

        offset += j;

        derSignature[offset++] = 2;
        derSignature[offset++] = (byte) l;

        System.arraycopy(jwsSignature, 2 * rawLen - k, derSignature, (offset + l) - k, k);

        return derSignature;
    }


    private boolean verifySignature(String rawCose) {
        try {
            byte[] cborDate = getBytes(rawCose);

            CBORObject messageObject = CBORObject.DecodeFromBytes(cborDate);
            byte[] coseSignature = messageObject.get(3).GetByteString();
            byte[] protectedHeader = messageObject.get(0).GetByteString();
            CBORObject unprotectedHeader = messageObject.get(1);
            byte[] content = messageObject.get(2).GetByteString();
            CBORObject cborObject = CBORObject.NewArray();
            cborObject.Add("Signature1");
            cborObject.Add(protectedHeader);
            cborObject.Add(new byte[]{});
            cborObject.Add(content);
            byte[] dataToValidate = cborObject.EncodeToBytes();

            String kid = getKid(protectedHeader, unprotectedHeader);

            PublicKey pubKey = getCertificateFromKid(kid);
            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
            ecdsaVerify.initVerify(pubKey);

            ecdsaVerify.update(dataToValidate);

            boolean verify = ecdsaVerify.verify(transcodeSignatureToDER(coseSignature));
            return verify;
        } catch (DataFormatException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public CertificateModel getCertificateModel(String rawCose) throws Exception {

        CertificateModel certificateModel = new CertificateModel();

        GreenCertificate gc = decodeGreenPass(rawCose);
        certificateModel.setCertificate(gc);
        certificateModel.setCborValid(verifySignature(rawCose));
        certificateModel.setType(gc.getType());

        //certificateModel.setStatus(checkStatus(gc));


        return certificateModel;
    }
}
