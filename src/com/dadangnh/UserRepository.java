package com.dadangnh;

import java.sql.*;
import java.util.ArrayList;

public class UserRepository  {
    private Connection db;

    public UserRepository() throws SQLException {
        db = DBDriver.init();
    }

    public boolean create(User u) {
        int success = -1;

        try {
            PreparedStatement pst = this.db.prepareStatement("INSERT INTO buku.user (name, email, registereddate) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, u.getName());
            pst.setString(2, u.getEmail());
            pst.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));

            success = pst.executeUpdate();

            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success == 1;
    }

    public boolean edit(User u) {
        int success = -1;

        try {
            PreparedStatement pst = this.db.prepareStatement("UPDATE buku.user set name = ?, email = ? where id = ?");
            pst.setString(1, u.getName());
            pst.setString(2, u.getEmail());
            pst.setInt(3, u.getId());

            success = pst.executeUpdate();

            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success == 1;
    }



    public boolean delete(int id) {
        int success = -1;

        try {
            PreparedStatement pst = this.db.prepareStatement("delete from buku.user where id = ?");
            pst.setInt(1, id);

            success = pst.executeUpdate();

            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success == 1;
    }


    public ArrayList<User> getAllUser() {
        ArrayList<User> list = new ArrayList<>();

        // get user
        try {
            PreparedStatement pst = this.db.prepareStatement("SELECT id,name,email,registereddate FROM buku.user ORDER BY id");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setRegistereddate(rs.getDate("registereddate").toLocalDate());

                list.add(u);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public User getUserById(Integer id) {
        // get user
        User u = null;
        try {
            PreparedStatement pst = this.db.prepareStatement("SELECT id,name,email,registereddate FROM buku.user WHERE id = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                u = new User();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setRegistereddate(rs.getDate("registereddate").toLocalDate());
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return u;
    }

    public void printAllUser() {
        ArrayList<User> list = getAllUser();

        if (list.isEmpty()) {
            System.out.println("No user available, please add one or generate them.");
        } else {
            System.out.println("User List:");
            System.out.println("ID\tName\tEmail\tCreated Date");
            int total = 0;
            for (User u: list) {
                total++;
                System.out.println(u.getId() + "\t" + u.getName() + "\t" + u.getEmail() + "\t" + u.getRegistereddate());
            }

            System.out.println("-----------------------------------------------------------");
            System.out.println("Total " + total + " users");
            System.out.println("-----------------------------------------------------------");
        }
    }


    public Integer getCurrentMaxId() {
        int maxId = 0;
        // get id
        try {
            PreparedStatement pst = this.db.prepareStatement("SELECT max(id) as id FROM buku.user");
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                maxId = rs.getInt("id");
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxId;
    }

    public Integer getCurrentMinId() {
        int minId = 1;
        // get id
        try {
            PreparedStatement pst = this.db.prepareStatement("SELECT min(id) as id FROM buku.user");
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                minId = rs.getInt("id");
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return minId;
    }

    public boolean checkUserExistById(Integer id) {
        return this.getUserById(id) != null;
    }

}
