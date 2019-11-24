package com.fr.project;

import com.fr.project.game.Game;

public class Main {
	public static void main(String[] args) {
		new Thread(new Game()).start();
	}
}
