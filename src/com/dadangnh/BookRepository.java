package com.dadangnh;

import java.sql.*;
import java.util.ArrayList;

public class BookRepository {
    private Connection db;

    public BookRepository() throws SQLException {
        db = DBDriver.init();
    }

    public boolean create(Book b) {
        int success = -1;

        try {
            PreparedStatement pst = this.db.prepareStatement("INSERT INTO buku.book (name, stock, borrowed, author, description, keyword, createddate, availablefrom) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, b.getName());
            pst.setInt(2, b.getStock());
            pst.setInt(3, b.getBorrowed());
            pst.setString(4, b.getAuthor());
            pst.setString(5, b.getDescription());
            pst.setString(6, b.getKeyword());
            pst.setDate(7, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pst.setDate(8, java.sql.Date.valueOf(java.time.LocalDate.now()));

            success = pst.executeUpdate();

            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1 == success;
    }

    public boolean edit(Book b) {
        int success = -1;

        try {
            PreparedStatement pst = this.db.prepareStatement("UPDATE buku.book set name = ?, stock = ?, borrowed = ?, author = ?, description = ?, keyword = ? where id = ?");
            pst.setString(1, b.getName());
            pst.setInt(2, b.getStock());
            pst.setInt(3, b.getBorrowed());
            pst.setString(4, b.getAuthor());
            pst.setString(5, b.getDescription());
            pst.setString(6, b.getKeyword());
            pst.setInt(7, b.getId());

            success = pst.executeUpdate();

            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1 == success;
    }



    public boolean delete(int id) {
        int success = -1;

        try {
            PreparedStatement pst = this.db.prepareStatement("delete from buku.book where id = ?");
            pst.setInt(1, id);

            success = pst.executeUpdate();

            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1 == success;
    }


    public ArrayList<Book> getAllBook() {
        ArrayList<Book> list = new ArrayList<>();

        // get book
        try {
            PreparedStatement pst = this.db.prepareStatement("SELECT id, name, stock, borrowed, author, description, keyword, createddate, availablefrom FROM buku.book ORDER BY id");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Book b = new Book();
                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                b.setStock( rs.getInt("stock"));
                b.setBorrowed(rs.getInt("borrowed"));
                b.setAuthor(rs.getString("author"));
                b.setDescription(rs.getString("description"));
                b.setKeyword(rs.getString("keyword"));
                b.setCreateddate(rs.getDate("createddate").toLocalDate());
                b.setAvailablefrom(rs.getDate("availablefrom").toLocalDate());

                list.add(b);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Book getBookById(Integer id) {
        // get book
        Book b = null;
        try {
            PreparedStatement pst = this.db.prepareStatement("SELECT id, name, stock, borrowed, author, description, keyword, createddate, availablefrom FROM buku.book WHERE id = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                b = new Book();
                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                b.setStock( rs.getInt("stock"));
                b.setBorrowed(rs.getInt("borrowed"));
                b.setAuthor(rs.getString("author"));
                b.setDescription(rs.getString("description"));
                b.setKeyword(rs.getString("keyword"));
                b.setCreateddate(rs.getDate("createddate").toLocalDate());
                b.setAvailablefrom(rs.getDate("availablefrom").toLocalDate());
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return b;
    }

    public void printAllBook() {
        ArrayList<Book> list = getAllBook();

        if (list.isEmpty()) {
            System.out.println("=====================================================================================");
            System.out.println("No book available, please add one or generate them.");
        } else {
            System.out.println("=====================================================================================");
            System.out.println("Book List:");
            System.out.println("ID\tName\tAuthor\tDescription\tBooks Available");
            int total = 0;
            for (Book b: list) {
                Integer bookAvailable = b.getStock() - b.getBorrowed();
                if (bookAvailable > 0) {
                    total++;
                    System.out.println(b.getId() + "\t" + b.getName() + "\t" + b.getAuthor() + "\t" + b.getDescription() + "\t" + bookAvailable);
                }
            }

            System.out.println("-----------------------------------------------------------");
            System.out.println("Total " + total + " books");
            System.out.println("-----------------------------------------------------------");
        }
    }



    public Integer getCurrentMaxId() {
        int maxId = 0;
        // get id
        try {
            PreparedStatement pst = this.db.prepareStatement("SELECT max(id) as id FROM buku.book");
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
            PreparedStatement pst = this.db.prepareStatement("SELECT min(id) as id FROM buku.book");
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
    public boolean checkBookExistById(Integer id) {
        return this.getBookById(id) != null;
    }

    public Integer getBookStockAvailableById(Integer id) {
        Book book = getBookById(id);
        return book.getStockAvailable();
    }

    public boolean rent(Book b) {
        int currentBorrowed = b.getBorrowed();
        ++currentBorrowed;

        if (currentBorrowed >= b.getStock()) {
            currentBorrowed = b.getStock();
        }

        b.setBorrowed(currentBorrowed);

        return this.edit(b);
    }

    public boolean back(Book b) {
        int currentBorrowed = b.getBorrowed();
        --currentBorrowed;

        if (currentBorrowed <= 0) {
            currentBorrowed = 0;
        }

        b.setBorrowed(currentBorrowed);

        return this.edit(b);
    }

}
