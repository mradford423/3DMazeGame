package generator;

import java.awt.*;
import java.awt.event.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import javax.swing.JFrame;

import com.sun.opengl.util.*;

import java.nio.ByteBuffer;

import javax.media.opengl.GLEventListener;
import javax.swing.JFrame;

public class GeneratorMain extends JFrame implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, ActionListener{
	
	// mouse control variables
		private boolean initialized = false;
		private boolean bounds = false;
		private final GLCanvas canvas;
		private int winW = 750, winH = 750;
		private int mouseX, mouseY;
		private int mouseButton;
		private boolean mouseClick = false;
		private boolean clickedOnShape = false;

		// gl shading/transformation variables
		private float tx = 0.0f, ty = 0.0f;
		private float scale = 1.0f;
		private float angleY = 0.0f;
		private float angleX = 0.0f;
		private float cameraAngleY = 0.0f;
		private float cameraAngleX = 900f;
		private float cameraPositionX = 1.0f;
		private float cameraPositionY = 0.5f;
		private float cameraPositionZ = 8f;
		private boolean drawWireframe = false;
		private float lightPos[] = { -5.0f, 10.0f, 5.0f, 1.0f };
		private GL gl;
		private final GLU glu = new GLU();
		private final GLUT glut = new GLUT();
		private Cell[][] maze;
		private int mazeWidth = 2;
		private int mazeHeight = 2;
		int cellBoundariesX[];
		int cellBoundariesZ[];

