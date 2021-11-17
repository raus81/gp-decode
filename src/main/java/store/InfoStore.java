package store;

import model.decoder.PassInfo;

public class InfoStore extends BaseStore<PassInfo> {
    public InfoStore() {
        baseName = "info";
    }
}
