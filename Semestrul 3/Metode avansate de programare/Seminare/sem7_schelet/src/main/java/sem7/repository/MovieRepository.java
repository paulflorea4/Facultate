package sem7.repository;



import sem7.domain.Movie;
import sem7.util.paging.Page;
import sem7.util.paging.Pageable;

import java.util.List;

public interface MovieRepository extends PagingRepository<Long, Movie> {
    List<Integer> getYears();
}
