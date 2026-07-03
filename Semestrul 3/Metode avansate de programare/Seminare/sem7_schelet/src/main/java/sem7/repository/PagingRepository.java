package sem7.repository;


import sem7.domain.Entity;
import sem7.util.paging.Page;
import sem7.util.paging.Pageable;

public interface PagingRepository<ID , E extends Entity<ID>> extends Repository<ID, E> {

    Page<E> findAllOnPage(Pageable pageable);
}
