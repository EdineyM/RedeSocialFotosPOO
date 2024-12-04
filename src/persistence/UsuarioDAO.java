package persistence;

import data.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements IUsuarioDAO {
    @Override
    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (username, fullname, password, biography) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, usuario.getUsername());
            preparedStatement.setString(2, usuario.getFullName());
            preparedStatement.setString(3, usuario.getPassword());
            preparedStatement.setString(4, usuario.getBiography());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    usuario.setId(resultSet.getInt("id"));
                }
            }
        }
    }

    @Override
    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Usuario usuario = new Usuario(resultSet.getString("username"), resultSet.getString("fullname"), resultSet.getString("password"));
                    usuario.setId(resultSet.getInt("id"));
                    usuario.setBiography(resultSet.getString("biography"));
                    usuario.setFollowing(buscarSeguindo(usuario.getId()));
                    return usuario;
                }
            }
        }

        return null;
    }

    @Override
    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET username = ?, fullname = ?, password = ?, biography = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, usuario.getUsername());
            preparedStatement.setString(2, usuario.getFullName());
            preparedStatement.setString(3, usuario.getPassword());
            preparedStatement.setString(4, usuario.getBiography());
            preparedStatement.setInt(5, usuario.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        }
    }

    public static List<Usuario> buscarSeguindo(int usuarioId) throws SQLException {
        String sql = "SELECT u.* FROM usuarios u JOIN user_following uf ON u.id = uf.following_id WHERE uf.user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, usuarioId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Usuario> usuarios = new ArrayList<>();

                while (resultSet.next()) {
                    Usuario usuario = new Usuario(resultSet.getString("username"), resultSet.getString("fullname"), resultSet.getString("password"));
                    usuario.setId(resultSet.getInt("id"));
                    usuario.setBiography(resultSet.getString("biography"));
                    usuarios.add(usuario);
                }

                return usuarios;
            }
        }
    }
}