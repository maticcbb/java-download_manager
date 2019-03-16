package pl.sda.downloadmanager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class DownloadService {

	private DownloadManager downloadManager;

	/**
	 * Anonymous method witch set output of the {@code listeners}.
	 * Listeners run when file starts downloading and after its finished.
	 *
	 * @param downloadManager
	 */
	public DownloadService(DownloadManager downloadManager) {
		this.downloadManager = downloadManager;
		this.downloadManager
			.registerListener(new DownloadEventListener() {
				@Override
				public void onStart(URL file) {
					System.out
						.println("File download " +
							"started: " + file);
				}

				@Override
				public void onFinish(URL file) {
					System.out
						.println("File download " +
							"finished: " + file);
				}
			});
	}

	void download(String urlString) {
		URL url = wrapInUrl(urlString);
		downloadManager.download(url);
	}

	void downloadAll(String pathToFile) {
		try {
			downloadManager.downloadAll(Paths.get(pathToFile));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	void copyToDrive(String urlString) {
		try {
			downloadManager.copyToDrive(wrapInUrl(urlString));
		}
		catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private URL wrapInUrl(String urlString) {
		try {
			return new URL(urlString);
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("Bad url", e);
		}
	}
}
