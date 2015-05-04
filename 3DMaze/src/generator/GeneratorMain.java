package generator;

import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import javax.sound.sampled.*;
import javax.swing.*;

import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;

import java.io.*;

public class GeneratorMain extends JFrame implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, ActionListener{
	
		private boolean initialized = false;
		private final GLCanvas canvas;
		private int winW = 750, winH = 750;
		//mouse control variables
		// gl shading/transformation variables
		private float tx = 0.0f, ty = 0.0f;
		private float scale = 1.0f;
		private float angleY = 0.0f;
		private float angleX = 0.0f;
		private float angleZ = 0f;
		//Camera variables
		private float cameraAngleY = 0.0f;
		private float cameraAngleX = 900f;
		private float cameraPositionX = 1.1f;
		private float cameraPositionY = 0.5f;
		private float cameraPositionZ = 8.5f;
		
		private boolean drawWireframe = false;
		private float lightPos[] = { -5.0f, 10.0f, 5.0f, 1.0f };
		//OpenGL variables
		private GL gl;
		private final GLU glu = new GLU();
		private final GLUT glut = new GLUT();
		//Maze variables
		private Cell[][] maze;
		private int mazeWidth = 2;
		private int mazeHeight = 2;
		int cellBoundariesX[];
		int cellBoundariesZ[];
		Texture textures[] = new Texture[3];

	public static void main(String[] args) {
		new GeneratorMain();
	}
	
