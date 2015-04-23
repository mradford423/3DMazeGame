package generator;

public class Generator {
	
	private Cell[][] maze = new Cell[16][16];
	
	public Generator(){
		
	}

	public void generate(){
		Cell temp;
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 16; j++){
				temp = maze[i][j] = new Cell(i,j);
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
				else if(i == 15){ //bottom row
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
		maze[15][0].setLeft(true);
		maze[15][0].setRight(false);
		maze[0][15].setRight(true);
		maze[0][15].setLeft(false);
		
	}
	
	public Cell[][] getMaze(){
		return maze;
	}
	
	public String toString(){
		String ret = "";
		boolean[] lastDown = new boolean[16];
		for(int i = 0; i < 16; i++){
			boolean[] LandR = new boolean[17];
			for(int j = 0; j < 16; j++){
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
			for(int k = 0; k < 17; k++){
				if(LandR[k])
					ret+= "|";
				else
					ret+= " ";
				ret+="   ";
			}
			ret+="\n";
		}
		for(int i = 0; i < 16; i++){
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
