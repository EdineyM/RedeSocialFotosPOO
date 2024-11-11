package data;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String username;
    private String password;
    private String fullName;
    private String biography;
    private List<Usuario> following;

    public Usuario(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.biography = "";
        this.following = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getFullName() {
        return fullName;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<Usuario> getFollowing() {
        return new ArrayList<>(following);
    }

    public void addFollowing(Usuario usuario) {
        if (!following.contains(usuario)) {
            following.add(usuario);
        }
    }

    public void removeFollowing(Usuario usuario) {
        following.remove(usuario);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Usuario) {
            Usuario other = (Usuario) obj;
            return this.username.equals(other.username);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}