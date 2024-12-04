package data;

import persistence.PostDAO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private static int id = 1;
    private Photo photo;
    private String caption;
    private Usuario author;
    private List<Usuario> likes;
    private LocalDateTime createdAt;
    private int likesId;

    private static PostDAO postDAO = new PostDAO();

    public Post(Photo photo, String caption, Usuario author, int likesId) {
        this.photo = photo;
        this.caption = caption;
        this.author = author;
        this.likes = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.likesId = likesId;
    }

    public Post(Photo photo, String caption, Usuario author) {
        this(photo, caption, author, 0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getLikesId() {
        return likesId;
    }

    public void setLikesId(int likesId) {
        this.likesId = likesId;
    }

    public void addLike(Usuario usuario) {
        if (!likes.contains(usuario)) {
            likes.add(usuario);
            postDAO.addLike(this, usuario);
        }
    }

    public void removeLike(Usuario usuario) {
        likes.remove(usuario);
        postDAO.removeLike(this, usuario);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Post) {
            Post other = (Post) obj;
            return this.id == other.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}