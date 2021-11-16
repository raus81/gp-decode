package service;

import exception.DataErrorException;
import model.CertificateX509;
import model.VaccineInfo;
import model.decoder.PassInfo;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.io.File;
import java.io.IOException;

public class KeyService {

    public static final String VACCINE_START_DAY_COMPLETE = "vaccine_start_day_complete";
    public static final String VACCINE_END_DAY_COMPLETE = "vaccine_end_day_complete";
    public static final String VACCINE_END_DAY_NOT_COMPLETE = "vaccine_end_day_not_complete";
    public static final String VACCINE_START_DAY_NOT_COMPLETE = "vaccine_start_day_not_complete";

    public CertificateX509 getFromKid(String kid) throws IOException {
        StoreService sService = new StoreService();
        return sService.getCertificateStore().get(kid);
    }


    public VaccineInfo getFromName(String vaccineName) throws DataErrorException {

        ChronicleMap<CharSequence, PassInfo> passInfoStore = null;
        try {
            passInfoStore = new StoreService().getPassInfoStore();
        } catch (IOException e) {
            throw new DataErrorException("Impostazioni non trovate");
        }

        VaccineInfo vi = new VaccineInfo();

        PassInfo startComplete = passInfoStore.get(vaccineName + "_" + VACCINE_START_DAY_COMPLETE);
        if (startComplete != null) {
            vi.setVaccineStartDayComplete(startComplete.getValue());
        }
        PassInfo endComplete = passInfoStore.get(vaccineName + "_" + VACCINE_END_DAY_COMPLETE);
        if (endComplete != null) {
            vi.setVaccineEndDayComplete(endComplete.getValue());
        }
        PassInfo startNotComplete = passInfoStore.get(vaccineName + "_" + VACCINE_START_DAY_NOT_COMPLETE);
        if (startNotComplete != null) {
            vi.setVaccineStartDayNotComplete(startNotComplete.getValue());
        }
        PassInfo endNotComplete = passInfoStore.get(vaccineName + "_" + VACCINE_END_DAY_NOT_COMPLETE);
        if (endNotComplete != null) {
            vi.setVaccineEndDayNotComplete(endNotComplete.getValue());
        }

        return vi;
    }
}