	public GeneratorMain(){
		canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		getContentPane().add(canvas);
		setSize(winW, winH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		canvas.requestFocus();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(initialized == true){
			int newX = e.getX();
			int newY = e.getY();
			cameraAngleY += 1 * (newY - winH/2);
			cameraAngleX += 1 * (newX - winW/2);
			
			//Limit looking up/down
			if(cameraAngleY > 90){
				cameraAngleY = 90;
			}
			else if(cameraAngleY < -90){
				cameraAngleY = -90;
			}
			
			//Spin in a circle (wrap around)
			if(cameraAngleX > (1500)){
				cameraAngleX = (-300);
			}
			else if(cameraAngleX < (-1500)){
				cameraAngleX = 300;
			}
			
			//Move mouse back to center
			try{
				Robot robot = new Robot();
				robot.mouseMove((winW/2) + 8, winH/2 + 30);
			}
			catch(AWTException e1){
				e1.printStackTrace();
			}
			canvas.display();
		}
		
	}
	
	private Cell[] calculateCurrentCell(float x, float y){
		int heightCount = 0;
		int widthCount = 0;
		float yCopy = 10;
		float xCopy = 0;
		
		//Find y value of current cell
		while(y <= yCopy){
			yCopy -= mazeHeight;
			heightCount++;
		}
		heightCount--;
		
		//Find x value of current cell
		while(x >= xCopy){
			xCopy += mazeWidth;
			widthCount++;
		}
		widthCount--;
		
		//Store three cells: current cell, cell above, cell to the left
		Cell[] cellList = new Cell[3];
		cellList[0] = maze[heightCount][widthCount];
		if(heightCount > 0){
			cellList[1] = maze[heightCount-1][widthCount];
		}
		if(widthCount > 0){
			cellList[2] = maze[heightCount][widthCount-1];
		}
		return cellList;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		
		//Quit the game
		case KeyEvent.VK_ESCAPE:
		case KeyEvent.VK_Q:
			System.exit(0);
			break;
			
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch(e.getKeyChar()){
		case 'w':
			Cell testCell = calculateCurrentCell(cameraPositionX, cameraPositionZ)[0];
			int count = 0;
			
			//Calculate current global Z value of boundary in w-direction
			while(cameraPositionZ < cellBoundariesZ[count]){
				count++;
			}
			
			//Try to move forward, if hitting a wall, move back
			cameraPositionZ -= .1f;
			if(cameraPositionZ <= (cellBoundariesZ[count] + .1f) && testCell.getDown() == true){
				cameraPositionZ += .1f;
			}
			
			//If cell contains finish, end the game
			if(testCell.getFinish()){
				System.out.println("You win!");
				GeneratorMain.infoBox("You win!", "Congratulations");
				System.exit(0);
			}
			
			//If cell contains powerup, walk on opposite of current surface: floor or ceiling
			if(testCell.getPowerUp()){
				angleZ += 180f;
				testCell.setPowerUp(false);
				lightPos[0] *= -1;
				lightPos[1] *= -1;
				lightPos[3] *= -1;
			}
			break;
		case 's':
			Cell[] testCell2 = new Cell[3];
			testCell2 = calculateCurrentCell(cameraPositionX, cameraPositionZ);
			int count2 = 0;
			
			//Calculate current global Z value of boundary in s-direction
			while(cameraPositionZ < cellBoundariesZ[count2]){
				count2++;
			}
			
			//Try to move, if hitting a wall, move back
			cameraPositionZ += .1f;
			if(testCell2[1] != null && count2 != 0){ //If at edge of maze
				if(cameraPositionZ > (cellBoundariesZ[count2-1] - .1f) && testCell2[1].getDown() == true){
					cameraPositionZ -= .1f;
				}
			}
			else{
				if(cameraPositionZ > (cellBoundariesZ[0] - .1f) && testCell2[0].getUp() == true){
					cameraPositionZ -= .1f;
				}
			}
			
			//If cell contains finish, end game
			if(testCell2[0].getFinish()){
				System.out.println("You win!");
				GeneratorMain.infoBox("You win!", "Congratulations");
				System.exit(0);
			}
			
			//If cell contains powerup, walk on opposite of current surface: floor or ceiling
			if(testCell2[0].getPowerUp()){
				angleZ += 180f;
				testCell2[0].setPowerUp(false);
				lightPos[0] *= -1;
				lightPos[1] *= -1;
				lightPos[3] *= -1;
			}
				break;
		case 'd':
			Cell[] testCell3 = new Cell[3];
			testCell3 = calculateCurrentCell(cameraPositionX, cameraPositionZ);
			int count3 = 0;
			
			//Calculate current global X value of boundary in d-direction
			while(cameraPositionX > cellBoundariesX[count3]){
				count3++;
			}
			
			//Try to move, if hitting a wall, move back
			cameraPositionX -= .1f;
			if(testCell3[2] != null && count3 != 0){ //At edge of maze
				if(cameraPositionX <= (cellBoundariesX[count3-1] + .1f) && testCell3[2].getRight() == true){
					cameraPositionX += .1f;
				}
			}
			else{
				if(cameraPositionX <= (cellBoundariesX[0] + .1f) && testCell3[0].getLeft() == true){
					cameraPositionX += .1f;
				}
			}
			
			//If cell contains finish, end game
			if(testCell3[0].getFinish()){
				System.out.println("You win!");
				GeneratorMain.infoBox("You win!", "Congratulations");
				System.exit(0);
			}
			
			//If cell contains powerup, walk on opposite of current surface: floor or ceiling
			if(testCell3[0].getPowerUp()){
				angleZ += 180f;
				testCell3[0].setPowerUp(false);
				lightPos[0] *= -1;
				lightPos[1] *= -1;
				lightPos[3] *= -1;
			}
			break;
		case 'a':
			Cell testCell4 = calculateCurrentCell(cameraPositionX, cameraPositionZ)[0];
			int count4 = 0;
			
			//Calculate current global X value of boundary in a-direction
			while(cameraPositionX > cellBoundariesX[count4]){
				count4++;
			}
			
			//Try to move, if hitting a wall, move back
			cameraPositionX += .1f;
			if(cameraPositionX >= (cellBoundariesX[count4] - .1f) && testCell4.getRight() == true){
					cameraPositionX -= .1f;
			}
			
			//If cell contains finish, end game
			if(testCell4.getFinish()){
				System.out.println("You win!");
				GeneratorMain.infoBox("You win!", "Congratulations");
				System.exit(0);
			}
			
			//If cell contains powerup, walk on opposite of current surface: floor or ceiling
			if(testCell4.getPowerUp()){
				angleZ += 180f;
				testCell4.setPowerUp(false);
				lightPos[0] *= -1;
				lightPos[1] *= -1;
				lightPos[3] *= -1;
			}
			break;
		}
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, drawWireframe ? GL.GL_LINE : GL.GL_FILL);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		drawShape();
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
	}
	
