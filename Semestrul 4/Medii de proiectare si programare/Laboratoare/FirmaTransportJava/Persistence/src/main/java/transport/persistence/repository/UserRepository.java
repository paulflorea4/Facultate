package transport.persistence.repository;

import transport.model.User;

public interface UserRepository extends IRepository<Long, User> {
    User findByUsername(String username);
}
