package com.zerocool.systemcontroller.participant;

/**
 * @author ZeroCool
 * The Record Class.
 * 
 * This class creates a record of a participant's
 * performance in one event.
 */
public class Record {

	private long eventId;
	private String eventName;
	private long startTime;
	private long finishTime;
	private boolean dnf;
	
	/**
	 * Creates a new instance of the Record class with values
	 * @param startTime The participant's start time
	 * @param finishTime The participant's finish time
	 * @param dnf If the participant did not finish
	 * @param eventId The event's id number
	 * @param eventName The name of the event
	 */
	public Record(long startTime, long finishTime, boolean dnf, long eventId, String eventName) {
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.dnf = dnf;
		this.eventId = eventId;
		this.eventName = eventName;
	}
	
	/**
	 * Creates a new instance of the Record class without values
	 */
	public Record() {
		
	}

	// ----- Access ----- \\
	/**
	 * @return If the participant finished the event
	 */
	public boolean getDnf() {
		return this.dnf;
	}
	
	/**
	 * @return The finish time
	 */
	public long getFinishTime() {
		return this.finishTime;
	}
	
	/**
	 * @return The start time
	 */
	public long getStartTime() {
		return this.startTime;
	}
	
	/**
	 * @return The event name this record is for
	 */
	public String getEventName() {
		return this.eventName;
	}
	
	/**
	 * @return The event Id
	 */
	public long getEventID() {
		return this.eventId;
	}

	// ----- Mutate ----- \\
	/**
	 * Sets the event's name
	 * @param eventName
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	/**
	 * Sets the start time of the participant's record
	 * @param startTime
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * Sets the finish time of the participant's record
	 * @param finishTime
	 */
	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}
	
	/**
	 * Sets whether the participant finished the event or not
	 * @param dnf
	 */
	public void setDnf(boolean dnf) {
		this.dnf = dnf;
	}
	
	/**
	 * @param eventId
	 */
	public void setEventId(long eventId){
		this.eventId = eventId;
	}
}
