# OS CPU Scheduling Simulator - Project C5

## 📝 Project Overview
This project is a high-fidelity CPU scheduling simulator developed for the Operating Systems course at Helwan University. It provides a comprehensive comparative analysis between two fundamental scheduling algorithms:
1. **Round Robin (RR)**: Focused on fairness and time-slicing using a dynamic Time Quantum.
2. **Shortest Job First (SJF)**: Focused on efficiency by prioritizing processes with the shortest execution time.

The simulator evaluates performance based on key metrics: Waiting Time (WT), Turnaround Time (TAT), and Response Time (RT).

## 👥 Team Members
* **[Mohamed Ahmed Mohamed abdeul majeed]** - ID: [20245069]
* **[]** - ID: [Partner's ID]
* **[]** -ID: [ID]
* **[]** -ID: [ID]
* **[]** -ID: [ID]
* **[]** -ID: [ID]

## 🚀 Key Features
- **Graphic Interface**: Built with JavaFX and Scene Builder for intuitive user interaction.
- **Dynamic Workload**: Users can add a dynamic number of processes at runtime.
- **Dual Gantt Charts**: Separate, clear Gantt chart visualizations for both algorithms.
- **Metric Comparison Tables**: Side-by-side results for Per-process WT, TAT, and RT.
- **Automated Analysis**: Calculates and displays average WT, TAT, and RT for both algorithms simultaneously.
- **Input Validation**: Safely rejects negative values, zero burst times, and non-numeric inputs.

## 🛠 Tech Stack
- **Language**: Java 17+
- **GUI Framework**: JavaFX
- **Tools**: IntelliJ IDEA / Eclipse, Scene Builder
- **Version Control**: Git & GitHub

## 📂 Repository Structure
Following the recommended professional structure:
```text
project-root(OS-Scheduling-Comparison)/
|--src/
|   |── model/      # Data models (Process class)
|   ├── scheduler/  # Logic for RR and SJF algorithms
|   ├── metrics/    # Performance calculation logic
|   ├── gui/        # FXML files and Controllers
|   └── util/       # Input validation and helper utilities
|-- assets/          # UI icons and CSS
|--screenshots/     # UI and Gantt Chart captures
|_test-cases/      # Documentation of tested scenarios


---------------------------------------------------------

## 📊 Test Scenarios
The simulator has been verified using the following 5 required scenarios to ensure accuracy and robustness:

1.  **Scenario A (Basic Workload)**: Mixed arrival and burst times to verify the general accuracy of calculation for both algorithms.
2.  **Scenario B (Short-Job-Heavy)**: Features several short processes to demonstrate how **SJF** behavior becomes clearly more efficient in minimizing average wait times.
3.  **Scenario C (Fairness Case)**: Prepared a workload to show how **Round Robin** distributes CPU service fairly across multiple active processes using the Time Quantum.
4.  **Scenario D (Long-Job Sensitivity)**: A case where one long process competes with shorter ones, highlighting how SJF might cause starvation while RR maintains responsiveness.
5.  **Scenario E (Validation Case)**: Includes at least one invalid input example (e.g., negative arrival time or non-numeric burst) to demonstrate the system's safety and error-handling behavior.

---

## ⚙️ How to Run
To run this simulator on your local machine, follow these steps:

1.  **Prerequisites**: Ensure you have **Java 17 (JDK)** or higher installed and the **JavaFX SDK** configured.
2.  **Clone the Repository**: 
    ```bash
    git clone [Your-Repo-URL]
    ```
3.  **IDE Configuration**:
    * Open the project in your IDE (IntelliJ IDEA is recommended).
    * Add the JavaFX library to your Project Structure.
    * Add the following **VM Options** in your Run Configuration:
      `--module-path [path-to-your-javafx-sdk/lib] --add-modules javafx.controls,javafx.fxml`
4.  **Launch**: Run the `Main.java` file to start the Graphical User Interface.

---

## 📌 Conclusion
After conducting a detailed comparative analysis between **Round Robin** and **SJF**, our team observed the following:

* **Performance Metrics**: **SJF** consistently achieved a lower **Average Waiting Time** and **Turnaround Time**, making it the most efficient choice for minimizing overall delay.
* **Execution Fairness**: **Round Robin** appeared more balanced and fair. It prevents "Starvation" and ensures that every process gets a turn in the CPU, regardless of its length.
* **Impact of Time Quantum**: We observed that as the Time Quantum increases, Round Robin behavior starts to converge toward FCFS. A smaller Quantum improves **Response Time** but increases the overhead due to frequent context switching.
* **Final Recommendation**: **SJF** is highly recommended for batch processing systems, while **Round Robin** is the optimal choice for interactive, time-sharing environments where user experience and fairness are priorities.


