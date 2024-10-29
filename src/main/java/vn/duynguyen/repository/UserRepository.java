package vn.duynguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.duynguyen.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
