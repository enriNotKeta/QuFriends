package app.service;

import app.model.Hobby;
import app.model.Report;
import app.model.User;
import app.model.UserHobby;
import app.repository.UserHobbyRepository;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

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


    public Map<Hobby,Double> getAvgRatingPerHobby() {
        List<Hobby> hobbies = hobbyService.findAll();
        HashMap<Hobby,Double> mapHobbyAvgRating =new HashMap<Hobby,Double>();//Creating HashMap

        Set<User> users = userService.getUsersWithHobbiesToRecommend();

        for (Hobby hobby : hobbies) {
            Double hobbyTotalRating = userHobbyRepository.getHobbyTotalRatingById(hobby.getId());
            Double hobbyCount = userHobbyRepository.getHobbyCountById(hobby.getId());
            DecimalFormat df = new DecimalFormat("####0.00");
            Double hobbyAvgRating = hobbyTotalRating/hobbyCount;
            mapHobbyAvgRating.put(hobby, Double.valueOf(df.format(hobbyAvgRating)));
            System.out.println(mapHobbyAvgRating.get(hobby) + ", hobby");
        }

        Map<Hobby,Double> orderedHobbyAvgs =
                mapHobbyAvgRating.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return orderedHobbyAvgs;

    }
}
