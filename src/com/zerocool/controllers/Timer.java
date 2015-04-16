package com.zerocool.controllers;

import java.util.ArrayList;
import java.util.Iterator;

import com.zerocool.entities.AbstractEvent;
import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.entities.Group;
import com.zerocool.entities.Individual;
import com.zerocool.entities.ParGroup;
import com.zerocool.entities.ParIndividual;
import com.zerocool.entities.Participant;
import com.zerocool.services.SystemTime;

public class Timer {
	
	private ArrayList<Participant> totalParticipants;
	private SystemTime systemTime;
	private AbstractEvent currentEvent;
	private ArrayList<AbstractEvent> totalEvents;
	
	public Timer(SystemTime systemTime) {
		this.systemTime = systemTime;
		totalParticipants = new ArrayList<Participant>();
		totalEvents = new ArrayList<AbstractEvent>();
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
	
	// USE FOR TESTING PURPOSES ONLY!
	public void resetEventId() {
		currentEvent.resetEventId();
	}
	
	// ----- functional methods ----- \\
	
	/**
	 * Starts the Event.  Depending on the Event depends on the type of
	 * start.
	 * @throws IllegalStateException - There are no Participants in
	 * 	the starting queue.
	 */
	public void triggered(int channel) {
		System.out.println("triggered(channel): " + channel);
		currentEvent.triggered(systemTime.getTime(), channel);
	}
	
	public void setDnf() {
		currentEvent.setDnf(systemTime.getTime());
	}
	
	/**
	 * Indicates a false start so it resets the data back to before the start was called.
	 */
	public void cancelStart() {
		currentEvent.resetEvent();
	}
	
	
	// ----- accessors ----- \\
	
	/**
	 * Gets the current event's time.
	 * @return The current event's time.
	 */
	public long getEventTime() {
		return currentEvent.getEventTime();
	}
	
	public ArrayList<Participant> getTotalParticipants() { 
		return totalParticipants; 
	}
	
	public Participant findParticipant(int participantId) {
		for (Participant par : totalParticipants) {
			if (par.getId() == participantId) {
				return par;
			}
		}
		
		return null;
	}
	
	public String getEventData() {
		return currentEvent.getFormattedData();
	}
	
	public String getEventParticipantData() {
		String data = "";
		
		for (Iterator<Participant> it = currentEvent.getFinishedQueue().iterator(); it.hasNext();) {
			data += it.next().getFormattedData() + "\n";
		}
		
		return data;
	}
	
	public String getEventParticipantElapsedData() {
		String data = "";
		
		for (Iterator<Participant> it = currentEvent.getFinishedQueue().iterator(); it.hasNext();) {
			data += it.next().getElapsedFormattedData() + "\n";
		}
		
		return data;
	}
	
	// USE ONLY FOR TESTING PURPOSES!
	public AbstractEvent getCurrentEvent() {
		return currentEvent;
	}
	
		
	// ----- mutators ----- \\
	
	/**
	 * 
	 * @param type
	 * @param eventName
	 */
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
		totalEvents.add(currentEvent);
		System.out.println("added currentEvent to totalEvents: " + totalEvents.size());
	}
	
	/**
	 * 
	 * @param type
	 * @param eventName
	 * @param participants
	 */
	public void createEvent(EventType type, String eventName, ArrayList<Participant> participants) {
		createEvent(type, eventName);
		for (Participant par : participants) {
			addParticipantToStart(par);
		}
	}

	/**
	 * Adds a participant to totalParticipants, currentParticipants, creates a new record and
	 * add them to the starting queue.
	 * @param participant - The Participant to add.
	 * @throws IllegalArgumentException - The Participant is null.
	 */
	public void addParticipantToStart(Participant participant) {
		if (participant == null) {
			throw new IllegalArgumentException("Participant can't be null.");
		}
		
		if (!totalParticipants.contains(participant)) {
			totalParticipants.add(participant);
		}
		
		participant.createNewRecord(currentEvent.getEventName(), currentEvent.getEventId());
		currentEvent.addParticipant(participant);
	}
	
	/**
	 * Creates a new Participant if one does not already exist with the given id.
	 * Adds the participant to totalParticipants, currentParticipants, creates a new record and
	 * add them to the starting queue.
	 * @param participantId - The id to create a new Participant from.
	 */
	public void addParticipantToStart(int participantId) {
		addParticipantToStart("Aaron " + participantId, participantId);
	}
	
	public ArrayList<AbstractEvent> getTotalEvents(){
		return totalEvents;
	}
	
	/**
	 * Creates a new Participant if one does not already exist with the given id.
	 * Adds the participant to totalParticipants, currentParticipants, creates a new record and
	 * add them to the starting queue.
	 * @param name - The name of the new Participant.
	 * @param id - The id of the new Participant.
	 */
	public void addParticipantToStart(String name, int id) {
		Participant par = findParticipant(id);
		
		if (par == null) {
			par = new Participant(name, id);
		}
		
		addParticipantToStart(par);
	}
	
	public void createNewRun() {
		currentEvent.newRun();
	}
	
	/**
	 * Exits gracefully.
	 */
	public void exit() {
		for (Participant par: totalParticipants) {
			par.exit();
		}
		currentEvent.exit();
	}
	
}
