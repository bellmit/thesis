package platform.modelResults;

import platform.model.DPUser;

import java.io.Serializable;

public class ProjectMember implements Serializable {
    private long id;
    private String givenName;
    private String surname;
    private String roles;

    public ProjectMember(DPUser dpUser, String roles) {
        this.id = dpUser.getId();
        this.givenName = dpUser.getGivenName();
        this.surname = dpUser.getSurname();
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
