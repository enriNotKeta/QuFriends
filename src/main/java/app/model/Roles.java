package app.model;

import javax.persistence.*;
import java.util.Set;
@Entity
@Table(name = "roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private int id;

    @Column(name = "role_name")
    private String role;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getRoles(){
        return role;
    }
    public void setRoles(String role){
        this.role = role;
    }

    public Set<User> getUsers(){
        return users;
    }
    public void setUsers(Set<User> users){
        this.users = users;
    }



}
