package com.example.librarymanage.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBook extends SQLiteOpenHelper {

    // Tên cơ sở dữ liệu
    private static final String DATABASE_NAME = "LibraryManagement.db";
    // Phiên bản cơ sở dữ liệu
    private static final int DATABASE_VERSION = 2;

    public DataBook(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Account để lưu thông tin tài khoản
        db.execSQL("CREATE TABLE IF NOT EXISTS Account (" +
                "account_id INTEGER PRIMARY KEY AUTOINCREMENT, " +  // ID duy nhất của tài khoản
                "username VARCHAR, " +                              // Tên đăng nhập của tài khoản
                "password VARCHAR, " +                              // Mật khẩu của tài khoản
                "role VARCHAR, " +                                  // Vai trò của tài khoản (Quản trị viên/Bạn đọc)
                "user_id INTEGER, " +                               // ID người dùng liên kết
                "FOREIGN KEY (user_id) REFERENCES User(user_id))"); // Khóa ngoại tới bảng User

        // Tạo bảng User để lưu thông tin người dùng
        db.execSQL("CREATE TABLE IF NOT EXISTS User (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +     // ID duy nhất của người dùng
                "name VARCHAR, " +                                  // Tên người dùng
                "gender VARCHAR, " +                                // Giới tính người dùng
                "phone VARCHAR, " +                                 // Số điện thoại của người dùng
                "email VARCHAR, " +                                 // Email của người dùng
                "student_code VARCHAR, " +                          // Mã số sinh viên của người dùng
                "major_id INTEGER, " +                              // ID ngành học của người dùng
                "FOREIGN KEY (major_id) REFERENCES Major(major_id))"); // Khóa ngoại tới bảng Major

        // Tạo bảng Books để lưu thông tin sách
        db.execSQL("CREATE TABLE IF NOT EXISTS Books (" +
                "book_id INTEGER PRIMARY KEY AUTOINCREMENT, " +     // ID duy nhất của sách
                "title VARCHAR, " +                                 // Tên sách
                "location_id INTEGER, " +                           // ID vị trí của sách trong thư viện
                "author_id INTEGER, " +                             // ID tác giả của sách
                "category_id INTEGER, " +                           // ID thể loại của sách
                "major_id INTEGER, " +                              // ID ngành học liên quan đến sách
                "published_year YEAR, " +                           // Năm xuất bản của sách
                "description TEXT, " +                              // Mô tả sách
                "image VARCHAR, " +                                 // Đường dẫn hình ảnh của sách
                "FOREIGN KEY (location_id) REFERENCES Locations(location_id), " +  // Khóa ngoại tới bảng Locations
                "FOREIGN KEY (author_id) REFERENCES Author(author_id), " +         // Khóa ngoại tới bảng Author
                "FOREIGN KEY (category_id) REFERENCES Category(category_id), " +   // Khóa ngoại tới bảng Category
                "FOREIGN KEY (major_id) REFERENCES Major(major_id))");             // Khóa ngoại tới bảng Major

        // Tạo bảng Locations để lưu vị trí của sách
        db.execSQL("CREATE TABLE IF NOT EXISTS Locations (" +
                "location_id INTEGER PRIMARY KEY AUTOINCREMENT, " + // ID duy nhất của vị trí
                "name VARCHAR)");                                   // Tên của vị trí

        // Tạo bảng Author để lưu thông tin tác giả
        db.execSQL("CREATE TABLE IF NOT EXISTS Author (" +
                "author_id INTEGER PRIMARY KEY AUTOINCREMENT, " +   // ID duy nhất của tác giả
                "name VARCHAR)");                                   // Tên tác giả

        // Tạo bảng Category để lưu thông tin thể loại sách
        db.execSQL("CREATE TABLE IF NOT EXISTS Category (" +
                "category_id INTEGER PRIMARY KEY AUTOINCREMENT, " + // ID duy nhất của thể loại
                "name VARCHAR)");                                   // Tên thể loại

        // Tạo bảng Major để lưu thông tin ngành học
        db.execSQL("CREATE TABLE IF NOT EXISTS Major (" +
                "major_id INTEGER PRIMARY KEY AUTOINCREMENT, " +    // ID duy nhất của ngành học
                "name VARCHAR)");                                   // Tên ngành học

        // Tạo bảng BorrowRecords để lưu thông tin mượn sách
        db.execSQL("CREATE TABLE IF NOT EXISTS BorrowRecords (" +
                "record_id INTEGER PRIMARY KEY AUTOINCREMENT, " +   // ID duy nhất của bản ghi mượn sách
                "user_id INTEGER, " +                               // ID người dùng mượn sách
                "book_id INTEGER, " +                               // ID sách được mượn
                "borrow_date DATE, " +                              // Ngày mượn sách
                "return_date DATE, " +                              // Ngày dự kiến trả sách
                "actual_return_date DATE, " +                       // Ngày thực tế trả sách
                "status VARCHAR, " +                                // Trạng thái mượn sách (đã trả/chưa trả)
                "FOREIGN KEY (user_id) REFERENCES User(user_id), " +  // Khóa ngoại tới bảng User
                "FOREIGN KEY (book_id) REFERENCES Books(book_id))"); // Khóa ngoại tới bảng Books

        // Tạo bảng FineRecords để lưu thông tin phạt
        db.execSQL("CREATE TABLE IF NOT EXISTS FineRecords (" +
                "fine_id INTEGER PRIMARY KEY AUTOINCREMENT, " +     // ID duy nhất của bản ghi phạt
                "record_id INTEGER, " +                             // ID bản ghi mượn sách
                "amount DECIMAL, " +                                // Số tiền phạt
                "paid BOOLEAN, " +                                  // Trạng thái đã trả phạt (đã trả/chưa trả)
                "FOREIGN KEY (record_id) REFERENCES BorrowRecords(record_id))"); // Khóa ngoại tới bảng BorrowRecords

        // Tạo bảng Reviews để lưu đánh giá sách
        db.execSQL("CREATE TABLE IF NOT EXISTS Reviews (" +
                "review_id INTEGER PRIMARY KEY AUTOINCREMENT, " +   // ID duy nhất của đánh giá
                "book_id INTEGER, " +                               // ID sách được đánh giá
                "user_id INTEGER, " +                               // ID người dùng đánh giá
                "rating INTEGER, " +                                // Điểm đánh giá của người dùng (1-5)
                "comment TEXT, " +                                  // Nhận xét của người dùng
                "review_date DATE, " +                              // Ngày đánh giá
                "FOREIGN KEY (book_id) REFERENCES Books(book_id), " + // Khóa ngoại tới bảng Books
                "FOREIGN KEY (user_id) REFERENCES User(user_id))");// Khóa ngoại tới bảng User
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu tồn tại và tạo bảng mới khi cập nhật phiên bản
        db.execSQL("DROP TABLE IF EXISTS Account");
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS Books");
        db.execSQL("DROP TABLE IF EXISTS Locations");
        db.execSQL("DROP TABLE IF EXISTS Author");
        db.execSQL("DROP TABLE IF EXISTS Category");
        db.execSQL("DROP TABLE IF EXISTS Major");
        db.execSQL("DROP TABLE IF EXISTS BorrowRecords");
        db.execSQL("DROP TABLE IF EXISTS FineRecords");
        db.execSQL("DROP TABLE IF EXISTS Reviews");
        onCreate(db);
    }
    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Xóa tất cả các bảng cần thiết
            db.execSQL("DROP TABLE IF EXISTS Account");
            db.execSQL("DROP TABLE IF EXISTS User");
            db.execSQL("DROP TABLE IF EXISTS Books");
            db.execSQL("DROP TABLE IF EXISTS Locations");
            db.execSQL("DROP TABLE IF EXISTS Author");
            db.execSQL("DROP TABLE IF EXISTS Category");
            db.execSQL("DROP TABLE IF EXISTS Major");
            db.execSQL("DROP TABLE IF EXISTS BorrowRecords");
            db.execSQL("DROP TABLE IF EXISTS FineRecords");
            db.execSQL("DROP TABLE IF EXISTS Reviews");
            // Nếu cần, có thể xóa thêm các bảng khác
        } catch (Exception e) {
            // Xử lý lỗi (nếu có)
            e.printStackTrace();
        } finally {
            db.close(); // Đảm bảo đóng kết nối
        }
    }
    private void insertSampleData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO Account (username, password, role, user_id) VALUES ('user1', 'password1', 'reader', 1);");
        db.execSQL("INSERT INTO Account (username, password, role, user_id) VALUES ('admin', 'admin123', 'admin', 2);");

        db.execSQL("INSERT INTO User (name, gender, phone, email, student_code, major_id) VALUES ('Nguyen Van A', 'Male', '0123456789', 'a@example.com', 'SV001', 1);");
        db.execSQL("INSERT INTO User (name, gender, phone, email, student_code, major_id) VALUES ('Tran Thi B', 'Female', '0987654321', 'b@example.com', 'SV002', 2);");

        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, major_id, published_year, description, image) VALUES ('Book Title 1', 1, 1, 1, 1, 2020, 'Description of Book Title 1', 'image1.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, major_id, published_year, description, image) VALUES ('Book Title 2', 2, 2, 2, 2, 2021, 'Description of Book Title 2', 'image2.jpg');");

        db.execSQL("INSERT INTO Locations (name) VALUES ('Library A');");
        db.execSQL("INSERT INTO Locations (name) VALUES ('Library B');");

        db.execSQL("INSERT INTO Author (name) VALUES ('Author 1');");
        db.execSQL("INSERT INTO Author (name) VALUES ('Author 2');");

        db.execSQL("INSERT INTO Category (name) VALUES ('Fiction');");
        db.execSQL("INSERT INTO Category (name) VALUES ('Non-Fiction');");

        db.execSQL("INSERT INTO Major (name) VALUES ('Computer Science');");
        db.execSQL("INSERT INTO Major (name) VALUES ('Literature');");

        db.execSQL("INSERT INTO BorrowRecords (user_id, book_id, borrow_date, return_date, actual_return_date, status) VALUES (1, 1, '2024-11-01', '2024-11-15', NULL, 'Borrowed');");
        db.execSQL("INSERT INTO BorrowRecords (user_id, book_id, borrow_date, return_date, actual_return_date, status) VALUES (2, 2, '2024-11-02', '2024-11-16', NULL, 'Borrowed');");

        db.execSQL("INSERT INTO FineRecords (record_id, amount, paid) VALUES (1, 5.0, 0);");
        db.execSQL("INSERT INTO FineRecords (record_id, amount, paid) VALUES (2, 2.5, 1);");

        db.execSQL("INSERT INTO Reviews (book_id, user_id, rating, comment, review_date) VALUES (1, 1, 5, 'Great book!', '2024-11-01');");
        db.execSQL("INSERT INTO Reviews (book_id, user_id, rating, comment, review_date) VALUES (2, 2, 4, 'Interesting read.', '2024-11-02');");
    }
}
