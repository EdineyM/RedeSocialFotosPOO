package data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private Long id;
    private Photo photo;
    private String caption;
    private Usuario author;
    private List<Usuario> likes;
    private LocalDateTime createdAt;
    private static Long nextId = 1L;

    public Post(Photo photo, String caption, Usuario author) {
        this.id = nextId++;
        this.photo = photo;
        this.caption = caption;
        this.author = author;
        this.likes = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Photo getPhoto() {
        return photo;
    }

    public String getCaption() {
        return caption;
    }

    public Usuario getAuthor() {
        return author;
    }

    public List<Usuario> getLikes() {
        return new ArrayList<>(likes);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void addLike(Usuario usuario) {
        if (!likes.contains(usuario)) {
            likes.add(usuario);
        }
    }

    public void removeLike(Usuario usuario) {
        likes.remove(usuario);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Post) {
            Post other = (Post) obj;
            return this.id.equals(other.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}