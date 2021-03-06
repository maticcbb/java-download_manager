package pl.sda.downloadmanager;

import pl.sda.downloadmanager.gdrive.DriveServiceFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class DownloadManagerFactory {

	/**
	 * Set config.properties file in {@code DownloadManager}.
	 *
	 * @return DownloadManager
	 */
	public static DownloadManager createDownloadManager() {
		Properties properties = new Properties();

		try (InputStream configInputStream = new FileInputStream(
			"config.properties")) {
			properties.load(configInputStream);
			String downloadDir = properties
				.getProperty("downloadDir");
			DriveServiceFactory factory =
				new DriveServiceFactory();
			return new DownloadManager(Paths
				.get(downloadDir), factory
				.createAuthorizationCodeFlowDriveService());
		}
		catch (IOException e) {
			String msg = String
				.format("Failed to create download manager " + "using factory. Cannot load config " + "file.");
			throw new RuntimeException(msg, e);
		}
	}
}
