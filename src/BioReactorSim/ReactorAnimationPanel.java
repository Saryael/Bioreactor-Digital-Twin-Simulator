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

        // initial microbes
        for(int i = 0; i < 40; i++){
            cells.add(new Cell());
        }
    }

    public void updateBiomass(double b){

        biomass = b;

        // scale microbial population with biomass
        int targetCells = (int)(biomass * 80);

        while(cells.size() < targetCells){
            cells.add(new Cell());
        }

        // spawn oxygen bubbles
        if(rand.nextDouble() < 0.2){
            bubbles.add(new Bubble());
        }

        // update bubbles
        for(Bubble bubble : bubbles){
            bubble.update();
        }

        // update microbes
        for(Cell cell : cells){
            cell.update();
        }

        // remove bubbles reaching top
        bubbles.removeIf(bubble -> bubble.y < 0);

        // rotate impeller
        impellerAngle += 0.2;

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

        // -------- GLASS REACTOR --------

        g2.setColor(new Color(230,240,255));
        g2.fillRoundRect(tankX, tankTop, tankWidth, tankHeight, 40, 40);

        g2.setColor(Color.BLACK);
        g2.drawRoundRect(tankX, tankTop, tankWidth, tankHeight, 40, 40);

        // glass reflection
        g2.setColor(new Color(255,255,255,120));
        g2.fillRect(tankX + 8, tankTop + 10, 10, tankHeight - 20);

        // -------- BROTH COLOR --------

        float progress = (float)Math.min(biomass / 10.0, 1);

        Color broth = new Color(
                50,
                (int)(150 * progress),
                200 - (int)(120 * progress)
        );

        int liquidHeight = tankHeight - 20;

        g2.setColor(broth);

        g2.fillRect(
                tankX + 1,
                tankTop + tankHeight - liquidHeight,
                tankWidth - 2,
                liquidHeight
        );

        // liquid surface highlight
        g2.setColor(new Color(255,255,255,150));

        g2.drawLine(
                tankX + 5,
                tankTop + tankHeight - liquidHeight,
                tankX + tankWidth - 5,
                tankTop + tankHeight - liquidHeight
        );

        // -------- MICROBES --------

        g2.setColor(Color.WHITE);

        for(Cell cell : cells){

            int x = tankX + 10 + (int)cell.x;
            int y = tankTop + 10 + (int)cell.y;

            g2.fillOval(x, y, 3, 3);
        }

        // -------- BUBBLES --------

        g2.setColor(Color.CYAN);

        for(Bubble bubble : bubbles){

            int x = tankX + 20 + (int)bubble.x;
            int y = tankTop + (int)bubble.y;

            g2.drawOval(x, y, 8, 8);
        }

        // -------- IMPELLER SHAFT --------

        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(cx, tankTop, cx, tankTop + tankHeight);

        // -------- ROTATING IMPELLER --------

        g2.translate(cx, tankTop + tankHeight/2);
        g2.rotate(impellerAngle);

        g2.setColor(new Color(120,120,120));

        g2.fillRect(-40, -4, 80, 8);
        g2.fillRect(-4, -40, 8, 80);

        g2.rotate(-impellerAngle);
        g2.translate(-cx, -(tankTop + tankHeight/2));

        // -------- TITLE --------

        g2.setColor(Color.BLACK);
        g2.drawString("Fermentation Reactor", cx - 60, 30);
    }

    // ---------------- BUBBLE CLASS ----------------

    class Bubble{

        double x;
        double y;
        double speed;

        Bubble(){

            x = rand.nextInt(80);
            y = 220 + rand.nextInt(20);
            speed = 1 + rand.nextDouble()*2;
        }

        void update(){
            y -= speed;
        }
    }

    // ---------------- CELL CLASS ----------------

    class Cell{

        double x;
        double y;

        Cell(){

            x = rand.nextInt(100);
            y = rand.nextInt(200);
        }

        void update(){

            double centerX = 50;
            double centerY = 100;

            double dx = x - centerX;
            double dy = y - centerY;

            double swirl = 0.02;

            x += -dy * swirl;
            y += dx * swirl;

            x += rand.nextDouble()*0.5 - 0.25;
            y += rand.nextDouble()*0.5 - 0.25;
        }
    }
}