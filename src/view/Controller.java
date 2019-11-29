package view;
import exceptions.ElementAlreadyPresentException;
import exceptions.ElementNotFoundException;
import exceptions.WrongEdgeTypeException;
import exceptions.WrongGraphTypeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.ProblemSolver;

public class Controller {

    @FXML
    private ComboBox<String> selectProblemCB;

    @FXML
    private CheckBox rdInputCB;

    @FXML
    private TextArea inputTA;

    @FXML
    private TextArea outputTA;

    @FXML
    private AnchorPane displayAP;

    @FXML
    private Button solveButton;

    @FXML
    private Button nextTCButton;

    @FXML
    private Button prevTCButton;


    /**This determines whether or not the problem is the designated as the Degrees of Separation problem.*/
    private boolean dOP;

    /**The connection to the model.*/
    private ProblemSolver model;

    @FXML
    void initialize(){
        model = new ProblemSolver();
    }

    @FXML
    void enableOption(ActionEvent e){
        Alert n = new Alert(Alert.AlertType.INFORMATION);
        n.setTitle("Not implemented yet");
        n.setContentText("Random input hasn't been yet implemented. Will be in future updates.\n" +
                "We deeply thank your patience :D");
        n.showAndWait();
        rdInputCB.setSelected(false);
    }

    @FXML
    void enableIO(ActionEvent e){
        if(selectProblemCB.getValue().equals("Degrees of Separation")){
            dOP = true;
        }else if(selectProblemCB.getValue().equals("Babel")){
            dOP = false;
        }
        model.setDOP(dOP);
        rdInputCB.setDisable(false);
        inputTA.setDisable(false);
        outputTA.setDisable(false);
        solveButton.setDisable(false);
    }

    @FXML
    void solveProblem(ActionEvent e){
        String input = inputTA.getText();
        try {
            outputTA.setText(model.readInput(input));
        } catch (Exception ex){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error!");
            a.setContentText("You gave an input for an incorrect problem");
            ex.printStackTrace();
            a.showAndWait();
        }
        //TODO Translate input to actual program and display solution display.
    }

    @FXML
    void prevTC(ActionEvent e){

    }

    @FXML
    void nextTC(ActionEvent e){

    }

}
