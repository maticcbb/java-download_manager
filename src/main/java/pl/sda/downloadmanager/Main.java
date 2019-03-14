package pl.sda.downloadmanager;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: <url>");
            System.out.println("Usage: downloadAll <path-to-source-file>");
            System.exit(0);
        }

        DownloadManager downloadManager = DownloadManagerFactory.createDownloadManager();
        if (args[0].equals("downloadAll")) {
            downloadManager.downloadAll(Paths.get(args[1]));
        } else {
            URL url = new URL(args[0]);
            downloadManager.download(url);
        }
    }
}
