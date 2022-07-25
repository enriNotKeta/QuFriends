package app.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "relationship")
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean userALikesUserB;

    private Boolean userBLikesUserA;

    private Boolean userAUnmatchesUserB;

    private Boolean userBUnmatchesUserA;

    private Boolean userABlocksUserB;

    private Boolean userBBlocksUserA;

    @ManyToOne(fetch = FetchType.LAZY) //join cols?
    @JoinColumn(name = "userA", nullable = true)
    private User userA;

    @ManyToOne(fetch = FetchType.LAZY) //join cols?
    @JoinColumn(name = "userB", nullable = true)
    private User userB;

    public Long getId() {
        return id;
    }

    public Boolean getUserALikesUserB() {
        return userALikesUserB;
    }

    public void setUserALikesUserB(Boolean userALikesUserB) {
        this.userALikesUserB = userALikesUserB;
    }

    public Boolean getUserBLikesUserA() {
        return userBLikesUserA;
    }

    public void setUserBLikesUserA(Boolean userBLikesUserA) {
        this.userBLikesUserA = userBLikesUserA;
    }

    public Boolean getUserAUnmatchesUserB() {
        return userAUnmatchesUserB;
    }

    public void setUserAUnmatchesUserB(Boolean userAUnmatchesUserB) {
        this.userAUnmatchesUserB = userAUnmatchesUserB;
    }

    public Boolean getUserBUnmatchesUserA() {
        return userBUnmatchesUserA;
    }

    public void setUserBUnmatchesUserA(Boolean userBUnmatchesUserA) {
        this.userBUnmatchesUserA = userBUnmatchesUserA;
    }

    public Boolean getUserABlocksUserB() {
        return userABlocksUserB;
    }

    public void setUserABlocksUserB(Boolean userABlocksUserB) {
        this.userABlocksUserB = userABlocksUserB;
    }

    public Boolean getUserBBlocksUserA() {
        return userBBlocksUserA;
    }

    public void setUserBBlocksUserA(Boolean userBBlocksUserA) {
        this.userBBlocksUserA = userBBlocksUserA;
    }

    public User getUserA() {
        return userA;
    }

    public void setUserA(User userA) {
        this.userA = userA;
    }

    public User getUserB() {
        return userB;
    }

    public void setUserB(User userB) {
        this.userB = userB;
    }


}