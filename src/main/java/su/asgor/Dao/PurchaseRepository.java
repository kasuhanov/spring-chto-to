package su.asgor.Dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import su.asgor.model.Purchase;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {

}