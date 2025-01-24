package es.gigashop.DAO;

import es.gigashop.beans.LineaPedido;
import es.gigashop.beans.Pedido;
import es.gigashop.beans.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LineaPedidoDAO implements ILineaPedidoDAO {

    @Override
    public LineaPedido obtenerLineaPedido(Short idPedido, Short idProducto) {
        if (idPedido == null || idProducto == null) {
            throw new IllegalArgumentException("Los parámetros idPedido e idProducto no pueden ser null");
        }

        LineaPedido lineaPedido = null;
        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        String sql = "SELECT * FROM lineaspedidos WHERE idPedido = ? AND idProducto = ?";

        try {
            connection = ConnectionFactory.getConnection();
            if (connection == null) {
                throw new SQLException("Conexión a la base de datos fallida");
            }

            preparada = connection.prepareStatement(sql);
            preparada.setShort(1, idPedido);
            preparada.setShort(2, idProducto);

            resultado = preparada.executeQuery();

            if (resultado.next()) {
                lineaPedido = new LineaPedido();
                lineaPedido.setIdLinea(resultado.getShort("idLinea"));

                Pedido pedido = new Pedido();
                pedido.setIdPedido(resultado.getShort("idPedido"));
                lineaPedido.setPedido(pedido);

                // Cargar producto completo
                Producto producto = new ProductoDAO().getProductoPorID(resultado.getShort("idProducto"));
                lineaPedido.setProducto(producto);

                lineaPedido.setCantidad(resultado.getByte("cantidad"));
            }
        } catch (SQLException e) {
            System.err.println("Error en la consulta SQL: " + e.getMessage());
            throw new RuntimeException("Error al consultar línea de pedido", e);
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
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }

        return lineaPedido;
    }

    @Override
    public List<LineaPedido> obtenerLineasPedido(Short idPedido) throws Exception {
        if (idPedido == null) {
            throw new IllegalArgumentException("El ID del pedido no puede ser nulo");
        }

        List<LineaPedido> lineas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String query = "SELECT lp.idLinea, lp.idProducto, lp.cantidad, "
                + "p.nombre, p.precio, p.marca, p.imagen "
                + "FROM lineaspedidos lp "
                + "JOIN productos p ON lp.idProducto = p.idProducto "
                + "WHERE lp.idPedido = ?";

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setShort(1, idPedido);

            rs = stmt.executeQuery();

            while (rs.next()) {
                LineaPedido linea = new LineaPedido();
                linea.setIdLinea(rs.getShort("idLinea"));

                // Asociar pedido al que pertenece la línea
                Pedido pedido = new Pedido();
                pedido.setIdPedido(idPedido);
                linea.setPedido(pedido);

                // Crear producto asociado
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
            throw new Exception("Error al obtener las líneas del pedido", e);
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
    public void insertarOActualizarLineaPedido(LineaPedido lineaPedido) {
        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet rs = null;
        String sqlCheck = "SELECT idLinea FROM lineaspedidos WHERE idPedido = ? AND idProducto = ?";
        String sqlUpdate = "UPDATE lineaspedidos SET cantidad = cantidad + 1 WHERE idPedido = ? AND idProducto = ?";
        String sqlInsert = "INSERT INTO lineaspedidos (idPedido, idProducto, cantidad) VALUES (?, ?, ?)";
        try {
            connection = ConnectionFactory.getConnection();

            // Logs de depuración
            System.out.println("ID Pedido: " + lineaPedido.getPedido().getIdPedido());
            System.out.println("ID Producto: " + lineaPedido.getProducto().getIdProducto());

            preparada = connection.prepareStatement(sqlCheck);
            preparada.setShort(1, lineaPedido.getPedido().getIdPedido());
            preparada.setShort(2, lineaPedido.getProducto().getIdProducto());
            rs = preparada.executeQuery();

            if (rs.next()) {
                System.out.println("Producto encontrado, actualizando cantidad");
                preparada = connection.prepareStatement(sqlUpdate);
                preparada.setShort(1, lineaPedido.getPedido().getIdPedido());
                preparada.setShort(2, lineaPedido.getProducto().getIdProducto());
            } else {
                System.out.println("Producto no encontrado, insertando nuevo");
                preparada = connection.prepareStatement(sqlInsert);
                preparada.setShort(1, lineaPedido.getPedido().getIdPedido());
                preparada.setShort(2, lineaPedido.getProducto().getIdProducto());
                preparada.setByte(3, (byte) 1);
            }
            preparada.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
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
    }

    @Override
    public void eliminarLineaPedido(short idPedido, short idProducto) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            String sql = "DELETE FROM lineaspedidos WHERE idPedido = ? AND idProducto = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setShort(1, idPedido);
            pstmt.setShort(2, idProducto);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar línea de pedido: " + e.getMessage());
            throw new RuntimeException("No se pudo eliminar la línea de pedido", e);
        } finally {
            // Cerrar recursos
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }

}
