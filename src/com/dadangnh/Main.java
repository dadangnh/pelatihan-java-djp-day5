package com.dadangnh;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) throws SQLException {
        UserRepository userRepository = new UserRepository();
        BookRepository bookRepository = new BookRepository();
        TransactionRepository transactionRepository = new TransactionRepository();
        Scanner scanner = new Scanner(System.in);
        boolean run = true;

        while (run) {
            System.out.println("=====================================================================================");
            System.out.println(" Welcome to Dadang NH Library");
            System.out.println("=====================================================================================");
            System.out.println("Menu:");
            System.out.println("0. Generate Dummy Data");
            System.out.println("1. Manage Books");
            System.out.println("2. Manage Users");
            System.out.println("3. Book's Transactions");
            System.out.println("4. Exit");
            System.out.println("=====================================================================================");
            System.out.println("Choose the menu [0, 1, 2, 3, 4]");
            int menuOptions = scanner.nextInt();

            if (menuOptions == 1) {
                boolean runBookMenu = true;

                while (runBookMenu) {
                    System.out.println("=====================================================================================");
                    System.out.println("Manage Books:");
                    System.out.println("=====================================================================================");
                    System.out.println("1. Show All Books");
                    System.out.println("2. Auto Generate Book");
                    System.out.println("3. Add Book");
                    System.out.println("4. Edit Book");
                    System.out.println("5. Delete Book");
                    System.out.println("6. Back");
                    System.out.println("=====================================================================================");
                    System.out.println("Choose the menu [1 - 6]");
                    int manageBooksMenu = scanner.nextInt();

                    if (1 == manageBooksMenu) {
                        bookRepository.printAllBook();
                        run = runBookMenu = confirmation();
                    } else if (2 == manageBooksMenu) {
                        generateBooks(bookRepository);
                        run = runBookMenu = confirmation();
                    } else if (3 == manageBooksMenu) {
                        addBooks(bookRepository);
                    } else if (4 == manageBooksMenu) {
                        editBook(bookRepository);
                        run = runBookMenu = confirmation();
                    } else if (5 == manageBooksMenu) {
                        deleteEntity(transactionRepository, bookRepository, userRepository, "book");
                        run = runBookMenu = confirmation();
                    } else  if (6 == manageBooksMenu) {
                        runBookMenu = false;
                    }
                }

            }

            if (menuOptions == 2) {
                boolean runUserMenu = true;

                while (runUserMenu) {
                    System.out.println("=====================================================================================");
                    System.out.println("Manage Users:");
                    System.out.println("=====================================================================================");
                    System.out.println("1. Show All Users");
                    System.out.println("2. Auto Generate Users");
                    System.out.println("3. Add Users");
                    System.out.println("4. Edit Users");
                    System.out.println("5. Delete Users");
                    System.out.println("6. Back");
                    System.out.println("=====================================================================================");
                    System.out.println("Choose the menu [1 - 6]");
                    int manageUsersMenu = scanner.nextInt();

                    if (1 == manageUsersMenu) {
                        userRepository.printAllUser();
                        run = runUserMenu = confirmation();
                    } else if (2 == manageUsersMenu) {
                        generateUsers(userRepository);
                        run = runUserMenu = confirmation();
                    } else if (3 == manageUsersMenu) {
                        addUser(userRepository);
                    } else if (4 == manageUsersMenu) {
                        editUser(userRepository);
                        run = runUserMenu = confirmation();
                    } else if (5 == manageUsersMenu) {
                        deleteEntity(transactionRepository, bookRepository, userRepository, "user");
                        run = runUserMenu = confirmation();
                    } else  if (6 == manageUsersMenu) {
                        runUserMenu = false;
                    }
                }
            }
            if (menuOptions == 3) {
                boolean runTransaction = true;

                while (runTransaction) {
                    System.out.println("=====================================================================================");
                    System.out.println("Manage Transaction:");
                    System.out.println("=====================================================================================");
                    System.out.println("1. Show All Transaction");
                    System.out.println("2. Auto Generate Transaction");
                    System.out.println("3. Add Transaction / Login as User");
                    System.out.println("4. Edit Transaction (As admin)");
                    System.out.println("5. Delete Transaction (As admin)");
                    System.out.println("6. Back");
                    System.out.println("=====================================================================================");
                    System.out.println("Choose the menu [1 - 6]");
                    int menuManageTransaction = scanner.nextInt();

                    if (1 == menuManageTransaction) {
                        transactionRepository.printAllTransaction(bookRepository, userRepository);
                        run = runTransaction = confirmation();
                    } else if (2 == menuManageTransaction) {
                        generateTransaction(transactionRepository, bookRepository, userRepository);
                        run = runTransaction = confirmation();
                    } else if (3 == menuManageTransaction) {
                        addTransaction(transactionRepository, bookRepository, userRepository);
                    } else if (4 == menuManageTransaction) {
                        editTransaction(transactionRepository, bookRepository, userRepository);
                        run = runTransaction = confirmation();
                    } else if (5 == menuManageTransaction) {
                        deleteEntity(transactionRepository, bookRepository, userRepository, "transaction");
                        run = runTransaction = confirmation();
                    } else  if (6 == menuManageTransaction) {
                        runTransaction = false;
                    }
                }
            }

            if (menuOptions == 4) {
                run = false;
                System.out.println("Good bye!");
            }

            if (menuOptions == 0) {
//                generateAllData(customerManager, productManager, transactionManager);
                System.out.println("Generate dummy data not available at this moment");
                run = confirmation();
            }
        }

    }

    // Confirmation
    private static boolean confirmation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=====================================================================================");
        System.out.println("Do you want to continue? [Y, n]");
        String next = scanner.nextLine();

        // Return true if user input not an "N" or "n"
        return !next.toLowerCase().equals("n");
    }

    // Books
    private static void generateBooks(BookRepository bookRepository) {
        System.out.println("Generating Books");
        for (int i = 0; i < 20; i++) {
            Book b = new Book();
            Random random = new Random();
            int stock = random.nextInt(100);
            if (stock < 3) {
                stock = 3;
            }
            int authorId = ThreadLocalRandom.current().nextInt(0, (bookRepository.getCurrentMaxId() + 1));
            int uniqueId = bookRepository.getCurrentMaxId() + 1;
            b.setName("Book " + uniqueId);
            b.setAuthor("Author " + authorId);
            b.setDescription("Description of Book " + uniqueId);
            b.setKeyword("Keyword of Book " + uniqueId);
            b.setStock(stock);
            b.setBorrowed(ThreadLocalRandom.current().nextInt(1, stock));

            bookRepository.create(b);
        }

        // Show product
        ArrayList<Book> list = bookRepository.getAllBook();
        if (!list.isEmpty()) {
            System.out.println("ID\tName\tAuthor\tStock");
            for (Book b: list) {
                System.out.println(b.getId() + "\t" + b.getName() + "\t" + b.getAuthor() + "\t" + b.getStock());
            }
        }
    }


    private static void addBooks(BookRepository bookRepository) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Create new book");
        boolean createAgain = true;

        do {
            String keepGoing;

            if (bookRepository.create(inputBookData(bookRepository, scanner, null))) {
                System.out.println("Book saved");
            } else {
                System.out.println("Failed saving new book");
            }


            System.out.println("Do you want to input a new book again? [Y, N]");
            keepGoing = scanner.next();

            if (keepGoing.toLowerCase().equals("n")) {
                createAgain = false;
            }
        } while (createAgain);

    }


    private static void editBook(BookRepository bookRepository) {
        Scanner scanner = new Scanner(System.in);
        boolean inputCorrect = true;
        int maxBookId = bookRepository.getCurrentMaxId();
        int minBookId = bookRepository.getCurrentMinId();

        while (inputCorrect) {
            System.out.println("=====================================================================================");
            System.out.println("Please specify the ID of the Book: [" + minBookId + " - " + maxBookId + "]");
            Integer next = scanner.nextInt();

            Book book = bookRepository.getBookById(next);
            System.out.println("You will edit: " + book.getName());

            if (bookRepository.edit(inputBookData(bookRepository, scanner, book.getId()))) {
                System.out.println("Book saved");
                inputCorrect = false;
            } else {
                System.out.println("Failed updating the book");
            }
        }
    }

    private static Book inputBookData(BookRepository bookRepository, Scanner scanner, Integer defaultId) {
        Book b = new Book();
        if (null == defaultId) {
            Integer existingId = bookRepository.getCurrentMaxId();
            b.setId(existingId + 1);
        } else {
            b.setId(defaultId);
            scanner.nextLine();
        }

        String name = "";
        String author = "";
        String description = "";
        String keyword = "";
        int stock = 0;
        int borrowed = 0;

        while (name.isBlank()) {
            System.out.println("Book name?");
            name = scanner.nextLine();

            if (!name.isBlank()) {
                b.setName(name);
            }
        }

        while (author.isBlank()) {
            System.out.println("Book author?");
            author = scanner.nextLine();

            if (!author.isBlank()) {
                b.setAuthor(author);
            }
        }

        while (description.isBlank()) {
            System.out.println("Book description?");
            description = scanner.nextLine();

            if (!description.isBlank()) {
                b.setDescription(description);
            }
        }

        while (keyword.isBlank()) {
            System.out.println("Book keyword?");
            keyword = scanner.nextLine();

            if (!keyword.isBlank()) {
                b.setKeyword(keyword);
            }
        }

        while (0 == stock) {
            System.out.println("Book stock?");
            stock = scanner.nextInt();

            if (0 != stock) {
                b.setStock(stock);
            }
        }

        while (0 >= borrowed) {
            System.out.println("How many books borrowed right now?");
            borrowed = scanner.nextInt();

            if (borrowed >= b.getStock()) {
                System.out.println("books borrowed cannot higher than book stock");
                borrowed = -1;
            }

            if (0 <= borrowed) {
                b.setBorrowed(borrowed);
            }
        }

        return b;

    }

    // Users
    private static void generateUsers(UserRepository userRepository) {
        System.out.println("Generating Users");
        for (int i = 0; i < 20; i++) {
            User u = new User();
            int uniqueId = userRepository.getCurrentMaxId() + 1;
            u.setName("User " + uniqueId);
            u.setEmail("user_" + uniqueId + "@dadangnh.com");

            userRepository.create(u);
        }

        // Show product
        ArrayList<User> list = userRepository.getAllUser();
        if (!list.isEmpty()) {
            System.out.println("ID\tName\tEmail\tRegistered Date");
            for (User u: list) {
                System.out.println(u.getId() + "\t" + u.getName() + "\t" + u.getEmail() + "\t" + u.getRegistereddate());
            }
        }
    }


    private static void addUser(UserRepository userRepository) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Create new user");
        boolean createAgain = true;

        do {
            String keepGoing;

            if (userRepository.create(inputUserData(userRepository, scanner, null))) {
                System.out.println("User saved");
            } else {
                System.out.println("Failed saving new user");
            }


            System.out.println("Do you want to input a new user again? [Y, N]");
            keepGoing = scanner.nextLine();

            if (keepGoing.toLowerCase().equals("n")) {
                createAgain = false;
            }
        } while (createAgain);

    }


    private static void editUser(UserRepository userRepository) {
        Scanner scanner = new Scanner(System.in);
        boolean inputCorrect = true;
        int maxUserId = userRepository.getCurrentMaxId();
        int minUserId = userRepository.getCurrentMinId();

        while (inputCorrect) {
            System.out.println("=====================================================================================");
            System.out.println("Please specify the ID of the User: [" + minUserId + " - " + maxUserId + "]");
            Integer next = scanner.nextInt();

            User user = userRepository.getUserById(next);
            System.out.println("You will edit: " + user.getName());

            if (userRepository.edit(inputUserData(userRepository, scanner, user.getId()))) {
                System.out.println("User saved");
                inputCorrect = false;
            } else {
                System.out.println("Failed updating the user");
            }

        }
    }

    private static User inputUserData(UserRepository userRepository, Scanner scanner, Integer defaultId) {
        User u = new User();
        if (null == defaultId) {
            Integer existingId = userRepository.getCurrentMaxId();
            u.setId(existingId + 1);
        } else {
            u.setId(defaultId);
            scanner.nextLine();
        }

        String name;
        String email;

        do {
            System.out.println("User name?");
            name = scanner.nextLine();

            if (!name.isBlank()) {
                u.setName(name);
            }
        } while (name.isBlank());
        do {
            System.out.println("User email?");
            email = scanner.nextLine();

            if (!email.isBlank()) {
                u.setEmail(email);
            }
        } while (email.isBlank());

        return u;
    }

    // Transaction
    private static void generateTransaction(TransactionRepository transactionRepository, BookRepository bookRepository, UserRepository userRepository) {
        System.out.println("Generating Transaction");
        for (int i = 0; i < 20; i++) {
            Transaction t = new Transaction();
            boolean loopUser = true;
            boolean loopBook = true;

            // check if user and book exist and assign it
            while (loopUser){
                Integer randomUser = ThreadLocalRandom.current().nextInt(userRepository.getCurrentMinId(), userRepository.getCurrentMaxId());
                if (userRepository.checkUserExistById(randomUser)) {
                    t.setUserId(userRepository.getUserById(randomUser));
                    loopUser = false;
                }
            }

            while (loopBook){
                Integer randomBook = ThreadLocalRandom.current().nextInt(bookRepository.getCurrentMinId(), bookRepository.getCurrentMaxId());
                if (bookRepository.checkBookExistById(randomBook)) {
                    t.setBook(bookRepository.getBookById(randomBook));
                    loopBook = false;
                }
            }

            // set end date 2 weeks from now

            LocalDate today = LocalDate.now();
            t.setEndDate(today.plusWeeks(2));
            t.setFinished(false);

            transactionRepository.create(t, bookRepository);
        }

        // Show product
        ArrayList<Transaction> list = transactionRepository.getAllTransaction(bookRepository, userRepository);
        if (!list.isEmpty()) {
            System.out.println("ID\tName\tBook\tStart Date\tEnd Date");
            for (Transaction t: list) {
                System.out.println(t.getId() + "\t" + t.getUserId().getName() + "\t" + t.getBook().getName() + "\t" + t.getStartDate() + "\t" + t.getEndDate());
            }
        }
    }


    private static void addTransaction(TransactionRepository transactionRepository, BookRepository bookRepository, UserRepository userRepository) {
        Scanner scanner = new Scanner(System.in);
        boolean createAgain = true;

        while (createAgain) {
            Integer maxUserId = userRepository.getCurrentMaxId();
            System.out.println("Choose the User ID [1 - " + maxUserId + "]: ");
            Integer userId = scanner.nextInt();

            // check if customer valid or not
            if (userRepository.checkUserExistById(userId)) {
                User theUser = userRepository.getUserById(userId);
                System.out.println("Hello, " + theUser.getName());
                System.out.println("please choose the menu below");
                boolean runAtUser = true;

                while (runAtUser) {
                    System.out.println("=====================================================================================");
                    System.out.println("Hi " + theUser.getName() + ",");
                    System.out.println("Welcome to Dadang NH Library, choose your menu below:");
                    System.out.println("=====================================================================================");
                    System.out.println("1. Browse Books");
                    System.out.println("2. Rent a Book");
                    System.out.println("3. Return the book");
                    System.out.println("4. Book currently on loan");
                    System.out.println("5. History Transaction");
                    System.out.println("6. Back");
                    System.out.println("=====================================================================================");
                    System.out.println("Choose the menu [1 - 6]");
                    int menuTransactionAtUser = scanner.nextInt();

                    if (menuTransactionAtUser == 1) {
                        bookRepository.printAllBook();
                        runAtUser = confirmation();
                    }

                    if (menuTransactionAtUser == 2) {
                        String rentBook = "Y";
                        Integer maxBookId = bookRepository.getCurrentMaxId();

                        do {
                            String confirmRent = "N";
                            Integer bookId;
                            do {
                                System.out.println("=====================================================================================");
                                System.out.println("Enter Book ID [1 - " + maxBookId + "] : ");
                                bookId = scanner.nextInt();

                                if (bookRepository.checkBookExistById(bookId)) {
                                    Book theBook = bookRepository.getBookById(bookId);
                                    System.out.println("Are you sure you want to rent " + theBook.getName() + " ? [Y / n]");
                                    confirmRent = scanner.next();
                                } else {
                                    System.out.println("Book ID is not available. Please enter the correct one.");
                                }

                            } while (confirmRent.toLowerCase().equals("n"));

                            if (bookRepository.checkBookExistById(bookId)) {
                                Book theBook = bookRepository.getBookById(bookId);
                                int stockAvailable = theBook.getStockAvailable();

                                if (stockAvailable <= 0) {
                                    System.out.println("-----------------------------------------------------------");
                                    System.out.println("Unfortunately, there is no book stock available at the moment for: " + theBook.getName());
                                    System.out.println("All of our " + theBook.getStock() + " is already borrowed");
                                    System.out.println("-----------------------------------------------------------");
                                } else {
                                    System.out.println("You will rent: " + theBook.getName() + " (stock available: " + stockAvailable +")");
                                    Transaction t = new Transaction();
                                    t.setUserId(theUser);
                                    t.setBook(theBook);
                                    LocalDate endDate = LocalDate.now().plusWeeks(2);
                                    t.setEndDate(endDate);
                                    t.setFinished(false);

                                    System.out.println("-----------------------------------------------------------");
                                    if (transactionRepository.create(t, bookRepository)) {
                                        System.out.println("Transaction successful. You borrowed " + theBook.getName() + " until " + t.getEndDate());
                                    } else {
                                        System.out.println("Transaction failed. Sometimes God has a plan for us");
                                    }
                                    System.out.println("-----------------------------------------------------------");

                                }

                                System.out.println("Do you want to make transaction again? [Y/N]");
                                rentBook = scanner.next();
                            }
                        } while (rentBook.toLowerCase().equals("y"));
                    }

                    if (menuTransactionAtUser == 3) {
                        String returnBook = "Y";

                        System.out.println("-----------------------------------------------------------");
                        System.out.println("All books currently borrowed by : " + theUser.getName());
                        transactionRepository.printAllOnLoanByUser(bookRepository, userRepository, theUser);
                        System.out.println("-----------------------------------------------------------");

                        do {
                            String confirmReturn = "N";
                            Integer transactionId;
                            do {
                                System.out.println("=====================================================================================");
                                System.out.println("Enter Transaction ID : ");
                                transactionId = scanner.nextInt();

                                if (transactionRepository.checkTransactionExistByUserAndId(bookRepository, userRepository, transactionId, theUser, false)) {
                                    Transaction theTransaction = transactionRepository.getTransactionById(bookRepository, userRepository, transactionId);
                                    System.out.println("Are you sure you want to return " + theTransaction.getBook().getName() + " ? [Y / n]");
                                    confirmReturn = scanner.next();
                                } else {
                                    System.out.println("Transaction ID is not available for your account or book already returned.");
                                }
                            } while (confirmReturn.toLowerCase().equals("n"));

                            if (transactionRepository.checkTransactionExistByUserAndId(bookRepository, userRepository, transactionId, theUser, false)) {
                                Transaction theTransaction = transactionRepository.getTransactionById(bookRepository, userRepository, transactionId);

                                System.out.println("-----------------------------------------------------------");
                                if (transactionRepository.back(theTransaction, bookRepository)) {
                                    System.out.println("Transaction successful. You returned " + theTransaction.getBook().getName());
                                } else {
                                    System.out.println("Transaction failed. Sometimes God has a plan for us");
                                }
                                System.out.println("-----------------------------------------------------------");
                                System.out.println("Do you want to return a book transaction again? [Y/N]");
                                returnBook = scanner.next();
                            }
                        } while (returnBook.toLowerCase().equals("y"));
                    }

                    if (menuTransactionAtUser == 4) {
                        System.out.println("-----------------------------------------------------------");
                        System.out.println("Book currently on loan by : " + theUser.getName());
                        transactionRepository.printAllOnLoanByUser(bookRepository, userRepository, theUser);
                        System.out.println("-----------------------------------------------------------");
                        runAtUser = confirmation();

                    }

                    if (menuTransactionAtUser == 5) {
                        System.out.println("-----------------------------------------------------------");
                        System.out.println("All transaction by : " + theUser.getName());
                        transactionRepository.printAllTransactionByUser(bookRepository, userRepository, theUser);
                        System.out.println("-----------------------------------------------------------");
                        runAtUser = confirmation();
                    }

                    if (menuTransactionAtUser == 6) {
                        createAgain = runAtUser = false;
                    }
                }
            } else {
                System.out.println("-----------------------------------------------------------");
                System.out.println("There is no user available with id: " + userId);
                System.out.println("Please choose different user id");
                System.out.println("-----------------------------------------------------------");
            }
        }
    }

    private static void editTransaction(TransactionRepository transactionRepository, BookRepository bookRepository, UserRepository userRepository) {
        Scanner scanner = new Scanner(System.in);
        boolean inputCorrect = true;
        int maxTransactionId = transactionRepository.getCurrentMaxId();
        int minTransactionId = userRepository.getCurrentMinId();

        while (inputCorrect) {
            System.out.println("=====================================================================================");
            System.out.println("Note: You can only edit the user id, book id, and rental status.");
            System.out.println("Please specify the Transaction ID: [" + minTransactionId + " - " + maxTransactionId + "]");
            Integer next = scanner.nextInt();

            if (transactionRepository.checkTransactionExistById(bookRepository, userRepository, next)) {
                Transaction t = transactionRepository.getTransactionById(bookRepository, userRepository, next);
                System.out.println("You will edit transaction ID: " + t.getId() + " by: " + t.getUserId().getName());

                if (transactionRepository.edit(inputTransactionData(bookRepository, userRepository, scanner, t))) {
                    System.out.println("Transaction saved");
                    inputCorrect = false;
                } else {
                    System.out.println("Failed updating the transaction");
                }
            } else {
                System.out.println("Transaction doesn't exist. Please input the correct one.");
            }
        }
    }

    private static void deleteEntity(TransactionRepository transactionRepository, BookRepository bookRepository, UserRepository userRepository, String entityType) {
        Scanner scanner = new Scanner(System.in);
        boolean inputCorrect = true;
        Integer maxId = 0;
        Integer minId = 0;
        String entityName = "";
        switch (entityType) {
            case "transaction":
                maxId = transactionRepository.getCurrentMaxId();
                minId = transactionRepository.getCurrentMinId();
                entityName = "Transaction";
                break;
            case "user":
                maxId = userRepository.getCurrentMaxId();
                minId = userRepository.getCurrentMinId();
                entityName = "User";
                break;
            case "book":
                maxId = bookRepository.getCurrentMaxId();
                minId = bookRepository.getCurrentMinId();
                entityName = "Book";
                break;
        }

        while (inputCorrect) {
            System.out.println("=====================================================================================");
            System.out.println("Please specify the ID of the " + entityName + ": [" + minId + " - " + maxId + "]");
            Integer next = scanner.nextInt();
            String confirmActionValid = "N";
            boolean entityValid = false;

            switch (entityType) {
                case "transaction":
                    entityValid = transactionRepository.checkTransactionExistById(bookRepository, userRepository, next);
                    break;
                case "user":
                    entityValid = userRepository.checkUserExistById(next);
                    break;
                case "book":
                    entityValid = bookRepository.checkBookExistById(next);
                    break;
            }

            if (entityValid){
                String confirmDelete = "n";
                Integer entityId = 0;
                String entityOwner = "";
                switch (entityType) {
                    case "transaction":
                        Transaction transaction = transactionRepository.getTransactionById(bookRepository, userRepository, next);
                        entityId = transaction.getId();
                        entityOwner = transaction.getUserId().getName();
                        break;
                    case "user":
                        User user = userRepository.getUserById(next);
                        entityId = user.getId();
                        entityOwner = user.getName();
                        break;
                    case "book":
                        Book book = bookRepository.getBookById(next);
                        entityId = book.getId();
                        entityOwner = book.getName();
                        break;
                }
                while (!confirmActionValid.toLowerCase().equals("y")){
                    System.out.println("You will delete a " + entityName + " with ID: " + entityId + (entityType.equals("transaction") ? " (by: " + entityOwner + ") " : " (name: " + entityOwner + ") "));
                    System.out.println("=====================================================================================");
                    System.out.println("Do you confirm [Y / N]");
                    confirmDelete = scanner.next();

                    if (confirmDelete.toLowerCase().equals("y") || confirmDelete.toLowerCase().equals("n")) {
                        confirmActionValid = "y";
                    }
                }

                if (confirmDelete.toLowerCase().equals("y")) {
                    boolean successDelete = false;
                    switch (entityType) {
                        case "transaction":
                            if (transactionRepository.delete(entityId)) {
                                successDelete = true;
                            }
                            break;
                        case "user":
                            if (userRepository.delete(entityId)) {
                                successDelete = true;
                            }
                            break;
                        case "book":
                            if (bookRepository.delete(entityId)) {
                                successDelete = true;
                            }
                            break;
                    }
                    if (successDelete) {
                        System.out.println("Success delete");
                    } else {
                        System.out.println("Failed deleting an entity.");
                    }
                } else {
                    System.out.println("Canceled to delete the " + entityType);
                }

                inputCorrect = false;

            } else {
                System.out.println("There is no " + entityType + " valid with that ID. Please input the correct one");
            }
        }
    }

    private static Transaction inputTransactionData(BookRepository bookRepository, UserRepository userRepository, Scanner scanner, Transaction t) {
        boolean userValidation = true;
        boolean bookValidation = true;
        boolean finishedValidation = true;

        while (userValidation ) {
            System.out.println("Please specify the user id");
            Integer userId = scanner.nextInt();

            if (userRepository.checkUserExistById(userId)) {
                User theUser = userRepository.getUserById(userId);
                System.out.println("Are you sure you want to change the user to: " + theUser.getName() + " [Y / N]");
                String userAgree = scanner.next();

                if (userAgree.toLowerCase().equals("y")) {
                    t.setUserId(theUser);
                    userValidation = false;
                }
            }
        }


        while (bookValidation ) {
            System.out.println("Please specify the book id");
            Integer bookId = scanner.nextInt();

            if (bookRepository.checkBookExistById(bookId)) {
                Book theBook = bookRepository.getBookById(bookId);
                System.out.println("Are you sure you want to change the book to: " + theBook.getName() + " [Y / N]");
                String bookAgree = scanner.next();

                if (bookAgree.toLowerCase().equals("y")) {
                    t.setBook(theBook);
                    bookValidation = false;
                }
            }
        }


        while (finishedValidation ) {
            System.out.println("Is this transaction already finished? (Book returned) [Y/n]");
            String finished = scanner.next();

            if (finished.toLowerCase().equals("y") || finished.toLowerCase().equals("n")) {
                if (finished.toLowerCase().equals("y")) {
                    t.setFinished(true);
                } else {
                    t.setFinished(false);
                }
                finishedValidation = false;
            }
        }

        return t;
    }

}
