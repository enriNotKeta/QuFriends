package app.model;

import javax.persistence.*;

@Entity
@Table(name = "userreport")
public class UserReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_user", nullable = false)
    private User reporterUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user", nullable = false)
    private User reportedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportFK", nullable = false)
    private Report report;

    public Long getId() {
        return id;
    }

    public User getReporterUser() {
        return reporterUser;
    }

    public void setReporterUser(User reporterUser) {
        this.reporterUser = reporterUser;
    }

    public User getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
