package it.univr.telemedicina.controller.doctor;

import it.univr.telemedicina.users.Doctor;
import it.univr.telemedicina.utilities.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.Condition;

import javafx.scene.chart.XYChart.Data;

public class DoctorStatisticsSceneController {

    @FXML
    private StackedBarChart<String, Integer> stackedBarChart;
    @FXML
    private RadioButton radioP1Low;
    @FXML
    private RadioButton radioPOptimal;
    @FXML
    private RadioButton radioPNormal;
    @FXML
    private RadioButton radioPNormalHigh;
    @FXML
    private RadioButton radioP1Borderline;
    @FXML
    private RadioButton radioP2Moderate;
    @FXML
    private RadioButton radioP3Critic;
    @FXML
    private RadioButton radioPISBorderline;
    @FXML
    private RadioButton radioPIS;
    @FXML
    private RadioButton radioTDuretic;
    @FXML
    private RadioButton radioTCalcium;
    @FXML
    private RadioButton radioTSympatholytic;
    @FXML
    private RadioButton radioTBeta;
    @FXML
    private RadioButton radioTACE;
    @FXML
    private RadioButton radioTSart;
    @FXML
    private Button buttonShowGraph;
    @FXML
    private Label lblActualGraphic;
    @FXML
    private DatePicker dateStart;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private Tab tabPressure;
    @FXML
    private Tab tabTherapies;

    // Doctor instance
    private static Doctor doctor;

    public void changeTab() {
        stackedBarChart.getData().clear();
        if (tabPressure.isSelected()) {
            lblActualGraphic.setText("ANDAMENTO PRESSIONI");
            // remove all selected radio button in the other tab
            if (radioTACE != null)
                radioTACE.setSelected(false);
            if (radioTBeta != null)
                radioTBeta.setSelected(false);
            if (radioTCalcium != null)
                radioTCalcium.setSelected(false);
            if (radioTDuretic != null)
                radioTDuretic.setSelected(false);
            if (radioTSart != null)
                radioTSart.setSelected(false);
            if (radioTSympatholytic != null)
                radioTSympatholytic.setSelected(false);
        } else {
            lblActualGraphic.setText("ANDAMENTO TERAPIE");
            if (radioP1Borderline != null)
                radioP1Borderline.setSelected(false);
            if (radioPIS != null)
                radioPIS.setSelected(false);
            if (radioP1Low != null)
                radioP1Low.setSelected(false);
            if (radioP2Moderate != null)
                radioP2Moderate.setSelected(false);
            if (radioPISBorderline != null)
                radioPISBorderline.setSelected(false);
            if (radioP3Critic != null)
                radioP3Critic.setSelected(false);
            if (radioPNormal != null)
                radioPNormal.setSelected(false);
            if (radioPNormalHigh != null)
                radioPNormalHigh.setSelected(false);
            if (radioPOptimal != null)
                radioPOptimal.setSelected(false);

        }

    }

