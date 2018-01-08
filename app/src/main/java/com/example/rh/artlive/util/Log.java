package com.example.rh.artlive.util;



/**
 * Wrapper API for outputing the log record.
 */
public class Log {

	protected static final String TAG_DEFAULT	= "Weijp";

	/**
	 * Check if the log is enabled.
	 */
	private static boolean isEnabled() {
		return true;
	}

	/**
	 * Send a VERBOSE log message.
	 */
	public static void v(Object msg) {
		if (isEnabled()) {
			android.util.Log.v(TAG_DEFAULT, buildMessage(msg));
		}
	}

	/**
	 * Send a VERBOSE log message with the given tag.
	 */
	public static void v(String tag, Object msg) {
		if (isEnabled() && msg != null) {
			android.util.Log.v(tag, msg.toString());
		}
	}

	/**
	 * Send a DEBUG log message.
	 */
	public static void d(Object msg) {
		if (isEnabled()) {
			android.util.Log.d(TAG_DEFAULT, buildMessage(msg));
		}
	}

	/**
	 * Send a DEBUG log message with the given tag.
	 */
	public static void d(String tag, Object msg) {
		if (isEnabled() && msg != null) {
			android.util.Log.d(tag, msg.toString());
		}
	}

	/**
	 * Send a INFO log message.
	 */
	public static void i(Object msg) {
		if (isEnabled()) {
			android.util.Log.i(TAG_DEFAULT, buildMessage(msg));
		}
	}

	/**
	 * Send a INFO log message with the given tag.
	 */
	public static void i(String tag, Object msg) {
		if (isEnabled() && msg != null) {
			android.util.Log.e(tag, msg.toString());
		}
	}

	/**
	 * Send a WARN log message.
	 */
	public static void w(Object msg) {
		if (isEnabled()) {
			android.util.Log.w(TAG_DEFAULT, buildMessage(msg));
		}
	}

	/**
	 * Send a WARN log message with the given tag.
	 */
	public static void w(String tag, Object msg) {
		if (isEnabled() && msg != null) {
			android.util.Log.w(tag, msg.toString());
		}
	}

	/**
	 * Send a ERROR log message.
	 */
	public static void e(Object msg) {
		if (isEnabled()) {
			android.util.Log.e(TAG_DEFAULT, buildMessage(msg));
		}
	}

	/**
	 * Send a ERROR log message with the given tag.
	 */
	public static void e(String tag, Object msg) {
		if (isEnabled() && msg != null) {
			android.util.Log.e(tag, msg.toString());
		}
	}

	/**
	 * Send a ERROR log message with the given tag.
	 */
	public static void e(String tag, Object msg, Throwable throwable) {
		if (isEnabled() && msg != null) {
			android.util.Log.e(tag, msg.toString(), throwable);
		}
	}

	/**
	 * Build the complete message.
	 */
	private static String buildMessage(Object message) {
		if (message == null) {
			return "NULL MESSAGE";
		}

		StackTraceElement ste = new Throwable().fillInStackTrace().getStackTrace()[2];

		StringBuffer sb = new StringBuffer();
		sb.append(ste.getClassName());
		sb.append(".");
		sb.append(ste.getMethodName());
		sb.append("(): ");
		sb.append(message);

		return sb.toString();
	}

}
