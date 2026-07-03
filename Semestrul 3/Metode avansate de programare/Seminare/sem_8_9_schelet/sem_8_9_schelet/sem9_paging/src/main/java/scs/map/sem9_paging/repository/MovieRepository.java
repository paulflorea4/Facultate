package scs.map.sem9_paging.repository;



import scs.map.sem9_paging.domain.Movie;

import scs.map.sem9_paging.util.paging.Page;
import scs.map.sem9_paging.util.paging.Pageable;

import java.util.List;

public interface MovieRepository extends PagingRepository<Long, Movie> {
    List<Integer> getYears();


}
