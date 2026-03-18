package BioReactorSim;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ReactorAnimationPanel extends JPanel {

    private double impellerAngle = 0;
    private double biomass = 0;

    private ArrayList<Bubble> bubbles = new ArrayList<>();
    private ArrayList<Cell> cells = new ArrayList<>();

    private Random rand = new Random();

    public ReactorAnimationPanel() {

        for(int i = 0; i < 40; i++){
            cells.add(new Cell());
        }
    }

    public void updateBiomass(double b){

        biomass = b;

        int targetCells = (int)(biomass * 80);

        while(cells.size() < targetCells){
            cells.add(new Cell());
        }

        if(rand.nextDouble() < 0.25){
            bubbles.add(new Bubble());
        }

        for(Bubble bubble : bubbles){
            bubble.update();
        }

        for(Cell cell : cells){
            cell.update();
        }

        bubbles.removeIf(bubble -> bubble.y < 0);

        impellerAngle += 0.25;

        repaint();
    }
    public void resetAnimation(){

        bubbles.clear();
        cells.clear();

        // reset microbes
        for(int i = 0; i < 40; i++){
            cells.add(new Cell());
        }

        impellerAngle = 0;
        biomass = 0;

        repaint();
    }
    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();
        int cx = w/2;

        int tankTop = 60;
        int tankHeight = 260;
        int tankWidth = 140;
        int tankX = cx - tankWidth/2;

        // ===== GLASS REACTOR =====

        g2.setColor(new Color(230,240,255));
        g2.fillRoundRect(tankX, tankTop, tankWidth, tankHeight, 40, 40);

        g2.setColor(Color.BLACK);
        g2.drawRoundRect(tankX, tankTop, tankWidth, tankHeight, 40, 40);

        g2.setColor(new Color(255,255,255,120));
        g2.fillRect(tankX + 8, tankTop + 10, 10, tankHeight - 20);

        // ===== BROTH =====

        float progress = (float)Math.min(biomass / 10.0, 1);

        Color broth = new Color(
                60 + (int)(progress * 80),
                120,
                200 - (int)(progress * 120)
        );

        int liquidHeight = tankHeight - 20;

        g2.setColor(broth);
        g2.fillRect(
                tankX + 1,
                tankTop + tankHeight - liquidHeight,
                tankWidth - 2,
                liquidHeight
        );

        // surface highlight
        g2.setColor(new Color(255,255,255,150));
        g2.drawLine(
                tankX + 5,
                tankTop + tankHeight - liquidHeight,
                tankX + tankWidth - 5,
                tankTop + tankHeight - liquidHeight
        );

        // ===== FOAM =====

        if(bubbles.size() > 25){

            g2.setColor(new Color(255,255,255,180));

            for(int i=0;i<25;i++){
                int fx = tankX + rand.nextInt(tankWidth);
                int fy = tankTop + 5 + rand.nextInt(12);

                g2.fillOval(fx, fy, 6, 6);
            }
        }

        // ===== CLIP TO TANK (IMPORTANT FIX) =====

        Shape oldClip = g2.getClip();
        g2.setClip(tankX + 1, tankTop + 1, tankWidth - 2, tankHeight - 2);

        // ===== MICROBES =====

        g2.setColor(Color.WHITE);

        for(Cell cell : cells){

            int x = tankX + 5 + (int)cell.x;
            int y = tankTop + 5 + (int)cell.y;

            g2.fillOval(x, y, 3, 3);
        }

        // ===== BUBBLES =====

        g2.setColor(Color.CYAN);

        for(Bubble bubble : bubbles){

            int x = tankX + 20 + (int)bubble.x;
            int y = tankTop + (int)bubble.y;

            g2.drawOval(x, y, 8, 8);
        }

        // ===== RESTORE CLIP =====

        g2.setClip(oldClip);

        // ===== SHAFT =====

        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(cx, tankTop, cx, tankTop + tankHeight);

        // ===== IMPELLER =====

        g2.translate(cx, tankTop + tankHeight/2);
        g2.rotate(impellerAngle);

        g2.setColor(new Color(120,120,120));

        g2.fillRect(-40, -4, 80, 8);
        g2.fillRect(-4, -40, 8, 80);

        g2.rotate(-impellerAngle);
        g2.translate(-cx, -(tankTop + tankHeight/2));

        // ===== FLOW VISUAL =====

        g2.setColor(Color.BLACK);

        g2.drawLine(cx, tankTop + tankHeight - 30, cx, tankTop + 40);
        g2.drawString("↑ Flow", cx + 5, tankTop + 80);

        g2.drawArc(cx - 30, tankTop + tankHeight/2 - 30, 60, 60, 0, 270);

        // ===== LABELS =====

        g2.setColor(Color.BLUE);
        g2.drawString("O₂ In", tankX - 45, tankTop + tankHeight);

        g2.setColor(Color.RED);
        g2.drawString("CO₂ Out", tankX + tankWidth + 5, tankTop + 20);

        g2.setColor(Color.WHITE);
        g2.drawString("Cells", tankX + 20, tankTop + 30);

        g2.setColor(Color.GRAY);
        g2.drawString("Mixing", tankX + 35, tankTop + tankHeight/2);

        g2.setColor(Color.BLACK);
        g2.drawString("Fermentation Reactor", cx - 60, 30);
    }

    // ===== BUBBLE CLASS =====

    class Bubble {

        double x;
        double y;
        double vy;

        Bubble(){

            // spawn from bottom center (sparger)
            x = 40 + rand.nextInt(20);
            y = 230;

            vy = 1 + rand.nextDouble()*1.5;
        }

        void update(){

            // rise upward
            y -= vy;

            // slight sideways drift
            x += rand.nextDouble()*0.6 - 0.3;
        }
    }

    // ===== CELL CLASS =====

    class Cell {

        double x, y;
        double vx, vy;

        Cell(){
            x = rand.nextInt(100);
            y = rand.nextInt(200);

            vx = rand.nextDouble()*0.5 - 0.25;
            vy = rand.nextDouble()*0.5 - 0.25;
        }

        void update(){

            double centerX = 50;
            double centerY = 100;

            double dx = x - centerX;
            double dy = y - centerY;

            double swirl = 0.002; // smooth vortex

            // vortex force
            vx += -dy * swirl;
            vy += dx * swirl;

            // upward flow (aeration)
            vy -= 0.01;

            // damping (prevents crazy speeds)
            vx *= 0.98;
            vy *= 0.98;

            x += vx;
            y += vy;

            // 🧱 BOUNDARY COLLISION (bounce)

            if(x < 0){
                x = 0;
                vx *= -0.6;
            }
            if(x > 100){
                x = 100;
                vx *= -0.6;
            }

            if(y < 0){
                y = 0;
                vy *= -0.3;
            }
            if(y > 200){
                y = 200;
                vy *= -0.6;
            }
        }
    }
    
}
