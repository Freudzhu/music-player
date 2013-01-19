
package com.example.persistance;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Mp3Info;

// Import - Android Content
import android.content.ContentValues;
import android.content.Context;

// Import - Android Database
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.DatabaseUtils;

public class Database extends SQLiteOpenHelper
{

	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "SimplePlayer";
	private static final String TABLE_NAME = "Mp3";

	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";

	private static final String KEY_ARTIST = "artist";
	private static final String KEY_ALBUM = "album";

	private static final String KEY_SRC = "src";



	public Database(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
				  + KEY_ID + " INTEGER PRIMARY KEY, "
				  + KEY_TITLE + " TEXT, "
				  + KEY_ARTIST + " TEXT, "
				  + KEY_ALBUM + " TEXT, "
				  + KEY_SRC + " TEXT )";
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db,
								 int oldVersion,
								 int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public void push(final Mp3Info mp3)
	{

		write(new DbWriteOp()
		{
			public void write(SQLiteDatabase db)
			{
				ContentValues values = new ContentValues();

				values.put(KEY_TITLE, mp3.getMp3Name());
				values.put(KEY_ARTIST, mp3.getArtist());
				values.put(KEY_ALBUM, mp3.getAlbumArt());
				values.put(KEY_SRC, mp3.getSrc());


				db.insert(TABLE_NAME, null, values);


			}
		});
	}

	public List<Mp3Info> pull()
	{
		return read(new DbReadOp<List<Mp3Info>>()
		{
			public List<Mp3Info> read(SQLiteDatabase db)
			{
				List<Mp3Info> tList = new ArrayList<Mp3Info>();

				String query = "SELECT * FROM " + TABLE_NAME;

				Cursor cursor = db.rawQuery(query, null);

				Mp3Info mp3;

				if (cursor.moveToFirst())
				{
					do
					{
						mp3 = new Mp3Info();
						mp3.setId(cursor.getString(1));
						mp3.setMp3Name(cursor.getString(2));
						mp3.setArtist(cursor.getString(3));
						mp3.setAlbumArt(cursor.getString(4));
						tList.add(mp3);

					} while (cursor.moveToNext());
				}

				return tList;
			}
		});
	}


	public void clear()
	{
		write(new DbWriteOp()
		{
		
			public void write(SQLiteDatabase db)
			{
				db.execSQL("DELETE FROM " + TABLE_NAME);
			}
		});
	}

	public boolean isEmpty()
	{
		return read(new DbReadOp<Boolean>()
		{
			
			public Boolean read(SQLiteDatabase db)
			{
				return DatabaseUtils.queryNumEntries(db, TABLE_NAME) == 0;
			}
		});
	}

	//--------------------------------------------- DB Access helper

	private interface DbReadOp<R>
	{
		public R read(SQLiteDatabase db);
	}

	private interface DbWriteOp
	{
		public void write(SQLiteDatabase db);
	}

	private <R> R read(DbReadOp<R> dbOperation)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		try
		{
			return dbOperation.read(db);
		} finally
		{
			db.close();
		}
	}

	private void write(DbWriteOp dbOperation)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		try
		{
			dbOperation.write(db);
		} finally
		{
			db.close();
		}
	}

	
}