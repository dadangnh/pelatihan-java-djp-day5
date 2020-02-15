package com.dadangnh;

import java.sql.*;
import java.util.ArrayList;

public class TransactionRepository {
    private Connection db;

    public TransactionRepository() throws SQLException {
        db = DBDriver.init();
    }

    public Boolean create(Transaction t, BookRepository bookRepository) {
        int success = -1;

        try {
            PreparedStatement pst = this.db.prepareStatement("INSERT INTO buku.transaction (book, userid, startdate, enddate, finished) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, t.getBook().getId());
            pst.setInt(2, t.getUserId().getId());
            pst.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pst.setDate(4, java.sql.Date.valueOf(t.getEndDate()));
            pst.setBoolean(5, t.getFinished());

            success = pst.executeUpdate();

            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (1 == success) {
            return bookRepository.rent(t.getBook());
        }

        return false;
    }

    public Boolean edit(Transaction t) {
        int success = -1;

        try {
            PreparedStatement pst = this.db.prepareStatement("UPDATE buku.transaction SET book = ?, userid = ?, enddate = ?, finished = ? WHERE id = ?",
                    Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, t.getBook().getId());
            pst.setInt(2, t.getUserId().getId());
            pst.setDate(3, java.sql.Date.valueOf(t.getEndDate()));
            pst.setBoolean(4, t.getFinished());
            pst.setInt(5, t.getId());

            success = pst.executeUpdate();

            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1 == success;
    }

    public boolean back(Transaction t, BookRepository b) {
        t.setFinished(true);

        if (this.edit(t)) {
            return b.back(t.getBook());
        }

        return false;
    }

    public boolean delete(int id) {
        int success = -1;

        try {
            PreparedStatement pst = this.db.prepareStatement("delete from buku.transaction where id = ?");
            pst.setInt(1, id);

            success = pst.executeUpdate();

            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1 == success;
    }

    public ArrayList<Transaction> getAllTransaction(BookRepository b, UserRepository u) {
        ArrayList<Transaction> list = new ArrayList<>();

        // get transaction
        try {
            PreparedStatement pst = this.db.prepareStatement("SELECT id, book, userid, startdate, enddate, finished FROM buku.transaction ORDER BY id");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setBook(b.getBookById(rs.getInt("book")));
                t.setUserId(u.getUserById(rs.getInt("userid")));
                t.setStartDate(rs.getDate("startdate").toLocalDate());
                t.setEndDate(rs.getDate("enddate").toLocalDate());
                t.setFinished(rs.getBoolean("finished"));

                list.add(t);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean checkTransactionExistById(BookRepository b, UserRepository u, Integer id) {
        return this.getTransactionById(b, u, id) != null;
    }


    public Transaction getTransactionById(BookRepository b, UserRepository u, Integer id) {
        Transaction t = null;
        // get transaction
        try {
            PreparedStatement pst = this.db.prepareStatement("SELECT id, book, userid, startdate, enddate, finished FROM buku.transaction WHERE id = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setBook(b.getBookById(rs.getInt("book")));
                t.setUserId(u.getUserById(rs.getInt("userid")));
                t.setStartDate(rs.getDate("startdate").toLocalDate());
                t.setEndDate(rs.getDate("enddate").toLocalDate());
                t.setFinished(rs.getBoolean("finished"));
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }


    public void printAllTransaction(BookRepository b, UserRepository u) {
        ArrayList<Transaction> list = getAllTransaction(b, u);

        if (list.isEmpty()) {
            System.out.println("No transaction available, please add one or generate them.");
        } else {
            this.printTransactionFormat(list, ".", true, true);
        }
    }



    public Integer getCurrentMaxId() {
        int maxId = 0;
        // get id
        try {
            PreparedStatement pst = this.db.prepareStatement("SELECT max(id) as id FROM buku.transaction");
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
            PreparedStatement pst = this.db.prepareStatement("SELECT min(id) as id FROM buku.transaction");
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

    public ArrayList<Transaction> getAllTransactionByUser(BookRepository bookRepository, UserRepository userRepository, User u) {
        ArrayList<Transaction> list = new ArrayList<>();
        try {
            PreparedStatement pst = this.db.prepareStatement("SELECT id, book, userid, startdate, enddate, finished FROM buku.transaction WHERE userid = ? ORDER BY id");
            pst.setInt(1, u.getId());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setBook(bookRepository.getBookById(rs.getInt("book")));
                t.setUserId(userRepository.getUserById(rs.getInt("userid")));
                t.setStartDate(rs.getDate("startdate").toLocalDate());
                t.setEndDate(rs.getDate("enddate").toLocalDate());
                t.setFinished(rs.getBoolean("finished"));

                list.add(t);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void printAllTransactionByUser(BookRepository bookRepository, UserRepository userRepository, User u) {
        ArrayList<Transaction> list = getAllTransactionByUser(bookRepository, userRepository, u);

        if (list.isEmpty()) {
            System.out.println("No transaction available for user");
        } else {
            this.printTransactionFormat(list, ".", false, true);
        }
    }

    public Boolean checkTransactionExistByUserAndId(BookRepository b, UserRepository u, Integer id, User user, Boolean finished) {
        // get transaction by id
        Transaction t = this.getTransactionById(b, u, id);
        if (null != t) {
            // if finished is true, means that we look for all transaction by this user
            // otherwise, we look for currently active transaction by this user (book didn't return yet)
            if (!finished) {
                // check if book already returned, if so, return false
                if (t.getFinished() && t.getUserId().getId().equals(user.getId())) {
                    return false;

                // if book haven't returned, check whether the user is correct
                } else {
                    return t.getUserId().getId().equals(user.getId());
                }
            } else {
                return t.getUserId().getId().equals(user.getId());
            }

        }

        return false;
    }

    public ArrayList<Transaction> getAllCurrentOnLoanByUser(BookRepository bookRepository, UserRepository userRepository, User u) {
        ArrayList<Transaction> list = new ArrayList<>();

        try {
            PreparedStatement pst = this.db.prepareStatement("SELECT id, book, userid, startdate, enddate, finished FROM buku.transaction WHERE userid = ? AND finished = ? ORDER BY id");
            pst.setInt(1, u.getId());
            pst.setBoolean(2, false);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setBook(bookRepository.getBookById(rs.getInt("book")));
                t.setUserId(userRepository.getUserById(rs.getInt("userid")));
                t.setStartDate(rs.getDate("startdate").toLocalDate());
                t.setEndDate(rs.getDate("enddate").toLocalDate());
                t.setFinished(rs.getBoolean("finished"));

                list.add(t);
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public void printAllOnLoanByUser(BookRepository bookRepository, UserRepository userRepository, User u) {
        ArrayList<Transaction> list = getAllCurrentOnLoanByUser(bookRepository, userRepository, u);

        if (list.isEmpty()) {
            System.out.println("No transaction available for user");
        } else {
            this.printTransactionFormat(list, " on loan haven't returned.", false, false);
        }
    }

    private void printTransactionFormat(ArrayList<Transaction> list, String footer, Boolean showUserName, Boolean showStatistics) {
        System.out.println("Transaction List:");
        if (showUserName) {
            System.out.println("ID\tName\tBook\tStart Date\tEnd Date\tFinished");
        } else {
            System.out.println("No\tID Transaction\tBook\tStart Date\tEnd Date\tFinished");
        }
        int number = 0;
        int finished = 0;
        int borrowed = 0;
        for (Transaction t: list) {
            number++;
            if (t.getFinished()) {
                finished++;
            } else {
                borrowed++;
            }
            if (showUserName) {
                System.out.println(t.getId() + "\t" + t.getUserId().getName() + "\t" + t.getBook().getName() + "\t" + t.getStartDate() + "\t" + t.getEndDate() + "\t" + t.getFinished());
            } else {
                System.out.println(number + "\t" + t.getId() + "\t" + t.getBook().getName() + "\t" + t.getStartDate() + "\t" + t.getEndDate() + "\t" + t.getFinished());

            }
        }

        System.out.println("-----------------------------------------------------------");
        System.out.println("Total " + number + " transactions" + footer);
        if (showStatistics) {
            System.out.println("Statistics:");
            System.out.println("Total currently borrowed: " + borrowed + " books.");
            System.out.println("Total finished: " + finished + " books.");
        }
        System.out.println("-----------------------------------------------------------");
    }
}