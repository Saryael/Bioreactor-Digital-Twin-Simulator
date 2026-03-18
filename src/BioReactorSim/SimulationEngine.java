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
    
    public String getPhase(){

        double t = time;

        if(t < 2) return "Lag Phase";
        else if(t < 10) return "Exponential Growth";
        else if(t < 18) return "Stationary Phase";
        else return "Death Phase";
    }
}
