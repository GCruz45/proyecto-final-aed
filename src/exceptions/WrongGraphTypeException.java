package exceptions;

/**
 * Custom exception to be thrown when a vertex or edge is not found in the graph.
 */
public class WrongGraphTypeException extends Exception {

    /**
     * Constructor that replaces the message shown by the super class by the one provided.
     *
     * @param message is to be shown when the exception is thrown
     */
    public WrongGraphTypeException(String message) {
        super(message);
    }
}
