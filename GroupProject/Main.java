package GroupProject;

import java.awt.*;
import javax.swing.JFrame;


public class Main{
    public static class Point{
        int row;
        int col;

        public Point(int row, int col){
            this.row = row;
            this.col = col;
        }

    }

    public static class Node{
        Point point;
        Node next;

        public Node(Point point){
            this.point = point;
            this.next = null;
        }
    }

    public static class MyStack{
        private int size;
        private Node head;

        public MyStack(){
            size = 0;
            head = null;
        }

        public int size(){
            return this.size;
        }

        public boolean isEmpty(){
            return size == 0;
        }

        public boolean push(Point point){
            Node node = new Node(point);
            if(!isEmpty()){
                node.next = head;
                
            }
            head = node;
            size++;
            return true;
        }

        public boolean pop(){
            if(isEmpty()){
                return false;
            }
            head = head.next;
            size--;
            return true;
        }

        public Point peek(){
            if(isEmpty()){
                return null;
            }
            return head.point;
        }
    }

    public static class Maze{
        int rows;
        int cols;
        String[] map;
        int robotRow;
        int robotCol;
        int steps;

        public Maze() {
            rows = 30;
            cols = 100;
            map = new String[rows];
            map[0] = "....................................................................................................";
            map[1] = ".                                              ..                                                  .";
            map[2] = ".                                              ..                          ..                      .";
            map[3] = ".                                              ..                          ..                      .";
            map[4] = ".      ..............                          ..                  ...     ..                      .";
            map[5] = ".            .............                     ..                  ...     ..   ....................";
            map[6] = ".                                              ..                  ...     ..   ....................";
            map[7] = ".       ....  .   .  .  .....                                                   ...     ..         .";
            map[8] = ".       .  .  .. ..  .    .            ........                     .........   ...     ..         .";
            map[9] = ".       ....  . . .  .    .            ........                    .........    ...     ..         .";
            map[10] = ".       ..    .   .  .    .            ........                                 ...     ..         .";
            map[11] = ".       . .   .   .  .    .        .   ........                        ..                       ....";
            map[12] = ".       .  .  .   .  .    .        .   ........                        ..                       ....";
            map[13] = ".                                  .                                   ..  ..           .......    .";
            map[14] = ".    ..       ..    ..       ..    .        ..      ..    ...          ..  ..           .......    .";
            map[15] = ".    ....     ..    ....     ..    .        ..      ..    .....        ..  ..                      .";
            map[16] = ".    .. ..    ..    .. ..    ..        .    ..      ..    ..  ...  ..      ..                      .";
            map[17] = ".    ..  ..   ..    ..  ..   ..   .......   ..........    ..   ..  ..      ..                      .";
            map[18] = ".    ..   ..  ..    ..   ..  ..   .......   ..........    ..   ..  ..      ..                      .";
            map[19] = ".    ..    .. ..    ..    .. ..        .    ..      ..    ..  ...          ..                      .";
            map[20] = ".    ..     ....    ..     ....             ..      ..    .....            ..    ..........        .";
            map[21] = ".    ..       ..    ..       ..             ..      ..    ...              ..    ..........        .";
            map[22] = ".                                                                          ..                      .";
            map[23] = ".      .            .           .            .                             ..          X           .";
            map[24] = ".      .     .      .           .            .                             ..                      .";
            map[25] = ".      .     .      .                        .        .                    ..                      .";
            map[26] = ".      .     .      .                        .        .                    ..                      .";
            map[27] = ".      .     .      .           .            .        .                    ..                      .";
            map[28] = ".            .                  .                     .                    ..                      .";
            map[29] = "....................................................................................................";
            robotRow = 1;
            robotCol = 1;
            steps = 0;
        }

        public String go(String direction) {
            if (!direction.equals("UP") &&
                !direction.equals("DOWN") &&
                !direction.equals("LEFT") &&
                !direction.equals("RIGHT")) {
                // invalid direction
                steps++;
                return "false";
            }
            int currentRow = robotRow;
            int currentCol = robotCol;
            if (direction.equals("UP")) {
                currentRow--;
            } else if (direction.equals("DOWN")) {
                currentRow++;
            } else if (direction.equals("LEFT")) {
                currentCol--;
            } else {
                currentCol++;
            }

            // check the next position
            if (map[currentRow].charAt(currentCol) == 'X') {
                // Exit gate
                steps++;
                System.out.println("Steps to reach the Exit gate " + steps);
                return "win";
            } else if (map[currentRow].charAt(currentCol) == '.') {
                // Wall
                steps++;
                return "false";
            } else {
                // Space => update robot location
                steps++;
                robotRow = currentRow;
                robotCol = currentCol;
                return "true";
            }       
        }
    }

