package br.com.welissontiago.IntegrationTests.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Objects;

@XmlRootElement
public class UserCredentialsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String fullname;


    public UserCredentialsDTO() {
    }

    public UserCredentialsDTO(String username, String password, String fullname) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
    }

    public UserCredentialsDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserCredentialsDTO that)) return false;
        return Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getFullname(), that.getFullname()) && Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getFullname(), getPassword());
    }
}
