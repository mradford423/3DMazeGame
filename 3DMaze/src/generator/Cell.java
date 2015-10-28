package generator;

public class Cell {
	private int column;
	private int row;
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean isEdge;
	private boolean powerUp;
	private float width;
	private float height;
	private boolean finish;
	

	
	public Cell(int col, int r, float width, float height){
		this.column = col;
		this.row = r;
		this.up = false;
		this.down = false;
		this.left = false;
		this.right = false;
		this.isEdge = false;
		this.width = width;
		this.height = height;
		this.powerUp = false;
		this.finish = false;
	}
	
	public Cell(int col, int r, boolean isUp, boolean isDown, boolean isLeft, boolean isRight){
		this.column = col;
		this.row = r;
		this.up = isUp;
		this.down = isDown;
		this.left = isLeft;
		this.right = isRight;
		this.isEdge = false;
	}
	
	public void setUp(boolean isUp){
		up = isUp;
	}
	
	public void setDown(boolean isDown){
		down = isDown;
	}
	
	public void setLeft(boolean isLeft){
		left = isLeft;
	}
	
	public void setRight(boolean isRight){
		right = isRight;
	}
	
	public void setColumn(int col){
		column = col;
	}
	
	public void setRow(int r){
		row = r;
	}
	
	public void setEdge(boolean edge){
		isEdge = edge;
	}
	
	public boolean getUp(){
		return up;
	}
	
	public boolean getDown(){
		return down;
	}
	
	public boolean getLeft(){
		return left;
	}
	
	public boolean getRight(){
		return right;
	}
	
	public int getColumn(){
		return column;
	}
	
	public int getRow(){
		return row;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
	public boolean getEdge(){
		return isEdge;
	}
	
	public void setPowerUp(boolean power){
		powerUp = power;
	}
	
	public boolean getPowerUp(){
		return powerUp;
	}
	
	public void setFinish(boolean isFinish){
		finish = isFinish;
	}
	
	public boolean getFinish(){
		return finish;
	}
}
