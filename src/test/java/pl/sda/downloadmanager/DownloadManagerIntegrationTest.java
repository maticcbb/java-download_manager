package pl.sda.downloadmanager;

import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class DownloadManagerIntegrationTest {

    private Server server;

    @AfterEach
    void tearDown() throws Exception {
        server.stop();
    }

    @Test
    @DisplayName("given path to http resource, then download file to the directory")
    void test() throws Exception {
        // given
        String testFileContent = "hello";
        server = JettyEmbeddedServer.prepareServerWithResponse(testFileContent, 8080);
        server.start();
        Path downloadDir = Files.createTempDirectory("files");
        DownloadManager downloadManager = new DownloadManager(downloadDir);
        URL url = new URL("http://localhost:8080/test-file.txt");

        //when
        downloadManager.download(url);

        //then
        assertThat(downloadDir.resolve("test-file.txt")).hasContent(testFileContent);
    }
}
