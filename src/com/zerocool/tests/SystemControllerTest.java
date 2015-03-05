package com.zerocool.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Queue;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.controllers.SystemController;
import com.zerocool.controllers.TaskList.Task;
import com.zerocool.entities.Participant;
import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.entities.Sensor.SensorType;

public class SystemControllerTest {
	
	SystemController sysCont = null;
	File file = null;
	FileReader fileReader = null;
	Scanner inFile = null;

	@Before
	public void setUp() throws Exception {
		sysCont = new SystemController();
		System.out.println("\tTESTING INITIATED\n\n");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("\n\n\tTESTING TERMINATED");
	}
	
	@Test
	public void testSystemController() {
		System.out.println("\t-------Testing SystemController()-------");
		assertNotNull(sysCont.getChannels());
		assertNotNull(sysCont.getEventLog());
		assertNotNull(sysCont.getTimer());
		assertNotNull(sysCont.getSystemTime());
		assertEquals(0, sysCont.getId());
		System.out.println("\t-------Successfully tested SystemController()-------\n");
	}
	
	@Test
	public void testReadFile() {
		System.out.println("\t-------Testing readFile()-------");
		file = new File("test_files/syscontroljunittestfile.txt");
		sysCont.testReadFile(file);
		Queue<Task> commandList = sysCont.getTaskList().getTaskList();
		assertEquals("TIME", commandList.peek().getTaskCommand());
		assertEquals("12:01:00.000", commandList.poll().getTaskArgumentOne());
		assertEquals("ON", commandList.poll().getTaskCommand());
		assertEquals("OFF", commandList.poll().getTaskCommand());
		assertEquals("CONN", commandList.peek().getTaskCommand());
		assertEquals("GATE", commandList.peek().getTaskArgumentOne());
		assertEquals("1", commandList.poll().getTaskArgumentTwo());
		assertEquals("TOGGLE", commandList.peek().getTaskCommand());
		assertEquals("1", commandList.poll().getTaskArgumentOne());
		assertEquals("NUM", commandList.peek().getTaskCommand());
		assertEquals("315", commandList.poll().getTaskArgumentOne());
		assertEquals("START", commandList.poll().getTaskCommand());
		assertEquals("FIN", commandList.poll().getTaskCommand());
		assertEquals("DNF", commandList.poll().getTaskCommand());
		assertEquals("PRINT", commandList.poll().getTaskCommand());
		assertEquals("EXIT", commandList.poll().getTaskCommand());
		assertTrue(commandList.isEmpty());
		System.out.println("\t-------Successfully tested readFile()-------\n");
	}
	
	@Test
	public void testReadInput() {
		System.out.println("\t-------Testing readInput()-------");
		System.out.println("This is a special type of test...\n" +
				"Since this is testing user input...it may not always be consistent...\n" +
				"Well in order for the test to test the data is accurate it has to know what to expect...AKA be consistent...\n" +
				"So please enter the commands like they are written pressing enter after each command:\n" +
				"<<OR if you're not really feeling like running this test just type EXIT and this test will be skipped>>\n\n" +
				"ON\n" +
				"OFF\n" +
				"ON\n" +
				"CONN GATE 1\n" +
				"TOGGLE 1\n" +
				"NUM 315\n" +
				"START\n" +
				"FIN\n" +
				"DNF\n" +
				"PRINT\n" +
				"EXIT");
		sysCont.readInput();
		System.out.println("\nIf no errors were thrown..then this test passed. YAY");
		System.out.println("\t-------Successfully tested readInput()-------\n");
	}
	
