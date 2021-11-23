package service;

import exception.DataErrorException;
import model.CertificateX509;
import model.TestInfo;
import model.VaccineInfo;
import model.decoder.PassInfo;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import store.InfoStore;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class KeyService {

    private String baseUrl = "c:/gamma/api/store";

    public KeyService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public KeyService() {
    }

    public static final String VACCINE_START_DAY_COMPLETE = "vaccine_start_day_complete";
    public static final String VACCINE_END_DAY_COMPLETE = "vaccine_end_day_complete";
    public static final String VACCINE_END_DAY_NOT_COMPLETE = "vaccine_end_day_not_complete";
    public static final String VACCINE_START_DAY_NOT_COMPLETE = "vaccine_start_day_not_complete";

    public CertificateX509 getFromKid(String kid) throws IOException, ClassNotFoundException {
        StoreService sService = new StoreService(baseUrl);
        return sService.getCertificateStore().getItem(kid);
    }


    public TestInfo getTestInfoFromName(String testName) throws DataErrorException, IOException, ClassNotFoundException {
        TestInfo ti = new TestInfo();
        InfoStore passInfoStore = null;
        try {
            passInfoStore = new StoreService(baseUrl).getPassInfoStore();
        } catch (IOException e) {
            throw new DataErrorException("Impostazioni non trovate");
        }
        String prefix = "";
        if (testName.equals("LP217198-3")) {
            prefix = "rapid";
        } else if (testName.equals("LP6464-4")) {
            prefix = "molecular";
        } else {
            throw new DataErrorException("Tipo di test non trovato");
        }
        //TODO: handling missing info error
        PassInfo startHour = passInfoStore.getItem("GENERIC_" + prefix + "_test_start_hours");
        if (startHour != null) {
            ti.setStartHours(startHour.getValue());
        }

        PassInfo endHour = passInfoStore.getItem("GENERIC_" + prefix + "_test_end_hours");
        if (endHour != null) {
            ti.setEndHours(endHour.getValue());
        }

        return ti;

    }

    public VaccineInfo getVaccineInfoFromName(String vaccineName) throws DataErrorException, IOException, ClassNotFoundException {

        InfoStore passInfoStore = null;
        try {
            passInfoStore = new StoreService(baseUrl).getPassInfoStore();
        } catch (IOException e) {
            throw new DataErrorException("Informazioni del ministero non trovate");
        }

        VaccineInfo vi = new VaccineInfo();

        //TODO: handling missing info error

        PassInfo startComplete = passInfoStore.getItem(vaccineName + "_" + VACCINE_START_DAY_COMPLETE);
        if (startComplete != null) {
            vi.setVaccineStartDayComplete(startComplete.getValue());
        }
        PassInfo endComplete = passInfoStore.getItem(vaccineName + "_" + VACCINE_END_DAY_COMPLETE);
        if (endComplete != null) {
            vi.setVaccineEndDayComplete(endComplete.getValue());
        }
        PassInfo startNotComplete = passInfoStore.getItem(vaccineName + "_" + VACCINE_START_DAY_NOT_COMPLETE);
        if (startNotComplete != null) {
            vi.setVaccineStartDayNotComplete(startNotComplete.getValue());
        }
        PassInfo endNotComplete = passInfoStore.getItem(vaccineName + "_" + VACCINE_END_DAY_NOT_COMPLETE);
        if (endNotComplete != null) {
            vi.setVaccineEndDayNotComplete(endNotComplete.getValue());
        }

        return vi;
    }
}
