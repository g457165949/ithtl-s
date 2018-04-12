package com.ven.repo;

import com.ven.model.account.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ExtJpaRepository<User,Integer>,JpaSpecificationExecutor {

	User findByUsername(String name);

	User dynamicSave(Integer id, User user);
}
