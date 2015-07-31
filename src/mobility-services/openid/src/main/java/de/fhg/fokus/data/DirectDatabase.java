package de.fhg.fokus.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;

import de.fhg.fokus.mdc.propertyProvider.Constants;
import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
import de.fhg.fokus.utils.BCrypt;
import de.fhg.fokus.utils.Util;

@ApplicationScoped
@Alternative
public class DirectDatabase implements DataAccess {
	private static final String DB_URL;
	private static final String DB_USER;
	private static final String DB_PW;
	static {
		Properties props = null;
		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}

		DB_URL = props.getProperty(Constants.CONNECTION_URL_PRIVATE);
		DB_USER = props.getProperty(Constants.CONNECTION_USERNAME_PRIVATE);
		DB_PW = props.getProperty(Constants.CONNECTION_PASSWORD_PRIVATE);
	}

	private Connection getDBConnection() {
		Connection con;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(DB_URL, DB_USER, DB_PW);
			return con;

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DirectDatabase.class.getName());
			lgr.log(Level.INFO, ex.getMessage(), ex);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getUserNameForAuthCode(String authCode) {
		Connection con = getDBConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "SELECT username FROM authcode WHERE auth_code= ?";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, authCode);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

		return null;

	}

	@Override
	public String getUserNameForRefreshToken(String refeshtoken) {
		Connection con = getDBConnection();

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "SELECT username FROM refreshtoken WHERE refresh_token= ?";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, refeshtoken);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}

			return null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	@Override
	public void invalidateAuthCode(String auth_code) {
		Connection con = getDBConnection();

		PreparedStatement preparedStatement = null;
		String sql = "UPDATE authcode SET valid_until='0' WHERE auth_code= ?";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, auth_code);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public boolean isAuthenticatedClient(String client_id, String client_secret) {
		Connection con = getDBConnection();

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "SELECT hashed_client_secret FROM clients WHERE client_id= ?";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, client_id);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return BCrypt.checkpw(client_secret, rs.getString(1));
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public boolean isValidAccessToken(String accesstoken, String scope,
			String username) {
		Connection con = getDBConnection();

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "SELECT valid_until,scope,username FROM accesstoken WHERE access_token= ?";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, accesstoken);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return Util.isStillValid(rs.getLong(1))
						&& isValidScope(scope, rs.getString(2))
						&& username.equals(rs.getString(3));
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public boolean isValidAuthCode(String authcode, String client_id,
			String redirect_uri, String scope) {
		Connection con = getDBConnection();

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "SELECT client_id,valid_until,scope,redirect_uri,username FROM authcode WHERE auth_code= ?";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, authcode);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				if (Util.isStillValid(rs.getLong(2))
						&& rs.getString(1).equals(client_id)
						&& rs.getString(4).equals(redirect_uri)
						&& isValidScope(scope, rs.getString(3))) {
					return true;
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public boolean isValidClientId(String client_id) {
		Connection con = getDBConnection();

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "SELECT client_id FROM clients WHERE client_id= ?";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, client_id);
			rs = preparedStatement.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public boolean isValidRefreshToken(String client_id, String refresh_token,
			String scope) {
		Connection con = getDBConnection();

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "SELECT client_id,valid_until,scope FROM refreshtoken WHERE refresh_token= ?";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, refresh_token);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return Util.isStillValid(rs.getLong(2))
						&& rs.getString(1).equals(client_id)
						&& isValidScope(scope, rs.getString(3));

			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public boolean isValidScope(String givenScope, String dbScope) {
		if (givenScope == null || givenScope.isEmpty()) {
			return false;
		}
		// create set of space separated string
		Set<String> givenScopesSet = OAuthUtils.decodeScopes(givenScope);
		Set<String> dbScopesSet = OAuthUtils.decodeScopes(dbScope);

		for (String scope : givenScopesSet) {
			// check if value is specified in DB
			if (!isValidScope(scope)) {
				return false;
			}
		}
		// check if dbScopesSet is a superset from givenScopesSet
		return dbScopesSet.containsAll(givenScopesSet);
	}

	public boolean isValidScope(String givenScope) {
		Connection con = getDBConnection();

		Set<String> givenScopesSet = OAuthUtils.decodeScopes(givenScope);

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "SELECT name FROM scopes WHERE name= ?";
		try {
			for (String scope : givenScopesSet) {
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, scope);
				rs = preparedStatement.executeQuery();
				if (!rs.next()) {
					return false;
				}
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public void saveAccessToken(String access_token, String scope,
			String username) {
		Connection con = getDBConnection();

		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO accesstoken (access_token,valid_until,scope,username) VALUES (?,?,?,?)";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, access_token);
			preparedStatement.setString(2,
					String.valueOf(Util.computeExpiryDate(3600l)));
			preparedStatement.setString(3, scope);
			preparedStatement.setString(4, username);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void saveAuthCode(String authCode, String client_id,
			String redirect_uri, String scope, String username) {
		Connection con = getDBConnection();

		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO authcode (client_id,auth_code,valid_until,scope,redirect_uri,username) VALUES (?,?,?,?,?,?)";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, client_id);
			preparedStatement.setString(2, authCode);
			preparedStatement.setString(3,
					String.valueOf(Util.computeExpiryDate(600l)));
			preparedStatement.setString(4, scope);
			preparedStatement.setString(5, redirect_uri);
			preparedStatement.setString(6, username);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void saveClient(String client_id, String client_secret,
			String client_name, String client_url, String client_description,
			String redirect_uri) {
		Connection con = getDBConnection();

		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO clients (client_id,hashed_client_secret,client_name,client_url, client_description,redirect_uri) VALUES (?,?,?,?,?,?)";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, client_id);
			preparedStatement.setString(2,
					BCrypt.hashpw(client_secret, BCrypt.gensalt()));
			preparedStatement.setString(3, client_name);
			preparedStatement.setString(4, client_url);
			preparedStatement.setString(5, client_description);
			preparedStatement.setString(6, redirect_uri);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void saveRefreshToken(String refreshToken, String client_id,
			String scope, String username) {
		Connection con = getDBConnection();

		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO refreshtoken (client_id,refresh_token,valid_until,scope,username) VALUES (?,?,?,?,?)";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, client_id);
			preparedStatement.setString(2, refreshToken);
			preparedStatement.setString(3,
					String.valueOf(Util.computeExpiryDate(15811200l)));
			preparedStatement.setString(4, scope);
			preparedStatement.setString(5, username);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public Map<String, String> getDescriptionForScope(String givenScope) {
		Map<String, String> toReturn = new HashMap<String, String>();

		Set<String> givenScopesSet = OAuthUtils.decodeScopes(givenScope);
		Connection con = getDBConnection();

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "SELECT description FROM scopes WHERE name= ?";
		try {
			for (String scope : givenScopesSet) {
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, scope);
				rs = preparedStatement.executeQuery();
				if (rs.next()) {
					String description = rs.getString(1);
					toReturn.put(scope, description);
				}
			}
			return toReturn;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
