package su.asgor.Dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import su.asgor.model.Customer;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

}