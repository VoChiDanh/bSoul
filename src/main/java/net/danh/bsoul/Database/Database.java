package net.danh.bsoul.Database;

import net.danh.bsoul.bSoul;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;


public abstract class Database {
    public String table = "PlayerData";
    bSoul main;
    Connection connection;

    public Database(bSoul instance) {
        main = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize() {
        connection = getSQLConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
            ResultSet rs = ps.executeQuery();
            close(ps, rs);

        } catch (SQLException ex) {
            bSoul.getInstance().getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }

    // These are the methods you can use to get things out of your database. You of course can make new ones to return different things in the database.
    // This returns the number of people the player killed.
    public PlayerData getData(String player) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '" + player + "';");
            rs = ps.executeQuery();
            if (rs.next()) {
                return new PlayerData(rs.getString("player"), rs.getInt("soul"), rs.getInt("max_soul"));
            }
        } catch (SQLException ex) {
            bSoul.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                bSoul.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return null;
    }

    public void createTable(PlayerData playerData) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("INSERT INTO " + table + " (player,soul,max_soul) VALUES(?,?,?)"); // IMPORTANT. In SQLite class, We made 3 colums. player, Kills, Total.
            ps.setString(1, playerData.getName());
            ps.setInt(2, playerData.getdSoul());
            ps.setInt(3, playerData.getmSoul());
            ps.executeUpdate();
        } catch (SQLException ex) {
            bSoul.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                bSoul.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    public void updateTable(PlayerData playerData) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("UPDATE " + table + " SET soul = ?, max_soul = ? " +
                    "WHERE player = ?");
            conn.setAutoCommit(false);
            ps.setInt(1, playerData.getdSoul());
            ps.setInt(2, playerData.getmSoul());
            ps.setString(7, playerData.getName());
            ps.addBatch();
            ps.executeBatch();
            conn.commit();
        } catch (SQLException ex) {
            bSoul.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                bSoul.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }


    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        } catch (SQLException ex) {
            Error.close(bSoul.getInstance(), ex);
        }
    }
}
