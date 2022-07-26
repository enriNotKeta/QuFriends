package app.repository;


import app.model.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    @Query("SELECT ur FROM UserReport ur WHERE (ur.reportedUser.isActive is NULL OR ur.reportedUser.isActive = True) AND (ur.isIncorrect IS NULL OR ur.isIncorrect = False)")
    List<UserReport> getReports();

}
