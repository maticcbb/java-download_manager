package pl.sda.downloadmanager;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DownloadManager {

    private Path downloadDirectory;
    private HttpClient httpClient;

    public DownloadManager(Path downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
        httpClient = HttpClientBuilder.create().build();
    }


    public void download(URL fileUrl) {
        try {
            String file = exctractFileName(fileUrl);

            try (InputStream fileInputStream = openInputStream(fileUrl)) {
                Files.copy(fileInputStream, downloadDirectory.resolve(file));
            }

        } catch (IOException | URISyntaxException e) {
            String message = String.format("Failed to download file %s. Cannot open connection ", fileUrl);
            throw new RuntimeException(message, e);
        }
    }

    private String exctractFileName(URL fileUrl) {
        String fileUrlString = fileUrl.getFile();
        String file = fileUrlString.substring(fileUrlString.lastIndexOf('/') + 1);
        return file;
    }

    private InputStream openInputStream(URL fileUrl) throws URISyntaxException, IOException {
        return httpClient.execute(new HttpGet(fileUrl.toURI())).getEntity().getContent();
    }

    /**
     * Use this method to download multiple files at once. Each line in the source {@code file} represents a single URL to the file that will be downloaded. This method behaves exactly as {@link #download(URL)} that would be invoked for each line in the source file.
     *
     * @param file a source file in UTF-8 encoding, each line in this file is a URL to the remote file that we want to download
     * @throws IOException
     */
    public void downloadAll(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file, Charset.forName("UTF-8"));
        for (String line : lines) {
            download(new URL(line));
        }
    }

}
