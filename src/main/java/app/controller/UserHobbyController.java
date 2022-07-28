package app.controller;

import app.model.Hobby;
import app.model.UserHobby;
import app.service.HobbyService;
import app.service.UserHobbyService;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:6060")
public class UserHobbyController {

    private final UserService userService;
    private final UserHobbyService userHobbyService;

    @Autowired
    public UserHobbyController(UserService userService ,UserHobbyService userHobbyService) {
        this.userService = userService;
        this.userHobbyService = userHobbyService;
    }

    @PostMapping(value = "/userhobby/save/hobby/{hobbyId}/rating/{hobbyRating}")
    public void addUserHobby(@RequestBody UserHobby userHobby ,
                             @PathVariable("hobbyId") String hobbyId, @PathVariable("hobbyRating") String hobbyRating){
        Long hId = Long.valueOf(hobbyId).longValue();
        Integer ratingId = Integer.valueOf(hobbyRating).intValue();

        userHobbyService.updateOrSave(hId, ratingId);
    }


}
