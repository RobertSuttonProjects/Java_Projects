import java.awt.Color;
import java.awt.Graphics;

enum Type{
	BALL,
	PADDLE
}
public class Entity {
	public Entity(double x, double y, double width, double height, Type type) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
	}

	public double x, y, width, height, xSpeed, ySpeed;
	public Type type;

	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		if(type == Type.BALL) {
			g.fillOval((int)x, (int)y, (int)width, (int)height);
		} else if (type == Type.PADDLE){
			g.fillRect((int)x, (int)y, (int)width, (int)height);
		}
	}
	public void update() {
		moveX();
		moveY();
	}
	public void moveX() {
		x += xSpeed;
	}

	public void moveY() {
		y += ySpeed;
	}
	public void increaseSpeed(double xAmount, double yAmount) {
		this.xSpeed += xAmount;
		this.ySpeed += yAmount;
	}

}
