package service;

import model.CertificateX509;
import org.junit.jupiter.api.Test;
import store.CertificateStore;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InfoSyncTest {

    @Test
    void downloadKeys() throws IOException {
        InfoSync infoSync = new InfoSync();
        infoSync.downloadKeys();
        ;

    }

    @Test
    void downloadVerificationData() throws IOException {
        InfoSync infoSync = new InfoSync();
        infoSync.downloadVerificationData();

    }


    @Test
    void syncDataTest() throws IOException, ClassNotFoundException {

        CertificateStore store = new CertificateStore();
        CertificateX509 item = new CertificateX509();
        item.setCertificate("aaaa");
        store.putItem("test", item);

        CertificateX509 item2 = store.getItem("test");

        System.out.println(item2);
    }
}