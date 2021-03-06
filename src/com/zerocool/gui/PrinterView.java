package com.zerocool.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.zerocool.controllers.SystemController;
import com.zerocool.controllers.TaskList;
import com.zerocool.controllers.TaskList.Task;

public class PrinterView extends JTextArea {

	private static final long serialVersionUID = 1L;

	private SystemController admin;
	private TaskList.Task lastTask;

	public PrinterView(SystemController admin) {
		super();

		this.admin = admin;
		setPrefs();
	}

	private void setPrefs() {
		setLineWrap(true);
		setEditable(false);
		setFont(new Font("Tahoma", Font.CENTER_BASELINE, 10));
		setBackground(Main.BLEACHED_ALMOND);
		setForeground(Color.DARK_GRAY);
		setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY, 2), new EmptyBorder(15, 15, 15, 15)));
	}

	private void addText(String text) {
		setText(getText() + "\n" + text);
	}

	public void update() {
		if (admin.getIsPrinterOn()) {
			Task nextTask = admin.getLastTask();
			if (nextTask != null && !nextTask.equals(lastTask)) {
				lastTask = nextTask;
				addText(nextTask.toString());
			}
			
			if (admin.shouldPrint()) {
				addText(admin.getPrintData());
			}
		}
	}
	
	public void printError(String error) {
		addText(error);
	}

	public void toggleEnabled(boolean enabled) {
		setEnabled(enabled);
		setBackground(enabled ? Main.BLEACHED_ALMOND : Main.BLACK);
	}

}
