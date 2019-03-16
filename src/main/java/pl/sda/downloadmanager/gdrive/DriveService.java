package pl.sda.downloadmanager.gdrive;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.io.InputStream;

public class DriveService {
	private Drive drive;

	public DriveService(Drive drive) {
		this.drive = drive;
	}

	public void upload(InputStream content, String name) throws IOException {
		File file = new File();
		file.setName(name);
		AbstractInputStreamContent contentToUpload =
			new InputStreamContent("application/octet-stream",
				content);
		drive.files().create(file, contentToUpload).execute();
	}
}
