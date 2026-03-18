package BioReactorSim;

import javax.swing.*;
import java.awt.*;

public class BioreactorGUI extends JFrame {

    private JLabel biomassLabel;
    private JLabel substrateLabel;
    private JLabel productLabel;
    private JLabel timeLabel;
    private JLabel phaseLabel;

    private JTextField biomassInput;
    private JTextField substrateInput;

    private JSlider temperatureSlider;
    private JSlider oxygenSlider;

    private JLabel temperatureLabel;
    private JLabel oxygenLabel;

    private SimulationEngine engine;
    private Timer timer;

    private GraphPanel graph;
    private ReactorAnimationPanel reactorPanel;

    public BioreactorGUI() {

        setTitle("Bioreactor Digital Twin Simulator");
        setSize(800,550);
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
        // CENTER PANEL (graph + reactor)
        // ------------------------

        JPanel centerPanel = new JPanel(new GridLayout(1,2));

        graph = new GraphPanel();
        reactorPanel = new ReactorAnimationPanel();

        centerPanel.add(graph);
        centerPanel.add(reactorPanel);

        add(centerPanel, BorderLayout.CENTER);

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
        // STATUS PANEL
        // ------------------------

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(5,1));

        timeLabel = new JLabel();
        biomassLabel = new JLabel();
        substrateLabel = new JLabel();
        productLabel = new JLabel();
        phaseLabel = new JLabel("Phase: ");

        statusPanel.add(timeLabel);
        statusPanel.add(biomassLabel);
        statusPanel.add(substrateLabel);
        statusPanel.add(productLabel);
        statusPanel.add(phaseLabel);

        // ------------------------
        // BUTTON PANEL
        // ------------------------

        JPanel buttonPanel = new JPanel();

        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton resetButton = new JButton("Reset");

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resetButton);

        // ------------------------
        // COMBINED BOTTOM PANEL
        // ------------------------

        JPanel bottomPanel = new JPanel(new BorderLayout());

        bottomPanel.add(statusPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

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
        // TIMER (SMOOTH ANIMATION)
        // ------------------------

        timer = new Timer(30, e -> updateSimulation());

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
            reactorPanel.resetAnimation();

            updateLabels();

        });

        setVisible(true);
    }

    private void updateSimulation(){

        if(engine == null) return;

        engine.update();

        BioreactorModel reactor = engine.getReactor();

        // 🔥 REAL-TIME CONTROL
        reactor.setTemperature(temperatureSlider.getValue());
        reactor.setOxygen(oxygenSlider.getValue());

        graph.addPoint(reactor.getBiomass());

        reactorPanel.updateBiomass(reactor.getBiomass());

        updateLabels();
    }

    private void updateLabels() {

        if(engine == null) return;

        BioreactorModel reactor = engine.getReactor();

        timeLabel.setText("Time: " +
                String.format("%.2f", engine.getTime()) + " hr");

        biomassLabel.setText("Biomass: " +
                String.format("%.2f", reactor.getBiomass()) + " g/L");

        substrateLabel.setText("Substrate: " +
                String.format("%.2f", reactor.getSubstrate()) + " g/L");

        productLabel.setText("Product: " +
                String.format("%.2f", reactor.getProduct()) + " g/L");

        phaseLabel.setText("Phase: " + engine.getPhase());
    }
}
