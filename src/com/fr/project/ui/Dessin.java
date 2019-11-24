package com.fr.project.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.fr.project.game.Game;

public class Dessin extends JPanel {
	
	private Game game;
	private String message;
	
	public Dessin(Game game) {	
		this.game=game;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		this.setBackground(Color.BLACK);

		dessinPad1(g);	
		dessinPad2(g);
		dessinBall(g);
		
		if(!game.hasStartedOnce())
			message="Appuyez sur C pour commencer";
		else {
			if(game.isPlaying())
				message="Appuyez sur P pour mettre en pause";
			else
				message="Appuyez sur P pour reprendre - Appuyez sur R pour recommencer";
			
		}
		g.drawString(message, 0, 20);
		g.drawString("Appuyez sur ECHAP pour quitter",0,40);
		
		if(game.getPad1().isSuperStrikeAvailable()) {
			g.drawString("Super Strike disponible - appuyez sur S pour l'utiliser",0,60);
		}
		
		
		g.setFont(new Font("TimesRoman",Font.BOLD, 40)); 
		g.setColor(Color.RED);
		g.drawString(game.getScoreJoueur1()+" - "+game.getScoreJoueur2(),750,100);
		
		
		if(!game.isPlaying()&&game.hasStartedOnce()) {
			if(game.getCountdown()!=0) {
				g.setFont(new Font("TimesRoman",Font.BOLD, 80)); 
				g.setColor(Color.RED);
				g.drawString(String.valueOf(game.getCountdown()),730,520);
			}
		}
		
		if(game.isEnd()) {
				g.setFont(new Font("TimesRoman",Font.BOLD, 80)); 
				g.setColor(Color.RED);
				g.drawString(String.valueOf(game.getEndGame()),400,520);
		}
		
	}
	
	public void dessinPad1(Graphics g) {
		if(game.getPad1().isSuperStrikeInUse())
			g.setColor(Color.getHSBColor((float)Math.random(), (float)Math.random(), (float)Math.random()));
		else
			g.setColor(Color.WHITE);
		g.fillRect(game.getPad1().getPositionCenterX()-game.getPad1().sizeX/2, game.getPad1().getPositionCenterY()-game.getPad1().sizeY/2, game.getPad1().sizeX, game.getPad1().sizeY);
	}
	
	public void dessinPad2(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(game.getPad2().getPositionCenterX()-game.getPad2().sizeX/2, game.getPad2().getPositionCenterY()-game.getPad2().sizeY/2, game.getPad2().sizeX, game.getPad2().sizeY);
		
	}
	
	public void dessinBall(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillOval(game.getBall().getPositionX()-game.getBall().size/2, game.getBall().getPositionY()-game.getBall().size/2, game.getBall().size, game.getBall().size);
	}

}
