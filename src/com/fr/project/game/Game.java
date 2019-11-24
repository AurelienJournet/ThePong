package com.fr.project.game;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.fr.project.entity.AbstractPad;
import com.fr.project.entity.Ball;
import com.fr.project.entity.Pad1;
import com.fr.project.entity.Pad2;
import com.fr.project.ui.Dessin;

public class Game extends JFrame implements Runnable, KeyListener {

	private boolean startedOnce;
	private boolean playing;
	private boolean end;
	private String endGame;
	private final int refreshRate = 60;
	private AbstractPad pad1;
	private AbstractPad pad2;
	private Ball ball;
	private int scoreJoueur1;
	private int scoreJoueur2;
	public final int winScore = 6;
	private int countdown;

	public Game() {

		end = false;
		endGame = "";
		this.setResizable(false);
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		JPanel scene = new Dessin(this);
		scene.setPreferredSize(new Dimension(1500, 1000));
		contentPane.add(scene);
		pack();
		// this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("This is the pong");
		this.pad1 = new Pad1();
		this.pad2 = new Pad2();
		pad1.setSuperStrikeAvailable(true);
		this.ball = new Ball();

		addKeyListener(this);
		this.setVisible(true);

		startedOnce = false;
		playing = false;
		scoreJoueur1 = 0;
		scoreJoueur2 = 0;
		countdown = 0;
	}

