package service;

import model.CertificateX509;
import model.decoder.PassInfo;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import store.CertificateStore;
import store.InfoStore;

import java.io.File;
import java.io.IOException;

public class StoreService {
    private String baseUrl = "c:/gamma/api/store";

    public StoreService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public StoreService() {

    }

    public CertificateStore getCertificateStore() throws IOException {

        return new CertificateStore(baseUrl);
//        String filename = getClass().getClassLoader().getResource("").getPath() + "keys.dat";
//        File keyPath = new File(filename);
//
//        return ChronicleMapBuilder
//                .of(CharSequence.class, CertificateX509.class)
//                .name("certificate-list")
//                .entries(300)
//                .averageKeySize(50)
//                .averageValueSize(1000)
//                .createOrRecoverPersistedTo(keyPath, false);
//
//


    }


    public InfoStore getPassInfoStore() throws IOException {
        return new InfoStore(baseUrl);
//        String filename = getClass().getClassLoader().getResource("").getPath() + "info.dat";
//        File keyPath = new File(filename);
//
//        return ChronicleMapBuilder
//                .of(CharSequence.class, PassInfo.class)
//                .name("pass-info")
//                .entries(100)
//                .averageKeySize(10)
//                .averageValueSize(50)
//                .createOrRecoverPersistedTo(keyPath, false);
    }


}
