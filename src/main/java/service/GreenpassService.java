package service;

public class GreenpassService {
    private String baseUrl = "c:/gamma/api/store";

    public GreenpassService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public GreenpassService() {
    }

    public DecodeService getDecodeService() {
        return new DecodeService(baseUrl);
    }

    public InfoSync getInfoSync() {
        return new InfoSync(baseUrl);
    }
}