	public static void main(String[] args) {
		new GeneratorMain();
		
		
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
			int oldX;
			int oldY;
			if(bounds == false){
				oldX = winW/2;
				oldY = winH/2;
			}
			else{
				oldX = mouseX;
				oldY = mouseY;
			}
			int newX = e.getX();
			//System.out.println(oldX);
			int newY = e.getY();
			float screensize = (float)winW/360;
			//System.out.println(screensize);
			//System.out.println(newX - oldX);
			cameraAngleY += 1 * (newY - winH/2);
			//System.out.println(cameraAngleX);
			//cameraAngleX = 0;
			cameraAngleX += 1 * (newX - winW/2);
			//System.out.println(cameraAngleX);
			//System.out.println(newY - winH/2);
			//System.out.println(newY);
			//System.out.println(winH/2);
			if(cameraAngleY > 90){
				cameraAngleY = 90;
			}
			else if(cameraAngleY < -90){
				cameraAngleY = -90;
			}
			if(cameraAngleX > (1500)){
				cameraAngleX = (-300);
			}
			else if(cameraAngleX < (-1500)){
				cameraAngleX = 300;
			}
			//System.out.println(cameraAngleY);
			//glu.gluLookAt(10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 0, 1.0, 0);
			/*if(newY > 90){
				newY = 90;
			}
			else if(newY < -90){
				newY = -90;
			}
			if(newX > 360){
				newX = newX - 360;
			}
			else if(newX < 0){
				newX = newX + 360;
			}*/
			mouseX = newX;
			mouseY = newY;
			bounds = true;
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
	
	private Cell calculateCurrentCell(float x, float y){
		int heightNum = (int) (maze.length/y);
		int heightCount = 0;
		int widthCount = 0;
		float yCopy = 10;
		float xCopy = 0;
		while(y <= yCopy){
			yCopy -= mazeHeight;
			heightCount++;
		}
		heightCount--;
		while(x >= xCopy){
			xCopy += mazeWidth;
			widthCount++;
		}
		widthCount--;
		//System.out.println(widthCount);
		return maze[heightCount][widthCount];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		/*case KeyEvent.VK_W:
				cameraPositionZ += .5f;
				break;*/
		case KeyEvent.VK_SPACE:
			cameraPositionY += .5f;
			break;
		case KeyEvent.VK_B:
			cameraPositionY -= .5f;
			break;
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
			Cell testCell = calculateCurrentCell(cameraPositionX, cameraPositionZ);
			int count = 0;
			while(cameraPositionZ < cellBoundariesZ[count]){
				count++;
			}
			cameraPositionZ -= .1f;
			if(cameraPositionZ <= (cellBoundariesZ[count] + .1f) && testCell.getDown() == true){
					cameraPositionZ += .1f;
			}
			break;
		case 's':
			Cell testCell2 = calculateCurrentCell(cameraPositionX, cameraPositionZ);
			int count2 = 0;
			while(cameraPositionZ > cellBoundariesZ[count2]){
				count2++;
			}
			cameraPositionZ += .1f;
			if(cameraPositionZ >= (cellBoundariesZ[count2] - .1f) && testCell2.getUp() == true){
					cameraPositionZ -= .1f;
			}
			break;
		case 'd':
			Cell testCell3 = calculateCurrentCell(cameraPositionX, cameraPositionZ);
			int count3 = 0;
			while(cameraPositionX < cellBoundariesX[count3]){
				count3++;
			}
			cameraPositionX -= .1f;
			if(cameraPositionX <= (cellBoundariesX[count3] + .1f) && testCell3.getLeft() == true){
					cameraPositionX += .1f;
			}
			break;
		case 'a':
			Cell testCell4 = calculateCurrentCell(cameraPositionX, cameraPositionZ);
			int count4 = 0;
			while(cameraPositionX > cellBoundariesX[count4]){
				count4++;
			}
			cameraPositionX += .1f;
			if(cameraPositionX >= (cellBoundariesX[count4] - .1f) && testCell4.getRight() == true){
					cameraPositionX -= .1f;
			}
			break;
		}
		//System.out.println(cameraPositionX);
		calculateCurrentCell(cameraPositionX, cameraPositionZ);
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, drawWireframe ? GL.GL_LINE : GL.GL_FILL);
		gl.glColor3f(1.0f, 0.3f, 0.1f);
		drawShape();
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
	}
	
	public void drawShape(){
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glRotatef(-cameraAngleY/5, 1.0f, 0, 0);
		//System.out.println(-cameraAngleX/3);
		gl.glRotatef(-cameraAngleX/5, 0, 1.0f, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos, 0);
		gl.glTranslatef(tx, ty, -10.0f);
		gl.glScalef(scale, scale, scale);
		gl.glRotatef(angleY, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(angleX, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(-cameraPositionX, -cameraPositionY, cameraPositionZ);
		//Display Maze
				for(int i = 0; i < maze.length; i++){
					for(int j = 0; j < maze[i].length; j++){
						float cwidth = maze[i][j].getWidth();
						float cheight = maze[i][j].getHeight();
						float x = cwidth * j;
						float z = cheight * i;
						if(i-1 >= 0){
							if(!maze[i-1][j].getDown() && maze[i][j].getUp()){
								gl.glBegin(GL.GL_QUADS);
								gl.glVertex3f(x, 0, z); //bottom left
								gl.glVertex3f(x, 1, z); //top left
								gl.glVertex3f(x+cwidth, 1, z); //top right
								gl.glVertex3f(x+cwidth, 0, z); //bottom right
								gl.glEnd();
							}
						}
						else if(maze[i][j].getUp()){
							gl.glBegin(GL.GL_QUADS);
							gl.glVertex3f(x, 0, z); //bottom left
							gl.glVertex3f(x, 1, z); //top left
							gl.glVertex3f(x+cwidth, 1, z); //top right
							gl.glVertex3f(x+cwidth, 0, z); //bottom right
							gl.glEnd();
						}
						if(j-1 >= 0){
							if(!maze[i][j-1].getRight() && maze[i][j].getLeft()){
								gl.glBegin(GL.GL_QUADS);
								gl.glVertex3f(x, 0, z); //bottom left
								gl.glVertex3f(x, 1, z); //top left
								gl.glVertex3f(x, 1, z+cheight); //top right
								gl.glVertex3f(x, 0, z+cheight); //bottom right
								gl.glEnd();
							}
						}
						else if(maze[i][j].getLeft()){
							gl.glBegin(GL.GL_QUADS);
							gl.glVertex3f(x, 0, z); //bottom left
							gl.glVertex3f(x, 1, z); //top left
							gl.glVertex3f(x, 1, z+cheight); //top right
							gl.glVertex3f(x, 0, z+cheight); //bottom right
							gl.glEnd();
						}
						if(maze[i][j].getDown()){
							gl.glBegin(GL.GL_QUADS);
							gl.glVertex3f(x, 0, z+cheight); //bottom left
							gl.glVertex3f(x, 1, z+cheight); //top left
							gl.glVertex3f(x+cwidth, 1, z+cheight); //top right
							gl.glVertex3f(x+cwidth, 0, z+cheight); //bottom right
							gl.glEnd();
						}
						if(maze[i][j].getRight()){
							gl.glBegin(GL.GL_QUADS);
							gl.glVertex3f(x+cwidth, 0, z); //bottom left
							gl.glVertex3f(x+cwidth, 1, z); //top left
							gl.glVertex3f(x+cwidth, 1, z+cheight); //top right
							gl.glVertex3f(x+cwidth, 0, z+cheight); //bottom right
							gl.glEnd();
						}
					}
				}
		//add floor
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex3f(maze.length*(maze[0][0].getHeight()), 0, 0); //bottom left
		gl.glVertex3f(0, 0, 0); //top left
		gl.glVertex3f(0, 0, maze[0].length*(maze[0][0].getWidth())); //top right
		gl.glVertex3f(maze[0][0].getHeight(), 0, maze[0].length*(maze[0][0].getWidth())); //bottom right
		gl.glEnd();
		//add ceiling 
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex3f(maze.length*(maze[0][0].getHeight()), 1, 0); //bottom left
		gl.glVertex3f(0, 1, 0); //top left
		gl.glVertex3f(0, 1, maze[0].length*(maze[0][0].getWidth())); //top right
		gl.glVertex3f(maze[0][0].getHeight(), 1, maze[0].length*(maze[0][0].getWidth())); //bottom right
		gl.glEnd();
		gl.glPopMatrix();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		Generator generator = new Generator(8,8,mazeWidth,mazeHeight);
		maze = generator.getMaze();
		generator.generate();
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
		//gl.glCullFace(GL.GL_BACK);
		//gl.glEnable(GL.GL_CULL_FACE);
		// set clear color: this determines the background color (which is dark gray)
		gl.glClearColor(.3f, .3f, .3f, 1f);
		gl.glClearDepth(1.0f);
		initialized = true;
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
