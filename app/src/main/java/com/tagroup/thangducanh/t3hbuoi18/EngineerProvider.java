package com.tagroup.thangducanh.t3hbuoi18;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tagroup.thangducanh.t3hbuoi18.database.EngineerSQLiteHelper;

public class EngineerProvider extends ContentProvider{

    private static final String AUTHORITY = "com.tagroup.thangducanh.t3hbuoi18.EngineerProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY + "/" + EngineerSQLiteHelper.ENGINEER_TABLE);

    private EngineerSQLiteHelper sqLiteHelper;

    private static final UriMatcher mUriMatcher;

    public static final int ENGINEER = 1;
    public static final int ID = 2;

    private EngineerSQLiteHelper mDataHelper;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, "engineer", ENGINEER);
        mUriMatcher.addURI(AUTHORITY, "engineer/#", ID);
    }

    @Override
    public boolean onCreate() {
        sqLiteHelper = new EngineerSQLiteHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mDataHelper.getReadableDatabase(); // có thể dược dùng cả getWriteable
        Cursor cursor = null;
        switch (mUriMatcher.match(uri)) {
            case ENGINEER:
                //TODO Case 1
                cursor = db.query(EngineerSQLiteHelper.ENGINEER_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                //TODO Case 2
                //cursor=db.rawQuery("SELECT * FROM "+EngineerSQLiteHelper.ENGINEER_TABLE,null);
                //TODO Case 3
                //SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                //qb.setTables(EngineerSQLiteHelper.ENGINEER_TABLE);
                //switch (mUriMatcher.match(uri)) {
                //  case ID:
                //      String id = uri.getPathSegments().get(1);
                //      qb.appendWhere(EngineerSQLiteHelper.ID + " = '" + id + "'");
                //   default:
                //   reak;
                //}
                //SQLiteDatabase db = mDataHelper.getWritableDatabase();
                //Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, null);
                //c.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            case ID:
                //TODO 2 ways to get param for query
                String id = uri.getPathSegments().get(1);
                //String id2=selectionArgs[0];
                cursor = db.query(EngineerSQLiteHelper.ENGINEER_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                //TODO Case 2
                //cursor = db.rawQuery("SELECT * FROM " + EngineerSQLiteHelper.ENGINEER_TABLE, null);
                //cursor=db.rawQuery("SELECT * FROM " + EngineerSQLiteHelper.ENGINEER_TABLE + " WHERE id= " + id, null);
                break;
        }
        return cursor;


//        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (mUriMatcher.match(uri) != ENGINEER) {
            throw new IllegalArgumentException("Unknow URI " + uri);
        }
        SQLiteDatabase db = mDataHelper.getWritableDatabase();
        long rowId = db.insert(EngineerSQLiteHelper.ENGINEER_TABLE, EngineerSQLiteHelper.NAME, values);
        if (rowId > 0) {
            Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(resultUri, null);
            return resultUri;
        }
        throw new IllegalArgumentException("Faild to insert row into " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mDataHelper.getWritableDatabase();
        int count = 0;
        switch (mUriMatcher.match(uri)) {
            case ENGINEER:
                count = db.update(EngineerSQLiteHelper.ENGINEER_TABLE, values, selection, selectionArgs);
                break;
            case ID:
                String id = uri.getPathSegments().get(1);
                String where = EngineerSQLiteHelper.ID + " = '" + id + "'";
                if (selection != null) {
                    where = where + " and " + selection;
                }
                count = db.update(EngineerSQLiteHelper.ENGINEER_TABLE, values, where, selectionArgs);
                break;
            default:
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDataHelper.getWritableDatabase();
        int count = 0;
        switch (mUriMatcher.match(uri)) {
            case ENGINEER:
                count = db.delete(EngineerSQLiteHelper.ENGINEER_TABLE, selection, selectionArgs);
                break;
            case ID:
                String id = uri.getPathSegments().get(1);
                String where = EngineerSQLiteHelper.ID + " = '" + id + "'";
                if (selection != null) {
                    where = where + " and " + selection;
                }
                count = db.delete(EngineerSQLiteHelper.ENGINEER_TABLE, where, selectionArgs);
                break;
            default:
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

}
