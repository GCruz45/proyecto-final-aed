package model;

/**
 * This class will use the algorithms implemented in the different classes along the program in order to solve two
 * specific competitive programming problems: Babel (UVa 11492) and Degrees of Separation (UVa 01056).<br>
 * Both this problems were chosen due to their solution being one that could be achieved by applying different algorithms
 * of the Graph data structure on them.<br>
 * This is a class that is part of a final algorithms and data structures course project.
 */
public class ProblemSolver {

    /**This field determines whether or not the problem to be solved is Degrees of Separation.*/
    private boolean dOP;

    /**Set the field <code>dOP</code> to the value specified in the parameter.
     * @param d Whether the problem to be solved is Degrees of Separation or not.
     */
    public void setDOP(boolean d){
        dOP = d;
    }

    public void readInput(String input){
        if(dOP){
            generateDOP(input);
        }else if(!dOP){
            generateBabel(input);
        }
    }

    private void generateDOP(String input){
        IGraph<String> g = new AdjacencyMatrixGraph<>();
        String[] parts = input.split("\n");
        int i = 0;
        while(!parts[i].equals("0 0")){
            
        }
    }

    private void generateBabel(String input){

    }
}
