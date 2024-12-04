package persistence;

import data.Photo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoDAO {
    public void inserir(Photo photo) throws SQLException {
        String sql = "INSERT INTO photo (photo_url, description, user_id) VALUES (?, ?, ?) RETURNING id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, photo.getFilename());
            preparedStatement.setString(2, ""); // Assuming description is empty for now
            preparedStatement.setInt(3, photo.getUserId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    photo.setId(resultSet.getInt("id"));
                }
            }
        }
    }

    public Photo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM photo WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Photo(resultSet.getBytes("photo_url"), resultSet.getString("description"), resultSet.getInt("user_id"));
                }
            }
        }

        return null;
    }

    public void atualizar(Photo photo) throws SQLException {
        String sql = "UPDATE photo SET photo_url = ?, description = ?, user_id = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, photo.getFilename());
            preparedStatement.setString(2, ""); // Assuming description is empty for now
            preparedStatement.setInt(3, photo.getUserId());
            preparedStatement.setInt(4, photo.getId());

            preparedStatement.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM photo WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        }
    }
}