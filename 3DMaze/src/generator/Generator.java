package generator;

public class Generator {
	
	private Cell[][] maze;
	private int height;
	private int width;
	private float cellWidth;
	private float cellHeight;
	
	public Generator(int m, int n, float cellWidth, float cellHeight){
		height = m;
		width = n;
		maze = new Cell[height][width];
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
	}

	public void generate(){
		Cell temp;
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				temp = maze[i][j] = new Cell(i,j,cellWidth,cellHeight);
				if(i == 0){//top row
					temp.setUp(true);
					temp.setEdge(true);
					/*if(j%2 == 0){
						temp.setRight(true);
					}
					if(j%2 == 1){
						temp.setLeft(true);
					}*/
				}
				else if(i == height-1){ //bottom row
					temp.setDown(true);
					temp.setEdge(true);
					/*if(j%2 == 0){
						temp.setLeft(true);
					}
					if(j%2 == 1){
						temp.setRight(true);
					}*/
				}
				
				if(j == 0){ //leftmost column
					temp.setLeft(true);
					temp.setEdge(true);
				}
				if(j == width-1){
					temp.setRight(true);
					temp.setEdge(true);
				}
				temp.setRight(true);
				temp.setDown(true);
			}
		}
		/*
		//fix corners
		maze[0][0].setLeft(true);
		//maze[0][0].setRight(false);
		maze[height-1][0].setLeft(true);
		maze[height-1][0].setRight(false);
		maze[0][width-1].setRight(true);
		maze[0][width-1].setLeft(false);
		*/
	}
	
	
	public Cell[][] getMaze(){
		return maze;
	}
	
	public String toString(){
		String ret = "";
		boolean[] lastDown = new boolean[width];
		for(int i = 0; i < height; i++){
			boolean[] LandR = new boolean[width+1];
			for(int j = 0; j < width; j++){
				ret+= "O";
				if(maze[i][j].getUp() || lastDown[j])
					ret+= "---";
				else
					ret+= "   ";
				if(maze[i][j].getLeft())
					LandR[j] = true;
				if(maze[i][j].getRight())
					LandR[j+1] = true;
				if(maze[i][j].getDown())
					lastDown[j] = true;
				else
					lastDown[j] = false;
			}
			ret+= "O";
			ret+= "\n";
			for(int k = 0; k < width+1; k++){
				if(LandR[k])
					ret+= "|";
				else
					ret+= " ";
				ret+="   ";
			}
			ret+="\n";
		}
		for(int i = 0; i < width; i++){
			ret+= "O";
			if(lastDown[i])
				ret+= "---";
			else
				ret+= "   ";
		}
		ret+= "O";
		return ret;
	}
	
	
	//These methods generate the various maze types
	public void maze0(){
		maze[0][0].setDown(false);
		maze[0][0].setRight(false);
		maze[1][0].setDown(false);
		maze[2][0].setDown(false);
		maze[2][0].setRight(false);
		maze[3][0].setRight(false);
		maze[4][0].setRight(false);
		maze[5][0].setRight(false);
		maze[5][0].setDown(false);
		maze[6][0].setDown(false);
		maze[7][0].setRight(false);
		
		maze[0][1].setDown(false);
		maze[1][1].setRight(false);
		maze[2][1].setRight(false);
		maze[3][1].setDown(false);
		maze[5][1].setDown(false);
		maze[6][1].setRight(false);
		
		maze[0][2].setDown(false);
		maze[0][2].setRight(false);
		maze[2][2].setRight(false);
		maze[3][2].setRight(false);
		maze[3][2].setDown(false);
		maze[4][2].setDown(false);
		maze[5][2].setRight(false);
		maze[6][2].setDown(false);
		maze[7][2].setRight(false);
		
		maze[0][3].setDown(false);
		maze[2][3].setDown(false);
		maze[2][3].setRight(false);
		maze[4][3].setRight(false);
		maze[4][3].setDown(false);
		maze[6][3].setDown(false);
		maze[6][3].setRight(false);
		maze[7][3].setRight(false);
		
		maze[0][4].setDown(false);
		maze[0][4].setRight(false);
		maze[1][4].setDown(false);
		maze[2][4].setDown(false);
		maze[3][4].setRight(false);
		maze[4][4].setDown(false);
		maze[5][4].setDown(false);
		maze[7][4].setRight(false);
		
		maze[0][5].setDown(false);
		maze[0][5].setRight(false);
		maze[1][5].setRight(false);
		maze[2][5].setDown(false);
		maze[2][5].setRight(false);
		maze[4][5].setRight(false);
		maze[4][5].setDown(false);
		maze[5][5].setRight(false);
		maze[6][5].setDown(false);
		maze[6][5].setRight(false);
		
		maze[0][6].setRight(false);
		maze[1][6].setRight(false);
		maze[2][6].setDown(false);
		maze[3][6].setDown(false);
		maze[4][6].setRight(false);
		maze[5][6].setDown(false);
		
		maze[1][7].setDown(false);
		maze[2][7].setDown(false);
		maze[4][7].setDown(false);
		maze[5][7].setDown(false);
		maze[6][7].setDown(false);
	}
	
	public void maze1(){
		maze[0][0].setDown(false);
		maze[0][0].setRight(false);
		maze[1][0].setDown(false);
		maze[2][0].setDown(false);
		maze[3][0].setRight(false);
		maze[4][0].setRight(false);
		maze[4][0].setDown(false);
		maze[5][0].setDown(false);
		maze[6][0].setDown(false);
		maze[7][0].setRight(false);
		
		maze[0][1].setDown(false);
		maze[0][1].setRight(false);
		maze[1][1].setRight(false);
		maze[2][1].setDown(false);
		maze[2][1].setPowerUp(true);
		maze[4][1].setRight(false);
		maze[5][1].setDown(false);
		maze[5][1].setRight(false);
		maze[6][1].setRight(false);
		maze[7][1].setRight(false);
		
		maze[0][2].setRight(false);
		maze[1][2].setDown(false);
		maze[2][2].setDown(false);
		maze[3][2].setDown(false);
		maze[5][2].setRight(false);
		maze[6][2].setDown(false);
		
		maze[0][3].setDown(false);
		maze[1][3].setRight(false);
		maze[2][3].setDown(false);
		maze[2][3].setRight(false);
		maze[3][3].setDown(false);
		maze[4][3].setDown(false);
		maze[6][3].setDown(false);
		maze[6][3].setRight(false);
		maze[7][3].setRight(false);
		
		maze[0][4].setRight(false);
		maze[1][4].setRight(false);
		maze[2][4].setRight(false);
		maze[3][4].setRight(false);
		maze[3][4].setDown(false);
		maze[4][4].setDown(false);
		maze[5][4].setRight(false);
		maze[6][4].setRight(false);
		
		maze[0][5].setRight(false);
		maze[1][5].setRight(false);
		maze[2][5].setDown(false);
		maze[4][5].setRight(false);
		maze[4][5].setDown(false);
		maze[6][5].setDown(false);
		maze[7][5].setRight(false);
		
		maze[0][6].setRight(false);
		maze[1][6].setDown(false);
		maze[2][6].setDown(false);
		maze[3][6].setRight(false);
		maze[4][6].setDown(false);
		maze[5][6].setDown(false);
		maze[6][6].setRight(false);
		
		maze[0][7].setDown(false);
		maze[1][7].setDown(false);
		maze[2][7].setDown(false);
		maze[4][7].setDown(false);
		maze[6][7].setDown(false);
	}
}
