package collections;

import java.util.Collection;

/**
 * An alternative Collections Interface that will be used among different implementations of IGraph. As of now, no class implements this interface.
 *
 * @param <T> The type of the elements to be stored in this collection.
 * @author AED Third Group - Universidad ICESI - 2019-2
 * @version 1.0 - 10/2019
 */
public interface ICollection<T> {

    /**
     * Adds a given element in the collection.
     *
     * @param element The element to be added in the collection.
     */
    void add(T element);

    /**
     * Retrieves and removes the first element in a data structure. First element should be defined by the implementor data structure.
     *
     * @return The element removed from the data structure.
     */
    T poll();

    /**
     * Retrieves, but does not remove, the first element in the data structure. First element should be defined by the implementor data structure.
     *
     * @return The element removed from the data structure.
     */
    T peek();

    /**
     * Determines whether a Collection is empty or not.
     *
     * @return True if this Collection has no items in it, false if not.
     */
    boolean isEmpty();

    void addAll(Collection<T> c);
}
