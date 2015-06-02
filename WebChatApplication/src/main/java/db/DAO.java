package db;

import db.DAOException;
import dbaccess.MessagesEntity;
import dbaccess.UsersEntity;
import messaging.Message;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class DAO {
    protected JDBCConnector connector;
    private Connection conn;

    /**
     * DAO constructor
     * @throws dbaccess.DAOException
     */
    public DAO() throws DAOException {
        connector = new JDBCConnector();
        try {
            conn = connector.getConnection();
        } catch (JDBCConnectionException e){
            throw new DAOException("Can't connect to database!", e);
        }
    }

    /**
     * Closing connection
     * @throws dbaccess.DAOException
     */
    public void closeConnection() throws db.DAOException {
        try {
            connector.close();
        } catch (JDBCConnectionException e){
            throw new db.DAOException("Can't connect to database!", e);
        }
    }
    public boolean addUser(String name) throws db.DAOException{
        try {
            PreparedStatement orderStatement = conn.prepareStatement("select id from `users` " +
                    "order by id desc;");
            ResultSet rs = orderStatement.executeQuery();
            rs.next();
            int orderNum = rs.getInt(1) + 1;
            String sqlInsert = "insert into `users` values (?, ?)";
            orderStatement = conn.prepareStatement(sqlInsert);
            orderStatement.setInt(1, orderNum);
            orderStatement.setString(2, name);
            orderStatement.executeUpdate();
        } catch (Exception ex){
            throw new db.DAOException(ex.getMessage());
        }
        return true;
    }

    public boolean addMessage(Message message) throws DAOException{
        try {
            PreparedStatement orderStatement = conn.prepareStatement("select id from `messages` " +
                    "order by id desc;");
            ResultSet rs = orderStatement.executeQuery();
            rs.next();
            //int orderNum = rs.getInt(1) + 1;
            int orderNum = Integer.valueOf(message.getId());
            orderStatement = conn.prepareStatement("select id from `users` " +
                    "where name=?;");
            orderStatement.setString(1, message.getUser());
            rs = orderStatement.executeQuery();
            rs.next();
            int userId = rs.getInt(1);
            String sqlInsert = "insert into `messages` values (?, ?, ?)";
            orderStatement = conn.prepareStatement(sqlInsert);
            orderStatement.setInt(1, orderNum);
            orderStatement.setString(2, message.getMessageText());
            orderStatement.setInt(3, userId);
            orderStatement.executeUpdate();
        } catch (Exception ex){
            throw new db.DAOException(ex.getMessage());
        }
        return true;
    }

    public List<Message> getMessagesByUserName(String name) throws DAOException{
        int id;
        try {
            PreparedStatement orderStatement = conn.prepareStatement("select id from `users` " +
                    "where name=?;");
            orderStatement.setString(1, name);
            ResultSet rs = orderStatement.executeQuery();
            rs.next();
            id = rs.getInt(1);
        } catch (Exception ex){
            throw new DAOException(ex.getMessage());
        }
        return getMessagesByUserId(id);
    }

    public boolean deleteMessage(Message message) throws DAOException{
        try{
            PreparedStatement orderStatement;
            String sqlInsert = "DELETE FROM messages WHERE id=?";
            orderStatement = conn.prepareStatement(sqlInsert);
            orderStatement.setInt(1, Integer.valueOf(message.getId()));
            orderStatement.executeUpdate();
        } catch (Exception ex){
            throw new DAOException(ex.getMessage());
        }
        return true;
    }

    public boolean updateMessage(Message message) throws DAOException{
        try{
            PreparedStatement orderStatement;
            String sqlInsert = "UPDATE messages set textdate=? where id=?";
            orderStatement = conn.prepareStatement(sqlInsert);
            orderStatement.setString(1, message.getMessageText());
            orderStatement.setInt(2, Integer.valueOf(message.getId()));
            orderStatement.executeUpdate();
        } catch (Exception ex){
            throw new DAOException(ex.getMessage());
        }
        return true;
    }

    public List<Message> getMessagesByUserId(Integer id) throws DAOException{
        List<Message> list = new ArrayList<Message>();
        try {
            PreparedStatement orderStatement = conn.prepareStatement("select * from `messages` " +
                    "where user_id=?;");
            orderStatement.setInt(1, id);
            ResultSet rs = orderStatement.executeQuery();
            while(rs.next()){
                list.add(new Message(rs.getString(1), rs.getString(2), rs.getString(3), false));
            }

        } catch (Exception ex){
            throw new DAOException(ex.getMessage());
        }
        return list;
    }

}