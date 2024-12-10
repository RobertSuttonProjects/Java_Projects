import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Panel extends JPanel {
	public static Panel Instance;

	int scoreLeft = 0;
	int scoreRight = 0;

	int powerupLeft = 3;
	int powerupRight = 3;

	boolean gameOver = false;
	Ball ball;
	Paddle paddle1;
	Paddle paddle2;
	Input input;

	JLabel leftScore = new JLabel();
	JLabel rightScore = new JLabel();
	JButton RB = new JButton();

	public Panel() {
		Instance = this;
		leftScore.setBounds(Constants.FRAME_WIDTH / 2 - 60, 0, 50, 50);
		rightScore.setBounds(Constants.FRAME_WIDTH / 2 + 10, 0, 50, 50);
		rightScore.setFont(new Font("Serif", Font.BOLD, 50));
		leftScore.setFont(new Font("Serif", Font.BOLD, 50));
		leftScore.setHorizontalAlignment(SwingConstants.CENTER);
		rightScore.setHorizontalAlignment(SwingConstants.CENTER);
		updateScore();
		this.add(leftScore);
		this.add(rightScore);

		this.addKeyListener(Input.Instance);
		this.setPreferredSize(new Dimension(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT));
		this.setFocusable(true);
		this.setLayout(null);
		innitGame();
		this.repaint();
		update();
	}

	public void updateScore() {
		leftScore.setText(scoreLeft + "");
		rightScore.setText(scoreRight + "");
		if (scoreLeft > 10 || scoreRight > 10) {
			gameOver();
		}
	}

	public void usePowerup(boolean isLeftUsing) {
		if (isLeftUsing && powerupLeft > 0) {
			powerupLeft--;
			ball.ySpeed *= -1;

		} else if (!isLeftUsing && powerupRight > 0) {
			powerupLeft--;
			ball.ySpeed *= -1;

		}

	}

	public void restartGame() {

		scoreLeft = 0;
		scoreRight = 0;
		this.remove(RB);
		gameOver = false;
	}

	public void gameOver() {
		gameOver = true;
		RB.setBounds(Constants.FRAME_WIDTH / 2 - 50, Constants.FRAME_HEIGHT / 2 - 25, 100, 50);
		RB.setText("Restart");
		RB.addActionListener(e -> restartGame());

		this.add(RB);
	}

	public void innitGame() {
		ball = new Ball(Constants.FRAME_WIDTH / 2, Constants.FRAME_HEIGHT / 2, 10, 10);
		paddle1 = new Paddle(30, 0, 10, 50);
		paddle2 = new Paddle(Constants.FRAME_WIDTH - 10 - 30, 0, 10, 50);

	}

	public void update() {
		if (gameOver) {
			return;
		}
		Input.Instance.updateL();
		Input.Instance.updateR();

		ball.update(paddle1, paddle2);
		paddle1.update();
		paddle2.update();

	}

	public void paintComponent(Graphics g) {

		g.setColor(Color.black);
		g.fillRect(0, 0, Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
		ball.draw(g);
		paddle1.draw(g);
		paddle2.draw(g);
		g.drawLine(Constants.FRAME_WIDTH / 2, 0, Constants.FRAME_WIDTH / 2, Constants.FRAME_HEIGHT);
		if (gameOver) {
			return;
		}
	}

}
