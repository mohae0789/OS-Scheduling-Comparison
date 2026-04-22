//package gui;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.HBox;
//import javafx.util.Callback;
//import model.Process;
//
//public class Controller {
//
//    @FXML private TextField txtQuantum;
//    @FXML private TextField txtArrival;
//    @FXML private TextField txtBurst;
//    @FXML private TableView<Process> table;
//    @FXML private TableColumn<Process, String> colId;
//    @FXML private TableColumn<Process, Integer> colArrival;
//    @FXML private TableColumn<Process, Integer> colBurst;
//    @FXML private TableColumn<Process, Void> colAction; // عمود الزرار
//    @FXML private HBox actionBox;
//
//    private ObservableList<Process> processList = FXCollections.observableArrayList();
//    private int pCounter = 1;
//
//    @FXML
//    public void initialize() {
//        colId.setCellValueFactory(new PropertyValueFactory<>("pid"));
//        colArrival.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
//        colBurst.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
//
//
//        addButtonToTable();
//
//        table.setItems(processList);
//    }
//
//    @FXML
//    private void handleAddProcess() {
//        String arrivalStr = txtArrival.getText().trim();
//        String burstStr = txtBurst.getText().trim();
//
//
//        if (arrivalStr.isEmpty() || burstStr.isEmpty()) {
//            showAlert("Validation Error", "Arrival Time and Burst Time cannot be empty!");
//            return;
//        }
//
//        try {
//            int arr = Integer.parseInt(arrivalStr);
//            int brst = Integer.parseInt(burstStr);
//
//            if (arr < 0 || brst <= 0) {
//                showAlert("Validation Error", "Arrival must be >= 0 and Burst must be > 0!");
//                return;
//            }
//
//
//            processList.add(new Process("P" + pCounter++, arr, brst));
//
//            table.setVisible(true);
//            actionBox.setVisible(true);
//            txtArrival.clear();
//            txtBurst.clear();
//
//        } catch (NumberFormatException e) {
//            showAlert("Input Error", "Please enter valid integer numbers!");
//        }
//    }
//
//    private void addButtonToTable() {
//        Callback<TableColumn<Process, Void>, TableCell<Process, Void>> cellFactory = param -> new TableCell<>() {
//            private final Button btn = new Button("Remove");
//            {
//                btn.getStyleClass().add("remove-button");
//              //  btn.setMaxWidth(Double.MAX_VALUE);
//                btn.setOnAction(event -> {
//                    Process p = getTableView().getItems().get(getIndex());
//                    processList.remove(p);
//                    if (processList.isEmpty()) {
//                        table.setVisible(false);
//                        actionBox.setVisible(false);
//                    }
//                });
//            }
//
//            @Override
//            public void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//                setGraphic(empty ? null : btn);
//            }
//        };
//        colAction.setCellFactory(cellFactory);
//    }
//
//    private void showAlert(String title, String content) {
//        Alert alert = new Alert(Alert.AlertType.WARNING);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//
//    @FXML
//    private void handleReset() {
//        processList.clear();
//        pCounter = 1;
//        table.setVisible(false);
//        actionBox.setVisible(false);
//        txtQuantum.clear();
//    }
//
//    @FXML
//    private void handleSimulate() {
//        String quantumStr = txtQuantum.getText().trim();
//
//        if (quantumStr.isEmpty()) {
//            showAlert("Missing Data", "Please enter the Time Quantum.");
//            return;
//        }
//
//        try {
//            int q = Integer.parseInt(quantumStr);
//            if (q <= 0) {
//                showAlert("Validation Error", "Time Quantum must be a positive number greater than zero!");
//                return;
//            }
//            System.out.println("Starting simulation with Quantum: " + q);
//
//        } catch (NumberFormatException e) {
//            showAlert("Input Error", "Please enter a valid integer for Time Quantum.");
//        }
//    }
//
//
//}



package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.Process;
import util.ValidationUtil;

public class Controller {

    @FXML private TextField txtQuantum, txtArrival, txtBurst;
    @FXML private TableView<Process> table;
    @FXML private TableColumn<Process, String> colId;
    @FXML private TableColumn<Process, Integer> colArrival, colBurst;
    @FXML private TableColumn<Process, Void> colAction;
    @FXML private ComboBox<String> comboScenarios;
    @FXML private HBox actionBox;

    private ObservableList<Process> processList = FXCollections.observableArrayList();
    private int pCounter = 1;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("pid"));
        colArrival.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        colBurst.setCellValueFactory(new PropertyValueFactory<>("burstTime"));

        // إعداد السيناريوهات بناءً على ورقة المتطلبات
        comboScenarios.setItems(FXCollections.observableArrayList(
                "Scenario A: Basic mixed workload",
                "Scenario B: Short-job-heavy case",
                "Scenario C: Fairness case",
                "Scenario D: Long-job sensitivity case"
        ));

        setupActionColumn();
        table.setItems(processList);
    }

    @FXML
    private void handleAddProcess() {
        if (!ValidationUtil.isValidArrival(txtArrival.getText().trim()) ||
                !ValidationUtil.isPositiveInt(txtBurst.getText().trim())) {
            ValidationUtil.showWarning("Input Error", "Please enter valid Arrival (>=0) and Burst (>0) times.");
            return;
        }

        processList.add(new Process("P" + pCounter++, Integer.parseInt(txtArrival.getText().trim()), Integer.parseInt(txtBurst.getText().trim())));
        showTableComponents();
        txtArrival.clear(); txtBurst.clear();
    }

    @FXML
    private void handleScenarioSelection() {
        String selected = comboScenarios.getValue();
        if (selected == null) return;
        handleReset();

        switch (selected) {
            case "Scenario A: Basic mixed workload":
                txtQuantum.setText("4");
                processList.addAll(new Process("P1", 0, 8), new Process("P2", 1, 4), new Process("P3", 2, 9));
                break;
            case "Scenario B: Short-job-heavy case":
                txtQuantum.setText("2");
                processList.addAll(new Process("P1", 0, 20), new Process("P2", 2, 2), new Process("P3", 3, 1));
                break;
            case "Scenario C: Fairness case":
                txtQuantum.setText("3");
                processList.addAll(new Process("P1", 0, 10), new Process("P2", 0, 10), new Process("P3", 0, 10));
                break;
            case "Scenario D: Long-job sensitivity case":
                txtQuantum.setText("5");
                processList.addAll(new Process("P1", 0, 30), new Process("P2", 1, 2), new Process("P3", 2, 2));
                break;
        }
        showTableComponents();
    }

    private void setupActionColumn() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Remove");
            {
                btn.getStyleClass().add("remove-button");
                btn.setOnAction(event -> {
                    processList.remove(getTableView().getItems().get(getIndex()));
                    if (processList.isEmpty()) hideTableComponents();
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void showTableComponents() { table.setVisible(true); actionBox.setVisible(true); }
    private void hideTableComponents() { table.setVisible(false); actionBox.setVisible(false); pCounter = 1; }

    @FXML private void handleReset() { processList.clear(); hideTableComponents(); txtQuantum.clear(); }

    @FXML private void handleSimulate() {
        if (!ValidationUtil.isPositiveInt(txtQuantum.getText().trim())) {
            ValidationUtil.showWarning("Missing Data", "Please enter a positive Time Quantum.");
            return;
        }
        System.out.println("Simulating C5 Project...");
    }
}








