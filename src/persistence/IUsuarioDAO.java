package persistence;

import data.Usuario;

import java.sql.SQLException;

public interface IUsuarioDAO {
    void inserir(Usuario usuario) throws SQLException;
    Usuario buscarPorId(int id) throws SQLException;
    void atualizar(Usuario usuario) throws SQLException;
    void excluir(int id) throws SQLException;
}
