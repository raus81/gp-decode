package service;

import model.CertificateX509;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InfoSync {


    public static final String X_KID = "X-KID";
    public static final String X_RESUME_TOKEN = "X-RESUME-TOKEN";

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

    public void downloadKeys() throws IOException {
        ChronicleMap<CharSequence, CertificateX509> certificateStore = getCertificateStore();
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
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
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

            certificateStore.put(kid,cert);

        }
        /*

        RestTemplate restTemplate = new RestTemplate();
        while (true) {
            HttpHeaders headers = new HttpHeaders();
            if (resumeToken != null) {
                headers.set(X_RESUME_TOKEN, resumeToken);
            }
            //Create a new HttpEntity
            final HttpEntity<String> entity = new HttpEntity<String>(headers);
            //Execute the method writing your HttpEntity to the request
            ResponseEntity<String> response = restTemplate.exchange(gpUrl, HttpMethod.GET, entity, String.class);
            //System.out.println(response.getBody());
            if (response.getBody() == null || response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
                break;
            }
            nextToken = response.getHeaders()
                    .get(X_RESUME_TOKEN).stream()
                    .findFirst().orElse(null);
            String xKid = response.getHeaders().
                    get(X_KID).stream()
                    .findFirst().orElse(null);
            saveCertificate(response);
            if (nextToken == resumeToken) {
                break;
            }
            resumeToken = nextToken;
        }
        return true;
*/
        //System.out.println(certificates);
    }




}
