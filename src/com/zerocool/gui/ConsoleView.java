package com.zerocool.gui;

import java.util.ArrayList;

import com.zerocool.controllers.SystemController;
import com.zerocool.services.SystemTime;

public class ConsoleView {

	private ArrayList<Participant> waiting;
	private ArrayList<Participant> running;
	private ArrayList<Participant> finished;
	private String[] cmds;
	private SystemController admin;
	
	private int index;
	private int currentLine;
	
	public ConsoleView(SystemController systemController) {
		waiting = new ArrayList<Participant>();
		running = new ArrayList<Participant>();
		finished = new ArrayList<Participant>();
		admin = systemController;
		cmds = admin.getCommandList();
		index = -1;
	}
	
	public void prevCommand() {
		moveIndex(index - 1);
	}
	
	public void nextCommand() {
		moveIndex(index + 1);
	}
	
	public void moveUp() {
		moveLine(currentLine - 1);
	}
	
	public void moveDown() {
		moveLine(currentLine + 1);
	}
	
	public void addParticipant(String num, String addTime) {
		waiting.add(new Participant(num, addTime));
	}
	
	public void startParticipant() {
		
	}
	
	private void moveIndex(int next) {
		index = next < -1 ? cmds.length - 1 : next > cmds.length - 1 ? -1 : next;
	}
	
	private void moveLine(int next) {
		int totalLines = waiting.size() + running.size() + finished.size();
		currentLine = next < 0 ? totalLines : next > totalLines ? 0 : next;
		index = -1;
	}
	
	private String getCurrentCommand() {
		return index == - 1 ? "" : cmds[index];
	}
	
	public String getView() {
		String text = "";
		
		if (waiting.isEmpty() && running.isEmpty() && finished.isEmpty()) {
			text = ">" + getCurrentCommand();
		} else {
			int line = 0;
			for (Participant par : waiting) {
				text += par.print();
				if (line == currentLine) {
					text += " >" + getCurrentCommand();
				}
				text += "\n";
				line++;
			}
			
			text += "\n";
			
			for (Participant par : running) {
				text += par.print();
				if (line == currentLine) {
					text += " >" + getCurrentCommand();
				}
				text += "\n";
				line++;
			}
			
			text += "\n";
			
			for (Participant par : finished) {
				text += par.print();
				if (line == currentLine) {
					text += " >" + getCurrentCommand();
				}
				text += "\n";
				line++;
			}
		}
		
		return text;
	}
	
	private class Participant {
		
		private String num;
		private String addedTime;
		private String startTime;
		private String endTime;
		private String action;
		
		public Participant(String id, String time) {
			num = id;
			addedTime = time;
			startTime = "";
			endTime = "";
			action = "";
		}
		
		public String print() {
			return num + "  " + getTime() + " " + action + " ";
		}
		
		private String getTime() {
			return action.equals("") ? addedTime : action.equals("R") ? elapsedTime() : endTime;
		}
		
		private String elapsedTime() {
			return "" + (admin.getSystemTime().getTime() - SystemTime.getTimeInMillis(startTime));
		}
		
	}
	
}