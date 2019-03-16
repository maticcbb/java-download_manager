package pl.sda.downloadmanager;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;


public class UploadFileExample {
    public static void main(String[] args) throws IOException {
        // Create instance of object 'google drive' for connect to service, to upload a file.
        // HttpTransport - component for making requests HTTP that is execute from object 'Drive'
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory JsonFactory = JacksonFactory.getDefaultInstance();

        HttpRequestInitializer credentials = createServiceAccountCredential();
        Drive drive = new Drive.Builder(httpTransport, JsonFactory, credentials)
                .setApplicationName("UploadFileExample")
                .build();

        File file = new File();
        file.setName("HelloMat.txt");
        AbstractInputStreamContent content = new ByteArrayContent("text/plain", "Hello".getBytes());
        File uploadField = drive.files().create(file, content).execute();

        System.out.println(uploadField);

        System.out.println("All files on disk");
        System.out.println(drive.files().list().execute());
    }

    public static Credential createServiceAccountCredential() throws IOException {
        Credential credentials = GoogleCredential.fromStream(UploadFileExample.class
                .getResourceAsStream("/sdatarr4" +
                        "-e9bdaf4c3f18.json"))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return credentials;
    }

//    private static Credential createAuthorizationCodeFlow(JsonFactory jsonFactory) throws IOException {
//        // Load client secrets.
//        InputStream in = UploadFileExample.class
//                .getResourceAsStream("/credential.json");
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets
//                .load(jsonFactory, new InputStreamReader(in));
//
//        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow =
//                new GoogleAuthorizationCodeFlow.Builder(httpTransport,
//                        jsonFactory, clientSecrets, Collections
//                        .singleton(DriveScopes.DRIVE))
//                        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//                        .setAccessType("offline").build();
//        LocalServerReceiver receiver =
//                new LocalServerReceiver.Builder()
//                        .setPort(8888).build();
//        return new AuthorizationCodeInstalledApp(flow, receiver)
//                .authorize("user");
//    }
}
