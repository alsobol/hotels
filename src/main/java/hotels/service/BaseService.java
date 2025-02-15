package hotels.service;

import java.util.List;

public interface BaseService<T> {

	List<T> getAll();

	T getById(Long id);

	void delete(Long id);

	void save(T t);
}
