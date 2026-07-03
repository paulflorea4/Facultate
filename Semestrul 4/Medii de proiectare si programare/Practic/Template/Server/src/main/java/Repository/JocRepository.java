package Repository;

import Domain.Joc;

public interface JocRepository extends Repository<Long, Joc> {
    Iterable<Joc> findAllGamesWithDetails();
}
