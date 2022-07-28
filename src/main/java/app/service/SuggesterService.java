package app.service;

import app.model.Hobby;
import app.model.User;
import app.model.UserHobby;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class SuggesterService {
    private final UserService userService;
    private final UserHobbyService userHobbyService;
    private final int HOBBY_COUNT = 10;
    private final int LIMIT_USERS_NUMBER = 40;

    @Autowired
    public SuggesterService(UserService userService, UserHobbyService userHobbyService) {
        this.userService = userService;
        this.userHobbyService = userHobbyService;

    }

    public List<User> getUsersToRecommend() {
        double ratingOfCurrUser[] = getHobbyRatingsOfCurrentUser();
        HashMap<User,Double> mapSimilarityVals = getHobbyRatingsOfOtherUsers(ratingOfCurrUser);//Creating HashMap
        List<User> suggestedUsersOrdered = getSortedSuggestedUsers(mapSimilarityVals);

        return suggestedUsersOrdered;
    }

    public double[] getHobbyRatingsOfCurrentUser() {
        User currentUser = userService.getCurrentUser();
        List<UserHobby> userHobbies = userHobbyService.findAllByUserId(currentUser.getId());
        double ratingOfCurrUser[] = new double[HOBBY_COUNT];
        int counter = 0;
        for (UserHobby userHobby : userHobbies) {
            System.out.println("userHobby " + userHobbies.size());
            ratingOfCurrUser[counter] = userHobby.getRating();
            counter++;
            System.out.println("ratingOfCurrUser " + ratingOfCurrUser[counter-1]);

        }
        System.out.println("here  " + ratingOfCurrUser);
        return ratingOfCurrUser;

    }


    public HashMap<User,Double> getHobbyRatingsOfOtherUsers(double[] ratingOfCurrUser) {
        Set<User> users = userService.getUsersWithHobbiesToRecommend();
        HashMap<User,Double> mapSimilarityVals =new HashMap<User,Double>();//Creating HashMap

        for (User user : users) {
            double ratingOfNewUser[] = new double[HOBBY_COUNT];
            int counter = 0;
            List<UserHobby> userHobbyList = userHobbyService.findAllByUserId(user.getId());
            for (UserHobby userHobby : userHobbyList) {
                ratingOfNewUser[counter] = userHobby.getRating();
                counter++;
            }
            double corr = new PearsonsCorrelation().correlation(ratingOfNewUser, ratingOfCurrUser);
            mapSimilarityVals.put(user, corr);
            System.out.println("mapSimilarityVals " + user.getUsername() + ", " + corr);
        }
        return mapSimilarityVals;

    }

    public List<User> getSortedSuggestedUsers(HashMap<User,Double> mapSimilarityVals) {
        Map<User,Double> topMatches =
                mapSimilarityVals.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(LIMIT_USERS_NUMBER)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        List<User> suggestedUsersOrdered = new ArrayList<>();
        for (var entry : topMatches.entrySet()) {
            System.out.println(entry.getKey().getUsername() + "/" + entry.getValue());
            suggestedUsersOrdered.add(entry.getKey());
        }

        for (User u : suggestedUsersOrdered) {
            System.out.println(u.getUsername() + ", unordered");
        }

        return suggestedUsersOrdered;
    }






}