    public static class Robot extends Canvas{
        MyStack path; 
        MyStack pathDraw;
        MyStack pathReverse;
        boolean deathZone[][]; //combined of visited points and deathzone
        int steps;
        
        public Robot(){
            path = new MyStack();
            pathDraw = new MyStack();
            pathReverse = new MyStack();
            deathZone = new boolean[1000][1000];
            steps = 0;
        }

        public String goBack(Point point){
            if(point.row - path.peek().row == 1){
                return "UP";
            } else if(point.row - path.peek().row == -1){
                return "DOWN";
            } else if(point.col - path.peek().col == 1){
                return "LEFT";
            } else {
                return "RIGHT";
            }
        }

        public void navigate(){
            Maze maze = new Maze();

            //add the Robot first position to the path stack
            path.push(new Point(maze.robotRow, maze.robotCol));
            deathZone[maze.robotRow][maze.robotCol] = true;
            pathDraw.push(new Point(maze.robotRow, maze.robotCol));

            while(true){
                Point left = new Point(maze.robotRow, maze.robotCol -1);
                Point right = new Point(maze.robotRow, maze.robotCol +1);
                Point up = new Point(maze.robotRow -1, maze.robotCol);
                Point down = new Point(maze.robotRow + 1, maze.robotCol);
                Point points[] = new Point[] {left, right, up, down};
                String directions[] =  new String[] {"LEFT","RIGHT","UP","DOWN"};

                for(int i = 0; i < points.length; i++){
                    //if the point is in the deathZone stack
                    //set direction to false;
                    if(deathZone[points[i].row][points[i].col] == true){
                        directions[i] = null;
                    }
                }

                for(int i = 0; i< directions.length; i++){
                    if(directions[i] != null){
                        String result = maze.go(directions[i]);
                        if(result == "win"){
                            path.push(points[i]);
                            steps = maze.steps;
                            return;
                        } else if(result == "false"){
                            //System.out.println("Face wall(not move): " + directions[i]);
                            directions[i] = null;
                            deathZone[points[i].row][points[i].col] = true;
                            break;
                        } else {
                            //System.out.println("Go: " + directions[i]);
                            path.push(points[i]);
                            deathZone[points[i].row][points[i].col] = true;
                            pathDraw.push(points[i]);
                            break;
                        }
                    }
                } 
                
                if(directions[0] == null && directions[1] == null && directions[2] == null && directions[3] == null){
                    deathZone[maze.robotRow][maze.robotCol] = true;
                    Point removedPoint = path.peek();
                    path.pop();
                    pathDraw.pop();
                    String direction = goBack(removedPoint);
                    maze.go(direction);
                    //System.out.println("Go back: " + direction);
                }
            }
        }

        public void paint(Graphics gd) {
        	Maze mazeDraw = new Maze();
        	Graphics2D g = (Graphics2D) gd;
    		
        	g.setColor(Color.BLACK);
            g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            g.drawString("Step Count: ", 20, 40);
            setBackground(Color.WHITE);
            g.drawString(String.valueOf(steps), 140, 40);
    		
    		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    		g.setRenderingHints(rh);
        	
    		for (int row = 0; row < mazeDraw.rows; row++) {
    			for (int col = 0; col < mazeDraw.cols; col++) {
    				
    				Color color;
    				switch (mazeDraw.map[row].charAt(col)) {
    					case '.' : color = Color.GRAY; break;
    					case ' ' : color = Color.WHITE; break;
    					case 'X' : color = Color.GREEN; break;
    					default : color = Color.WHITE;
    				}
    				
    				g.setColor(color);
    				g.fillRect(15*col + 20, 15*row + 90, 15, 15);
    				g.setColor(Color.BLACK);
    				g.drawRect(15*col + 20, 15*row + 90, 15, 15);
    			}
    		}
    		
    		while (pathDraw.peek() != null) {
    			pathReverse.push(pathDraw.peek());
    			pathDraw.pop();
    		}
    		
    		while (pathReverse.peek() != null) {
    			int pathX = pathReverse.peek().col;
    			int pathY = pathReverse.peek().row;
    			
    			pathReverse.pop();
    			g.setColor(Color.YELLOW);

    			g.fillRect(pathX*15 + 20, pathY*15 + 90, 15, 15);
    			g.drawRect(pathX*15 + 20, pathY*15 + 90, 15, 15);
    			try
    		      {
    		        // Delay for x second
    		        Thread.sleep(30);   
    		      }
    		      catch(InterruptedException ex)
    		      {
    		          ex.printStackTrace();
    		      }
    		}
        }
    }

    public static void main(String[] args){
        Robot rob1 = new Robot();
    	rob1.navigate();
    	JFrame f = new JFrame();
    	f.add(rob1);
        f.setSize(1600, 600);
        f.setTitle("Maze Solving Robot - Team 04");
        f.setVisible(true);
    }
}

