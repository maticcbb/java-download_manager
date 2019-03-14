package pl.sda.downloadmanager;

import java.net.URL;

public interface DownloadEventListener {
    void onStart(URL file);

    void onFinish(URL file);
}
