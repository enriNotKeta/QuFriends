package app.repository;


import app.model.Relationship;
import app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    //gets matches, beje per active
    @Query("SELECT r FROM Relationship r WHERE r.userALikesUserB = True AND r.userBLikesUserA = True AND (r.userA.id =:userId OR r.userB.id =:userId)")
    List<Relationship> getMatches(@Param("userId") Long userId);

    Relationship findByUserAAndUserB(User userToUnmatch, User currentUser);

    Relationship findByUserBAndUserA(User userToUnmatch, User currentUser);


}
