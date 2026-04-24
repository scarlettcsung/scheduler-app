package repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic in-memory repository abstraction.
 *
 * @param <T> type of item stored by the repository
 * @author CR EO
 * @version 3
 */
public abstract class Repository<T> {
    protected List<T> data;

    /**
     * Creates an empty repository.
     */
    public Repository() {
        this.data = new ArrayList<>();
    }
    public abstract T getItemById(String itemId);
    
    
    /**
     * Stores an item in the repository.
     *
     * @param item item to store
     */
    public void save(T item) {
        this.data.add(item);
    }

    /**
     * Returns a copy of the stored items.
     *
     * @return repository contents
     */
    public List<T> getAll() {
        return new ArrayList<>(this.data);
    }

    /**
     * Returns a descriptive repository type label.
     *
     * @return repository type name
     */
    public abstract String getRepositoryType();
}
