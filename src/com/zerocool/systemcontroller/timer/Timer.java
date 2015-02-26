package com.zerocool.systemcontroller.timer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.zerocool.systemcontroller.event.AbstractEvent;
import com.zerocool.systemcontroller.event.AbstractEvent.EventType;
import com.zerocool.systemcontroller.event.Group;
import com.zerocool.systemcontroller.event.Individual;
import com.zerocool.systemcontroller.event.ParGroup;
import com.zerocool.systemcontroller.event.ParIndividual;
import com.zerocool.systemcontroller.participant.Participant;
import com.zerocool.systemcontroller.systemtime.SystemTime;

public class Timer {
	
	private ArrayList<Participant> totalParticipants;
	private SystemTime systemTime;
	private AbstractEvent currentEvent;
	
	public Timer(SystemTime systemTime) {
		this.systemTime = systemTime;
	}
	
	public Timer(SystemTime systemTime, EventType type, String eventName) {
		this(systemTime);
		createEvent(type, eventName);
	}
	
	public Timer(SystemTime systemTime, EventType type, String eventName, ArrayList<Participant> participants) {
		this(systemTime);
		createEvent(type, eventName, participants);
		totalParticipants = participants;
	}	
	
	
	// ----- functional methods ----- \\
	
	public void startEvent() { 
		//Must actually initialize the event
		currentEvent.initializeEvent(totalParticipants);
		//Then start the event (which also fills in the event name and eventId of each participants record)
		currentEvent.startAllParticipants(systemTime.getTime());
	}
	
	public void endEvent() { 
		//Finish all participants (for this sprint this will work)
		//Further on, when we're doing more that just one single participant we need functionality to start and finish specific participants
		currentEvent.finishAllParticipants(systemTime.getTime());
	}
	
	
	// ----- accessors ----- \\
	
	public long getEventTime() {
		return currentEvent.getEventTime();
	}
	
	public ArrayList<Participant> getTotalParticipants() { 
		return totalParticipants; 
	}
	
	public AbstractEvent getCurrentEvent() { 
		return currentEvent; 
	}
		
	// ----- mutators ----- \\
	
	public void createEvent(EventType type, String eventName) {
		//IND, PARIND, GRP, PARGRP
		switch(type) {
		case IND:
			currentEvent = new Individual(eventName, systemTime.getTime());
			break;
		case PARIND:
			currentEvent = new ParIndividual(eventName, systemTime.getTime());
			break;
		case GRP:
			currentEvent = new Group(eventName, systemTime.getTime());
			break;
		case PARGRP:
			currentEvent = new ParGroup(eventName, systemTime.getTime());
			break;
		default:
			throw new IllegalArgumentException("Invalid Event Type");
		}
	}
	
	public void createEvent(EventType type, String eventName, ArrayList<Participant> participants) {
		createEvent(type, eventName);
		currentEvent.initializeEvent(participants);
	}
	
	public void exit() {
		System.out.println("exiting timer");
		for(Participant par: totalParticipants){
			par.exit();
		}
		currentEvent.exit();
		System.out.println("exiting timer");
	}

	public void addNewParticipant(int participant) {
		Participant newPar = new Participant(participant, ""+participant);
		totalParticipants.add(newPar);
		currentEvent.addNewParticipant(newPar);
	}
	
}
