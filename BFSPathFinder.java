package practice;

	import javax.swing.*;
	import java.awt.*;
	import java.awt.event.*;
	import java.util.*;

	public class BFSPathFinder extends JPanel implements MouseListener, KeyListener {

	    // Grid size
	    static final int COLS = 48;
	    static final int ROWS = 20;
	    static final int CELL = 20;

	    // Window size
	    static final int WIDTH = 1000;
	    static final int HEIGHT = 700;

	    // Grid data
	    private boolean[][] hurdles = new boolean[ROWS][COLS];
	    private java.util.List<Point> path = new ArrayList<>();

	    // Start and End points
	    private Point start = new Point(0, 0);
	    private Point end = new Point(COLS - 1, ROWS - 1);

	    // Modes
	    private boolean placeStart = true;
	    private boolean placeEnd = false;
	    private boolean placeHurdle = false;

	    public BFSPathFinder() {

	        setPreferredSize(new Dimension(WIDTH, HEIGHT));
	        setBackground(Color.WHITE);

	        addMouseListener(this);
	        addKeyListener(this);
	        requestFocusInWindow();

	        setFocusable(true);
	    }

	    // BFS Algorithm
	    private void findPath() {

	        path.clear();

	        boolean[][] visited = new boolean[ROWS][COLS];
	        Point[][] parent = new Point[ROWS][COLS];

	        Queue<Point> queue = new LinkedList<>();

	        queue.add(start);
	        visited[start.y][start.x] = true;

	        int[] dx = {0, 0, 1, -1};
	        int[] dy = {1, -1, 0, 0};

	        boolean found = false;

	        while (!queue.isEmpty()) {

	            Point current = queue.poll();

	            if (current.equals(end)) {
	                found = true;
	                break;
	            }

	            for (int i = 0; i < 4; i++) {

	                int nx = current.x + dx[i];
	                int ny = current.y + dy[i];

	                if (nx >= 0 && ny >= 0 &&
	                    nx < COLS && ny < ROWS &&
	                    !visited[ny][nx] &&
	                    !hurdles[ny][nx]) {

	                    visited[ny][nx] = true;

	                    parent[ny][nx] = current;

	                    queue.add(new Point(nx, ny));
	                }
	            }
	        }

	        if (!found) {

	            JOptionPane.showMessageDialog(
	                    this,
	                    "No Path Found!"
	            );

	            repaint();
	            return;
	        }

	        Point p = end;

	        while (p != null && !p.equals(start)) {

	            path.add(p);

	            p = parent[p.y][p.x];
	        }

	        repaint();
	    }

	    // Draw everything
	    @Override
	    protected void paintComponent(Graphics g) {

	        super.paintComponent(g);

	        // Title
	        g.setFont(new Font("Arial", Font.BOLD, 22));
	        g.setColor(Color.BLACK);

	        g.drawString("GPS Navigation using BFS", 20, 30);

	        // Instructions
	        g.setFont(new Font("Arial", Font.BOLD, 14));

	        g.drawString("S -> Place Start Point", 20, 70);
	        g.drawString("E -> Place End Point", 20, 95);
	        g.drawString("H -> Draw Hurdles", 20, 120);
	        g.drawString("R -> Random Hurdles", 20, 145);
	        g.drawString("SPACE -> Find Path", 20, 170);
	        g.drawString("O -> Clear Grid", 20, 195);

	        int offsetX = 20;
	        int offsetY = 220;

	        // Draw grid
	        for (int row = 0; row < ROWS; row++) {

	            for (int col = 0; col < COLS; col++) {

	                int x = offsetX + col * CELL;
	                int y = offsetY + row * CELL;

	                // Hurdles
	                if (hurdles[row][col]) {

	                    g.setColor(Color.RED);
	                    g.fillRect(x, y, CELL, CELL);
	                }

	                // Path
	                for (Point p : path) {

	                    if (p.x == col && p.y == row) {

	                        g.setColor(Color.BLACK);
	                        g.fillRect(x, y, CELL, CELL);
	                    }
	                }

	                // Start point
	                if (start.x == col && start.y == row) {

	                    g.setColor(Color.GREEN);
	                    g.fillRect(x, y, CELL, CELL);
	                }

	                // End point
	                if (end.x == col && end.y == row) {

	                    g.setColor(Color.BLUE);
	                    g.fillRect(x, y, CELL, CELL);
	                }

	                // Grid border
	                g.setColor(Color.GRAY);
	                g.drawRect(x, y, CELL, CELL);
	            }
	        }
	    }

	    // Mouse click
	    @Override
	    public void mouseClicked(MouseEvent e) {

	        int offsetX = 20;
	        int offsetY = 220;

	        int x = (e.getX() - offsetX) / CELL;
	        int y = (e.getY() - offsetY) / CELL;

	        if (x < 0 || y < 0 || x >= COLS || y >= ROWS)
	            return;

	        if (placeHurdle) {

	            if (!(x == start.x && y == start.y) &&
	                !(x == end.x && y == end.y)) {

	                hurdles[y][x] = true;
	            }
	        }

	        else if (placeStart) {

	            if (!hurdles[y][x]) {

	                start = new Point(x, y);
	            }
	        }

	        else if (placeEnd) {

	            if (!hurdles[y][x]) {

	                end = new Point(x, y);
	            }
	        }

	        repaint();
	        requestFocusInWindow();
	    }

	    // Key controls
	    @Override
	    public void keyPressed(KeyEvent e) {

	        int key = e.getKeyCode();

	        if (key == KeyEvent.VK_S) {

	            placeStart = true;
	            placeEnd = false;
	            placeHurdle = false;
	        }

	        else if (key == KeyEvent.VK_E) {

	            placeStart = false;
	            placeEnd = true;
	            placeHurdle = false;
	        }

	        else if (key == KeyEvent.VK_H) {

	            placeStart = false;
	            placeEnd = false;
	            placeHurdle = true;
	        }

	        else if (key == KeyEvent.VK_SPACE) {

	            findPath();
	        }

	        else if (key == KeyEvent.VK_O) {

	            hurdles = new boolean[ROWS][COLS];
	            path.clear();

	            repaint();
	        }

	        else if (key == KeyEvent.VK_R) {

	            Random rand = new Random();

	            for (int i = 0; i < 100; i++) {

	                int x = rand.nextInt(COLS);
	                int y = rand.nextInt(ROWS);

	                if (!(x == start.x && y == start.y) &&
	                    !(x == end.x && y == end.y)) {

	                    hurdles[y][x] = true;
	                }
	            }

	            repaint();
	        }
	    }

	    // Unused methods
	    @Override
	    public void mousePressed(MouseEvent e) {}

	    @Override
	    public void mouseReleased(MouseEvent e) {}

	    @Override
	    public void mouseEntered(MouseEvent e) {}

	    @Override
	    public void mouseExited(MouseEvent e) {}

	    @Override
	    public void keyTyped(KeyEvent e) {}

	    @Override
	    public void keyReleased(KeyEvent e) {}

	    // Main Method
	    public static void main(String[] args) {

	        JFrame frame = new JFrame("GPS Navigation using BFS");

	        BFSPathFinder panel = new BFSPathFinder();

	        frame.add(panel);

	        frame.pack();

	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        frame.setLocationRelativeTo(null);

	        frame.setVisible(true);
	    }
	}


