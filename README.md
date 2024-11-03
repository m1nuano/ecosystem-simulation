# Ecosystem Simulation Application

**Test Task Completed for SENLA**  
*Prepared by: [Лютыч Алексей]*

## Overview

The Ecosystem Simulation Application models complex interactions within a dynamic ecosystem, incorporating changes in
population, resource consumption, and environmental influences over time. This program enables users to simulate ecosystem
behavior, providing tools to predict population trends and visualize the effects of seasonal and environmental shifts on
plants, animals, and other resources.

## Core Functionalities

- Interactive Simulation: Run scenarios to observe animal and plant behaviors based on resource availability,
  environmental parameters, and seasonal factors.
- Forecasting Tool: Predicts population changes over a period to assess ecosystem sustainability.
- Customizable Ecosystem Settings: Users can add or remove species, adjust resources, and set environmental factors
  such as temperature, humidity, and water levels.

---

## Ecosystem Components and Interactions

### Animals
Animals include both herbivores and predators, each with specific population, water, and food requirements.
They interact with plants and each other in the following ways:
- Predators: Hunt herbivores with a success probability based on strength, and risk population decline if
  hunting fails for several cycles.
- Herbivores: Consume plants and reproduce based on available resources and population density.

### Plants
Plants form the foundation of the ecosystem, with growth impacted by weather, seasons, and resource availability:
- Growth Rates: Modulated by seasonal growth factors (e.g., Spring boosts growth to 1.5x, while Winter reduces it to 0.5x).
- Environmental Influence: Daily temperature and humidity changes can affect plant growth, especially during extreme
  weather events.

### Resources
The ecosystem relies on water, temperature, and seasonal data that regulate animal survival and plant growth:
- Water: Essential for animal hydration, and replenished through seasonal rains or reduced during droughts.
- Temperature and Humidity: Impact plant growth rates and seasonal transitions, influencing both the ecosystem's
  general health and specific animal behaviors.

---

## Animal Interaction and Behavior Probabilities

### Herbivore-Plant Interaction
- **Herbivores Consuming Plants**:
  - **Probability of Success**: Herbivores consume plants with a 65% chance each cycle.
  - **Food Calculation**: The amount of plants consumed depends on the herbivore's total food requirement, calculated as its food intake multiplied by population.
  - **Depletion of Plant Population**: Each unit of food consumed decreases the plant population accordingly, which is tracked to prevent overconsumption and simulate environmental balance.

### Predator-Prey Dynamics
- **Predator Hunting Success**:
  - Predators hunt herbivores based on relative strength levels, with probabilities adjusted for prey difficulty:
    - **Equal Levels or Higher**: 100% success rate.
    - **One Level Below**: 42% success rate.
    - **Two Levels Below**: 35% success rate.
    - **Three Levels Below**: 27% success rate.
    - **Four Levels Below**: 18% success rate.
    - **Special Case (Both Level 5)**: If both predator and prey are at maximum level, there is only a 9% success chance, representing a rare encounter among top predators.
  - **Hunger and Starvation**: Predators that fail to hunt for seven cycles face starvation, leading to population decrease.

### Starvation Mechanism
- **Herbivores and Predators**:
  - **Starvation Onset**: When any animal lacks adequate food for a prolonged period, starvation begins after 7 consecutive cycles without food.
  - **Daily Survival Reduction**: Animals experiencing starvation face population decline in cycles until their food requirements are met.

### Reproduction
- **Reproduction Chances**:
  - **Herbivores**: Reproduce with a 63% chance per cycle, contingent on population being above 2 and food resources being sufficient.
  - **Predators**: Reproduce with a 21% chance, provided there is an adequate prey population to sustain the offspring.
- **Population Increase**: Successful reproduction increases the population by a small factor (1% of the current population) plus one new individual, helping to stabilize populations over time.

### Resource-Driven Checks
- **Prey Availability Check**:
  - Herbivores need sufficient plants, while predators require enough herbivores to meet their consumption needs.
  - **Availability Confirmation**: Checks for enough plants or herbivores are made before each action to ensure that population growth and stability are ecologically viable.


## Simulation Structure

### Seasonal and Weather Adjustments
Seasons rotate every 91 days, each with unique characteristics influencing plant growth and animal behavior:
(if base temperature at init equals 20°C)
- Spring (Average 15°C): Optimal growth rate with frequent rainfall.
- Summer (Average 25°C): Moderate growth but higher drought probability.
- Autumn (Average 10°C): Steady growth with occasional rain.
- Winter (Average -5°C): Minimal growth, challenging survival conditions.

### User Control Interface
The application provides a command-line menu to interact with the ecosystem, enabling tasks such as:
1. Add Animal or Plant: Incorporate new species into the ecosystem.
2. Set Resources: Define environmental settings for accurate simulations.
3. Run Simulation: Observe interactions and population changes.
4. Forecast Changes: Get predictions on population dynamics.
5. Save Ecosystem: Preserve the current ecosystem state for future analysis.

### Example Commands
- Start New Ecosystem: Press 1 to initialize a fresh ecosystem with default resources.
- Load Existing Ecosystem: Press 2 to import a saved ecosystem from the /ecosystems directory.
- Save Progress: Select option 9 to save ecosystem data in a text file format.

---

## Technical Specifications

### Key Classes and Packages

- Main Class (`com.test`): Entry point with user interface logic.
- Model Classes (`com.test.model`): Define Animal, Plant, and Resource structures.
- Service Layer (`com.test.service`): EcosystemService for managing core actions.
- Prediction Module (`com.test.prediction`): EcosystemSimulation for forecasting changes.
- Utilities (`com.test.util`): Includes EcosystemFileManager for saving/loading and LoggerUtil for event logging.### Lombok Integration
The project uses Lombok to streamline the code by auto-generating common methods, such as getters and setters,
reducing boilerplate code. Ensure that Lombok is properly set up in your IDE (e.g., with the Lombok plugin in IntelliJ IDEA or Eclipse).

### Requirements
- Java JDK 11 or higher: Core development environment.
- Maven or Gradle: Build and dependency management.

### Running the Program
1. **Build the Project**: Use `./gradlew build` in the terminal or an IDE of your choice.
2. **Run**: Execute Main.java to start interacting with the simulation via the command-line interface.
3. **Batch File (RUN.bat)**: The project can be launched directly from the terminal using RUN.bat to streamline startup.

---

## Best Practices

- Save Frequently: Regular saves prevent data loss.
- Initialize Resources: Set initial water, temperature, and other values for realistic simulations.
- Monitor Logs: Utilize logging to track changes in ecosystem parameters, aiding in deeper analysis.