	private void drawShape(){
		gl.glLoadIdentity();
		gl.glPushMatrix();
		
		//Camera rotation
		gl.glRotatef(-cameraAngleY/5, 1.0f, 0, 0);
		gl.glRotatef(-cameraAngleX/5, 0, 1.0f, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos, 0);
		gl.glTranslatef(tx, ty, -10.0f);
		gl.glScalef(scale, scale, scale);
		gl.glRotatef(angleY, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(angleX, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(angleZ, 0.0f, 0.0f, 1.0f); //If powerup is activated, walk on opposite surface
		gl.glTranslatef(-cameraPositionX, -cameraPositionY, cameraPositionZ); //player movement
		//Display Maze
				for(int i = 0; i < maze.length; i++){
					for(int j = 0; j < maze[i].length; j++){
						float cwidth = maze[i][j].getWidth();
						float cheight = maze[i][j].getHeight();
						float x = cwidth * j;
						float z = cheight * i;
						if(i-1 >= 0){
							if(!maze[i-1][j].getDown() && maze[i][j].getUp()){
								
								//Use brick texture
								textures[0].enable();
								textures[0].bind();
								gl.glBegin(GL.GL_QUADS);
								
								//Attach Textures
								gl.glTexCoord2d(0.0, 0.0);
								gl.glVertex3f(x, 0, z); //bottom left
								gl.glTexCoord2d(0.0, 1.0);
								gl.glVertex3f(x, 1, z); //top left
								gl.glTexCoord2d(1.0, 1.0);
								gl.glVertex3f(x+cwidth, 1, z); //top right
								gl.glTexCoord2d(1.0, 0.0);
								gl.glVertex3f(x+cwidth, 0, z); //bottom right
								textures[0].disable();
								gl.glEnd();
							}
						}
						else if(maze[i][j].getUp()){
							
							//Use brick texture
							textures[0].enable();
							textures[0].bind();
							gl.glBegin(GL.GL_QUADS);
							
							//Attach texture
							gl.glTexCoord2d(0.0, 0.0);
							gl.glVertex3f(x, 0, z); //bottom left
							gl.glTexCoord2d(0.0, 1.0);
							gl.glVertex3f(x, 1, z); //top left
							gl.glTexCoord2d(1.0, 1.0);
							gl.glVertex3f(x+cwidth, 1, z); //top right
							gl.glTexCoord2d(1.0, 0.0);
							gl.glVertex3f(x+cwidth, 0, z); //bottom right
							textures[0].disable();
							gl.glEnd();
						}
						if(j-1 >= 0){
							if(!maze[i][j-1].getRight() && maze[i][j].getLeft()){
								
								//Use brick texture
								textures[0].enable();
								textures[0].bind();
								gl.glBegin(GL.GL_QUADS);
								
								//Attach Texture
								gl.glTexCoord2d(0.0, 0.0);
								gl.glVertex3f(x, 0, z); //bottom left
								gl.glTexCoord2d(0.0, 1.0);
								gl.glVertex3f(x, 1, z); //top left
								gl.glTexCoord2d(1.0, 1.0);
								gl.glVertex3f(x, 1, z+cheight); //top right
								gl.glTexCoord2d(1.0, 0.0);
								gl.glVertex3f(x, 0, z+cheight); //bottom right
								textures[0].disable();
								gl.glEnd();
							}
						}
						else if(maze[i][j].getLeft()){
							
							//Use brick texture
							textures[0].enable();
							textures[0].bind();
							gl.glBegin(GL.GL_QUADS);
							
							//Attach Texture
							gl.glTexCoord2d(0.0, 0.0);
							gl.glVertex3f(x, 0, z); //bottom left
							gl.glTexCoord2d(0.0, 1.0);
							gl.glVertex3f(x, 1, z); //top left
							gl.glTexCoord2d(1.0, 1.0);
							gl.glVertex3f(x, 1, z+cheight); //top right
							gl.glTexCoord2d(1.0, 0.0);
							gl.glVertex3f(x, 0, z+cheight); //bottom right
							textures[0].disable();
							gl.glEnd();
						}
						if(maze[i][j].getDown()){
							
							//Use brick texture
							textures[0].enable();
							textures[0].bind();
							gl.glBegin(GL.GL_QUADS);
							
							//Attach texture
							gl.glTexCoord2d(0.0, 0.0);
							gl.glVertex3f(x, 0, z+cheight); //bottom left
							gl.glTexCoord2d(0.0, 1.0);
							gl.glVertex3f(x, 1, z+cheight); //top left
							gl.glTexCoord2d(1.0, 1.0);
							gl.glVertex3f(x+cwidth, 1, z+cheight); //top right
							gl.glTexCoord2d(1.0, 0.0);
							gl.glVertex3f(x+cwidth, 0, z+cheight); //bottom right
							textures[0].disable();
							gl.glEnd();
						}
						if(maze[i][j].getRight()){
							
							//Use brick texture
							textures[0].enable();
							textures[0].bind();
							gl.glBegin(GL.GL_QUADS);
							
							//Attach texture
							gl.glTexCoord2d(0.0, 0.0);
							gl.glVertex3f(x+cwidth, 0, z); //bottom left
							gl.glTexCoord2d(0.0, 1.0);
							gl.glVertex3f(x+cwidth, 1, z); //top left
							gl.glTexCoord2d(1.0, 1.0);
							gl.glVertex3f(x+cwidth, 1, z+cheight); //top right
							gl.glTexCoord2d(1.0, 0.0);
							gl.glVertex3f(x+cwidth, 0, z+cheight); //bottom right
							textures[0].disable();
							gl.glEnd();
						}
						if(maze[i][j].getPowerUp()){
							gl.glPushMatrix();
							gl.glTranslatef(x+(cwidth/2), 0.25f, z+(cheight/2));
							gl.glScalef(0.2f, 0.2f, 0.2f);
							glut.glutSolidIcosahedron();
							gl.glPopMatrix();
						}
						if(maze[i][j].getFinish()){
							gl.glPushMatrix();
							gl.glTranslatef(x+(cwidth/2), 0.25f, z+(cheight/2));
							gl.glScalef(0.25f, 0.25f, 0.25f);
							glut.glutSolidTorus(0.5f, 2.0f, 32, 32);
							gl.glPopMatrix();
						}
					}
				}
				
		//add floor, use carpet texture
		textures[1].setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		textures[1].setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
		textures[1].enable();
		textures[1].bind();
		gl.glBegin(GL.GL_QUADS);
		
		//Attach texture
		gl.glTexCoord2d(maze.length*(maze[0][0].getHeight()), 0.0);
		gl.glVertex3f(maze.length*(maze[0][0].getHeight()), 0, 0); //bottom left
		gl.glTexCoord2d(0.0, 1.0);
		gl.glVertex3f(0, 0, 0); //top left
		gl.glTexCoord2d(1.0, maze[0].length*(maze[0][0].getWidth()));
		gl.glVertex3f(0, 0, maze[0].length*(maze[0][0].getWidth())); //top right
		gl.glTexCoord2d(maze.length*maze[0][0].getHeight(), maze[0].length*(maze[0][0].getWidth()));
		gl.glVertex3f(maze.length*maze[0][0].getHeight(), 0, maze[0].length*(maze[0][0].getWidth())); //bottom right
		textures[1].disable();
		gl.glEnd();
		
		//add ceiling, use tile texture 
		textures[2].setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		textures[2].setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
		textures[2].enable();
		textures[2].bind();
		gl.glBegin(GL.GL_QUADS);
		
		//Attach texture
		gl.glTexCoord2d(maze.length*(maze[0][0].getHeight())*2.5, 0.0);
		gl.glVertex3f(maze.length*(maze[0][0].getHeight()), 1, 0); //bottom left
		gl.glTexCoord2d(0.0, 1.0);
		gl.glVertex3f(0, 1, 0); //top left
		gl.glTexCoord2d(1.0, maze[0].length*(maze[0][0].getWidth())*2.5);
		gl.glVertex3f(0, 1, maze[0].length*(maze[0][0].getWidth())); //top right
		gl.glTexCoord2d(maze.length*maze[0][0].getHeight()*2.5, maze[0].length*(maze[0][0].getWidth())*2.5);
		gl.glVertex3f(maze.length*maze[0][0].getHeight(), 1, maze[0].length*(maze[0][0].getWidth())); //bottom right
		textures[2].disable();
		gl.glEnd();
		gl.glPopMatrix();
	}
	
	private void playAudio(){
		try {
			// Open an audio input stream.           
			File soundFile = new File("background.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);              
			// Get a sound clip resource.
			Clip clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			clip.loop(clip.LOOP_CONTINUOUSLY); //endlessly loop
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		Generator generator = new Generator(8,8,mazeWidth,mazeHeight);
		maze = generator.getMaze();
		generator.generate();
		lightPos[0] = (maze.length*maze[0][0].getHeight())/2;
		lightPos[1] = 5.0f;
		lightPos[2] = (maze.length*maze[0][0].getWidth())/2;
		lightPos[3] = 0f;
		generator.maze1();
		System.out.println(generator.toString());
		gl = drawable.getGL();
		gl.setSwapInterval(1);
		gl.glColorMaterial(GL.GL_FRONT, GL.GL_DIFFUSE);
		gl.glEnable( GL.GL_COLOR_MATERIAL ) ;
		gl.glEnable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_NORMALIZE);
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LESS);
		gl.glClearColor(.3f, .3f, .3f, 1f);
		gl.glClearDepth(1.0f);
		initialized = true;
		
		//Set all of the cell boundaries of the maze
		cellBoundariesX = new int[maze.length + 1];
		cellBoundariesZ = new int[maze[0].length + 1];
		int count = 0;
		int i = 0;
		while(i < maze.length){
			if(i != 0){
				count = count + mazeWidth;
			}
			cellBoundariesX[i] = count;
			i++;
		}
		count = count + mazeWidth;
		cellBoundariesX[i] = count;
		count = 10;
		i = 0;
		while(i < maze[0].length){
			if(i != 0){
				count = count - mazeHeight;
			}
			cellBoundariesZ[i] = count;
			i++;
		}
		count = count - mazeHeight;
		cellBoundariesZ[i] = count;
		
		//Load the wall, floor, and ceiling textures
		try {
			textures[0] = loadTexture("brick.png");
			textures[1] = loadTexture("carpet.png");
			textures[2] = loadTexture("tile.png");
		} catch (GLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		playAudio(); //Play background music
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		winW = width;
		winH = height;
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(30.0f, (float) width / (float) height, 0.01f, 100.0f);
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_MODELVIEW);
	}
	
	//Load a texture from a png image
	private static Texture loadTexture(String file) throws GLException, IOException
	{
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    ImageIO.write(ImageIO.read(new File(file)), "png", os);
	    InputStream fis = new ByteArrayInputStream(os.toByteArray());
	    return TextureIO.newTexture(fis, true, TextureIO.PNG);
	}
	
	private static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

	//Unused, but required, methods
	public void keyReleased(KeyEvent arg0) {}
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {}
	public void mouseReleased(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void actionPerformed(ActionEvent arg0) {}
	public void mouseDragged(MouseEvent e) {}
}
