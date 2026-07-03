package Repository;

import Domain.Runda;

import java.util.List;

public interface RundaRepository extends Repository<Long, Runda>{
    List<Runda> findRundeByJocId(Long jocId);
}
