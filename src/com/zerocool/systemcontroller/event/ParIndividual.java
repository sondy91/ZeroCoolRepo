package com.zerocool.systemcontroller.event;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.zerocool.systemcontroller.event.AbstractEvent.EventType;
import com.zerocool.systemcontroller.participant.Participant;



public class ParIndividual extends AbstractEvent{
	
	private EventType type;
	private String name;
	private long eventId;
	private Participant [] currentParticipants;
	//dateFormat.format(date) prints (example)   2014/08/06 15:59:48
	private DateFormat eventTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	//eventTime stored the entire date but the specific miliseconds, seconds, minutes, hours..etc can be accessed from such
	private Date eventTime;
	
	public ParIndividual(){
		super();
	}

	public ParIndividual(String name){
		super(name, EventType.GRP);
	}

	public ParIndividual(String name, EventType type){
		super(name, type, new Date());
	}
	
	public ParIndividual(String name, EventType type, Date eventTime){
		super(name, type, eventTime, -1);
	}
	
	public ParIndividual(String name, EventType type, Date eventTime, long eventId){
		super(name, type, eventTime, eventId);
	}
	
	void initializeEvent(Participant[] participants){
		//must check for null parameter
		currentParticipants = participants;
		//go through each participant and set their eventId and event name
		for(Participant curPar: currentParticipants){
			curPar.getLastRecord().setEventName(name);
			curPar.getLastRecord().setEventId(eventId);
		}
	}
	
	void startParticipants(){
		System.out.println("Starting ParIndividual Participants");
		//go through each participant and set the start time 
		for(Participant curPar: currentParticipants){
			curPar.setIsCompeting(true);
			curPar.getLastRecord().setStartTime(3425);
		}
	}
	
	
	void finishParticipant(){
		System.out.println("Finishing ParIndividual Participants");
		for(Participant curPar: currentParticipants){
			curPar.setIsCompeting(false);
			curPar.getLastRecord().setFinishTime(1123);
		}
	}
	
	public Participant[] getParticipants(){
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