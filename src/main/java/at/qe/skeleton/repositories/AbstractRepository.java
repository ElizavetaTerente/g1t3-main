package at.qe.skeleton.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;

/**
 * Common base repository for all other repositories. Provides basic methods for
 * loading, saving and removing entities.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 *
 * @param <T>  The domain type this repository manages.
 * @param <ID> The type of the id of the entity this repository manages.
 */
@NoRepositoryBean
public interface AbstractRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

    /**
     * Returns all instances of the type.
     *
     * @return All entities.
     */
    @Override
    List<T> findAll();
}
