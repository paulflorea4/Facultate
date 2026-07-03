package scs.map.sem9_paging.repository;


import scs.map.sem9_paging.domain.Entity;
import scs.map.sem9_paging.util.paging.Page;
import scs.map.sem9_paging.util.paging.Pageable;

public interface PagingRepository<ID , E extends Entity<ID>> extends Repository<ID, E> {

    Page<E> findAllOnPage(Pageable pageable);
}
