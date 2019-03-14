package pl.sda.downloadmanager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class DownloadService {

    private DownloadManager downloadManager;

    public DownloadService(DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
        this.downloadManager.registerListener(new DownloadEventListener() {
            @Override
            public void onStart(URL file) {
                System.out.println("File download started: " + file);
            }

            @Override
            public void onFinish(URL file) {
                System.out.println("File download finished: " + file);
            }
        });
    }

    void download(String urlString) {
        try {
            URL url = new URL(urlString);
            downloadManager.download(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Bad url", e);
        }
    }

    void downloadAll(String pathToFile) {
        try {
            downloadManager.downloadAll(Paths.get(pathToFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
