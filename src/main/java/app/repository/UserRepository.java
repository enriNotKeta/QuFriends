package app.repository;


import app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);

    User findByUsername(String name);

    User findByResetToken(String reset);

    @Query(value = "SELECT users.user_id, email, job_position, last_name, first_name, password, reset_token, username FROM users inner join user_role on user_role.user_id=users.user_id where role_id=:id", nativeQuery = true)
    List<User> findByRole(Long id);

    @Query(value = "SELECT DISTINCT (u) FROM User u  WHERE u.id <>:userId " +
            "AND u NOT IN :adminUsers " +
            "AND u.id NOT IN (SELECT r.userB FROM Relationship r WHERE r.userA.id =:userId)" +
            "AND u.id NOT IN (SELECT r.userA FROM Relationship r WHERE r.userB.id =:userId)")
    List<User> getAllUsersNotInRelationships(Long userId, @Param("adminUsers") List<User> adminUsers);


    @Query(value = "SELECT u FROM User u  WHERE u.id <>:userId " +
            "AND u IN (SELECT uh.user from UserHobby uh GROUP BY uh.user.id)" +
            " AND u NOT IN (SELECT r.userA FROM Relationship r WHERE r.userB.id =:userId) " +
            "AND u NOT IN (SELECT r.userB FROM Relationship r WHERE r.userA.id =:userId)")
    Set<User> getUsersWithHobbiesToRecommend(Long userId);

    @Query(value = "SELECT * FROM users WHERE user_id NOT IN " +
                    "(SELECT user_role.user_id from user_role WHERE user_role.role_id !=2)", nativeQuery = true)
    List<User> getAdminUsers();

    @Query(value = "SELECT u FROM User u  WHERE u IN (SELECT r.userA FROM Relationship r WHERE r.userB.id =:userId " +
            "AND r.userALikesUserB = True AND r.userBLikesUserA = False)" +
            "OR u IN (SELECT r.userB FROM Relationship r WHERE r.userA.id =:userId " +
            "AND r.userBLikesUserA = True AND r.userALikesUserB = False)")
    List<User> getRequestingUsers(Long userId);
}