	@Override
	public void run() {
		int i = 0;
		while (true) {
			this.repaint();
			try {
				Thread.sleep(1000 / refreshRate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (playing) {
				ball.move();
				pad1.move();
				pad2.move();
				checkCollision();
				checkPoints();
				iaPad2();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		int code = e.getExtendedKeyCode();

		switch (code) {
		case (KeyEvent.VK_UP):
			pad1.upKeyPress();
			break;
		case (KeyEvent.VK_DOWN):
			pad1.downKeyPress();
			break;
		case (27):
			System.exit(0);
			break;
		case (67):// touche c pour commencer
			if (!startedOnce) {
				startedOnce = true;
				setPlaying(true);
				end=false;
			}
			break;
		case (80):// touche p pour pause
			setPlaying(!isPlaying());
			break;

		case (82):// touche r pour réinitialiser
			if (!playing)
				init(true);
			break;
		default:
			break;
		case(83)://touche s pour super strike
			useSuperStrike(pad1);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getExtendedKeyCode()) {
		case (KeyEvent.VK_UP):
			pad1.releaseKeyPress();
			break;
		case (KeyEvent.VK_DOWN):
			pad1.releaseKeyPress();
			break;
		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public int getCountdown() {
		return countdown;
	}

	public void checkCollision() {

		int xTest;
		int yTest;

		// murs haut et bas
		if ((ball.getPositionY() + ball.size / 2 > 1000 && ball.getVelocityY() > 0)
				|| (ball.getPositionY() - ball.size / 2 < 0 && ball.getVelocityY() < 0))
			ball.setVelocityY(-ball.getVelocityY());

		// Pad 1 et 2
		AbstractPad[] tab = { pad1, pad2 };
		for (AbstractPad pad : tab) {
			double sign;

			if (pad.equals(pad1))
				sign = 1;
			else
				sign = -1;

			if ((ball.getPositionX() < (ball.size / 2 + 2 * pad.sizeX) && ball.getVelocityX() < 0)
					|| (ball.getPositionX() > 1500 - (ball.size / 2 + 2 * pad.sizeX) && ball.getVelocityX() > 0)) {
				int i = 0;
				double coefSuperStrike=1;
				boolean s = false;
				
				if(pad.isSuperStrikeInUse())
					coefSuperStrike=2;

				// Collisions avec les parties verticales
				while (i < 10 && !s) {
					// on discretise en 10 points la partie gauche de la balle

					xTest = ball.getPositionX() + (int) Math.round(
							(double) (ball.size / 2) * Math.cos(Math.PI / 2 + sign * Math.PI * ((double) i / 10)));
					yTest = ball.getPositionY() + (int) Math.round(
							(double) (ball.size / 2) * Math.sin(Math.PI / 2 + sign * Math.PI * ((double) i / 10)));

					if (yTest < pad.getPositionCenterY() + pad.sizeY / 2
							&& yTest > pad.getPositionCenterY() - pad.sizeY / 2 && pad.equals(pad1)
							&& xTest < (pad.getPositionCenterX() + pad.sizeX / 2)) {
						ball.setVelocityX(-coefSuperStrike*ball.getVelocityX());
						ball.setVelocityY(coefSuperStrike*(ball.getVelocityY() + pad.getVelocityY()));
						s = !s;
					}

					if (yTest < pad.getPositionCenterY() + pad.sizeY / 2
							&& yTest > pad.getPositionCenterY() - pad.sizeY / 2 && pad.equals(pad2)
							&& xTest > (pad.getPositionCenterX() - pad.sizeX / 2)) {
						ball.setVelocityX(-coefSuperStrike*ball.getVelocityX());
						ball.setVelocityY(coefSuperStrike*(ball.getVelocityY() + pad.getVelocityY()));
						s = !s;
					}
					i++;
				}

				// Collisions avec les parties horizontales
				while (i < 10 && !s) {

					if (ball.getVelocityY() < 0) {
						// on discretise en 10 points la partie haute de la balle
						xTest = ball.getPositionX()
								+ (int) Math.round((double) (ball.size / 2) * Math.cos(Math.PI * ((double) i / 10)));
						yTest = ball.getPositionY()
								+ (int) Math.round((double) (ball.size / 2) * Math.sin(Math.PI * ((double) i / 10)));

						if (xTest < pad.getPositionCenterX() + pad.sizeX / 2
								&& xTest > pad.getPositionCenterX() - pad.sizeX / 2
								&& yTest > pad.getPositionCenterY() + pad.sizeY / 2) {
							ball.setVelocityY(-ball.getVelocityY());
							s = !s;
						}

					} else
						// on discretise en 10 points la partie basse de la balle
						xTest = ball.getPositionX()
								+ (int) Math.round((double) (ball.size / 2) * Math.cos(-Math.PI * ((double) i / 10)));
					yTest = ball.getPositionY()
							+ (int) Math.round((double) (ball.size / 2) * Math.sin(-Math.PI * ((double) i / 10)));

					if (xTest < pad.getPositionCenterX() + pad.sizeX / 2
							&& xTest > pad.getPositionCenterX() - pad.sizeX / 2
							&& yTest > pad.getPositionCenterY() - pad.sizeY / 2) {
						ball.setVelocityY(-ball.getVelocityY());
						s = !s;
					}

					i++;
				}

			}
		}

		// TODO gérer collision avec le haut et bas des pads
	}

	public void iaPad2() {

		if (ball.getPositionY() > pad2.getPositionCenterY() - pad2.sizeY / 2
				&& ball.getPositionY() < pad2.getPositionCenterY() + pad2.sizeY / 2) {
			pad2.releaseKeyPress();
		} else if (ball.getPositionY() > pad2.getPositionCenterY())
			pad2.downKeyPress();
		else
			pad2.upKeyPress();

	}

	public void checkPoints() {
		// Si la balle touche à gauche
		if (ball.getPositionX() - ball.size / 2 < 0) {

			pad1.setSuperStrikeInUse(false);
			pad2.setSuperStrikeInUse(false);
			scoreJoueur2++;
			if (this.scoreJoueur2 == this.winScore) {
				endGame = "Vous avez perdu !";
				end = true;
				init(true);
			} else {
				init(false);
			}
		}

		if (ball.getPositionX() + ball.size / 2 > 1500) {
			pad1.setSuperStrikeInUse(false);
			pad2.setSuperStrikeInUse(false);
			scoreJoueur1++;
			if (this.scoreJoueur1 == this.winScore) {
				endGame = "Vous avez gagné !";
				end = true;
				init(true);
			} else
				init(false);
		}

	}

	public void init(boolean restart) {
		this.setPlaying(false);
		this.setStartedOnce(false);
		this.ball = null;
		this.ball = new Ball();

		// init du restart
		if (restart) {
			this.pad1 = null;
			this.pad1 = new Pad1();
			pad1.setSuperStrikeAvailable(true);
			this.pad2 = null;
			this.pad2 = new Pad2();
			pad2.setSuperStrikeAvailable(true);

			setScoreJoueur1(0);
			setScoreJoueur2(0);
			// init après point marqué
		} else {
			pad1.init();
			pad2.init();
			
			this.setStartedOnce(true);
			countdown = 3;

			while (countdown > 0) {
				this.repaint();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				countdown--;
			}
			setPlaying(true);
		}
	}
	
	public void useSuperStrike(AbstractPad pad) {	
		if(isPlaying()&&pad.isSuperStrikeAvailable()) {
			pad.setSuperStrikeInUse(true);
			pad.setSuperStrikeAvailable(false);
		}
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public boolean hasStartedOnce() {
		return startedOnce;
	}

	public void setStartedOnce(boolean startedOnce) {
		this.startedOnce = startedOnce;
	}

	public AbstractPad getPad1() {
		return pad1;
	}

	public AbstractPad getPad2() {
		return pad2;
	}

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public int getScoreJoueur1() {
		return scoreJoueur1;
	}

	public void setScoreJoueur1(int scoreJoueur1) {
		this.scoreJoueur1 = scoreJoueur1;
	}

	public int getScoreJoueur2() {
		return scoreJoueur2;
	}

	public void setScoreJoueur2(int scoreJoueur2) {
		this.scoreJoueur2 = scoreJoueur2;
	}

	public boolean isEnd() {
		return end;
	}

	public String getEndGame() {
		return endGame;
	}

}
