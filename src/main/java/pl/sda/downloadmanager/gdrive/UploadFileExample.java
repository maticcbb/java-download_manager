package pl.sda.downloadmanager.gdrive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
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
		//tworzymy instancję obiektu 'drive' aby połączyć się do
		// usługi GoogleDrive, aby móc wysłać plik
		// httpTransport - komponent służący do tworzenia requestów
		// HTTP, które zostaną wykonane przez obiekt 'Drive'
		// JsonFactory - komponent serializujący i deserializujący
		// obiekty Json
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		// ladujemy credentiale do service account w google drive
		// nazwanego sda-download-manager

		// utworzenie obiektu 'Drive', który służy do dostępu do API
		// Google drive - główna klasa przez którą wyonujemy wszyskie
		// operacje na dysku

		Credential credential =

			//createServiceAccountCredential();
			createAuthorisationCodeFlofCredential(jsonFactory,
				httpTransport);
		Drive drive = new Drive.Builder(httpTransport, jsonFactory,
			credential)
			.setApplicationName("UploadFileExample").build();
		// Tworzymy na dysku Google plik 'Hello.txt', który zawiera
		// napis 'Hello'
		File file = new File();
		file.setName("Hello.txt");
		// kiedy 'Drive' tworzy plik przyjmuje argumenty 'file -
		// metadane pliku' oraz 'content - zawartosć (w tym
		// prtzypadku utworzoną w pamięci)'
		AbstractInputStreamContent content = new ByteArrayContent(
			"text/plain", "Hello"
			.getBytes());
		// tworzy plik i zwraca jego Id
		File uploadFileId = drive.files().create(file, content)
			.execute();

		System.out.println(uploadFileId);

		System.out.println("ALL FILES on disk");
		System.out.println(drive.files().list().execute());
	}

	private static Credential createServiceAccountCredential() throws IOException {
		Credential credential = GoogleCredential //ładuje uprawnienia
			// do konta Google
			.fromStream(UploadFileExample.class
				// classPath zawiera wszystkie
				// skompilowane klasy i zasoby pliku -
				// umozliwia na dostęp do zasobu bez
				// znajomości ścieżki dostępu
				// domyślnie Maven dodaje do ścieżkę
				// src\main\resources a w przypadku
				// wykonywania testów dodatkowo
				// src\test\resources
				.getResourceAsStream("/SdaTarr4.json"))
			// prosi o uzyskanie uprawnienia dostępu do konta
			// pozwalających na zrobienie wszystkiego
			.createScoped(Collections.singleton(DriveScopes.DRIVE));
		return credential;
	}

	private static Credential createAuthorisationCodeFlofCredential(JsonFactory jsonFactory, HttpTransport httpTransport) throws IOException {
		// Load client secrets. Zczytanie pliku i załadowanie
		// Credentials
		InputStream in = UploadFileExample.class
			.getResourceAsStream("/credential.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets
			.load(jsonFactory, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
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
