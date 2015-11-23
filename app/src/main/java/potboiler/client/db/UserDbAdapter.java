package potboiler.client.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import potboiler.client.model.User;


public class UserDbAdapter {

	/*public void addUser(SQLiteDatabase db, User user){
		try{
	    	db.beginTransaction();
    		db.execSQL("insert into " + DbCommonHelper.USERS_TABLE_NAME + "(place, password, phone) " +
							" values(?,?,?)",
					new String[]{user.place, user.password, user.phone});
	    	db.setTransactionSuccessful();
    	} catch (SQLException e) {
    		e.printStackTrace();
    		throw new SQLException();
    	} finally {
    	  db.endTransaction();
    	}    
	}
	 
	public User getUserById(SQLiteDatabase db, int id){
		String sql="select _id, place, password, phone from "
				+DbCommonHelper.USERS_TABLE_NAME+" where _id=?";

        Cursor c = db.rawQuery(sql,new String[]{id+""});
        if (c != null) {  
        	if (c.moveToNext()) {
				User user = getUserFromCursor(c);
        		return user;
        	}
        }
        c.close();
		return null;
    }

	public User getUserByPhonePass(SQLiteDatabase db, String phone, String password){
		String sql="select _id, place, password, phone from "
				+DbCommonHelper.USERS_TABLE_NAME+" where password=? and phone=?";

		Cursor c = db.rawQuery(sql,new String[]{password, phone});
		if (c != null) {
			if (c.moveToNext()) {
				User user = getUserFromCursor(c);
				return user;
			}
		}
		c.close();
		return null;
	}

    public List<User> getUsers(SQLiteDatabase db){
        List<User> result = new ArrayList<User>();

        String sql="select _id, place, password, phone from "
                +DbCommonHelper.USERS_TABLE_NAME+"  order by _id";

        Cursor c = db.rawQuery(sql,new String[]{});
        if (c != null) {
            while (c.moveToNext()) {
                User image = getUserFromCursor(c);
                result.add(image);
            }
        }
		c.close();
        return result;
    }

	private User getUserFromCursor(Cursor c){
		final int ID = c.getColumnIndex("_id");
    	final int PLACE = c.getColumnIndex("place");
    	final int PASSWORD = c.getColumnIndex("password");
    	final int PHONE = c.getColumnIndex("phone");
		User user = new User();
		user.id = c.getInt(ID);
		user.place = c.getString(PLACE);
		user.password = c.getString(PASSWORD);
		user.phone = c.getString(PHONE);
		
		return user;
	}
	
	public void deleteUserById(SQLiteDatabase db, int id){
    	try{
	    	db.beginTransaction();
    		db.execSQL("delete from " + DbCommonHelper.USERS_TABLE_NAME + " where _id=?",
    				new String[]{id+""});
	    	db.setTransactionSuccessful();
    	} catch (SQLException e) {
    		e.printStackTrace();
    		throw new SQLException();
    	} finally {
    	  db.endTransaction();
    	}    	   
	}

	public void deleteUsers(SQLiteDatabase db){
		try{
			db.beginTransaction();
			db.execSQL("delete from " + DbCommonHelper.USERS_TABLE_NAME,
					new String[]{});
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			db.endTransaction();
		}
	}*/

}
