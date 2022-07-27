package app.repository;


import app.model.UserHobby;
import app.service.UserHobbyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHobbyRepository extends JpaRepository<UserHobby, Long> {
    List<UserHobby> findAllByUserId(Long userId);
}