	@Test
	public void executeCommand() {
		System.out.println("\t-------Testing executeCommand()-------");
		try {
		
			sysCont.executeCommand("12:01:00.0	TIME 12:01:01.0");
			
			sysCont.executeCommand("12:01:02.0	ON");
			assertNotNull(sysCont.getEventLog());
			assertNotNull(sysCont.getTimer());
			assertEquals(EventType.IND, sysCont.getTimer().getCurrentEvent().getType());
			assertEquals(EventType.IND + "", sysCont.getTimer().getCurrentEvent().getEventName());
			assertNotNull(sysCont.getTimer().getTotalParticipants());
			assertNotNull(sysCont.getTimer().getCurrentEvent().getCurrentParticipants());
			assertNotNull(sysCont.getChannels());
			assertFalse(sysCont.getIsPrinterOn());

			sysCont.executeCommand("12:01:04.0	OFF");
			assertNull(sysCont.getEventLog());
			assertNull(sysCont.getTimer());
			assertNull(sysCont.getChannels());
			assertFalse(sysCont.getIsPrinterOn());
			
			sysCont.executeCommand("12:01:08.0	ON");
			
			sysCont.executeCommand("12:01:10.0	CONN GATE 1");
			assertEquals(1, sysCont.getChannels().size());
			assertEquals(1, sysCont.getChannels().get(0).getId());
			assertTrue(sysCont.getChannels().get(0).getState());
			assertEquals(SensorType.GATE.toString(), sysCont.getChannels().get(0).getSensorType());
			assertFalse(sysCont.getChannels().get(0).getSensorState());
			
			sysCont.executeCommand("12:01:14.0	TOGGLE 1");
			assertEquals(1, sysCont.getChannels().size());
			assertTrue(sysCont.getChannels().get(0).getState());
			assertFalse(sysCont.getChannels().get(0).getSensorState());
			assertEquals(SensorType.GATE.toString(), sysCont.getChannels().get(0).getSensorType());
			
			sysCont.executeCommand("12:01:18.0	NUM 234");
			assertEquals(1, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(1, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(1, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			assertEquals(234, sysCont.getTimer().getCurrentEvent().getStartingQueue().peek().getId());
			
			sysCont.executeCommand("12:01:20.0	NUM 315");
			assertEquals(2, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(2, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(2, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			for (Participant curPar : sysCont.getTimer().getCurrentEvent().getCurrentParticipants()) {
				assertNotNull(curPar.getLastRecord());
				assertEquals(sysCont.getTimer().getCurrentEvent().getEventId(), curPar.getLastRecord().getEventId());
				assertEquals("IND", curPar.getLastRecord().getEventName());
			}
			
			sysCont.executeCommand("12:01:22.0	START");
			assertTrue(sysCont.getTimer().getCurrentEvent().getCompetingParticipants().get(0).getIsCompeting());
			assertEquals(315, sysCont.getTimer().getCurrentEvent().getStartingQueue().peek().getId());
			
			sysCont.executeCommand("12:01:24.0	NUM 435");
			assertEquals(3, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(2, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(3, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			
			sysCont.executeCommand("12:01:26.0	FIN");
			assertEquals(0, sysCont.getTimer().getCurrentEvent().getCompetingParticipants().size());
			assertEquals(315, sysCont.getTimer().getCurrentEvent().getStartingQueue().peek().getId());
			assertFalse(sysCont.getTimer().getCurrentEvent().getStartingQueue().peek().getIsCompeting());
			
			sysCont.executeCommand("12:01:28.0	START");
			assertTrue(sysCont.getTimer().getCurrentEvent().getCompetingParticipants().get(0).getIsCompeting());
			assertEquals(435, sysCont.getTimer().getCurrentEvent().getStartingQueue().peek().getId());
			
			sysCont.executeCommand("12:01:30.0	START");
			assertTrue(sysCont.getTimer().getCurrentEvent().getCompetingParticipants().get(0).getIsCompeting());
			assertEquals(0, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			
			sysCont.executeCommand("12:01:32.0	DNF");
			assertFalse(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(1).getIsCompeting());
			assertEquals(0, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			
			sysCont.executeCommand("12:01:34.0	FIN");
			assertEquals(0, sysCont.getTimer().getCurrentEvent().getCompetingParticipants().size());
			assertEquals(0, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			
			sysCont.executeCommand("12:01:36.0	PRINT");
			
			try {
				fileReader = new FileReader(sysCont.getEventLog().getEventFile());
				BufferedReader reader = new BufferedReader(fileReader);
				while (reader.ready()) {
					String line = reader.readLine();
					assertEquals("12:01:08.0", line.substring(0, 10));
					assertEquals("IND", line.substring(13));
					assertEquals("IND 12:01:08.000", reader.readLine().substring(2));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			sysCont.executeCommand("12:01:38.0	OFF");
			assertNull(sysCont.getEventLog());
			assertNull(sysCont.getTimer());
			assertNull(sysCont.getChannels());
			assertFalse(sysCont.getIsPrinterOn());
			
			sysCont.executeCommand("12:01:40.0	EXIT");
			assertNull(sysCont.getSystemTime());
			assertNull(sysCont.getEventLog());
			assertNull(sysCont.getTimer());
			assertNull(sysCont.getChannels());
			assertFalse(sysCont.getIsPrinterOn());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\t-------Successfully tested executeCommand()-------\n");
	}
}
