package BioReactorSim;

public class BioreactorModel {

    private double biomass;
    private double substrate;
    private double product;

    private double muMax = 0.5;
    private double Ks = 0.5;
    private double Yxs = 0.4;
    private double Ypx = 0.2;

    private double temperature = 37;
    private double oxygen = 60;

    public BioreactorModel(double biomass, double substrate) {
        this.biomass = biomass;
        this.substrate = substrate;
        this.product = 0;
    }

    public void step(double dt) {

        if (substrate <= 0) return;

        double tempFactor = temperature / 37.0;
        double oxygenFactor = oxygen / 100.0;

        double mu = muMax * (substrate / (Ks + substrate));
        mu = mu * tempFactor * oxygenFactor;

        double biomassChange = mu * biomass * dt;
        double substrateChange = (1.0 / Yxs) * mu * biomass * dt;
        double productChange = Ypx * mu * biomass * dt;

        biomass += biomassChange;
        substrate -= substrateChange;
        product += productChange;

        if (substrate < 0) substrate = 0;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setOxygen(double oxygen) {
        this.oxygen = oxygen;
    }

    public double getBiomass() {
        return biomass;
    }

    public double getSubstrate() {
        return substrate;
    }

    public double getProduct() {
        return product;
    }

}
