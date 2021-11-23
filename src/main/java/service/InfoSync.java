package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.DataErrorException;
import model.CertificateX509;
import model.decoder.PassInfo;
import net.openhft.chronicle.map.ChronicleMap;
import store.CertificateStore;
import store.InfoStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InfoSync {

    private String baseUrl = "c:/gamma/api/store";

    public InfoSync(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public InfoSync() {
    }

    public static final String X_KID = "X-KID";
    public static final String X_RESUME_TOKEN = "X-RESUME-TOKEN";

    public boolean checkVersion() throws DataErrorException {

        try {
            List<PassInfo> passInfos = getPassInfos();
            PassInfo passInfo1 = passInfos
                    .stream()
                    .filter(passInfo -> passInfo.getName().equals("android") && passInfo.getType().equals("APP_MIN_VERSION"))
                    .findFirst().orElse(null);

            PassInfo passInfoLocal = new StoreService(baseUrl).getPassInfoStore().getItem("APP_MIN_VERSION_android");

            if (passInfo1 == null) {
                throw new DataErrorException("Impossibile scaricare le informazioni di controllo dal Ministero della Salute");
            }

            if (passInfoLocal == null) {
                return false;
            }

            if (!passInfoLocal.getValue().equals(passInfo1.getValue())) {
                return false;
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            throw new DataErrorException("Impossibile verificare la versione delle informazioni locali");
        }
    }

    public void downloadVerificationData() throws IOException {
        StoreService storeService = new StoreService(baseUrl);
        InfoStore passInfoStore = storeService.getPassInfoStore();

        List<PassInfo> properties = getPassInfos();

        PassInfo piDownloadData = new PassInfo();
        piDownloadData.setName("DOWNLOAD_DATE");
        piDownloadData.setType("INFO");
        piDownloadData.setValue(LocalDate.now().format(DateTimeFormatter.ISO_DATE));

        properties.add(piDownloadData);
        properties.forEach(info -> {
            try {
                passInfoStore.putItem(info.getHashKey(), info);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private List<PassInfo> getPassInfos() throws IOException {
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
        return properties;
    }

    public void downloadKeys() throws IOException {
        //StoreService storeService = new StoreService();
        //ChronicleMap<CharSequence, CertificateX509> certificateStore = storeService.getCertificateStore();

        CertificateStore storeService = new StoreService(baseUrl).getCertificateStore();

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

            storeService.putItem(kid, cert);

        }

    }


}
