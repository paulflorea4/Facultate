package Repository;

import Domain.Jucator;

public interface JucatorRepository extends Repository<Long, Jucator> {
    Jucator findByAlias(String alias);
}
