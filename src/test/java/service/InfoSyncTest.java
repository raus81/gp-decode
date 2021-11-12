package service;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InfoSyncTest {

    @Test
    void downloadKeys() throws IOException {
        InfoSync infoSync = new InfoSync();
        infoSync.downloadKeys();;

    }
}