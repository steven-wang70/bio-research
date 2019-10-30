package bioinfo.utils;

import java.io.File;
import java.io.IOException;

import org.ini4j.InvalidFileFormatException;

public final class Settings {
	
	public static Settings instance() {
		return _instance;
	}
	
	private static Settings _instance = null;
	
	public static void loadSettings(String iniFile) {
		_instance = new Settings(iniFile);
	}

	private org.ini4j.Wini wini = null;
	private Settings(String iniFileName) {
		try {
			wini = new org.ini4j.Wini(new File(iniFileName));
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getSetting(String section, String option) {
		return this.wini.get(section,  option);
	}
	
	public <T> T getSetting(String section, String option, Class<T> class_) {
		return (T)this.wini.get(section, option, class_);
	}
}
