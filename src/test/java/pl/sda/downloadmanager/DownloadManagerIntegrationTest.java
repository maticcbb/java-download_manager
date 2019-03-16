package pl.sda.downloadmanager;

import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.sda.downloadmanager.gdrive.DriveService;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DownloadManagerIntegrationTest {

    private Server server;
    private DriveService driveService;

    @AfterEach
    void tearDown() throws Exception {
        server.stop();
        driveService = Mockito.mock(DriveService.class);
    }

    @Test
    @DisplayName("given path to http resource, then download file to the directory")
    void test() throws Exception {
        // given
        String testFileContent = "hello";
        server = JettyEmbeddedServer.prepareServerWithResponse(testFileContent, 8080);
        server.start();
        Path downloadDir = Files.createTempDirectory("files");
        DownloadManager downloadManager = new DownloadManager(downloadDir,
                driveService);
        URL url = new URL("http://localhost:8080/test-file.txt");

        //when
        downloadManager.download(url);

        //then
        assertThat(downloadDir.resolve("test-file.txt")).hasContent(testFileContent);
    }

    @Test
    @DisplayName("given file  with paths to files then download all of them")
    void test1() throws Exception {
        // given
        server = JettyEmbeddedServer.prepareServerWithResponse("anyContent", 8081);
        server.start();
        Path downloadDir = Files.createTempDirectory("files");
        DownloadManager downloadManager = new DownloadManager(downloadDir, driveService);
        Path source = Files.createTempFile("source", ".txt");
        List<String> urls = Arrays.asList(
                "http://localhost:8081/test-file.txt"
                , "http://localhost:8081/test-file1.txt"
                , "http://localhost:8081/test-file2.txt");
        Files.write(source,urls, Charset.forName("UTF-8"));

        //when
        downloadManager.downloadAll(source);

        //then
        assertThat(downloadDir.resolve("test-file.txt")).exists();
        assertThat(downloadDir.resolve("test-file1.txt")).exists();
        assertThat(downloadDir.resolve("test-file2.txt")).exists();
    }
}
