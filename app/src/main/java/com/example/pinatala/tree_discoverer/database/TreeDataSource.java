package com.example.pinatala.tree_discoverer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pinatala.tree_discoverer.model.TreeMarker;

import java.util.ArrayList;

/**
 * Created by YouYang on 14/12/16.
 */

public class TreeDataSource {
    //Create the fields
    private Context mContext;
    private TreeDatabaseOpenHelper mTreeDatabaseOpenHelper;

    public TreeDataSource(Context context){
        mContext = context;
        mTreeDatabaseOpenHelper = new TreeDatabaseOpenHelper(mContext);
    }

    private SQLiteDatabase open(){
        return mTreeDatabaseOpenHelper.getWritableDatabase();
    }

    private void close(SQLiteDatabase database){
        database.close();
    }

    //TreeMarker arrayList Retrieve methods: read() + readTreeMarkers()
    public ArrayList<TreeMarker> read(){
        ArrayList<TreeMarker> treeMarkers = readTreeMarkers();
        return treeMarkers;
    }

    private ArrayList<TreeMarker> readTreeMarkers() {
        SQLiteDatabase database = open();
        Cursor cursor = database.query(
                TreeDatabaseOpenHelper.TREE_TABLE_NAME,
                new String[]{TreeDatabaseOpenHelper.KEY_TREE_ID, TreeDatabaseOpenHelper.KEY_TREE_PHOTO_NAME,
                TreeDatabaseOpenHelper.KEY_LEAF_PHOTO_NAME, TreeDatabaseOpenHelper.KEY_DATE,
                TreeDatabaseOpenHelper.KEY_LATITUDE, TreeDatabaseOpenHelper.KEY_LONGITUDE,
                TreeDatabaseOpenHelper.KEY_TYPE_NAME},
                null, //selection
                null, //selection args
                null, //group by
                null, //having
                null); //order

        ArrayList<TreeMarker> treeMarkers = new ArrayList<>();
        // It goes through each row of the database and add them to the treeMarkers Arraylist.
        if(cursor.moveToFirst()){
            do{
                TreeMarker treeMarker = new TreeMarker(getIntFromColumnName(cursor,TreeDatabaseOpenHelper.KEY_TREE_ID),
                        getStringFromColumnName(cursor, TreeDatabaseOpenHelper.KEY_TREE_PHOTO_NAME),
                        getStringFromColumnName(cursor, TreeDatabaseOpenHelper.KEY_LEAF_PHOTO_NAME),
                        getStringFromColumnName(cursor, TreeDatabaseOpenHelper.KEY_DATE),
                        getDoubleFromColumnName(cursor, TreeDatabaseOpenHelper.KEY_LATITUDE),
                        getDoubleFromColumnName(cursor, TreeDatabaseOpenHelper.KEY_LONGITUDE),
                        getStringFromColumnName(cursor, TreeDatabaseOpenHelper.KEY_TYPE_NAME));
                treeMarkers.add(treeMarker);
            }while (cursor.moveToNext());
        }
        cursor.close();
        close(database);
        return treeMarkers;
    }

    //Support methods for readTreeMarkers()
    private int getIntFromColumnName(Cursor cursor, String columnName){
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getInt(columnIndex);
    }

    private String getStringFromColumnName(Cursor cursor, String columnName){
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getString(columnIndex);
    }

    private Double getDoubleFromColumnName(Cursor cursor, String columnName){
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getDouble(columnIndex);
    }

    //Method to create(insert) new treeMarker
    public void create(TreeMarker treeMarker){
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues treeValues = new ContentValues();
        treeValues.put(TreeDatabaseOpenHelper.KEY_TREE_ID, treeMarker.getId());
        treeValues.put(TreeDatabaseOpenHelper.KEY_TREE_PHOTO_NAME, treeMarker.getTreeImageName());
        treeValues.put(TreeDatabaseOpenHelper.KEY_LEAF_PHOTO_NAME,treeMarker.getLeafImageName());
        treeValues.put(TreeDatabaseOpenHelper.KEY_DATE, treeMarker.getCreateDate());
        treeValues.put(TreeDatabaseOpenHelper.KEY_LATITUDE, treeMarker.getLatitude());
        treeValues.put(TreeDatabaseOpenHelper.KEY_LONGITUDE, treeMarker.getLongitude());
        treeValues.put(TreeDatabaseOpenHelper.KEY_TYPE_NAME, treeMarker.getTreeType());

        long treeID = database.insert(TreeDatabaseOpenHelper.TREE_TABLE_NAME, null, treeValues);

        database.setTransactionSuccessful();;
        database.endTransaction();
        close(database);
    }

    //Method to delete one treeMarker record by ID
    public void delete (int treeId){
        SQLiteDatabase database = open();
        database.beginTransaction();

        //delete the item in table TREE_TABLE_NAME, whose id is equal to treeId
        database.delete(TreeDatabaseOpenHelper.TREE_TABLE_NAME,
                String.format("%s=%s", TreeDatabaseOpenHelper.KEY_TREE_ID, String.valueOf(treeId)),
                null);

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    //Method to update one treeMarker record
    // we use this to deleted one item by setting it "deleted_tree"
    public void update (int treeId){
        SQLiteDatabase database = open();
        database.beginTransaction();

        //update the item in table TREE_TABLE_NAME, whose id is equal to treeId
        ContentValues treeValues = new ContentValues();
        treeValues.put(TreeDatabaseOpenHelper.KEY_TREE_PHOTO_NAME, "");
        treeValues.put(TreeDatabaseOpenHelper.KEY_LEAF_PHOTO_NAME,"");
        treeValues.put(TreeDatabaseOpenHelper.KEY_DATE, "");
        treeValues.put(TreeDatabaseOpenHelper.KEY_LATITUDE, "");
        treeValues.put(TreeDatabaseOpenHelper.KEY_LONGITUDE, "");
        treeValues.put(TreeDatabaseOpenHelper.KEY_TYPE_NAME, "deleted_tree");

        database.update(TreeDatabaseOpenHelper.TREE_TABLE_NAME,
                treeValues,
                String.format("%s=%s", TreeDatabaseOpenHelper.KEY_TREE_ID, String.valueOf(treeId)),
                null);

        database.setTransactionSuccessful();
        database.endTransaction();
    }
}
