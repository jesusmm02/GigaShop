package es.gigashop.DAO;

import static es.gigashop.DAO.ConnectionFactory.dataSource;
import es.gigashop.beans.LineaPedido;
import es.gigashop.beans.Pedido;
import es.gigashop.beans.Producto;
import es.gigashop.beans.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public List<Pedido> obtenerPedidosPorEstadoYUsuario(String estado, int idUsuario) throws Exception {
        List<Pedido> pedidos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String query = "SELECT idPedido, fecha, importe FROM pedidos WHERE estado = ? AND idUsuario = ?";

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, estado);
            stmt.setInt(2, idUsuario);

            rs = stmt.executeQuery();

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setIdPedido(rs.getShort("idPedido"));
                pedido.setFecha(rs.getDate("fecha"));
                pedido.setImporte(rs.getDouble("importe"));
                pedido.setEstado(Pedido.Estado.valueOf(estado.toLowerCase())); // Asignar el estado al pedido
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener los pedidos por estado y usuario", e);
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

        return pedidos;
    }

    /**
     *
     * @param idPedido
     * @return
     */
    @Override
    public List<LineaPedido> obtenerLineasPedido(Short idPedido) {
        if (idPedido == null) {
            throw new IllegalArgumentException("El ID del pedido no puede ser nulo");
        }
        List<LineaPedido> lineas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT lp.idLinea, lp.idProducto, lp.cantidad, "
                + "p.nombre, p.precio, p.marca, p.imagen "
                + "FROM lineaspedidos lp "
                + "JOIN productos p ON lp.idProducto = p.idProducto "
                + "WHERE lp.idPedido = ?";

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

    @Override
    public void actualizarPedido(Pedido pedido) throws SQLException {
    String sql = "UPDATE pedidos SET importe = ?, iva = ? WHERE idPedido = ?";
    try (Connection connection = ConnectionFactory.getConnection();
         PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setDouble(1, pedido.getImporte());
        ps.setDouble(2, pedido.getIva());
        ps.setShort(3, pedido.getIdPedido());
        ps.executeUpdate();
    }
}


    @Override
    public void actualizarEstadoPedido(Pedido pedido) throws SQLException {
        String query = "UPDATE pedidos SET estado = ? WHERE idPedido = ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, pedido.getEstado().name());
            ps.setInt(2, pedido.getIdPedido());
            ps.executeUpdate();
        }
    }

    @Override
    public List<LineaPedido> obtenerLineasPedidosFinalizados(int idUsuario) throws SQLException {
        List<LineaPedido> lineas = new ArrayList<>();
        String query = "SELECT lp.*, p.nombre, p.precio, p.imagen "
                + "FROM lineaspedidos lp "
                + "JOIN productos p ON lp.idProducto = p.idProducto "
                + "JOIN pedidos pe ON lp.idPedido = pe.idPedido "
                + "WHERE pe.idUsuario = ? AND pe.estado = 'f'";
        try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LineaPedido linea = new LineaPedido();
                    Producto producto = new Producto();

                    // Datos del producto
                    producto.setIdProducto(rs.getShort("idProducto"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setPrecio(rs.getDouble("precio"));
                    producto.setImagen(rs.getString("imagen"));

                    // Datos de la línea de pedido
                    linea.setProducto(producto);
                    linea.setCantidad(rs.getByte("cantidad"));

                    lineas.add(linea);
                }
            }
        }
        return lineas;
    }

    @Override
    public boolean eliminarPedido(Short idPedido) {
        String sqlEliminarPedido = "DELETE FROM pedidos WHERE idPedido = ?";
        String sqlEliminarLineasPedido = "DELETE FROM lineaspedidos WHERE idPedido = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmtLineas = conn.prepareStatement(sqlEliminarLineasPedido); PreparedStatement stmtPedido = conn.prepareStatement(sqlEliminarPedido)) {

            // Eliminar las líneas del pedido
            stmtLineas.setShort(1, idPedido);
            stmtLineas.executeUpdate();

            // Eliminar el pedido
            stmtPedido.setShort(1, idPedido);
            int filasAfectadas = stmtPedido.executeUpdate();

            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }

}
