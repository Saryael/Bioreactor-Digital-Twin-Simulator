package BioReactorSim;
/*public class BioreactorApp {

    public static void main(String[] args) {

        // Initial conditions
        BioreactorModel reactor = new BioreactorModel(0.1, 20.0);

        SimulationEngine engine = new SimulationEngine(reactor, 0.1);

        for (int i = 0; i < 100; i++) {

            engine.update();

            System.out.println("Time: " + engine.getTime() + " hr");
            System.out.println("Biomass: " + reactor.getBiomass() + " g/L");
            System.out.println("Substrate: " + reactor.getSubstrate() + " g/L");
            System.out.println("Product: " + reactor.getProduct() + " g/L");

            System.out.println("--------------------------");
        }
    }
}
*/
public class BioreactorApp {
    public static void main(String[] args) {
        new BioreactorGUI();
    }
}
