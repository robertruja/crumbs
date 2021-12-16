package org.crumbs.jdbc.connector;

import org.crumbs.core.annotation.Crumb;
import org.crumbs.core.annotation.Property;
import org.crumbs.core.logging.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;

@Crumb
public class JdbcConnector {

    private Logger LOG = Logger.getLogger(JdbcConnector.class);

    private static final int CONNECTION_TTL_SECONDS = 300; // 5 minutes idle time

    @Property("crumbs.jdbc.driver-class")
    private String driverClass;
    @Property("crumbs.jdbc.url")
    private String url;
    @Property("crumbs.jdbc.username")
    private String username;
    @Property("crumbs.jdbc.password")
    private String password;
    @Property("crumbs.jdbc.connection-ttl")
    private Integer connectionTTL;

    private boolean driverInitialized;

    private void initDriver() {
        if(!driverInitialized) {
            LOG.debug("Connection settings class {}, url {}, username {}, password {}",
                    driverClass, url, username, password);
            try {
                Class.forName(driverClass);
                driverInitialized = true;
            } catch (Exception ex) {
                throw new RuntimeException("Could not load driver class " + driverClass);
            }
        }
    }

    private ConnectionHolder connectionHolder = new ConnectionHolder();

    public AutoCloseableConnection getConnection() throws SQLException {
        initDriver();
        AutoCloseableConnection autoCloseableConnection = connectionHolder.getAutoCloseableConnection();
        if(autoCloseableConnection == null || autoCloseableConnection.isClosed()) {
            LOG.debug("Connection not created or expired, creating a new one");
            connectionHolder.setAutoCloseableConnection(
                    new AutoCloseableConnection(DriverManager.getConnection(url, username, password),
                            connectionTTL != null ? connectionTTL :CONNECTION_TTL_SECONDS));
        }
        return connectionHolder.getAutoCloseableConnection();
    }

    private static class ConnectionHolder {
        private AutoCloseableConnection autoCloseableConnection;

        public AutoCloseableConnection getAutoCloseableConnection() {
            return autoCloseableConnection;
        }

        public void setAutoCloseableConnection(AutoCloseableConnection autoCloseableConnection) {
            this.autoCloseableConnection = autoCloseableConnection;
        }
    }
}
