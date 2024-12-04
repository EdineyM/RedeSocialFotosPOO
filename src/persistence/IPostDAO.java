package persistence;

import data.Post;

import java.sql.SQLException;
import java.util.List;

public interface IPostDAO {
    void inserir(Post post) throws SQLException;
    List<Post> buscarPorFoto(int fotoId) throws SQLException;
    void excluir(int id) throws SQLException;
}
