package pl.sda.downloadmanager;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DownloadManager {

    private Path downloadDirectory;
    private HttpClient httpClient;
    private Collection<DownloadEventListener> listeners;

    public DownloadManager(Path downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
        httpClient = HttpClientBuilder.create().build();
        listeners = new LinkedList<>();
    }


    public void download(URL fileUrl) {
        listeners.forEach(listener -> listener.onStart(fileUrl));
        try {
            String file = exctractFileName(fileUrl);

            try (InputStream fileInputStream = openInputStream(fileUrl)) {
                Files.copy(fileInputStream, downloadDirectory.resolve(file));
                listeners.forEach(listener -> listener.onFinish(fileUrl));
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
        ExecutorService executorService = Executors.newCachedThreadPool();

        List<String> lines = Files.readAllLines(file, Charset.forName("UTF-8"));
        for (String line : lines) {
            executorService.submit(() -> download(wrapInUrl(line)));
        }

        try {
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private URL wrapInUrl(String line) {
        try {
            return new URL(line);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerListener(DownloadEventListener listener) {
        listeners.add(listener);

    }
}
