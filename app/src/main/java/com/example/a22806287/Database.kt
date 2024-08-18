package com.example.a22806287

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Defining the Database Class and Utilizing SQLITE DB
class Database (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Database Information
    companion object{

        // Database Name & Version
        private const val DATABASE_NAME = "DB"
        private const val DATABASE_VERSION = 1

        // Table Names
        const val ADMIN_TABLE = "admin"
        const val STUDENT_TABLE = "student"
        const val POST_TABLE = "post"
        const val COMMENT_TABLE = "comment"

        // Admin Table Columns
        const val ADMIN_ID_COL = "adminID"
        const val ADMIN_EMAIL_COL = "adminEmail"
        const val ADMIN_PASSWORD_COL = "adminPassword"

        // Student Table Columns
        const val STUDENT_ID_COL = "studentID"
        const val STUDENT_FULL_NAME_COL = "fullName"
        const val STUDENT_SCHOOL_EMAIL_COL = "schoolEmail"
        const val STUDENT_DATE_OF_BIRTH_COL = "DateOfBirth"
        const val STUDENT_PASSWORD_COL = "studentPassword"

        // Post Table Columns
        const val POST_ID_COL = "postID"
        const val POST_TITLE_COL = "postTitle"
        const val POST_DESCRIPTION_COL = "postDescription"

        // Comment Table Columns
        const val COMMENT_ID_COL = "commentID"
        const val COMMENT_COL = "comment"
        const val SCORE_COL = "score"

        // GlobalFunctions Columns
        const val DATE_CREATED_COL = "dateCreated"
        const val DATE_UPDATED_COL = "dateUpdated"
    }

    // Creating the Tables and Inserting Data
    override fun onCreate(db: SQLiteDatabase?) {

        val adminQuery = ("CREATE TABLE " + ADMIN_TABLE + " ("
                + ADMIN_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ADMIN_EMAIL_COL + " TEXT," +
                ADMIN_PASSWORD_COL + " TEXT," +
                DATE_CREATED_COL + " TEXT" + ")")

        val studentQuery = ("CREATE TABLE " + STUDENT_TABLE + " ("
                + STUDENT_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STUDENT_FULL_NAME_COL + " TEXT," +
                STUDENT_SCHOOL_EMAIL_COL + " TEXT," +
                STUDENT_DATE_OF_BIRTH_COL + " TEXT," +
                STUDENT_PASSWORD_COL + " TEXT," +
                DATE_CREATED_COL + " TEXT," +
                DATE_UPDATED_COL + " TEXT" + ")")

        val postQuery = ("CREATE TABLE " + POST_TABLE + " ("
                + POST_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STUDENT_ID_COL + " INTEGER," +
                POST_TITLE_COL + " TEXT," +
                POST_DESCRIPTION_COL + " TEXT," +
                DATE_CREATED_COL + " TEXT," +
                DATE_UPDATED_COL + " TEXT," +
                " FOREIGN KEY(" + STUDENT_ID_COL + ") REFERENCES "+ STUDENT_TABLE +" ("+ STUDENT_ID_COL +"))")

        val commentQuery = ("CREATE TABLE " + COMMENT_TABLE + " ("
                + COMMENT_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                POST_ID_COL + " INTEGER," +
                STUDENT_ID_COL + " INTEGER," +
                COMMENT_COL + " TEXT," +
                SCORE_COL + " TEXT," +
                DATE_CREATED_COL + " TEXT," +
                " FOREIGN KEY(" + POST_ID_COL + ") REFERENCES "+ POST_TABLE +" ("+ POST_ID_COL +")," +
                " FOREIGN KEY(" + STUDENT_ID_COL + ") REFERENCES "+ STUDENT_TABLE +" ("+ STUDENT_ID_COL +"))")

        val dataInsertAdmin1 = ("INSERT INTO $ADMIN_TABLE " +
                "( $ADMIN_ID_COL,$ADMIN_EMAIL_COL,$ADMIN_PASSWORD_COL, $DATE_CREATED_COL) " +
                "VALUES (0 , 'Admin1@gmail.com', 'admin', 'null')")

        val dataInsertAdmin2 = ("INSERT INTO $ADMIN_TABLE " +
                "( $ADMIN_ID_COL,$ADMIN_EMAIL_COL,$ADMIN_PASSWORD_COL, $DATE_CREATED_COL) " +
                "VALUES (1 , 'Admin2@gmail.com', 'admin', 'null')")

        if(db != null){
            db.execSQL(adminQuery)
            db.execSQL(studentQuery)
            db.execSQL(postQuery)
            db.execSQL(commentQuery)
            db.execSQL(dataInsertAdmin1)
            db.execSQL(dataInsertAdmin2)
        }
    }

    // Upgrading the Database
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

        val adminQuery = "DROP TABLE IF EXISTS $ADMIN_TABLE"
        val studentQuery = "DROP TABLE IF EXISTS $STUDENT_TABLE"
        val postQuery = "DROP TABLE IF EXISTS $POST_TABLE"
        val commentQuery = "DROP TABLE IF EXISTS $COMMENT_TABLE"

        if(db != null){
            db.execSQL(adminQuery)
            db.execSQL(studentQuery)
            db.execSQL(postQuery)
            db.execSQL(commentQuery)
            onCreate(db)
        }
    }

    // Checks for Admin Credentials
    fun checkAdmin(email: String, password: String): Boolean {
        val columns = arrayOf(
            ADMIN_EMAIL_COL
        )
        val db = this.readableDatabase
        val selection =
            "$ADMIN_EMAIL_COL = ? AND $ADMIN_PASSWORD_COL = ?"
        val selectionArgs = arrayOf(email, password)

        val cursor: Cursor = db.query(
            ADMIN_TABLE,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val cursorCount: Int = cursor.count
        cursor.close()
        db.close()
        return cursorCount > 0
    }

    // Adds a Students Information to the Database
    fun addStudent(
        studentName: String,
        studentEmail: String,
        studentPassword: String,
        dateOfBirth: String,
        dateCreated: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(STUDENT_FULL_NAME_COL, studentName)
        values.put(STUDENT_SCHOOL_EMAIL_COL, studentEmail)
        values.put(STUDENT_PASSWORD_COL, studentPassword)
        values.put(STUDENT_DATE_OF_BIRTH_COL, dateOfBirth)
        values.put(DATE_CREATED_COL, dateCreated)

        db.insert(STUDENT_TABLE, null, values)

        db.close()
    }

    // Updates a Students Information
    fun updateStudent(
        studentID: Int,
        studentName: String,
        studentEmail: String,
        studentPassword: String,
        dateOfBirth: String,
        dateUpdated: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(STUDENT_FULL_NAME_COL, studentName)
        values.put(STUDENT_SCHOOL_EMAIL_COL, studentEmail)
        values.put(STUDENT_PASSWORD_COL, studentPassword)
        values.put(STUDENT_DATE_OF_BIRTH_COL, dateOfBirth)
        values.put(DATE_UPDATED_COL, dateUpdated)

        db.update(STUDENT_TABLE, values, "$STUDENT_ID_COL=?", arrayOf(studentID.toString()))
        db.close()
    }

    // Check if the Student's Credentials are valid
    fun checkStudent(email: String, password: String): Boolean {
        val columns = arrayOf(
            STUDENT_SCHOOL_EMAIL_COL
        )
        val db = this.readableDatabase
        val selection =
            "$STUDENT_SCHOOL_EMAIL_COL = ? AND $STUDENT_PASSWORD_COL = ?"
        val selectionArgs = arrayOf(email, password)

        val cursor: Cursor = db.query(
            STUDENT_TABLE,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val cursorCount: Int = cursor.count
        cursor.close()
        db.close()
        return cursorCount > 0
    }

    // Fetch Student Information
    fun getStudent(email: String): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery(
            "SELECT * FROM $STUDENT_TABLE WHERE $STUDENT_SCHOOL_EMAIL_COL = '$email'",
            null
        )

    }

    // Fetch Student Information BY ID
    fun getStudentByID(studentID: Int): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery(
            "SELECT * FROM $STUDENT_TABLE WHERE $STUDENT_ID_COL = '$studentID'",
            null
        )

    }

    // Check the Student Email
    fun checkStudentEmail(email: String): Boolean {
        val columns = arrayOf(
            STUDENT_SCHOOL_EMAIL_COL
        )
        val db = this.readableDatabase

        val selection = "$STUDENT_SCHOOL_EMAIL_COL = ?"

        val selectionArgs = arrayOf(email)

        val cursor: Cursor = db.query(
            STUDENT_TABLE,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val cursorCount: Int = cursor.count
        cursor.close()
        db.close()
        return cursorCount > 0
    }

    // Fetch Post Information BY ID
    fun getPost(postID: Int): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery(
            "SELECT * FROM $POST_TABLE WHERE $POST_ID_COL = '$postID'",
            null
        )

    }

    // Add a Post
    fun addPost(
        studentID: Int,
        postTitle: String,
        postDescription: String,
        dateCreated: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(STUDENT_ID_COL, studentID)
        values.put(POST_TITLE_COL, postTitle)
        values.put(POST_DESCRIPTION_COL, postDescription)
        values.put(DATE_CREATED_COL, dateCreated)

        db.insert(POST_TABLE, null, values)

        db.close()
    }

    // Update a Post
    fun updatePost(
        postID: Int,
        postTitle: String,
        postDescription: String,
        dateUpdated: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(POST_ID_COL, postID)
        values.put(POST_TITLE_COL, postTitle)
        values.put(POST_DESCRIPTION_COL, postDescription)
        values.put(DATE_UPDATED_COL, dateUpdated)

        db.update(POST_TABLE, values, "$POST_ID_COL=?", arrayOf(postID.toString()))
        db.close()
    }

    // Add a Comment
    fun addComment(
        studentID: Int,
        postID: Int,
        comment: String,
        dateCreated: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(STUDENT_ID_COL, studentID)
        values.put(POST_ID_COL, postID)
        values.put(COMMENT_COL, comment)
        values.put(SCORE_COL, 0)
        values.put(DATE_CREATED_COL, dateCreated)

        db.insert(COMMENT_TABLE, null, values)

        db.close()
    }

    // Update a comments Score
    fun updateCommentScore(
        commentID: Int,
        score: Int
    ) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COMMENT_ID_COL, commentID)
        values.put(SCORE_COL, score)

        db.update(COMMENT_TABLE, values, "$COMMENT_ID_COL=?", arrayOf(commentID.toString()))
        db.close()
    }

    // Fetch all Messages for a specific Post
    fun getAllMessagesPost(tableName: String, postID: Int): Cursor {

        val db = readableDatabase
        val query = "SELECT * FROM $tableName WHERE $POST_ID_COL = '$postID'"
        return db.rawQuery(query, null)

    }

    // Reusable Delete Table Column Function
    fun delete(tableName: String, nameID: String, id: Int) {

        val db = this.writableDatabase

        db.delete(tableName, "$nameID=?", arrayOf(id.toString()))
        db.close()
    }

    // Reusable Fetch All Table Column Function
    fun getAll(tableName: String): Cursor {

        val db = readableDatabase
        val query = "SELECT * FROM $tableName"
        return db.rawQuery(query, null)

    }

}