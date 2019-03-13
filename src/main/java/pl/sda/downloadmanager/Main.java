package pl.sda.downloadmanager;

import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        DownloadManager downloadManager = DownloadManagerFactory.createDownloadManager();
        URL url = new URL(args[0]);
        downloadManager.download(url);
    }
}
