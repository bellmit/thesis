package platform.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name= "DPUSER")
public class DPUser  implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dpuser_id")
    private long id;
    private String userName;
    private String password;
    private String givenName;
    private String surname;
    private String email;


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "mDPUsers")
    private Set<Role> roles;


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "projectMembers")
    private List<Project> projects;

    @ManyToOne
    @JoinColumn(name = "usertype_id")
    private UserType usertype;



    public DPUser() {
    }

    public DPUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public DPUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String userName, String password1) {
        this.userName = userName;
        this.password = password1;
    }

    public DPUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, String userName, String password1, String givenName, String surname, String email, Set<Role> roles, List<Project> projects) {
        this.userName = userName;
        this.password = password1;
        this.givenName = givenName;
        this.surname = surname;
        this.email = email;
        this.roles = roles;
        this.projects = projects;
    }

    public DPUser(String userName, String givenName, String surname) {
        this.userName = userName;
        this.givenName = givenName;
        this.surname = surname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public UserType getUsertype() {
        return usertype;
    }

    public void setUsertype(UserType usertype) {
        this.usertype = usertype;
    }
}
