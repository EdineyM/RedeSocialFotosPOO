package persistence;

import data.Photo;

import java.sql.SQLException;
import java.util.List;

public interface IPhotoDAO {
    void inserir(Photo Photo) throws SQLException;
    Photo buscarPorId(int id) throws SQLException;
    List<Photo> buscarPorUsuario(int usuarioId) throws SQLException;
    void atualizar(Photo Photo) throws SQLException;
    void excluir(int id) throws SQLException;
}
