package Repository;

import Domain.Cuvant;
import Domain.Runda;

public interface CuvantRepository extends Repository<Long, Cuvant>{
    Cuvant getRandomCuvant(String complexitate);
}
