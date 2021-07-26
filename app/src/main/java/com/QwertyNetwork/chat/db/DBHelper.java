package com.QwertyNetwork.chat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.QwertyNetwork.chat.models.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final int VERSIO_BD = 1;
    public static final String NAME_BD = "qwertyChats.bd";
    public static final String TABLE_USERS = "users";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_NICK = "user_nick";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_PHONE = "phone";
    public static final String COLUMN_USER_EMAIL = "email";

    private User user = new User();

    public static final String CREATE_TABLES = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT," + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_NICK + " TEXT," + COLUMN_USER_PHONE + " INTEGER,"
            + COLUMN_USER_PASSWORD + " INTEGER" + ")";
    public static final String DROP_TABLES = "DROP TABLE IF EXISTS " + TABLE_USERS;

    public DBHelper(Context context) {
        super(context, NAME_BD, null, VERSIO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLES);
        onCreate(db);
    }

    /* Этот метод предназначен для создания пользовательской записи */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesC = new ContentValues();
        valuesC.put(COLUMN_USER_NAME, user.getName());
        valuesC.put(COLUMN_USER_EMAIL, user.getEmail());
        valuesC.put(COLUMN_USER_NICK, user.getNick());
        valuesC.put(COLUMN_USER_PHONE, user.getPhone());
        valuesC.put(COLUMN_USER_PASSWORD, user.getPass());

        db.insert(TABLE_USERS, null, valuesC);
        db.close();
    }

    /* Этот метод состоит в том, чтобы получить всех пользователей и вернуть список пользовательских записей. */
    public List<User> getAllUser() {
        // массив столбцов для выборки
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_NICK
        };

        // сортировка заказов
        String sortOrder = COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();

        // запрашиваем пользовательскую таблицу
        Cursor cursor = db.query(TABLE_USERS,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);
        // Обходим все строки и добавляем в список
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setNick(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NICK)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPass(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                user.setPhone(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE))));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // возвращаем список пользователей
        return userList;
    }

    /*  Этот метод обновления пользовательской записи */
    public void updateUser (User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_NICK, user.getNick());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_PASSWORD, user.getPass());
        // удаляем запись пользователя по id
        db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /* Этот метод, чтобы проверить, существует ли пользователь по email*/
    public boolean checkUser(String email) {
        // массив столбцов для выборки
        String[] columns = {COLUMN_USER_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        // критерий отбора
        String selection = COLUMN_USER_EMAIL + " = ?";
        // аргумент выбора
        String[] selectionArgs = {email};

        // запрашиваем пользовательскую таблицу с условием
        /*
        Здесь функция запроса используется для извлечения записей из пользовательской таблицы, эта функция работает так же, как мы используем запрос sql.
         * SQL-запрос, эквивалентный этой функции запроса:
         * ВЫБЕРИТЕ user_id ОТ пользователя WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USERS, // Таблица для запроса
                columns,                      // Cтолбцы для возврата
                selection,                    // столбцы для предложения WHERE
                selectionArgs,                // Значения для предложения WHERE
                null,                 // группируем строки
                null,                  // фильтрация по группам строк
                null);                // Порядок сортировки
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    /* Этот метод, чтобы проверить, существует ли пользователь по email и паролю*/
    public boolean checkUser ( String email, String password ) {

        // массив столбцов для выборки
        String[] columns = {COLUMN_USER_ID};

        SQLiteDatabase db = this.getReadableDatabase();
        // критерий отбора
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // аргументы выбора
        String[] selectionArgs = {email, password};

        // запрашиваем пользовательскую таблицу с условиями
        Cursor cursor = db.query(TABLE_USERS, // Таблица для запроса
                columns,                      // Cтолбцы для возврата
                selection,                    // столбцы для предложения WHERE
                selectionArgs,                // Значения для предложения WHERE
                null,                 // группируем строки
                null,                  // фильтрация по группам строк
                null);                // Порядок сортировки
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
}
