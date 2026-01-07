package pl.kiszkiel.realpay.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import pl.kiszkiel.realpay.paper.RealPay;

import java.sql.*;

public class Database {
    private final HikariDataSource source;

    public Database(String host, int port, String db, String user, String pass) {
        HikariConfig conf = new HikariConfig();
        conf.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false&autoReconnect=true");
        conf.setUsername(user);
        conf.setPassword(pass);
        conf.setMaximumPoolSize(5);
        conf.setMinimumIdle(1);
        source = new HikariDataSource(conf);
        initializeTable();
    }

    public void initializeTable() {
        try (Connection conn = source.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "realpay", null);
            if (tables.next()) return;

            conn.createStatement().execute("CREATE TABLE realpay (username VARCHAR(36) not null, balance DOUBLE, PRIMARY KEY ( username ))");
            RealPay.LOGGER.info("Balances table named \"realpay\" has created");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        source.close();
    }

    public void update(String sql, Object @NotNull ... params) {
        try (Connection conn = source.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                st.setObject(i + 1, params[i]);

            st.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public <T> T query(String sql, ResultMapper<T> mapper, Object @NotNull ... params) {
        try (Connection conn = source.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++)
                st.setObject(i + 1, params[i]);

            try (ResultSet rs = st.executeQuery()) {
                return mapper.map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface ResultMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}
