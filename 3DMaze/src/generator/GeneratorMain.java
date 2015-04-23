package generator;

public class GeneratorMain {

	public static void main(String[] args) {
		Generator generator = new Generator(8,8);
		generator.generate();
		System.out.println(generator.toString());
		
		
		/*Cell[][] maze = generator.getMaze();
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 16; j++){
				int count = 0;
				if(maze[i][j].getUp())
					count+=1;
				if(maze[i][j].getDown())
					count+=1;
				if(maze[i][j].getLeft())
					count+=1;
				if(maze[i][j].getRight())
					count+=1;
				System.out.print(count + " ");
			}
			System.out.println("");
		}*/

	}

}
