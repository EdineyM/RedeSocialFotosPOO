package business;

import data.Usuario;
import data.Post;
import data.Photo;
import persistence.PhotoDAO;
import persistence.PostDAO;
import persistence.UsuarioDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sistema {
    private UsuarioDAO usuarioDAO;
    private PostDAO postDAO;
    private PhotoDAO photoDAO;
    private List<Usuario> usuarios;
    private List<Post> posts;

    public Sistema() {
        this.usuarios = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.photoDAO = new PhotoDAO();
        connectDB();
    }

    public void connectDB() {
        usuarioDAO = new UsuarioDAO();
        postDAO = new PostDAO();
        photoDAO = new PhotoDAO();
        try {
            usuarios = usuarioDAO.buscarTodos();
            posts = postDAO.buscarTodos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario cadastrarUsuario(String username, String password, String fullName) throws Exception {
        if (buscarUsuario(username) != null) {
            throw new Exception("Username já existe");
        }

        Usuario novoUsuario = new Usuario(username, password, fullName);
        usuarios.add(novoUsuario);

        usuarioDAO = new UsuarioDAO();
        usuarioDAO.inserir(novoUsuario);

        return novoUsuario;
    }

    public Usuario login(String username, String password) throws Exception {
        usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        /*usuario = buscarUsuario(username);*/
        if (usuario == null) {
            throw new Exception("Usuário não encontrado");
        }

        if (!usuario.checkPassword(password)) {
            throw new Exception("Senha incorreta");
        }

        return usuario;
    }

    public Post criarPost(Photo photo, String caption, Usuario author) throws Exception {
        if (!usuarios.contains(author)) {
            throw new Exception("Usuário não encontrado no sistema");
        }

        Post novoPost = new Post(photo, caption, author);
        posts.add(novoPost);

        photoDAO.inserir(photo);

        postDAO.inserir(novoPost);

        return novoPost;
    }

    public List<Post> getFeedUsuario(Usuario usuario) throws SQLException {
        PostDAO postDAO = new PostDAO();
        List<Post> posts = postDAO.buscarTodos();

        return posts.stream()
                .filter(post -> usuario.getFollowing().contains(post.getAuthor()) || post.getAuthor().equals(usuario))
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public Usuario buscarUsuario(String username) throws SQLException {
        usuarioDAO = new UsuarioDAO();
        return usuarioDAO.buscarPorUsername(username);
        /*return usuarios.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
         */
    }

    public void seguirUsuario(Usuario seguidor, Usuario seguido) throws Exception {
        if (!usuarios.contains(seguidor) || !usuarios.contains(seguido)) {
            throw new Exception("Usuário não encontrado no sistema");
        }

        seguidor.addFollowing(seguido);
    }

    public void pararSeguirUsuario(Usuario seguidor, Usuario seguido) throws Exception {
        if (!usuarios.contains(seguidor) || !usuarios.contains(seguido)) {
            throw new Exception("Usuário não encontrado no sistema");
        }

        seguidor.removeFollowing(seguido);
    }

    public void curtirPost(Usuario usuario, Post post) throws Exception {
        if (!usuarios.contains(usuario)) {
            throw new Exception("Usuário não encontrado no sistema");
        }
        if (!posts.contains(post)) {
            throw new Exception("Post não encontrado no sistema");
        }

        post.addLike(usuario);
    }

    public void descurtirPost(Usuario usuario, Post post) throws Exception {
        if (!usuarios.contains(usuario)) {
            throw new Exception("Usuário não encontrado no sistema");
        }
        if (!posts.contains(post)) {
            throw new Exception("Post não encontrado no sistema");
        }

        post.removeLike(usuario);
    }
}