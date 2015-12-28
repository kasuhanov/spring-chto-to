package su.asgor.Dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import su.asgor.model.User;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}