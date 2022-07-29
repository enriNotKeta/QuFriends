package app.service;

import app.repository.RoleRepository;
import app.repository.UserRepository;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class RecommendationService {
    private DataSource dataSource;

    @Autowired
    public RecommendationService(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    private static final int NEIGHBOR_HOOD_SIZE = 5;

    public Recommender getRecommender() throws Exception {

        JDBCDataModel dataModel = new MySQLJDBCDataModel(
                dataSource, "userhobby", "hobbyfk",
                "userfk", "rating", "null");


        /* Get Pearson correlation instance from given model */
        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);

        /*
         * Computes a neighborhood consisting of the nearest n users to a given
         * user.
         */
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(
                NEIGHBOR_HOOD_SIZE, similarity, dataModel);

        /* Get Recommender */
        Recommender recommender = new GenericUserBasedRecommender(dataModel,
                neighborhood, similarity);


        return recommender;
    }


    public List<RecommendedItem> getRecommendations(
            Recommender recommender, Long userId, int noOfRecommendations)
            throws Exception {
        return recommender.recommend(userId, noOfRecommendations);
    }

    public void displayRecommendations(int userId,
                                              List<RecommendedItem> recommendations) {
        System.out.println("Recommendations for user " + userId + " are:");
        System.out.println("*************************************************");

        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation);
        }

        System.out.println("*************************************************");
    }


}
