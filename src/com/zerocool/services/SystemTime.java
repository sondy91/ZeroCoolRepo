package com.zerocool.services;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.StopWatch;

public class SystemTime extends StopWatch {

	// Used when setting the time of the system to a specific time.
	private long offset;
	
	public SystemTime() {
		super();
	}
	
	public SystemTime(long startTime) {
		this();
		offset = startTime;
	}
	
	public SystemTime(int hours, int minutes, int seconds) {
		this(getTimeInMillis(hours, minutes, seconds));
	}
	
	/**
	 * Used to format milliseconds to the desired look.
	 * @param millis - The milliseconds to format.
	 * @return A string representing the milliseconds in the desired format.
	 */
	public static String formatTime(long millis) {
		return DurationFormatUtils.formatDuration(millis, "HH:mm:ss.S");
	}
	
	/**
	 * A helper to get a time formatted in HH:mm:ss.S
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return
	 */
	public static long getTimeInMillis(int hours, int minutes, int seconds) {
		return hours * 3600000 + minutes * 60000 + seconds * 1000;
	}
	
	/**
	 * Resets and sets the time.
	 * @param startTime - The time in milliseconds to set the time to.
	 */
	public void setTime(long startTime) {
		reset();
		offset = startTime;
	}
	
	/**
	 * A more friendly overloaded method to reset and set the time.
	 * @param hours - The hour(s) to set the time to.
	 * @param minutes - The minute(s) to set the time to.
	 * @param seconds - The second(s) to set the time to.
	 */
	public void setTime(int hours, int minutes, int seconds) {
		reset();
		offset = getTimeInMillis(hours, minutes, seconds);
	}
	
	/**
	 * Overrides the reset method to remove the offset so the time starts at 0 again.
	 */
	@Override
	public void reset() {
		super.reset();
		offset = 0;
	}
	
	/**
	 * Just overrides the original method to add the offset so we get the
	 * desired result.
	 */
	@Override
	public long getTime() {
		return super.getTime() + offset;
	}
	
	/**
	 * Overrides the toString() method to output the formatted version.
	 */
	@Override
	public String toString() {
		return formatTime(getTime());
	}
	
	/**
	 * Exit method to 'end gracefully'.
	 */
	public void exit() {
		super.stop();
		this.reset();
	}
	
}
