package pl.sda.downloadmanager;

public class Main {
    public static void main(String[] args) {
        DownloadService downloadService = DownloadServiceFactory.create();

        if (args.length == 0) {
            System.out.println("Usage: <url>");
            System.out.println("Usage: downloadAll <path-to-source-file>");
            System.exit(0);
        }

        if (args[0].equals("downloadAll")) {
            downloadService.downloadAll(args[1]);
        } else {
            downloadService.download(args[0]);
        }
    }
}
