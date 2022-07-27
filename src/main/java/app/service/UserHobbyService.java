package app.service;

import app.model.Report;
import app.model.UserHobby;
import app.repository.UserHobbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserHobbyService {

    private final UserService userService;
    private final UserHobbyRepository userHobbyRepository;

    @Autowired
    public UserHobbyService(UserService userService, UserHobbyRepository userHobbyRepository) {
        this.userService = userService;
        this.userHobbyRepository = userHobbyRepository;

    }

    public List<UserHobby> findAllByUserId(Long userId) {
        List<UserHobby> userHobbies = userHobbyRepository.findAllByUserId(userId);
        return userHobbies;
    }
}
