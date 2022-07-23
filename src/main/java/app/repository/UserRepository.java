package app.repository;


import app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    public User findByEmail(String email);

    public User findByUsername(String name);

    public User findByResetToken(String reset);

    @Query( value = "SELECT users.user_id, email, job_position, last_name, first_name, password, reset_token, username FROM users inner join user_role on user_role.user_id=users.user_id where role_id=:id", nativeQuery = true )
    public List<User> findByRole(Long id);
}
