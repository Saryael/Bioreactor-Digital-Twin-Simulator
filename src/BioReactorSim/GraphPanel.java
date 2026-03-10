package BioReactorSim;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphPanel extends JPanel {

    private ArrayList<Double> data;

    public GraphPanel() {
        data = new ArrayList<>();
    }

    public void addPoint(double value) {

        data.add(value);
        repaint();
    }

    public void clear() {

        data.clear();
        repaint();
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        g.drawLine(40,height-40,width-20,height-40);
        g.drawLine(40,20,40,height-40);

        if(data.size()<2) return;

        double max = data.stream()
                .max(Double::compare)
                .orElse(1.0);

        int prevX = 40;
        int prevY = height-40 -
                (int)((data.get(0)/max)*(height-80));

        for(int i=1;i<data.size();i++) {

            int x = 40 +
                    i*(width-60)/data.size();

            int y = height-40 -
                    (int)((data.get(i)/max)*(height-80));

            g.drawLine(prevX,prevY,x,y);

            prevX = x;
            prevY = y;
        }
    }
}
