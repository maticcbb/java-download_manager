package pl.sda.downloadmanager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class UploadFileUsingDownloadManagerExample {
	public static void main(String[] args) throws IOException,
		URISyntaxException {
		DownloadManager downloadManager = DownloadManagerFactory
			.createDownloadManager();

		downloadManager
			.copyToDrive(new URL("http://www.hochmuth" + ".com/mp3"
				+ "/Haydn_Cello_Concerto_D-1.mp3"));
	}
}
