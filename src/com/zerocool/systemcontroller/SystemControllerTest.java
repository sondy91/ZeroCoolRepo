package com.zerocool.systemcontroller;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.systemcontroller.SystemController;
import com.zerocool.systemcontroller.event.*;
import com.zerocool.systemcontroller.eventlog.EventLog;
import com.zerocool.systemcontroller.systemtime.SystemTime;
import com.zerocool.systemcontroller.timer.Timer;

public class SystemControllerTest {
	
	SystemController sysCont = null;

	@Before
	public void setUp() throws Exception {
		sysCont = new SystemController();
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testSystemController(){
		System.out.println("Testing SystemController constructor");
		assertNotNull(sysCont.getChannels());
		assertNotNull(sysCont.getEventLog());
		assertNotNull(sysCont.getTimer());
		assertNotNull(sysCont.getSystemTime());
		assertEquals(0, sysCont.getId());
	}
	
	@Test
	public void testReadFile(){
		System.out.println("Testing SystemContoller");
		File file = new File("test_files/syscontroljunittestfile.txt");
		sysCont.readFile(file);
	}

}
