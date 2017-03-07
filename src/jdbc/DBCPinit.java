package jdbc;

import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class DBCPinit extends HttpServlet {

	@Override
	public void init() throws ServletException {
		super.init();
		loadJDBCDriver();
		initConnectionPool();
	}

	private void loadJDBCDriver() {
		try {
			String driver = getInitParameter("jdbcdriver");
			Class.forName(driver);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void initConnectionPool() {
		
		try {
			String jdbcDriver = getInitParameter("jdbcUrl");
			String dbUser = getInitParameter("dbUser");
			String dbPass = getInitParameter("dbPass");

			ConnectionFactory connFactory = new DriverManagerConnectionFactory(jdbcDriver, dbUser, dbPass);

			PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connFactory, null);
			poolableConnectionFactory.setValidationQuery("select 1");

			
			
			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

			poolConfig.setTimeBetweenEvictionRunsMillis(1000L * 60L * 5L);
			poolConfig.setTestWhileIdle(true);

			poolConfig.setMinIdle(4);
			poolConfig.setMaxTotal(50);

			
			
			GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory,poolConfig);
			poolableConnectionFactory.setPool(connectionPool);

			
			
			Class.forName("org.apache.commons.dbcp2.PoolingDriver");
			PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");

			String poolName=getInitParameter("poolName");
			driver.registerPool(poolName, connectionPool);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