    public void showNewGraph(ActionEvent actionEvent) {


        createGraph();
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void createGraph() {
        stackedBarChart.getData().clear();
        ArrayList<String> list;
        LocalDate start = dateStart.getValue();
        LocalDate end = dateEnd.getValue();

        //Check date
        if (start.isAfter(end)) {
            dateStart.setStyle("-fx-text-fill: red;");
            dateEnd.setStyle("-fx-text-fill: red;");
            return;
        } else {
            dateStart.setStyle("-fx-text-fill: black;");
            dateEnd.setStyle("-fx-text-fill: black;");
        }

        try {
            Database db = new Database(2);
            list = db.getQuery("SELECT Date, ConditionPressure FROM BloodPressures WHERE Date BETWEEN '" + start.toString() + "' AND '" + end.toString() + "'", new String[]{"Date", "ConditionPressure"});
            db.closeAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ArrayList<LocalDate> allDate = new ArrayList<>();

        // Insert all the date into dataTaken
        start.datesUntil(end).forEach(allDate::add);
        allDate.add(end);

        // All category
        ArrayList<String> category = new ArrayList<>(Arrays.asList("Ottimale", "Normale", "Normale - alta", "Ipertensione di Grado 1 borderline", "Ipertensione di Grado 1 lieve", "Ipertensione di Grado 2 moderata", "Ipertensione di Grado 3 grave", "Ipertensione sistolica isolata borderline", "Ipertensione sistolica isolata"));
        ArrayList<String> categorySelected = new ArrayList<>();

        // Inizialite categorySelected
        if (radioPOptimal.isSelected())
            categorySelected.add("Ottimale");
        if (radioPNormal.isSelected())
            categorySelected.add("Normale");
        if (radioPNormalHigh.isSelected())
            categorySelected.add("Normale - alta");
        if (radioP1Borderline.isSelected())
            categorySelected.add("Ipertensione di Grado 1 borderline");
        if (radioP1Low.isSelected())
            categorySelected.add("Ipertensione di Grado 1 lieve");
        if (radioP2Moderate.isSelected())
            categorySelected.add("Ipertensione di Grado 2 moderata");
        if (radioP3Critic.isSelected())
            categorySelected.add("Ipertensione di Grado 3 grave");
        if (radioPISBorderline.isSelected())
            categorySelected.add("Ipertensione sistolica isolata borderline");
        if (radioPIS.isSelected())
            categorySelected.add("Ipertensione sistolica isolata");

        // Call method to set Graph
        setGraph(categorySelected, start, end, allDate, list);
    }

    public void createGraphTherapie(){
        // reset data
        stackedBarChart.getData().clear();
        ArrayList<String> queryResult;
        LocalDate start = dateStart.getValue();
        LocalDate end = dateEnd.getValue();

        //Check date
        if (start.isAfter(end) || end.equals(null) || start.equals(null)) {
            dateStart.setStyle("-fx-text-fill: red;");
            dateEnd.setStyle("-fx-text-fill: red;");
            return;
        } else {
            dateStart.setStyle("-fx-text-fill: black;");
            dateEnd.setStyle("-fx-text-fill: black;");
        }

        try {
            Database db = new Database(2);
            queryResult = db.getQuery("SELECT StartDate, TherapyName FROM Therapies WHERE StartDate >='" + start.toString() + "'", new String[]{"StartDate", "TherapyName"});
            System.out.println(queryResult.toString());
            db.closeAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ArrayList<LocalDate> allDate = new ArrayList<>();

        // Insert all the date into dataTaken
        start.datesUntil(end).forEach(allDate::add);
        allDate.add(end);

        //All category
        ArrayList<String> therapiesSelected = new ArrayList<>();

        if (radioTACE.isSelected())
            therapiesSelected.add("ACE-inibitori");
        if (radioTBeta.isSelected())
            therapiesSelected.add("Beta bloccanti");
        if (radioTCalcium.isSelected())
            therapiesSelected.add("Calcio-antagonisti");
        if (radioTDuretic.isSelected())
            therapiesSelected.add("Diuretiche");
        if (radioTSart.isSelected())
            therapiesSelected.add("Sartani");
        if (radioTSympatholytic.isSelected())
            therapiesSelected.add("Simpaticolitici");

        setGraph(therapiesSelected, start, end, allDate, queryResult);
    }

    private void setGraph(ArrayList<String> categorySelected, LocalDate start, LocalDate end, ArrayList<LocalDate> allDate, ArrayList<String> queryResult){
        int j = 0;  // Go trough days
        XYChart.Series<String, Integer>[] series = new XYChart.Series[categorySelected.size()];

        // Cycle all the categories selected
        for (String category : categorySelected) {
            int i = categorySelected.indexOf(category);     // Go trough categories
            series[i] = new XYChart.Series<>();     // Create a new series for the category
            series[i].setName(category);
            pos = 1;

            // 14 days or less
            if (ChronoUnit.DAYS.between(start, end) <= 14) {
                for (LocalDate date : allDate)
                    series[i].getData().add(returnNumberCondition(date.toString(), null, queryResult, category));
            }
            // 31 days
            else if (ChronoUnit.DAYS.between(start, end) <= 31)  {
                // I take the range of one week
                for (j = 0; j < allDate.size() - 6; j += 7) {
                    series[i].getData().add(returnNumberCondition(allDate.get(j).toString(), allDate.get(j + 6).toString(), queryResult, category));
                    pos++;
                }
                // I check if there are other days less than 7 days
                if(allDate.size()-1-j != 0 ){
                    series[i].getData().add(returnNumberCondition(allDate.get(j).toString(), allDate.get(allDate.size()-1).toString(), queryResult, category));
                    pos++;
                }
            }
            else {
                // I take the range of one month
                for (j = 0; j < allDate.size() - 29; j += 30) {
                    series[i].getData().add(returnNumberCondition(allDate.get(j).toString(), allDate.get(j + 29).toString(), queryResult, category));
                    pos++;
                }

                // I check if there are other days less than 30 days
                if(allDate.size()-1-j != 0 ){
                    series[i].getData().add(returnNumberCondition(allDate.get(j).toString(), allDate.get(allDate.size()-1).toString(), queryResult, category));
                    pos++;
                }
            }

            // Set number on bar
            series[i].getData().forEach(data -> {
                Label label = new Label(data.getYValue().toString());
                label.setAlignment(Pos.TOP_CENTER);
                label.setStyle("-fx-font-size: 16px; -fx-text-fill: white");
                data.setNode(label);
                data.getNode().setNodeOrientation(NodeOrientation.INHERIT);

            });
        }

        //stackedBarChart.setStyle(".chart-series-0 .default-color8.chart-bar { -fx-bar-fill: black;} ");
        stackedBarChart.getData().addAll(series);
        stackedBarChart.setAnimated(false);
    }

    /**
     * Method that calculates occurrences in the specified period and which satisfies a certain condition
     * @param dateStart
     * @param dateEnd
     * @param queryResult
     * @param condition pressure or therapy
     * @return XYChart.Data<String, Integer>    return XYChart.Data to add to the Series, String is value for day range and Integer is number of occurrences of the condition in the date range
     */
    private XYChart.Data<String, Integer> returnNumberCondition(String dateStart,String dateEnd, ArrayList<String> queryResult, String condition) {
        int count = 0;
        for (int i = 0; i < list.size() - 1; i = i + 2) {
            // Date equal i have value
            if (list.get(i).equals(date) && list.get(i + 1).equals(condition)) {
                count++;
            }
        }
        return new XYChart.Data<>(date, count);
    }


}



/*



series.setName(Cate)
 series.getData.add(newXYCHART.DATA("Brazil", 132);
 series.getData.add(newXYCHART.DATA("AF", 132);
 series.getData.add(newXYCHART.DATA("sf", 132);
 series.getData.add(newXYCHART.DATA("fg", 132);
 series.getData.add(newXYCHART.DATA("azil", 132);
 series.getData.add(newXYCHART.DATA("d", 132);
 series.setName(Cate)
 series.getData.add(newXYCHART.DATA("Brazil", 132);
 series.getData.add(newXYCHART.DATA("AF", 132);
 series.getData.add(newXYCHART.DATA("sf", 132);
 series.getData.add(newXYCHART.DATA("fg", 132);
 series.getData.add(newXYCHART.DATA("azil", 132);
 series.getData.add(newXYCHART.DATA("d", 132);
 */










/*
list -> giorni  condizione


treemap -> giorni, 0
treemap -> giorni, ++

arraylist(xychart("funzione", valore(treemap)))
TREEMAP.0 -> arraylist.0



ARRAYLIST<STRING date>
serieX.setName(arralist.get(0)).getData.add(arralist.get(0))*/


/*
// Insert all the date into dataTaken
        start.datesUntil(end).forEach(allDate::add);
        allDate.add(end);

        XYChart.Series<String, Integer>[] series = new XYChart.Series[allDate.size()];
        int i = 0;

        for(LocalDate date : allDate){
            series[i] = new XYChart.Series<>();
            series[i].setName(date.toString());
            if(radioPOptimal.isSelected())
                series[i].getData().add(returnNumberCondition(date.toString(), list, "Ottimale"));
            if(radioPNormal.isSelected())
                series[i].getData().add(returnNumberCondition(date.toString(), list, "Normale"));
            if(radioPNormalHigh.isSelected())
                series[i].getData().add(returnNumberCondition(date.toString(), list, "Normale - alta"));
            if(radioP1Borderline.isSelected())
                series[i].getData().add(returnNumberCondition(date.toString(), list, "Ipertensione di Grado 1 borderline"));
            if(radioP1Low.isSelected())
                series[i].getData().add(returnNumberCondition(date.toString(), list, "Ipertensione di Grado 1 lieve"));
            if(radioP2Moderate.isSelected())
                series[i].getData().add(returnNumberCondition(date.toString(), list, "Ipertensione di Grado 2 Moderata"));
            if(radioP3Critic.isSelected())
                series[i].getData().add(returnNumberCondition(date.toString(), list, "Ipertensione di Grado 3 grave"));
            if(radioPISBorderline.isSelected())
                series[i].getData().add(returnNumberCondition(date.toString(), list, "Ipertensione sistolica isolata borderline"));
            if(radioPIS.isSelected())
                series[i].getData().add(returnNumberCondition(date.toString(), list, "Ipertensione sistolica isolata"));

            //Add number on
            /*
            series[i].getData().forEach(data -> {
                Label label = new Label(data.getYValue().toString());
                label.setAlignment(Pos.TOP_CENTER);
                label.setStyle("-fx-font-size: 16px; -fx-text-fill: white");
                data.setNode(label);
                data.getNode().setNodeOrientation(NodeOrientation.INHERIT);
            });


            i++;

                    }
                    stackedBarChart.getRotationAxis();
                    stackedBarChart.getData().addAll(series);


                    stackedBarChart.setAnimated(false);

 */