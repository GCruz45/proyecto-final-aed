package view;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
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
        rdInputCB.setDisable(false);
        if(selectProblemCB.getValue().equals("Degrees of Separation")){
            dOP = true;
        }else if(selectProblemCB.getValue().equals("Babel")){
            dOP = false;
        }
        model.setDOP(dOP);
    }

    @FXML
    void enableOptions(ActionEvent e){
        if(rdInputCB.isSelected()){
            //TODO generate random input
            inputTA.setDisable(true);
        }else{
            inputTA.setDisable(false);
        }
        outputTA.setDisable(false);
        solveButton.setDisable(false);
    }

    @FXML
    void solveProblem(ActionEvent e){
        String input = inputTA.getText();
        model.readInput(input);
        //TODO Transalte input to actual program and display solution display.
    }


}
