package pl.sda.downloadmanager;

public class DownloadServiceFactory {
    public static DownloadService create() {
        DownloadManager downloadManager = DownloadManagerFactory.createDownloadManager();
        return new DownloadService(downloadManager);
    }
}
