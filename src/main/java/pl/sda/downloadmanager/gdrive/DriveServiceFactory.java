package pl.sda.downloadmanager.gdrive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import pl.sda.downloadmanager.DownloadManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

public class DriveServiceFactory {
	private JsonFactory jsonFactory;
	private HttpTransport httpTransport;

	public DriveServiceFactory() {
		httpTransport = new NetHttpTransport();
		jsonFactory = JacksonFactory.getDefaultInstance();
	}

	public DriveService createServiceAccountDriveService() throws IOException {
		Credential credential = createServiceAccountCredential();
		return createDriveServiceUsingCredential(credential);
	}

	public DriveService createAuthorizationCodeFlowDriveService() throws IOException {
		Credential credential =
			createAuthorizationCodeFlowCredential();
		return createDriveServiceUsingCredential(credential);
	}

	private DriveService createDriveServiceUsingCredential(Credential credential) {
		Drive drive = new Drive.Builder(httpTransport, jsonFactory,
			credential)
			.setApplicationName("DownloadManager").build();

		return new DriveService(drive);
	}

	private Credential createServiceAccountCredential() throws IOException {
		Credential credential = GoogleCredential
			.fromStream(DownloadManager.class
				.getResourceAsStream("/SdaTarr4.json"))
			.createScoped(Collections.singleton(DriveScopes.DRIVE));
		return credential;
	}

	private Credential createAuthorizationCodeFlowCredential() throws IOException {
		InputStream in = UploadFileExample.class
			.getResourceAsStream("/credential.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets
			.load(jsonFactory, new InputStreamReader(in));

		GoogleAuthorizationCodeFlow flow =
			new GoogleAuthorizationCodeFlow.Builder(httpTransport,
				jsonFactory, clientSecrets, Collections
			.singleton(DriveScopes.DRIVE))
			.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
			.setAccessType("offline").build();
		LocalServerReceiver receiver =
			new LocalServerReceiver.Builder()
			.setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver)
			.authorize("user");
	}

}
