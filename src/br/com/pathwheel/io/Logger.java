package br.com.pathwheel.io;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Logger {
	
	public static void info(String txt) {
		System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(new Date())+" [Pathwheel] "+txt);
	}
	
	public static void error(String txt) {
		System.err.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(new Date())+" [Pathwheel] ERROR => "+txt);
	}
}
