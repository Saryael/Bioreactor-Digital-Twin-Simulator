package BioReactorSim;
public class SimulationEngine {

    private BioreactorModel reactor;
    private double time;
    private double dt;

    public SimulationEngine(BioreactorModel reactor, double dt) {
        this.reactor = reactor;
        this.dt = dt;
        this.time = 0.0;
    }

    public void update() {
        reactor.step(dt);
        time += dt;
    }

    public double getTime() {
        return time;
    }

    public BioreactorModel getReactor() {
        return reactor;
    }
}
