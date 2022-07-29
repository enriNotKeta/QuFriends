package app.repository;


import app.model.UserHobby;
import app.service.UserHobbyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHobbyRepository extends JpaRepository<UserHobby, Long> {
    List<UserHobby> findAllByUserId(Long userId);
    UserHobby findByUserIdAndHobbyId(Long userId, Long hobbyId);
    @Query(value = "SELECT COUNT(uh) FROM UserHobby uh WHERE uh.user.id <>:id")
    int otherUsersHaveHobbies(Long id);
    Boolean existsByUserId(Long id);

    @Query(value = "SELECT SUM(uh.rating) FROM UserHobby uh WHERE uh.hobby.id =:hobbyId GROUP BY uh.hobby")
    Double getHobbyTotalRatingById(Long hobbyId);

    @Query(value = "SELECT COUNT(uh.id) FROM UserHobby uh WHERE uh.hobby.id =:hobbyId GROUP BY uh.hobby")
    Double getHobbyCountById(Long hobbyId);
}
