package BioReactorSim;

import javax.swing.*;
import java.awt.*;

/*public class BioreactorGUI extends JFrame {

    private JLabel biomassLabel;
    private JLabel substrateLabel;
    private JLabel productLabel;
    private JLabel timeLabel;
    private JTextField biomassInput;
    private JTextField substrateInput;

    private SimulationEngine engine;
    private Timer timer;

    public BioreactorGUI() {

        BioreactorModel model = new BioreactorModel(0.1, 20.0);
        engine = new SimulationEngine(model, 0.1);

        setTitle("Bioreactor Digital Twin Simulator");
        setSize(400,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6,1));

        timeLabel = new JLabel();
        biomassLabel = new JLabel();
        substrateLabel = new JLabel();
        productLabel = new JLabel();

        add(timeLabel);
        add(biomassLabel);
        add(substrateLabel);
        add(productLabel);
        
        JPanel inputPanel = new JPanel();

        biomassInput = new JTextField("0.1", 5);
        substrateInput = new JTextField("20", 5);

        inputPanel.add(new JLabel("Initial Biomass:"));
        inputPanel.add(biomassInput);

        inputPanel.add(new JLabel("Initial Substrate:"));
        inputPanel.add(substrateInput);

        add(inputPanel);
        
        JPanel buttonPanel = new JPanel();

        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton resetButton = new JButton("Reset");

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resetButton);

        add(buttonPanel);

        timer = new Timer(500, e -> updateSimulation());

        startButton.addActionListener(e -> timer.start());
        stopButton.addActionListener(e -> timer.stop());

        resetButton.addActionListener(e -> {
            timer.stop();
            engine = new SimulationEngine(new BioreactorModel(0.1,20.0),0.1);
            updateLabels();
        });

        updateLabels();

        setVisible(true);
    }

    private void updateSimulation() {
        engine.update();
        updateLabels();
    }

    private void updateLabels() {

        BioreactorModel reactor = engine.getReactor();

        timeLabel.setText("Time: " + engine.getTime() + " hr");
        biomassLabel.setText("Biomass: " + reactor.getBiomass() + " g/L");
        substrateLabel.setText("Substrate: " + reactor.getSubstrate() + " g/L");
        productLabel.setText("Product: " + reactor.getProduct() + " g/L");
    }
}*/


public class BioreactorGUI extends JFrame {

    private JLabel biomassLabel;
    private JLabel substrateLabel;
    private JLabel productLabel;
    private JLabel timeLabel;

    private JTextField biomassInput;
    private JTextField substrateInput;

    private JSlider temperatureSlider;
    private JSlider oxygenSlider;

    private JLabel temperatureLabel;
    private JLabel oxygenLabel;

    private SimulationEngine engine;
    private Timer timer;

    private GraphPanel graph;

    public BioreactorGUI() {

        setTitle("Bioreactor Digital Twin Simulator");
        setSize(600,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ------------------------
        // TOP PANEL (inputs)
        // ------------------------

        JPanel inputPanel = new JPanel();

        biomassInput = new JTextField("0.1",5);
        substrateInput = new JTextField("20",5);

        inputPanel.add(new JLabel("Initial Biomass:"));
        inputPanel.add(biomassInput);

        inputPanel.add(new JLabel("Initial Substrate:"));
        inputPanel.add(substrateInput);

        add(inputPanel, BorderLayout.NORTH);

        // ------------------------
        // CENTER PANEL (graph)
        // ------------------------

        graph = new GraphPanel();
        add(graph, BorderLayout.CENTER);

        // ------------------------
        // RIGHT PANEL (controls)
        // ------------------------

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(6,1));

        temperatureSlider = new JSlider(20,50,37);
        oxygenSlider = new JSlider(0,100,60);

        temperatureLabel = new JLabel("Temperature: 37 °C");
        oxygenLabel = new JLabel("Oxygen: 60 %");

        controlPanel.add(temperatureLabel);
        controlPanel.add(temperatureSlider);

        controlPanel.add(oxygenLabel);
        controlPanel.add(oxygenSlider);

        add(controlPanel, BorderLayout.EAST);

        // ------------------------
        // BOTTOM PANEL (status)
        // ------------------------

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(4,1));

        timeLabel = new JLabel();
        biomassLabel = new JLabel();
        substrateLabel = new JLabel();
        productLabel = new JLabel();

        statusPanel.add(timeLabel);
        statusPanel.add(biomassLabel);
        statusPanel.add(substrateLabel);
        statusPanel.add(productLabel);

        add(statusPanel, BorderLayout.WEST);

        // ------------------------
        // BUTTONS
        // ------------------------

        JPanel buttonPanel = new JPanel();

        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton resetButton = new JButton("Reset");

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resetButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // ------------------------
        // SLIDER EVENTS
        // ------------------------

        temperatureSlider.addChangeListener(e ->
                temperatureLabel.setText("Temperature: " +
                        temperatureSlider.getValue() + " °C"));

        oxygenSlider.addChangeListener(e ->
                oxygenLabel.setText("Oxygen: " +
                        oxygenSlider.getValue() + " %"));

        // ------------------------
        // TIMER
        // ------------------------

        timer = new Timer(500, e -> updateSimulation());

        // ------------------------
        // BUTTON ACTIONS
        // ------------------------

        startButton.addActionListener(e -> {

            try {

                double biomass =
                        Double.parseDouble(biomassInput.getText());

                double substrate =
                        Double.parseDouble(substrateInput.getText());

                BioreactorModel model =
                        new BioreactorModel(biomass, substrate);

                model.setTemperature(
                        temperatureSlider.getValue());

                model.setOxygen(
                        oxygenSlider.getValue());

                engine = new SimulationEngine(model,0.1);

                graph.clear();

                timer.start();

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(this,
                        "Enter valid numbers");

            }

        });

        stopButton.addActionListener(e -> timer.stop());

        resetButton.addActionListener(e -> {

            timer.stop();

            graph.clear();

            engine = new SimulationEngine(
                    new BioreactorModel(0.1,20),0.1);

            updateLabels();

        });

        setVisible(true);
    }

    private void updateSimulation() {

        engine.update();

        BioreactorModel reactor = engine.getReactor();

        graph.addPoint(reactor.getBiomass());

        updateLabels();
    }

    private void updateLabels() {

        if(engine == null) return;

        BioreactorModel reactor = engine.getReactor();

        timeLabel.setText("Time: " +
                engine.getTime() + " hr");

        biomassLabel.setText("Biomass: " +
                reactor.getBiomass() + " g/L");

        substrateLabel.setText("Substrate: " +
                reactor.getSubstrate() + " g/L");

        productLabel.setText("Product: " +
                reactor.getProduct() + " g/L");
    }
}
