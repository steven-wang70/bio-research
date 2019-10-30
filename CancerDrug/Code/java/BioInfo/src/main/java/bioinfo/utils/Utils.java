package bioinfo.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

public final class Utils {
	
	public static String booleanArray2String(boolean[] bools) {
		if (bools == null) {
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bools.length; i++) {
			if (bools[i]) {
				sb.append("1");
			} else {
				sb.append("0");
			}
		}
		
		return sb.toString();
	}
	
	public static boolean[] string2BooleanArray(String s) throws IllegalArgumentException {
		if (s == null) {
			return null;
		}
		
		boolean[] result = new boolean[s.length()];
		
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '0') {
				result[i] = false;
			} else if (s.charAt(i) == '1') {
				result[i] = true;
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		return result;
	}
	
	public static String intArray2String(int[] ints) {
		if (ints == null) {
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ints.length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			
			sb.append(ints[i]);
		}
		
		return sb.toString();
	}
	
	public static int[] string2IntArray(String s) {
		if (s == null) {
			return null;
		}
		
		s = s.trim();
		if (s.isEmpty()) {
			return new int[0];
		}
		
		String[] ints2 = s.split(",");
		int[] arr = new int[ints2.length];
		for (int i = 0; i < ints2.length; i++) {
			arr[i] = Integer.parseInt(ints2[i]);
		}
		
		return arr;
	}

	public static double similarity(String s1, String s2) {
		if (s1 == s2) {
			return 1;
		}
		
		if (s1 == null || s2 == null) {
			return 0;
		}
		
		if (s1.length() != s2.length()) {
			return 0;
		}
		
		if (s1.length() == 0) {
			return 1;
		}
		
		int counter = 0;
		for (int i = 0; i < s1.length(); i++) {
			if (s1.charAt(i) == s2.charAt(i)) {
				counter++;
			}
		}
		
		return counter * 1.0 / s1.length();
	}
	
	public static <T> double similarity(T[] v1, T[] v2) {
		if (v1 == v2) {
			return 1;
		}
		
		if (v1 == null || v2 == null) {
			return 0;
		}
		
		if (v1.length != v2.length) {
			return 0;
		}
		
		if (v1.length == 0) {
			return 1;
		}
		
		int counter = 0;
		for (int i = 0; i < v1.length; i++) {
			if (v1[i].equals(v2[i])) {
				counter++;
			}
		}
		
		return counter * 1.0 / v1.length;
	}
	
	public static double leastSimilarity(List<String> ss) {
		if (ss == null || ss.size() < 2) {
			return 0;
		}
		double rating = 1.0;
		for (int i = 1; i < ss.size(); i++) {
			double thisRating = similarity(ss.get(0), ss.get(i));
			if (thisRating < rating) {
				rating = thisRating;
			}
		}
		
		return rating;
	}
	
	private static int _ignoreProteinDecoration = -1;
	public static boolean IgnoreProteinDecoration() {
		if (_ignoreProteinDecoration == -1) {
			_ignoreProteinDecoration = Settings.instance().getSetting("General", "IgnoreProteinDecoration", Integer.class);
		}
		
		return _ignoreProteinDecoration == 1 ? true : false;
	}
	
	public static String UnifiedGeneName(String name) {
		if (IgnoreProteinDecoration()) {
			while (Character.isDigit(name.charAt(name.length() - 1))) {
				name = name.substring(0, name.length() - 1);
			}
		}

		return name;
	}
	
	public static void saveToFile(String fileName, String content) {
		try {
			createPathIfNotExist(fileName);
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static BufferedWriter openFileToWrite(String fileName) {
		try {
			createPathIfNotExist(fileName);
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			return bw;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void println(String s, BufferedWriter bw) {
		try {
			bw.write(s);
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void close(BufferedWriter bw) {
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void createPathIfNotExist(String filePath) {
		java.io.File f = new java.io.File(filePath);
		f.getParentFile().mkdirs();
	}
	
	public static final String PathSeparator = "/";
	
	public static String Array2String(double[] arr) {
		return Array2String(arr, null);
	}
	
	public static String Array2String(double[] arr, NumberFormat formatter) {
		StringBuilder sb = new StringBuilder();
		
		boolean firstOne = true;
        for (double i : arr) {
        	if (firstOne) {
        		firstOne = false;
        	} else {
        		sb.append('\t');
        	}
        	
        	if (formatter != null) {
        		sb.append(formatter.format(i));
        	} else {
            	sb.append(i);
        	}
        }
        
        return sb.toString();
	}
	
	public static String Array2String(int[] arr) {
		StringBuilder sb = new StringBuilder();
		
		boolean firstOne = true;
        for (int i : arr) {
        	if (firstOne) {
        		firstOne = false;
        	} else {
        		sb.append('\t');
        	}
        	
        	sb.append(i);
        }
        
        return sb.toString();
	}

}
