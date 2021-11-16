package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.CertificateX509;
import model.decoder.PassInfo;
import net.openhft.chronicle.map.ChronicleMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class InfoSync {


    public static final String X_KID = "X-KID";
    public static final String X_RESUME_TOKEN = "X-RESUME-TOKEN";


    public void downloadVerificationData() throws IOException {
        StoreService storeService = new StoreService();
        ChronicleMap<CharSequence, PassInfo> passInfoStore = storeService.getPassInfoStore();
        String settingsUrl = "https://get.dgc.gov.it/v1/dgc/settings";
        URL url = new URL(settingsUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        con.disconnect();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        List<PassInfo> properties = objectMapper.readValue(content.toString(), new TypeReference<List<PassInfo>>() {
        });

        properties.forEach(info -> {
            passInfoStore.put(info.getHashKey(),info);
        });
    }

    public void downloadKeys() throws IOException {
        StoreService storeService = new StoreService();
        ChronicleMap<CharSequence, CertificateX509> certificateStore = storeService.getCertificateStore();
        String gpUrl = "https://get.dgc.gov.it/v1/dgc/signercertificate/update";

        URL url = new URL(gpUrl);
        String resumeToken = null;
        String nextToken = null;
        HttpURLConnection con;

        while (true) {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (resumeToken != null) {
                con.setRequestProperty(X_RESUME_TOKEN, resumeToken);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();


            String kid = con.getHeaderField(X_KID);
            resumeToken = con.getHeaderField(X_RESUME_TOKEN);

            System.out.println("KEY: " + content.toString());


            int status = con.getResponseCode();

            System.out.println("Response code: " + status);
            System.out.println("Kid: " + kid);

            CertificateX509 cert = new CertificateX509();
            cert.setKid(kid);
            cert.setCertificate(content.toString());
            cert.setResumeToken(resumeToken);

            System.out.println("");
            con.disconnect();
            if (status != 200) {
                break;
            }

            certificateStore.put(kid, cert);

        }

    }


}
