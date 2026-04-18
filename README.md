# OS CPU Scheduling Simulator - Project C5

## 📝 Project Overview
This project is a high-fidelity CPU scheduling simulator developed for the Operating Systems course at Helwan University. It provides a comprehensive comparative analysis between two fundamental scheduling algorithms:
1. **Round Robin (RR)**: Focused on fairness and time-slicing using a dynamic Time Quantum.
2. **Shortest Job First (SJF)**: Focused on efficiency by prioritizing processes with the shortest execution time.

The simulator evaluates performance based on key metrics: Waiting Time (WT), Turnaround Time (TAT), and Response Time (RT).

## 👥 Team Members
* **[Your Name]** - ID: [Your ID]
* **[Partner's Name]** - ID: [Partner's ID]

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
src/
 ├── model/      # Data models (Process class)
 ├── scheduler/  # Logic for RR and SJF algorithms
 ├── metrics/    # Performance calculation logic
 ├── gui/        # FXML files and Controllers
 └── util/       # Input validation and helper utilities
assets/          # UI icons and CSS
screenshots/     # UI and Gantt Chart captures
test-cases/      # Documentation of tested scenarios

