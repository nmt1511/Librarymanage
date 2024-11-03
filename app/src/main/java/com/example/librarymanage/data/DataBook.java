package com.example.librarymanage.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.librarymanage.entities.Book;

import java.util.ArrayList;
import java.util.List;

public class DataBook extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    // Tên cơ sở dữ liệu
    private static final String DATABASE_NAME = "LibraryManagement.db";
    // Phiên bản cơ sở dữ liệu
    private static final int DATABASE_VERSION = 1;

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
                "student_code VARCHAR)");

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
        db.execSQL("INSERT INTO Account (username, password, role, user_id) VALUES ('user', 'user', 'reader', 1);");
        db.execSQL("INSERT INTO Account (username, password, role, user_id) VALUES ('admin', 'admin', 'admin', 2);");

        db.execSQL("INSERT INTO User (name, gender, phone, email, student_code) VALUES ('Nguyen Minh Thuan', 'Male', '0123456789', 'a@example.com', 'D21CNTT01');");
        db.execSQL("INSERT INTO User (name, gender, phone, email, student_code) VALUES ('Triệu Tử Long', 'Male', '0987654321', 'a@example.com', '212480123456');");
        db.execSQL("INSERT INTO User (name, gender, phone, email, student_code) VALUES ('Nguyễn Hải Hà', 'Female', '0987654322', 'b@example.com', '212480654321');");
        db.execSQL("INSERT INTO User (name, gender, phone, email, student_code) VALUES ('Thiên Hạ', 'Male', '0987654323', 'c@example.com', '212480987654');");
        db.execSQL("INSERT INTO User (name, gender, phone, email, student_code) VALUES ('Trần Thảo Linh', 'Female', '0987654324', 'd@example.com', '212480123789');");
        db.execSQL("INSERT INTO User (name, gender, phone, email, student_code) VALUES ('Trần Đăng Khoa', 'Male', '0987654325', 'e@example.com', '212480456123');");
        db.execSQL("INSERT INTO User (name, gender, phone, email, student_code) VALUES ('Trần Ngọc Mạnh', 'Male', '0987654326', 'f@example.com', '212480321654');");
        db.execSQL("INSERT INTO User (name, gender, phone, email, student_code) VALUES ('Hồng Quân', 'Male', '0987654327', 'g@example.com', '212480789456');");
        db.execSQL("INSERT INTO User (name, gender, phone, email, student_code) VALUES ('Nguyễn Hứa Văn', 'Male', '0987654328', 'h@example.com', '212480654789');");
        db.execSQL("INSERT INTO User (name, gender, phone, email, student_code) VALUES ('Hà Đình An', 'Male', '0987654329', 'i@example.com', '212480987321');");
        db.execSQL("INSERT INTO User (name, gender, phone, email, student_code) VALUES ('Vũ Nga', 'Female', '0987654330', 'j@example.com', '212480123654');");
        db.execSQL("INSERT INTO User (name, gender, phone, email, student_code) VALUES ('Nguyễn Văn Linh', 'Male', '0987654331', 'k@example.com', '212480456789');");

        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Dấu chân người lính', 1, 1, 6, 1986, 'Tiểu thuyết về cuộc sống của những người lính trong chiến tranh.', 'image1.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Sống chết mặc bay', 1, 2, 6, 1945, 'Tác phẩm nổi tiếng phản ánh đời sống và tâm tư của con người.', 'image2.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Chí Phèo', 1, 3, 6, 1941, 'Một trong những tác phẩm tiêu biểu của Nam Cao.', 'image3.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Hương rừng', 1, 4, 6, 1990, 'Tác phẩm miêu tả vẻ đẹp của thiên nhiên và con người.', 'image4.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Vang bóng một thời', 1, 5, 6, 1940, 'Tác phẩm nổi tiếng về văn hóa và xã hội Việt Nam.', 'image5.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Tâm sự với người lạ', 1, 6, 6, 1991, 'Tác phẩm nổi bật của Lỗ Tấn.', 'image6.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Trăm năm cô đơn', 1, 7, 6, 1967, 'Tiểu thuyết nổi tiếng của Gabriel Garcia Marquez.', 'image7.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Norwegian Wood', 1, 8, 6, 1987, 'Tác phẩm nổi bật của Haruki Murakami.', 'image8.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('The Old Man and the Sea', 1, 9, 6, 1952, 'Một trong những tác phẩm nổi tiếng nhất của Ernest Hemingway.', 'image9.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('1984', 1, 10, 6, 1949, 'Tiểu thuyết phản ánh xã hội toàn trị của George Orwell.', 'image10.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Tự nhiên miền Tây', 1, 11, 7, 2020, 'Khám phá thiên nhiên và sinh thái miền Tây.', 'image2.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Lịch sử khoa học và công nghệ', 1, 12, 7, 2018, 'Nghiên cứu về sự phát triển khoa học và công nghệ.', 'image3.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Khoa học về thời gian', 1, 13, 7, 2019, 'Tìm hiểu về khái niệm thời gian trong khoa học.', 'image4.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Sự phát triển của vũ trụ', 1, 14, 7, 2021, 'Khám phá nguồn gốc và sự phát triển của vũ trụ.', 'image5.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Lịch sử sinh học', 1, 15, 7, 2022, 'Nghiên cứu sự phát triển của sự sống trên trái đất.', 'image6.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('English Grammar in Use', 1, 16, 5, 2015, 'Cuốn sách ngữ pháp tiếng Anh phổ biến.', 'image7.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('The Elements of Style', 1, 17, 5, 2000, 'Hướng dẫn viết tiếng Anh hiệu quả.', 'image8.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Fluent English', 1, 18, 5, 2016, 'Kỹ năng giao tiếp tiếng Anh lưu loát.', 'image9.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Word Power Made Easy', 1, 19, 5, 2018, 'Tăng cường vốn từ vựng tiếng Anh.', 'image10.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('The Catcher in the Rye', 1, 20, 5, 1951, 'Tiểu thuyết nổi tiếng về tuổi trẻ.', 'image11.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Từ điển tiếng Việt', 1, 21, 4, 2007, 'Từ điển cơ bản cho tiếng Việt.', 'image12.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Từ điển Anh-Việt', 1, 22, 4, 2010, 'Từ điển cần thiết cho việc học tiếng Anh.', 'image13.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Ngữ pháp tiếng Việt', 1, 23, 4, 2015, 'Tài liệu tham khảo về ngữ pháp tiếng Việt.', 'image14.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Từ điển y học', 1, 24, 4, 2016, 'Từ điển về y học và sức khỏe.', 'image15.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Sách giáo khoa lịch sử', 1, 25, 4, 2020, 'Sách giáo khoa dành cho học sinh.', 'image16.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Nghiên cứu về văn hóa dân gian', 1, 26, 3, 2017, 'Phân tích văn hóa dân gian Việt Nam.', 'image17.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Phân tích văn học', 1, 27, 3, 2019, 'Phương pháp phân tích tác phẩm văn học.', 'image18.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Sách nghiên cứu về tâm lý học', 1, 28, 3, 2018, 'Khám phá các khía cạnh của tâm lý học.', 'image19.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Nghiên cứu xã hội học', 1, 29, 3, 2021, 'Nghiên cứu về xã hội và con người.', 'image20.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Lịch sử tư tưởng chính trị', 1, 30, 3, 2022, 'Tổng quan về tư tưởng chính trị qua các thời kỳ.', 'image21.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Đắc Nhân Tâm', 1, 31, 8, 1936, 'Hướng dẫn phát triển kỹ năng giao tiếp.', 'image22.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Người bán hàng vĩ đại nhất thế giới', 1, 32, 8, 1968, 'Câu chuyện truyền cảm hứng về bán hàng.', 'image23.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('7 thói quen của bạn trẻ thành đạt', 1, 33, 8, 1989, 'Những thói quen giúp bạn thành công.', 'image24.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Sống như một vị vua', 1, 34, 8, 2014, 'Hướng dẫn sống tốt và thành công.', 'image25.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Thay đổi thói quen', 1, 35, 8, 2012, 'Cách thay đổi thói quen để sống tốt hơn.', 'image26.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Lịch sử Việt Nam', 1, 36, 9, 2002, 'Khái quát lịch sử Việt Nam qua các thời kỳ.', 'image27.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Sự hình thành và phát triển của văn hóa Việt Nam', 1, 37, 9, 2010, 'Nghiên cứu văn hóa Việt Nam.', 'image28.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Văn hóa Việt Nam qua các thời kỳ', 1, 38, 9, 2015, 'Tìm hiểu văn hóa qua các thời kỳ.', 'image29.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Lịch sử thế giới', 1, 39, 9, 2018, 'Khái quát lịch sử thế giới.', 'image30.jpg');");
        db.execSQL("INSERT INTO Books (title, location_id, author_id, category_id, published_year, description, image) VALUES ('Văn hóa và xã hội Việt Nam', 1, 40, 9, 2020, 'Nghiên cứu văn hóa và xã hội Việt Nam hiện đại.', 'image31.jpg');");


        db.execSQL("INSERT INTO Locations (name) VALUES ('Kệ Sách Văn Học');");
        db.execSQL("INSERT INTO Locations (name) VALUES ('Kệ Sách Khoa Học');");
        db.execSQL("INSERT INTO Locations (name) VALUES ('Kệ Sách Ngoại Ngữ');");
        db.execSQL("INSERT INTO Locations (name) VALUES ('Kệ Sách Tham Khảo');");
        db.execSQL("INSERT INTO Locations (name) VALUES ('Kệ Sách Nghiên Cứu');");
        db.execSQL("INSERT INTO Locations (name) VALUES ('Kệ Sách Lịch Sử');");

        db.execSQL("INSERT INTO Author (name) VALUES ('Nguyễn Minh Châu');");
        db.execSQL("INSERT INTO Author (name) VALUES ('Nam Cao');");
        db.execSQL("INSERT INTO Author (name) VALUES ('Tô Hoài');");
        db.execSQL("INSERT INTO Author (name) VALUES ('Nguyễn Huy Thiệp');");
        db.execSQL("INSERT INTO Author (name) VALUES ('Nguyễn Tuân');");
        db.execSQL("INSERT INTO Author (name) VALUES ('Lỗ Tấn');");
        db.execSQL("INSERT INTO Author (name) VALUES ('Gabriel Garcia Marquez');");
        db.execSQL("INSERT INTO Author (name) VALUES ('Haruki Murakami');");
        db.execSQL("INSERT INTO Author (name) VALUES ('Ernest Hemingway');");
        db.execSQL("INSERT INTO Author (name) VALUES ('George Orwell');");

        db.execSQL("INSERT INTO Category (name) VALUES ('Sách giáo khoa');");
        db.execSQL("INSERT INTO Category (name) VALUES ('Sách tham khảo');");
        db.execSQL("INSERT INTO Category (name) VALUES ('Sách nghiên cứu');");
        db.execSQL("INSERT INTO Category (name) VALUES ('Tài liệu chuyên ngành');");
        db.execSQL("INSERT INTO Category (name) VALUES ('Sách ngoại ngữ');");
        db.execSQL("INSERT INTO Category (name) VALUES ('Sách văn học');");
        db.execSQL("INSERT INTO Category (name) VALUES ('Sách kỹ năng sống');");
        db.execSQL("INSERT INTO Category (name) VALUES ('Sách lịch sử và văn hóa');");
        db.execSQL("INSERT INTO Category (name) VALUES ('Sách khoa học');");
        db.execSQL("INSERT INTO Category (name) VALUES ('Tạp chí và báo cáo khoa học');");


//        db.execSQL("INSERT INTO BorrowRecords (user_id, book_id, borrow_date, return_date, actual_return_date, status) VALUES (1, 1, '2024-11-01', '2024-11-15', '2024-11-10', 'Đã Trả');");
//        db.execSQL("INSERT INTO BorrowRecords (user_id, book_id, borrow_date, return_date, actual_return_date, status) VALUES (2, 2, '2024-11-02', '2024-11-16', '2024-11-12', 'Đã Trả');");
//        db.execSQL("INSERT INTO BorrowRecords (user_id, book_id, borrow_date, return_date, actual_return_date, status) VALUES (3, 3, '2024-11-03', '2024-11-17', NULL, 'Mượn');");
//        db.execSQL("INSERT INTO BorrowRecords (user_id, book_id, borrow_date, return_date, actual_return_date, status) VALUES (4, 4, '2024-11-04', '2024-11-18', NULL, 'Mượn');");
//        db.execSQL("INSERT INTO BorrowRecords (user_id, book_id, borrow_date, return_date, actual_return_date, status) VALUES (5, 5, '2024-11-05', '2024-11-19', '2024-11-15', 'Đã Trả');");
//        db.execSQL("INSERT INTO BorrowRecords (user_id, book_id, borrow_date, return_date, actual_return_date, status) VALUES (6, 6, '2024-11-06', '2024-11-20', NULL, 'Mượn');");
//        db.execSQL("INSERT INTO BorrowRecords (user_id, book_id, borrow_date, return_date, actual_return_date, status) VALUES (7, 7, '2024-11-07', '2024-11-21', '2024-11-19', 'Đã Trả');");
//        db.execSQL("INSERT INTO BorrowRecords (user_id, book_id, borrow_date, return_date, actual_return_date, status) VALUES (8, 8, '2024-11-08', '2024-11-22', NULL, 'Mượn');");
//        db.execSQL("INSERT INTO BorrowRecords (user_id, book_id, borrow_date, return_date, actual_return_date, status) VALUES (9, 9, '2024-11-09', '2024-11-23', '2024-11-20', 'Đã Trả');");
//        db.execSQL("INSERT INTO BorrowRecords (user_id, book_id, borrow_date, return_date, actual_return_date, status) VALUES (10, 10, '2024-11-10', '2024-11-24', NULL, 'Mượn');");

        db.execSQL("INSERT INTO FineRecords (record_id, amount, paid) VALUES (1, 15.0, 0);");
        db.execSQL("INSERT INTO FineRecords (record_id, amount, paid) VALUES (2, 15.0, 1);");
        db.execSQL("INSERT INTO FineRecords (record_id, amount, paid) VALUES (3, 15.0, 0);");
        db.execSQL("INSERT INTO FineRecords (record_id, amount, paid) VALUES (4, 15.0, 1);");
        db.execSQL("INSERT INTO FineRecords (record_id, amount, paid) VALUES (5, 15.0, 0);");

        db.execSQL("INSERT INTO Reviews (book_id, user_id, rating, comment, review_date) VALUES (1, 1, 5, 'Cuốn sách tuyệt vời!', '2024-11-01');");
        db.execSQL("INSERT INTO Reviews (book_id, user_id, rating, comment, review_date) VALUES (2, 2, 4, 'Đọc rất thú vị.', '2024-11-02');");
        db.execSQL("INSERT INTO Reviews (book_id, user_id, rating, comment, review_date) VALUES (3, 3, 5, 'Một cuốn sách không thể bỏ qua!', '2024-11-03');");
        db.execSQL("INSERT INTO Reviews (book_id, user_id, rating, comment, review_date) VALUES (4, 4, 3, 'Khá trung bình.', '2024-11-04');");
        db.execSQL("INSERT INTO Reviews (book_id, user_id, rating, comment, review_date) VALUES (5, 5, 4, 'Rất sâu sắc.', '2024-11-05');");
    }
}
