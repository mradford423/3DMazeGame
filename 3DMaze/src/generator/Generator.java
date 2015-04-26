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
					if(j%2 == 0){
						temp.setRight(true);
					}
					if(j%2 == 1){
						temp.setLeft(true);
					}
				}
				else if(i == height-1){ //bottom row
					temp.setDown(true);
					temp.setEdge(true);
					if(j%2 == 0){
						temp.setLeft(true);
					}
					if(j%2 == 1){
						temp.setRight(true);
					}
				}
				else{
					temp.setLeft(true);
					temp.setRight(true);
				}
				/*if(j == 0){ //leftmost column
					temp.setLeft(true);
					temp.setEdge(true);
				}
				else{
					
				}
				if(j == 15){
					temp.setRight(true);
					temp.setEdge(true);
				}*/
				
			}
		}
		//fix corners
		maze[0][0].setLeft(true);
		//maze[0][0].setRight(false);
		maze[height-1][0].setLeft(true);
		maze[height-1][0].setRight(false);
		maze[0][width-1].setRight(true);
		maze[0][width-1].setLeft(false);
		
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
}
