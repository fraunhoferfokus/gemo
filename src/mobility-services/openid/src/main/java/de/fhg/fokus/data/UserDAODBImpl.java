package de.fhg.fokus.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.fhg.fokus.mdc.propertyProvider.Constants;
import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
import de.fhg.fokus.utils.BCrypt;
import de.fhg.fokus.utils.Util;

public class UserDAODBImpl implements UserDAO {
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

		DB_URL = props.getProperty(Constants.CONNECTION_URL);
		DB_USER = props.getProperty(Constants.CONNECTION_USERNAME);
		DB_PW = props.getProperty(Constants.CONNECTION_PASSWORD);
	}

	@Override
	public User getUser(String username) {
		Connection con = getUserDBConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		String sql = "SELECT firstname,lastname,username,password,email,birthdate,street,housenr,"
				+ "postalcode,location,phonenumber,bankaccountowner,bankaccountnumber,bankcode,preferences,"
				+ "vehicletype,publictransportaffinity,drivinglicenseclass,drivinglicensedate,drivinglicenselocation,drivinglicenseid,"
				+ "lang,creditcardnumber,termsandconditionsdate FROM users WHERE username= ?";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, username);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return new User(rs.getString(1), rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8),
						rs.getString(9), rs.getString(10), rs.getString(11),
						rs.getString(12), rs.getString(13), rs.getString(14),
						rs.getString(15), rs.getString(16), rs.getString(17),
						rs.getString(18), rs.getString(19), rs.getString(20),
						rs.getString(21), rs.getString(22), rs.getString(23),
						rs.getString(24));
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
		return null;
	}

	@Override
	public void saveUser(User user) {
		Connection con = getUserDBConnection();
		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO users (firstname,lastname,username,password,email,birthdate,street,housenr,"
				+ "postalcode,location,phonenumber,bankaccountowner,bankaccountnumber,bankcode,preferences,"
				+ "vehicletype,publictransportaffinity,drivinglicenseclass,drivinglicensedate,drivinglicenselocation,drivinglicenseid,"
				+ "lang,creditcardnumber,termsandconditionsdate) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, user.getFirstName());
			preparedStatement.setString(2, user.getLastName());
			preparedStatement.setString(3, user.getUsername());
			preparedStatement.setString(4, user.getHashedPassword());
			preparedStatement.setString(5, user.getEmailAddress());
			preparedStatement.setString(6, user.getDateOfBirth());
			preparedStatement.setString(7, user.getStreet());
			preparedStatement.setString(8, user.getHousenr());
			preparedStatement.setString(9, user.getPostalcode());
			preparedStatement.setString(10, user.getLocation());
			preparedStatement.setString(11, user.getPhonenumber());
			preparedStatement.setString(12, user.getBankaccountowner());
			preparedStatement.setString(13, user.getBankaccountnumber());
			preparedStatement.setString(14, user.getBankcode());
			preparedStatement.setString(15, user.getPreferences());
			preparedStatement.setString(16, user.getVehicletype());
			preparedStatement.setString(17, user.getPublictransportaffinity());
			preparedStatement.setString(18, user.getDrivinglicenseclass());
			preparedStatement.setString(19, user.getDrivinglicensedate());
			preparedStatement.setString(20, user.getDrivinglicenselocation());
			preparedStatement.setString(21, user.getDrivinglicenseid());
			preparedStatement.setString(22, user.getLang());
			preparedStatement.setString(23, user.getCreditcardnumber());
			preparedStatement.setString(24, user.getTermsandconditionsdate());
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
	public boolean isValidUser(String openid, String password) {
		User userFromDB = getUser(Util.extractUserName(openid));
		if (userFromDB == null) {
			return false;
		}
		return BCrypt.checkpw(password, userFromDB.getHashedPassword());
	}

	@Override
	public boolean doesUserExist(User user) {
		Connection con = getUserDBConnection();

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "SELECT username FROM users WHERE username= ?";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, user.getUsername());
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

	private Connection getUserDBConnection() {
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
}
