package com.zerocool.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.entities.Individual;
import com.zerocool.entities.Participant;
import com.zerocool.services.SystemTime;

public class TestIndividualEvent { 

	private ArrayList<Individual> events;
	private ArrayList<Participant> participants;

	SystemTime stopWatch;

	@Before
	public void setUp() throws Exception {
		events = new ArrayList<Individual>();
		participants = new  ArrayList<Participant>();

		stopWatch = new SystemTime();
		stopWatch.start();

	}

	@After
	public void tearDown() throws Exception {
		events = null;
		participants = null;

		stopWatch = null;
	}
	
	public void addEvents() {
		/*events.add(new Individual("100M Sprint"));
		events.add(new Group("CrossCountry Skiiing"));
		events.add(new ParIndividual("Marathon"));
		events.add(new ParGroup("Swimming"));*/
	}

	public void addParticipants(int numParticipants) {
		for(int i = 0; i < numParticipants; ++i) {
			participants.add(new Participant("Joe [" + i + "]", i));
		}
	}

//	public void link(int num) {
//		for(int i = 0; i < num; ++i){
//			events.get(i).setParticipants(participants);
//		}
//	}

	@Test
	public void testConstructor() {
		/*events.add(new Individual("100M Sprint"));
		assertTrue(events.get(0).getName().equals("100M Sprint"));
		assertTrue(events.get(0).getType().equals(AbstractEvent.EventType.IND));
		events.add(new Group("CrossCountry Skiing"));
		assertTrue(events.get(1).getName().equals("CrossCountry Skiing"));
		assertTrue(events.get(1).getType().equals(AbstractEvent.EventType.GRP));
		events.add(new ParIndividual("Marathon"));
		assertTrue(events.get(2).getName().equals("Marathon"));
		assertTrue(events.get(2).getType().equals(AbstractEvent.EventType.PARIND));
		events.add(new ParGroup("Swimming"));
		assertTrue(events.get(3).getName().equals("Swimming"));
		assertTrue(events.get(3).getType().equals(AbstractEvent.EventType.PARGRP));*/
	}


	@Test
	public void testGetParticipants() {
		addEvents();
		//add participants for testing
		addParticipants(1);

		for (Individual eve: events) {
			assertNotNull(eve.getCurrentParticipants());
			assertEquals(1, eve.getCurrentParticipants().size());
		}
	}

	@Test
	public void testInitialEvent() {
		addEvents();
		//add participants for testing
		addParticipants(1);	

		for (Individual eve: events) {
			assertNotNull(eve.getCurrentParticipants());
			assertEquals(1, eve.getCurrentParticipants().size());
			assertNotNull(eve.getCurrentParticipants().get(0).getLastRecord().getEventName());
			assertNotNull(eve.getCurrentParticipants().get(0).getLastRecord().getEventId());
		}
	}

	@Test
	public void testRace() {
		addEvents();
		//add participants for testing
		addParticipants(1);

		for (Individual eve: events) {
			long startTime = stopWatch.getTime();
			
			System.out.println("Start: " + SystemTime.formatTime(startTime));

			eve.startAllParticipants(startTime);
			assertTrue(eve.getCurrentParticipants().get(0).getIsCompeting());
			assertEquals(eve.getCurrentParticipants().get(0).getLastRecord().getStartTime(), startTime);

			// waits 1 second showing our participant ran the race.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) { };
			
			long finishTime = stopWatch.getTime();
			
			System.out.println("Finish: " + SystemTime.formatTime(finishTime));
			
			eve.finishAllParticipants(finishTime, false);
			assertFalse(eve.getCurrentParticipants().get(0).getIsCompeting());
			assertEquals(eve.getCurrentParticipants().get(0).getLastRecord().getFinishTime(), finishTime);
			assertEquals(eve.getCurrentParticipants().get(0).getLastRecord().getElapsedTime(), finishTime - startTime);
		}
	}
	
	@Test
	public void testNextParticipant() {
		addEvents();
		addParticipants(1);
		
		for (Individual eve: events) {

			long startTime = stopWatch.getTime();
			System.out.println("Start: " + SystemTime.formatTime(startTime));
			
			eve.startNextParticipant(startTime);
			assertTrue(eve.getCurrentParticipants().get(0).getIsCompeting());
			assertEquals(eve.getCurrentParticipants().get(0).getLastRecord().getStartTime(), startTime);

			// waits 1 second showing our participant ran the race.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) { };
			
			long finishTime = stopWatch.getTime();
			
			System.out.println("Finish: " + SystemTime.formatTime(finishTime));
			
			eve.finishParticipant(eve.getCompetingParticipants().get(0), finishTime, false);
			assertFalse(eve.getCurrentParticipants().get(0).getIsCompeting());
			assertEquals(eve.getCurrentParticipants().get(0).getLastRecord().getFinishTime(), finishTime);
			assertEquals(eve.getCurrentParticipants().get(0).getLastRecord().getElapsedTime(), finishTime - startTime);
			
		}
	}

}