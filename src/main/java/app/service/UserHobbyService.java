package app.service;

import app.model.Report;
import app.model.User;
import app.model.UserHobby;
import app.repository.UserHobbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserHobbyService {

    private final UserService userService;
    private final UserHobbyRepository userHobbyRepository;
    private final HobbyService hobbyService;

    @Autowired
    public UserHobbyService(UserService userService, UserHobbyRepository userHobbyRepository,
                            HobbyService hobbyService) {
        this.userService = userService;
        this.userHobbyRepository = userHobbyRepository;
        this.hobbyService = hobbyService;

    }

    public List<UserHobby> findAllByUserId(Long userId) {
        List<UserHobby> userHobbies = userHobbyRepository.findAllByUserId(userId);
        return userHobbies;
    }

    public UserHobby updateOrSave(Long hobbyId, int rating) {
        UserHobby userHobby = userHobbyRepository.findByUserIdAndHobbyId(userService.getCurrentUser().getId(), hobbyId);
        if (userHobby == null) {
            userHobby = new UserHobby();
            userHobby.setUser(userService.getCurrentUser());
            userHobby.setHobby(hobbyService.findById(hobbyId));
        }

        userHobby.setRating(rating);

        return userHobbyRepository.save(userHobby);
    }
}
