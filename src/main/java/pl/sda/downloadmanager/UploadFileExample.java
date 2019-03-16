package pl.sda.downloadmanager;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.IOException;
import java.util.Collections;


public class UploadFileExample {
    public static void main(String[] args) throws IOException {
        // Create instance of object 'google drive' for connect to service, to upload a file.
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory JsonFactory = JacksonFactory.getDefaultInstance();
        HttpRequestInitializer credentials;
        GoogleCredential.fromStream(UploadFileExample.class
                .getResourceAsStream("/sda-download-manager" +
                        "-a45cfa61168c.json"))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        Drive drive = new Drive.Builder(httpTransport,JsonFactory,credentials);
    }
}
