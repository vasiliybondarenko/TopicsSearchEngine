package infrascructure.data.util;

public class Trace {
	public static void trace(Exception ex) {
		ex.printStackTrace();
	}
	
	public static void trace(String s) {
		System.out.println(s);
	}
}
