package com.apptpro.apptpro.DAO;

import com.apptpro.apptpro.Models.User;
import org.w3c.dom.UserDataHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO{

    private static final String FIRST_NAME_COLUMN = "First_Name";
    private static final String LAST_NAME_COLUMN = "Last_Name";
    private static final String USER_SALT_COLUMN = "User_Salt";
    private static final String HASHED_PASSWORD = "Hashed_Password";
    private final Connection connection = JDBC.getConnection();
    public static final String USER_NAME_COLUMN = "User_Name";





    /**
     * @deprecated Not suitable for real world authentication. This would best be handled
     * by a third party authenticator, such as Google, Apple, or Facebook
     * @param userName Username of User who needs to be authenticated
     * @return User object of the user in the database, returns null if not found
     */
    public User getUser(String userName) {
        try (Statement st = connection.createStatement()) {
            String query = String.format("SELECT * FROM users WHERE User_Name = \'%1$s\'",userName);
            ResultSet rs = st.executeQuery(query);
            rs.next();
                return new User(
                        rs.getString(USER_NAME_COLUMN),
                        rs.getString(HASHED_PASSWORD),
                        rs.getString(FIRST_NAME_COLUMN),
                        rs.getString(LAST_NAME_COLUMN),
                        rs.getString(USER_SALT_COLUMN)
                );
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public boolean saveUser(User newUser) {
        try (Statement st = connection.createStatement()) {
            String query = String.format("INSERT INTO users (User_Name,Create_Date,Created_By,Last_Update,Last_Updated_By,First_Name,Last_Name,Hashed_Password,User_Salt)\n" +
                    "VALUES ('%1$s',%2$s,'%3$s',%4$s,'%5$s','%6$s','%7$s','%8$s','%9$s')",newUser.getUserName(),"NOW()","SignUpScript","NOW()","SignUpScript",newUser.getFirstName(),newUser.getLastName(),
                    newUser.getUserSaltedPassword(),newUser.getUserSalt());
            int i = st.executeUpdate(query);
            return i > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
