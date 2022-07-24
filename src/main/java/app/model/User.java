package app.model;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 4048798961366546485L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull(message = "First Name cannot be empty")
    @Column(name = "first_name")
    private String name;

    @NotNull(message = "Last Name cannot be empty")
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "Email cannot be empty")
    @Email(message = "Please enter a valid email address")
    @Column(name = "email")
    private String email;

    @NotNull(message = "Password cannot be empty")
    @Length(min = 6, message = "Password should be atleast 5 characters long")
    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

    @NotNull(message = "Job Position cannot be empty")
    @Column(name = "job_position")
    private String jobPosition;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(nullable = true, length = 64)
    private String photos;

//    @NotNull
    private LocalDate datePlannedMeeting;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")} )
    private Set<Roles> roles;


    @OneToMany(mappedBy="user", cascade = CascadeType.ALL) //session field at session class
    private List<UserHobby> userHobbies;



//    @OneToMany(mappedBy="user", fetch = FetchType.LAZY)
//    private Set<Board> boards = new HashSet<Board>();

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    //    public String getMobile(){
//        return mobile;
//    }
//    public void setMobile(String mobile){
//        this.mobile = mobile;
//    }
    public Set<Roles> getRoles(){
        return roles;
    }
    public void setRoles(Set<Roles> roles){
        this.roles = roles;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return name + " " + lastName;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    @Transient
    public String getPhotosImagePath() {
        if (photos == null || id == null) return null;

        return "/user-photos/" + id + "/" + photos;
    }


    public List<UserHobby> getUserHobbies() {
        return userHobbies;
    }

}
