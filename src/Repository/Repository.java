package Repository;

import User.AdminUser;
import User.User;
import java.util.ArrayList;
import java.util.List;

public abstract class Repository<T> {
    protected List<T> data;

    // general repository
    public Repository() {
        this.data = new ArrayList<>();
    }

    public void save(T item) {
        data.add(item);
    }

    public List<T> getAll() {
    	return new ArrayList<>(data);
    	
    }
}
 