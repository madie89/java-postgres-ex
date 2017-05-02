package java.postgres.sample.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Util {

	static Properties dbProps = new Properties();
	static File dbFile = null;
	static {
		try {
			dbFile = new File("ROOT/Config.properties");
			dbProps.load(new FileInputStream(dbFile.getAbsolutePath()));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getDBIp() {
		return dbProps.getProperty("app42.paas.db.ip");
	}

	public static String getDBPassword() {
		return dbProps.getProperty("app42.paas.db.password");
	}

	public static String getDBUser() {
		return dbProps.getProperty("app42.paas.db.username");
	}
	
	public static String getDBName() {
		return dbProps.getProperty("app42.paas.db.name");
	}

	public static int getDBPort() {
		return new Integer(dbProps.getProperty("app42.paas.db.port"));
	}

}
