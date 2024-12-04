package persistence;

import data.Post;
import data.Photo;
import data.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
    public void inserir(Post post) throws SQLException {
        String sql = "INSERT INTO post (caption, likes_id, user_id, photo_id) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, post.getCaption());
            preparedStatement.setInt(2, 0); // Assuming likes_id is 0 for now
            preparedStatement.setInt(3, post.getAuthor().getId());
            preparedStatement.setInt(4, post.getPhoto().getId()); //TODO: Corrigir esse sql e mudar para Blob o tipo de dado

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    post.setId(resultSet.getInt("id"));
                }
            }
        }
    }

    public Post buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM post WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Photo photo = new PhotoDAO().buscarPorId(resultSet.getInt("photo_id"));
                    Usuario author = new UsuarioDAO().buscarPorId(resultSet.getInt("user_id"));
                    return new Post(photo, resultSet.getString("caption"), author);
                }
            }
        }

        return null;
    }

    public void atualizar(Post post) throws SQLException {
        String sql = "UPDATE post SET caption = ?, likes_id = ?, user_id = ?, photo_id = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, post.getCaption());
            preparedStatement.setInt(2, 0); // Assuming likes_id is 0 for now
            preparedStatement.setInt(3, post.getAuthor().getId());
            preparedStatement.setInt(4, post.getPhoto().getId());
            preparedStatement.setInt(5, post.getId());

            preparedStatement.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM post WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        }
    }

    public List<Post> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM post";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Post> posts = new ArrayList<>();

            while (resultSet.next()) {
                Photo photo = new PhotoDAO().buscarPorId(resultSet.getInt("photo_id"));
                Usuario author = new UsuarioDAO().buscarPorId(resultSet.getInt("user_id"));
                Post post = new Post(photo, resultSet.getString("caption"), author);
                post.setId(resultSet.getInt("id"));
                posts.add(post);
            }

            return posts;
        }
    }

    public void addLike(Post post, Usuario usuario) {
        String sql = "INSERT INTO likes (post_id, user_id) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, post.getId());
            preparedStatement.setInt(2, usuario.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeLike(Post post, Usuario usuario) {
        String sql = "DELETE FROM likes WHERE post_id = ? AND user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, post.getId());
            preparedStatement.setInt(2, usuario.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}