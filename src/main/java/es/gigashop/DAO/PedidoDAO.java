package es.gigashop.DAO;

import static es.gigashop.DAO.ConnectionFactory.dataSource;
import es.gigashop.DAOFactory.DAOFactory;
import es.gigashop.beans.LineaPedido;
import es.gigashop.beans.Pedido;
import es.gigashop.beans.Producto;
import es.gigashop.beans.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO implements IPedidoDAO {

    @Override
    public Pedido obtenerPedidoPorUsuario(Short idUsuario) {
        Pedido pedido = null;
        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        String sql = "SELECT * FROM pedidos WHERE idUsuario = ? AND estado = 'c' LIMIT 1";

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            preparada.setShort(1, idUsuario);

            resultado = preparada.executeQuery();

            if (resultado.next()) {
                pedido = new Pedido();
                pedido.setIdPedido(resultado.getShort("idPedido"));
                pedido.setFecha(resultado.getDate("fecha"));
                pedido.setEstado(Pedido.Estado.valueOf(resultado.getString("estado")));
                pedido.setImporte(resultado.getDouble("importe"));
                pedido.setIva(resultado.getDouble("iva"));

                // Puedes completar las líneas de pedido aquí si es necesario
                pedido.setUsuario(new Usuario());
                pedido.getUsuario().setIdUsuario(idUsuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }
                if (preparada != null) {
                    preparada.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return pedido;
    }

    @Override
    public Short insertarPedido(Pedido pedido) {
        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet generatedKeys = null;

        String sql = "INSERT INTO pedidos (fecha, estado, idUsuario, importe, iva) VALUES (?, ?, ?, ?, ?)";
        Short idPedido = null;

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            // Establece los valores en la consulta preparada
            preparada.setDate(1, new java.sql.Date(pedido.getFecha().getTime()));
            preparada.setString(2, pedido.getEstado().name());
            preparada.setShort(3, pedido.getUsuario().getIdUsuario());
            preparada.setDouble(4, pedido.getImporte());
            preparada.setDouble(5, pedido.getIva());

            int filasAfectadas = preparada.executeUpdate();

            // Si se insertó al menos una fila, obtén las claves generadas
            if (filasAfectadas > 0) {
                generatedKeys = preparada.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idPedido = generatedKeys.getShort(1);
                    pedido.setIdPedido(idPedido); // Opcional: Actualiza el pedido con el nuevo ID
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (preparada != null) {
                    preparada.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return idPedido; // Devuelve el ID del pedido o null si no se pudo insertar
    }

    /**
     *
     * @param idPedido
     * @return
     */
    @Override
    public List<LineaPedido> obtenerLineasPedido(Short idPedido) {
        List<LineaPedido> lineas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT lp.idLinea, lp.idProducto, lp.cantidad, " +
                 "p.nombre, p.precio, p.marca, p.imagen " +
                 "FROM lineaspedidos lp " +
                 "JOIN productos p ON lp.idProducto = p.idProducto " +
                 "WHERE lp.idPedido = ?";

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setShort(1, idPedido);
            rs = stmt.executeQuery();

            while (rs.next()) {
                LineaPedido linea = new LineaPedido();
                linea.setIdLinea(rs.getShort("idLinea"));

                Pedido pedido = new Pedido();
                pedido.setIdPedido(idPedido); // Asociar el pedido
                linea.setPedido(pedido);

                Producto producto = new Producto();
                producto.setIdProducto(rs.getShort("idProducto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setMarca(rs.getString("marca"));
                producto.setImagen(rs.getString("imagen"));
                
                linea.setProducto(producto);
                linea.setCantidad(rs.getByte("cantidad"));

                lineas.add(linea);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lineas;
    }

    /*
    @Override
    public void guardarPedido(Pedido pedido) {
        Connection conn = null;
        PreparedStatement stmtPedido = null;
        PreparedStatement stmtLinea = null;

        String sqlPedido = "INSERT INTO pedidos (fecha, estado, usuario_id, importe, iva) VALUES (?, ?, ?, ?, ?)";
        String sqlLinea = "INSERT INTO lineaspedidos (pedido_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); // Transacción

            // Guardar el pedido
            stmtPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            stmtPedido.setDate(1, new java.sql.Date(pedido.getFecha().getTime()));
            stmtPedido.setString(2, pedido.getEstado().name());
            stmtPedido.setObject(3, pedido.getUsuario() != null ? pedido.getUsuario().getIdUsuario() : null);
            stmtPedido.setDouble(4, pedido.getImporte());
            stmtPedido.setDouble(5, pedido.getIva());
            stmtPedido.executeUpdate();

            // Obtener el ID generado
            ResultSet rs = stmtPedido.getGeneratedKeys();
            if (rs.next()) {
                pedido.setIdPedido(rs.getShort(1));
            }

            // Guardar las líneas de pedido
            stmtLinea = conn.prepareStatement(sqlLinea);
            for (LineaPedido linea : pedido.getLineasPedidos()) {
                stmtLinea.setShort(1, pedido.getIdPedido());
                stmtLinea.setShort(2, linea.getProducto().getIdProducto());
                stmtLinea.setInt(3, linea.getCantidad());
                stmtLinea.setDouble(4, linea.getProducto().getPrecio());
                stmtLinea.addBatch(); // Agregar al batch
            }
            stmtLinea.executeBatch(); // Ejecutar batch

            conn.commit(); // Confirmar transacción
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Deshacer cambios en caso de error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (stmtPedido != null) {
                    stmtPedido.close();
                }
                if (stmtLinea != null) {
                    stmtLinea.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
     */
    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }

}
