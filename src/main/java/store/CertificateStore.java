package store;

import model.CertificateX509;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Base64;

public class CertificateStore extends BaseStore<CertificateX509> {
    public CertificateStore(String baseUrl) {
        super(baseUrl);
        baseName = "cert";
    }

    @NotNull
    private String getHexKey(String key) {
        byte[] bytes = Base64.getDecoder().decode(key.getBytes());
        String hexKey = Hex.encodeHexString(bytes);
        return hexKey;
    }

    @Override
    public CertificateX509 putItem(String key, CertificateX509 item) throws IOException {
        String hexKey = getHexKey(key);
        return super.putItem(hexKey, item);
    }

    @Override
    public CertificateX509 getItem(String key) throws IOException, ClassNotFoundException {
        String hexKey = getHexKey(key);
        return super.getItem(hexKey);
    }
}
