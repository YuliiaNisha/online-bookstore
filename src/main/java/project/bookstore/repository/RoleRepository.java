package project.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.bookstore.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
