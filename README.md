# Bioreactor Digital Twin Simulator

A Java-based graphical simulator that models microbial fermentation in a stirred-tank bioreactor. The application allows users to adjust environmental conditions and observe real-time changes in biomass growth, substrate consumption, and product formation.

This project combines **bioprocess modeling** with **interactive software simulation** to visualize fermentation dynamics.

---

## Features

• Interactive graphical user interface (GUI)  
• Adjustable temperature and oxygen controls  
• Real-time fermentation simulation  
• Live growth graphs  
• Biomass, substrate, and product tracking  
• Educational visualization of bioprocess engineering concepts  

---

## Simulation Model

The reactor model is based on **Monod microbial growth kinetics**:

μ = μmax * S / (Ks + S)

Where:

- μ = microbial growth rate  
- μmax = maximum growth rate  
- S = substrate concentration  
- Ks = substrate saturation constant  

Environmental factors such as **temperature** and **oxygen concentration** modify the microbial growth rate during the simulation.

---

## Technologies Used

- Java  
- Java Swing (GUI framework)  
- Object-Oriented Programming  
- Scientific Modeling

---

## Project Structure
src/
├── BioReactorSim/
├── BioreactorApp.java
├── BioreactorGUI.java
├── BioreactorModel.java
├── SimulationEngine.java
├── GraphPanel.java


---

## How to Run

1. Copy the repository:
https://github.com/saryael/bioreactor-digital-twin-simulator.git

2. Open the project in an IDE (Eclipse, IntelliJ, or VS Code)

3. Run: BioreactorApp.java

---

## Future Improvements

- Animated bioreactor visualization
- Fed-batch fermentation models
- Multiple fermentation graphs
- Exportable simulation data

---

## License

This project is licensed under the MIT License.
