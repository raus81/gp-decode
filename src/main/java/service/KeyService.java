package service;

import model.CertificateX509;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.io.File;
import java.io.IOException;

public class KeyService {

    private ChronicleMap<CharSequence, CertificateX509> getCertificateStore() throws IOException {
        String filename = getClass().getClassLoader().getResource("").getPath() + "keys.dat";
        File keyPath = new File(filename);

        return ChronicleMapBuilder
                .of(CharSequence.class, CertificateX509.class)
                .name("certificate-list")
                .entries(300)
                .averageKeySize(50)
                .averageValueSize(1000)
                .createOrRecoverPersistedTo(keyPath, false);
    }

    public CertificateX509 getFromKid( String kid ) throws IOException {
        return getCertificateStore().get(kid);
    }
}
