package com.zerocool.systemcontroller.event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.zerocool.systemcontroller.event.AbstractEvent.EventType;
import com.zerocool.systemcontroller.participant.Participant;

public class Individual extends AbstractEvent {

	private EventType type;
	private String name;
	private long eventId;
	// only one Participant is needed since this is an Individual event
	private  ArrayList<Participant> currentParticipants;
	// dateFormat.format(date) prints (example) 2014/08/06 15:59:48
	private DateFormat eventTimeFormat = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");
	// eventTime stored the entire date but the specific miliseconds, seconds,
	// minutes, hours..etc can be accessed from such
	private Date eventTime;

	public Individual() {
		super();
	}

	public Individual(String name) {
		super(name, EventType.IND);
	}

	public Individual(String name, EventType type) {
		super(name, type, new Date());
	}

	public Individual(String name, EventType type, Date eventTime) {
		super(name, type, eventTime, -1);
	}

	public Individual(String name, EventType type, Date eventTime, long eventId) {
		super(name, type, eventTime, eventId);
	}

	void initializeEvent( ArrayList<Participant> participants) {
		// must check for null parameter
		currentParticipants = participants;
		// go through each participant and set their eventId and event name
		for (Participant curPar : currentParticipants) {
			curPar.getLastRecord().setEventName(name);
			curPar.getLastRecord().setEventId(eventId);
		}
	}

	void startParticipants() {
		System.out.println("Starting Individual Participants");
		// go through each participant and set the start time
		for (Participant curPar : currentParticipants) {
			curPar.setIsCompeting(true);
			curPar.getLastRecord().setStartTime(3399);
		}
	}

	void finishParticipant() {
		System.out.println("Finishing Individual Participants");
		for (Participant curPar : currentParticipants) {
			curPar.setIsCompeting(false);
			curPar.getLastRecord().setFinishTime(3);
		}
	}

	public  ArrayList<Participant> getParticipants() {
		return currentParticipants;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

}
