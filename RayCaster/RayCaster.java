import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;

public class RayCaster extends JPanel implements ActionListener {
    
    JFrame frame = new JFrame();

    Timer timer = new Timer(100, this);

    public int MCS = 64, MW = 8, MH = 10;

    public boolean Up = false, Down = false, Right = false, Left = false, shot = false;

    public static final double DEGREE = 0.0174533;
    public int[][] map = {{1,1,1,1,1,1,1,1},
                          {1,0,1,0,0,0,0,1},
                          {1,0,1,0,0,1,0,1},
                          {1,0,1,0,0,1,0,1},
                          {1,0,0,0,0,0,0,1},
                          {1,0,0,0,1,1,0,1},
                          {1,0,0,0,0,0,0,1},
                          {1,0,0,0,0,0,0,1},
                          {1,0,0,0,0,0,0,1},
                          {1,1,1,1,1,1,1,1}};

    public double pX, pY, pdX, pdY, pa;

    public RayCaster() {
        frame.setSize(1500, 520);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().add(this);
        frame.setBackground(Color.BLACK);
        pX = 300;
        pY = 300;
        pa = 0;
        pdX = Math.cos(pa);
        pdY = Math.sin(pa);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W ) {
                    Up = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
                    Down = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    Left = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    Right = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    shot = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W ) {
                    Up = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
                    Down = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    Left = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    Right = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    shot = false;
                }
            }
        });
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocus();
        timer.start();
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        drawBase(g);
        drawPlayer(g);
    }

    public void move() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(Left) {
                        pa -= 0.1;
                        if (pa < 0) pa += 2 * Math.PI;
                        pdX = Math.cos(pa) * 5;
                        pdY = Math.sin(pa) * 5;
                    }
                    if (Right) {
                        pa += 0.1;
                        if (pa > 2 * Math.PI) pa -= 2 * Math.PI;
                        pdX = Math.cos(pa) * 5;
                        pdY = Math.sin(pa) * 5;
                    }
                    if (Down) {
                        pX -= pdX;
                        pY -= pdY;
                        int xCo = (int)(pX  / 64);
                        int yCo = (int) (pY / 64);
                        if (map[yCo][xCo] == 1) {
                            pX += pdX;
                            pY += pdY;
                        }
                    }
                    if (Up) {
                        pX += pdX;
                        pY += pdY;
                        int xCo = (int)(pX  / 64);
                        int yCo = (int) (pY / 64);
                        if (map[yCo][xCo] == 1) {
                            pX -= pdX;
                            pY -= pdY;
                        }
                    }
                        Thread.sleep(30);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
            }
        }).start();
    }

    public void drawBase(Graphics g) {
        for (int y = 0; y < MH; y++) {
            for (int x = 0; x < MW; x++) {
                if (map[y][x] == 1) {
                    g.setColor(Color.WHITE);
                    g.fillRect(x * MCS, y * MCS, MCS, MCS);
                }
                if (map[y][x] == 0) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x * MCS, y * MCS, MCS, MCS);
                }
                if (map[y][x] == 2) {
                    g.setColor((Color.yellow));
                    g.fillRect(x * MCS, y * MCS, MCS, MCS);
                }
                g.setColor(Color.GRAY);
                g.drawRect(x * MCS, y * MCS, MCS, MCS);
                
            }
        }
    }

    public void drawPlayer(Graphics g) {
        g.setColor(Color.yellow);
        g.fillRect((int) pX, (int) pY, 10, 10);
        g.setColor(Color.RED);
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(3));
        g.drawLine((int) pX + 5, (int) pY + 5, (int) (pX + pdX) + 5, (int) (pY + pdY) + 5);
        g.setColor(Color.CYAN);
        g.fillRect(520, 0, 1000, 180);
        g.setColor(Color.GRAY);
        g.fillRect(520, 180, 1000, 145);
        drawRays3D(g2);
        g.setColor(Color.WHITE);
        g.drawLine(990,162, 1010, 162 );
        g.drawLine(1000, 152, 1000, 172);

    }

    public double getDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void drawRays3D(Graphics2D g) {
        int r, mX, mY, fov;
        double rX = 0, rY = 0, ra = 0, xO = 0, yO = 0;
        double disV = 0, vX = pX, vY = pY;
        double disH = 0, hX = pX, hY = pY;
        double lineO = 0;
        ra = pa - DEGREE * 30; 
        if (ra < 0) ra += 2*Math.PI;
        r = 180;
        for (int i = 0; i < r; i++) { 

            //Check Horizontal Lines
            fov = 0;
            double aTan = -1/Math.tan(ra);
            if (ra > Math.PI) { //Looking Down
                rY = (((int) pY >> 6)<<6) - 0.0001;
                rX = (pY - rY) * aTan + pX;
                yO = -64;
                xO = -yO * aTan; 
            } 
            if (ra <= Math.PI) { //Looking Up
                rY = (((int) pY >> 6)<<6) + 64;
                rX = (pY - rY) * aTan + pX;
                yO = 64;
                xO = -yO * aTan; 
            }
            
            if (ra == 0 || ra == Math.PI) {
                rX = pX;
                rY = pY;
                fov = 8; 
            }
            
            while (fov < 8) {
                mX = (int) (rX) >> 6; 
                mY = (int) (rY) >> 6;
                if (mX < MW && mX >= 0 && mY < MH && mY >= 0 && map[mY][mX] == 1) { //Hit Wall
                    fov = 8;
                } else {
                    rX += xO;
                    rY += yO;
                    fov++;
                }
            }
            hX = rX;
            hY = rY;
            disH = getDistance(pX + 5, pY +5, hX, hY);

            //Check Vertical Lines
            fov = 0;
            double nTan = -Math.tan(ra);
            if (ra > Math.PI / 2 && ra < 3 * Math.PI / 2) { //Looking Left
                rX = (((int) pX >> 6)<<6) - 0.0001;
                rY = (pX - rX) * nTan + pY;
                xO = -64;
                yO = -xO * nTan; 
            } 
            if (ra <= Math.PI / 2 || ra >= 3 * Math.PI / 2) { //Looking Right
                rX = (((int) pX >> 6)<<6) + 64;
                rY = (pX - rX) * nTan + pY;
                xO = 64;
                yO = -xO * nTan; 
            }
            
            if (ra == Math.PI / 2 || ra == 3*Math.PI/2) {
                rX = pX;
                rY = pY;
                fov = 8; 
            }
            
            while (fov < 8) {
                mX = (int) (rX) >> 6; 
                mY = (int) (rY) >> 6;
                if (mX < MW && mX >= 0 && mY < MH && mY >= 0 && map[mY][mX] == 1) { //Hit Wall
                    fov = 8;
                } else {
                    rX += xO;
                    rY += yO;
                    fov++;
                }
            }
            vX = rX;
            vY = rY;
            disV = getDistance(pX + 5, pY +5, vX, vY);
            g.setStroke(new BasicStroke(3));
            double dist;
            if (disH <= disV) {
                dist = disH;
                g.setColor(new Color(0, 100 ,0));
                g.drawLine((int) pX + 5, (int) pY + 5, (int)hX, (int)hY);
            } else {
                dist = disV;
                g.setColor(new Color(0, 150 ,0));
                g.drawLine((int) pX + 5, (int) pY + 5, (int)vX, (int)vY);
            }
            // Draw 3D
            double ca = pa-ra; if (ca < 0) ca += 2 * Math.PI; if (ca > 2 * Math.PI) ca -= 2 * Math.PI;
            dist *= Math.cos(ca);
            double lineH = (MCS * 320) / dist; if (lineH > 320) lineH = 320;
            lineO = 160 - lineH/2;
            g.setStroke(new BasicStroke(8));
            g.drawLine(i*8 + 520, (int) lineO , i*8 + 520, (int) (lineH + lineO ));
            ra+= DEGREE / 3;
            if (ra > 2 * Math.PI) ra -= 2 * Math.PI;
            //Shoot
            if (shot) {
                g.setColor(Color.RED);
                g.fillRect(990, 152, 20, 20);
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        move();
    }

    public static void main(String[] args) {
        new RayCaster();
    }
}