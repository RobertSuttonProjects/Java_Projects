public class Ball extends Entity{
    boolean isMovingRight = true;
    
    public Ball(double x, double y, double width, double height) {
        super(x, y, width, height, Type.BALL);
        this.increaseSpeed(2, 2); 
    }
    
    public void update(Paddle paddleL, Paddle paddleR) {

    	if(y <= 0) {
    		ySpeed *= -1;
    	}
    	if(y >= Constants.FRAME_HEIGHT - height) {
    		ySpeed *= -1;
    	}
        if (isMovingRight && x + width>= paddleR.x && x + width <= paddleR.x + width  && y+ height /2 <= paddleR.y + paddleR.height && y+ height /2 >= paddleR.y) {
            
        	xSpeed *= -1;
            isMovingRight = false;
        }
        else if (!isMovingRight && x <= paddleL.x + paddleL.width && x >= paddleL.x - width&& y+ height /2 <= paddleL.y + paddleL.height && y+ height /2 >= paddleL.y) {
            xSpeed *= -1;  
            isMovingRight = true;
        }
        
        super.update();
        
        if (x <= 0) {
            givePoint(true); 
            resetBall();
        } else if (x >= Constants.FRAME_WIDTH - width) {
            givePoint(false);
            resetBall();     
        }
    }
    
    public void givePoint(boolean isRightWon) {
    	if(isRightWon) {
    		Panel.Instance.scoreRight++;
    	} else {
    		Panel.Instance.scoreLeft++;
    	}
		Panel.Instance.powerupLeft = 3;
		Panel.Instance.powerupRight = 3;

		Panel.Instance.updateScore();

    }
    
    public void resetBall() {
        x = Constants.FRAME_WIDTH / 2;
        y = Constants.FRAME_HEIGHT / 2;
    }
}
