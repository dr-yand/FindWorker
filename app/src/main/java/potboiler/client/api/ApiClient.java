package potboiler.client.api;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import potboiler.client.model.Category;
import potboiler.client.model.Pot;
import potboiler.client.model.PotTemp;
import potboiler.client.model.ServerResponseStatus;
import potboiler.client.model.User;
import potboiler.client.util.CommonUtils;
import potboiler.client.util.FileUtils;
import potboiler.client.util.PreferenceUtils;


public class ApiClient {
	
	private Context mContext;
	
	public ApiClient(Context context){
		mContext = context;
	}

	public interface OnSignin{
		public void onSignin(ServerResponseStatus responseStatus);
	}

	public void signin(final OnSignin listener, final String login, final String password){
		GeneralTask.OnResultListener resultListener = new GeneralTask.OnResultListener() {
			ServerResponseStatus responseCode = ServerResponseStatus.ERROR;

			@Override
			public void onParseResult(String responseStr) {
				User user = new User();
				try{
					JSONObject jObject= new JSONObject(responseStr);
					responseCode = ServerResponseStatus.valueOf(jObject.getInt("result"));
					if(responseCode == ServerResponseStatus.OK){
						JSONObject userInfoObject = jObject.getJSONObject("info");
						user.serverId = userInfoObject.getInt("id_user");
						user.email = userInfoObject.getString("email");
						user.secretkey = userInfoObject.getString("secretkey");
						if(userInfoObject.has("name"))
							user.username = userInfoObject.getString("name");
						if(userInfoObject.has("avatar"))
							user.avatar = userInfoObject.getString("avatar");
						FileUtils.downloadFile(user.avatar,user.serverId+"");
						user.isConfirmedEmail = userInfoObject.getInt("confirm_email")==1;
						PreferenceUtils.saveUser(mContext, user);
					}
				}
				catch(Exception e){
					responseCode = ServerResponseStatus.ERROR;
					e.printStackTrace();
				}
			}

			@Override
			public void onReturnResult(){
				if(mContext instanceof Activity) {
					if(!((Activity)mContext).isFinishing()
							&& listener!=null){
						listener.onSignin(responseCode);
					}
				}
			}
		};
		Map<String, Object> params = new HashMap<>();
		params.put("mode", "1");
		params.put("soc_id", "0");
		params.put("username", login);
		params.put("password", CommonUtils.md5(password));
//		params.put("token", token);
        new GeneralTask(resultListener, mContext, params).execute(new Void[]{});
	}

    public interface OnRestore{
        public void onRestore(ServerResponseStatus responseStatus);
    }

    public void restore(final OnRestore listener, final String login){
        GeneralTask.OnResultListener resultListener = new GeneralTask.OnResultListener() {
			ServerResponseStatus responseCode = ServerResponseStatus.ERROR;

            @Override
            public void onParseResult(String responseStr) {
                User user = new User();
                try{
                    JSONObject jObject= new JSONObject(responseStr);
                    responseCode = ServerResponseStatus.valueOf(jObject.getInt("result"));
                }
                catch(Exception e){
                    responseCode = ServerResponseStatus.ERROR;
                    e.printStackTrace();
                }
            }


			@Override
			public void onReturnResult(){
				if(mContext instanceof Activity) {
					if(!((Activity)mContext).isFinishing()
							&& listener!=null){
						listener.onRestore(responseCode);
					}
				}
			}
        };
        Map<String, Object> params = new HashMap<>();
        params.put("mode", "2");
        params.put("email", login);
        new GeneralTask(resultListener, mContext, params).execute(new Void[]{});
    }

	public interface OnSignup{
		public void onSignup(ServerResponseStatus responseStatus);
	}

	public void signup(final OnSignup listener, int socInd, String username, String email, String password,
					   String token, String uid){
		GeneralTask.OnResultListener resultListener = new GeneralTask.OnResultListener() {
			ServerResponseStatus responseCode = ServerResponseStatus.ERROR;

			@Override
			public void onParseResult(String responseStr) {
				User user = new User();
				try{
					JSONObject jObject= new JSONObject(responseStr);
					responseCode = ServerResponseStatus.valueOf(jObject.getInt("result"));
					if(responseCode == ServerResponseStatus.OK){
                        JSONObject userInfoObject = jObject.getJSONObject("info");
						user.serverId = userInfoObject.getInt("id_user");
						user.email = userInfoObject.getString("email");
						if(userInfoObject.has("name"))
							user.username = userInfoObject.getString("name");
						if(userInfoObject.has("avatar"))
							user.avatar = userInfoObject.getString("avatar");
						FileUtils.downloadFile(user.avatar,FileUtils.getImagesPath(mContext)+user.serverId+"");
						user.secretkey = userInfoObject.getString("secretkey");
						PreferenceUtils.saveUser(mContext, user);
					}
				}
				catch(Exception e){
					responseCode = ServerResponseStatus.ERROR;
					e.printStackTrace();
				}
			}

			@Override
			public void onReturnResult(){
				if(mContext instanceof Activity) {
					if(!((Activity)mContext).isFinishing()
							&& listener!=null){
						listener.onSignup(responseCode);
					}
				}
			}
		};
		Map<String, Object> params = new HashMap<>();
		params.put("mode", "0");
		params.put("soc_id", socInd+"");
		params.put("username", username);
		params.put("email", email);
		params.put("password", CommonUtils.md5(password));
		params.put("token", token);
        params.put("uid", uid);
		new GeneralTask(resultListener, mContext, params).execute(new Void[]{});
	}

    public interface OnNewPot{
        public void onNewPot(ServerResponseStatus responseStatus);
    }

    public void newPot(final OnNewPot listener, PotTemp potTemp){
        GeneralTask.OnResultListener resultListener = new GeneralTask.OnResultListener() {
			ServerResponseStatus responseCode = ServerResponseStatus.ERROR;

            @Override
            public void onParseResult(String responseStr) {
                User user = new User();
                try{
                    JSONObject jObject= new JSONObject(responseStr);
                    responseCode = ServerResponseStatus.valueOf(jObject.getInt("result"));
                    if(responseCode == ServerResponseStatus.OK){
//                        JSONObject userInfoObject = jObject.getJSONObject("info");
//                        user.serverId = userInfoObject.getInt("id_user");
//                        user.email = userInfoObject.getString("email");
//                        user.secretkey = userInfoObject.getString("secretkey");
//                        PreferenceUtils.saveUser(mContext, user);
                    }
                }
                catch(Exception e){
                    responseCode = ServerResponseStatus.ERROR;
                    e.printStackTrace();
                }
            }


			@Override
			public void onReturnResult(){
				if(mContext instanceof Activity) {
					if(!((Activity)mContext).isFinishing()
							&& listener!=null){
						listener.onNewPot(responseCode);
					}
				}
			}
        };
        User user = PreferenceUtils.getUser(mContext);
        Map<String, Object> params = new HashMap<>();
        params.put("mode", "6");
        params.put("id_user", user.serverId+"");
        params.put("secretkey", user.secretkey);
        params.put("id_cat", potTemp.getCategory().getId());
        params.put("name", potTemp.getName());
        params.put("text", potTemp.getDescription());
        params.put("srok", new Date().getTime()+3600*24*7*1000+"");
        params.put("summ1", "0");
        params.put("summ2", "0");
        params.put("time_live", "600");
        params.put("age1", "0");
        params.put("age2", "0");
        params.put("weight", "100");
//        params.put("data_start", "0");
        params.put("show_all", "1");
        params.put("min_rating", "0");

        JSONArray adresArray = new JSONArray();
        JSONObject adres1 = new JSONObject();
        JSONObject adres2 = new JSONObject();
        try {
            adres1.put("coord_lat",39);
            adres1.put("coord_lon",51);
            adres1.put("adresname","мой дом 1");
            adres2.put("coord_lat",51);
            adres2.put("coord_lon",39);
            adres2.put("adresname","мой дом 2");
            adresArray.put(adres1);
            adresArray.put(adres2);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        params.put("adres", adresArray);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		Bitmap bitmap2 = BitmapFactory.decodeFile("/storage/emulated/0/Download/6244096.jpg", options);

		ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
		bitmap2.compress(Bitmap.CompressFormat.JPEG, 90, bao2);
		byte[] ba2 = bao2.toByteArray();
		String baStr2 = Base64.encodeToString(ba2, Base64.NO_WRAP);
//		params.put("jpg", baStr2);

		Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/Download/6244096.jpg", options);

		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 90, bao);
		byte[] ba = bao.toByteArray();
		String baStr = Base64.encodeToString(ba, Base64.NO_WRAP);

		JSONArray jsonArray = new JSONArray();
		JSONObject pngImage = new JSONObject();
		JSONObject jpgImage = new JSONObject();
		try {
			pngImage.put("num",1);
			pngImage.put("file",baStr);
			pngImage.put("ext","png");
			jpgImage.put("num",2);
			jpgImage.put("file",baStr2);
			jpgImage.put("ext","jpg");
            jsonArray.put(pngImage);
            jsonArray.put(jpgImage);
		} catch (Exception e) {
			e.printStackTrace();
		}

//		params.put("png", baStr);

//		params.put("foto", jsonArray);

        new GeneralTask(resultListener, mContext, params).execute(new Void[]{});
    }
 /*
	public interface OnAuth{
		public void onAuth(ServerResponseStatus responseStatus);
	}

	public void signin(final OnAuth listener, final User user){
		String action = "login";
		GeneralTask.OnResultListener resultListener = new GeneralTask.OnResultListener() {
			@Override
			public void onExecuteResult(String responseStr) {
				ServerResponseStatus responseCode = ServerResponseStatus.ERROR;
				int userId = -1;
				try{
					JSONObject jObject= new JSONObject(responseStr);
					responseCode = ServerResponseStatus.valueOf(jObject.getInt("response"));
					if(responseCode == ServerResponseStatus.OK){
						userId = jObject.getInt("id");
						user.id = userId;
						PreferenceUtils.saveUser(mContext,user);
					}
				}
				catch(Exception e){
					responseCode = ServerResponseStatus.ERROR;
					e.printStackTrace();
				}
				if(mContext instanceof Activity) {
					if(!((Activity)mContext).isFinishing()
							&& listener!=null){
						listener.onAuth(responseCode);
					}
				}
			}
		};
		Map<String, String> params = new HashMap<>();
		params.put("email", user.email.toLowerCase());
		params.put("password", user.password);
		new GeneralTask(resultListener, mContext, action, params).execute(new Void[]{});
	}

	public interface OnAddComment{
		public void onAddComment(ServerResponseStatus responseStatus);
	}

	public void addComment(final OnAddComment listener, final User user, final String imageId, String info){
		String action = "comment";
		GeneralTask.OnResultListener resultListener = new GeneralTask.OnResultListener() {
			@Override
			public void onExecuteResult(String responseStr) {
				ServerResponseStatus responseCode = ServerResponseStatus.ERROR;
				try{
					JSONObject jObject= new JSONObject(responseStr);
					responseCode = ServerResponseStatus.valueOf(jObject.getInt("response"));
					if(responseCode == ServerResponseStatus.OK){
                        DbCommonHelper dbHelper = new DbCommonHelper(mContext);
                        CommentDbAdapter dbAdapter = new CommentDbAdapter();
                        dbAdapter.deleteComments(dbHelper.open(), imageId);
                        if(!jObject.isNull("comments")) {
                            JSONArray commentsArray = jObject.getJSONArray("comments");
                            for (int i = 0; i < commentsArray.length(); i++) {
                                JSONObject commentObject = commentsArray.getJSONObject(i);
                                Comment c = new Comment();
                                c.serverId = commentObject.getString("id");
                                c.imageId = commentObject.getString("image_id");
                                c.userId = commentObject.getString("user_id");
                                c.info = commentObject.getString("info");
                                c.commentDate = commentObject.getString("comment_date");
                                c.userName = commentObject.getString("user_name");
                                dbAdapter.addComment(dbHelper.open(), c);
                            }
                        }
                        dbHelper.close();
					}
				}
				catch(Exception e){
					responseCode = ServerResponseStatus.ERROR;
					e.printStackTrace();
				}
				if(mContext instanceof Activity) {
					if(!((Activity)mContext).isFinishing()
							&& listener!=null){
						listener.onAddComment(responseCode);
					}
				}
			}
		};
		Map<String, String> params = new HashMap<>();
		params.put("user_id", user.id+"");
		params.put("password", user.password);
		params.put("email", user.email);
		params.put("image_id", imageId);
		params.put("info", info);
		new GeneralTask(resultListener, mContext, action, params).execute(new Void[]{});
	}*/


	public interface OnGetPots{
		public void onGetPots(ServerResponseStatus responseStatus, List<Pot> potList);
	}

	public void getPots(final OnGetPots listener){
		GeneralTask.OnResultListener resultListener = new GeneralTask.OnResultListener() {
			ServerResponseStatus responseCode = ServerResponseStatus.ERROR;
			List<Pot> potList = new ArrayList<>();

			@Override
			public void onParseResult(String responseStr) {
				try{
					JSONObject jObject= new JSONObject(responseStr);
					responseCode = ServerResponseStatus.valueOf(jObject.getInt("result"));
					if(responseCode == ServerResponseStatus.OK){
						JSONArray list = jObject.getJSONArray("list");
						for(int i=0;i<list.length();i++){
							JSONObject potObject = list.getJSONObject(i);
							Pot pot = new Pot(potObject);
							potList.add(pot);
						}
					}
				}
				catch(Exception e){
					responseCode = ServerResponseStatus.ERROR;
					e.printStackTrace();
				}
			}

			@Override
			public void onReturnResult(){
				if(mContext instanceof Activity) {
					if(!((Activity)mContext).isFinishing()
							&& listener!=null){
						listener.onGetPots(responseCode, potList);
					}
				}
			}
		};
		User user = PreferenceUtils.getUser(mContext);
		Map<String, Object> params = new HashMap<>();
		params.put("mode", "7");
		params.put("id_user", user.serverId+"");
		params.put("secretkey", user.secretkey);
		params.put("filter", 0);
		params.put("offset", 0);
		params.put("limit", 100);
//		params.put("summ1", 0);
//		params.put("summ2", 0);
//		params.put("age1", 0);
//		params.put("age2", 0);
//		params.put("min_rating", 0);
		new GeneralTask(resultListener, mContext, params).execute(new Void[]{});
	}

	public interface OnGetCategories{
		public void onGetCategories(ServerResponseStatus responseStatus, List<Category> list);
	}

	public void getCategories(final OnGetCategories listener, String idParent){
		GeneralTask.OnResultListener resultListener = new GeneralTask.OnResultListener() {
			ServerResponseStatus responseCode = ServerResponseStatus.ERROR;
			List<Category> potList = new ArrayList<>();

			@Override
			public void onParseResult(String responseStr) {
				try{
					JSONObject jObject= new JSONObject(responseStr);
					responseCode = ServerResponseStatus.valueOf(jObject.getInt("result"));
					if(responseCode == ServerResponseStatus.OK){
						if(jObject.get("info") instanceof  JSONArray) {
							JSONArray list = jObject.getJSONArray("info");
							for (int i = 0; i < list.length(); i++) {
								JSONObject potObject = list.getJSONObject(i);
								Category category = new Category(potObject);
								potList.add(category);
							}
						}
					}
				}
				catch(Exception e){
					responseCode = ServerResponseStatus.ERROR;
					e.printStackTrace();
				}
			}

			@Override
			public void onReturnResult(){
				if(mContext instanceof Activity) {
					if(!((Activity)mContext).isFinishing()
							&& listener!=null){
						listener.onGetCategories(responseCode, potList);
					}
				}
			}
		};
		User user = PreferenceUtils.getUser(mContext);
		Map<String, Object> params = new HashMap<>();
		params.put("mode", "50");
		params.put("id_user", user.serverId+"");
		params.put("secretkey", user.secretkey);
		params.put("id_parent", idParent);
		new GeneralTask(resultListener, mContext, params).execute(new Void[]{});
	}

	public interface OnGetCities{
		public void onGetCities(ServerResponseStatus responseStatus, List<Category> list);
	}

	public void getCities(final OnGetCities listener, String idParent){
		GeneralTask.OnResultListener resultListener = new GeneralTask.OnResultListener() {
			ServerResponseStatus responseCode = ServerResponseStatus.ERROR;
			List<Category> potList = new ArrayList<>();

			@Override
			public void onParseResult(String responseStr) {
				try{
					JSONObject jObject= new JSONObject(responseStr);
					responseCode = ServerResponseStatus.valueOf(jObject.getInt("result"));
					if(responseCode == ServerResponseStatus.OK){
						if(jObject.get("info") instanceof  JSONArray) {
							JSONArray list = jObject.getJSONArray("info");
							for (int i = 0; i < list.length(); i++) {
								JSONObject potObject = list.getJSONObject(i);
								Category category = new Category(potObject);
								potList.add(category);
							}
						}
					}
				}
				catch(Exception e){
					responseCode = ServerResponseStatus.ERROR;
					e.printStackTrace();
				}
			}

			@Override
			public void onReturnResult(){
				if(mContext instanceof Activity) {
					if(!((Activity)mContext).isFinishing()
							&& listener!=null){
						listener.onGetCities(responseCode, potList);
					}
				}
			}
		};
		User user = PreferenceUtils.getUser(mContext);
		Map<String, Object> params = new HashMap<>();
		params.put("mode", "50");
		params.put("id_user", user.serverId+"");
		params.put("secretkey", user.secretkey);
		params.put("id_parent", idParent);
		new GeneralTask(resultListener, mContext, params).execute(new Void[]{});
	}

	public interface OnGetUserInfo{
		public void onGetUserInfo(ServerResponseStatus responseStatus);
	}

	public void getUserInfo(final OnGetUserInfo listener, String userId){
		GeneralTask.OnResultListener resultListener = new GeneralTask.OnResultListener() {
			ServerResponseStatus responseCode = ServerResponseStatus.ERROR;

			@Override
			public void onParseResult(String responseStr) {
				User user = new User();
				try{
					JSONObject jObject= new JSONObject(responseStr);
					responseCode = ServerResponseStatus.valueOf(jObject.getInt("result"));
					if(responseCode == ServerResponseStatus.OK){
						JSONObject userInfoObject = jObject.getJSONObject("info");
						JSONObject block1Object = userInfoObject.getJSONObject("block1");
//						user.serverId = userInfoObject.getInt("id_user");
//						user.email = userInfoObject.getString("email");
//						user.secretkey = userInfoObject.getString("secretkey");
//						if(userInfoObject.has("name"))
//							user.username = userInfoObject.getString("name");
//						if(userInfoObject.has("avatar"))
//							user.avatar = userInfoObject.getString("avatar");
//						FileUtils.downloadFile(user.avatar,user.serverId+"");
//						user.isConfirmedEmail = userInfoObject.getInt("confirm_email")==1;
					}
				}
				catch(Exception e){
					responseCode = ServerResponseStatus.ERROR;
					e.printStackTrace();
				}
			}

			@Override
			public void onReturnResult(){
				if(mContext instanceof Activity) {
					if(!((Activity)mContext).isFinishing()
							&& listener!=null){
						listener.onGetUserInfo(responseCode);
					}
				}
			}
		};
		User user = PreferenceUtils.getUser(mContext);
		Map<String, Object> params = new HashMap<>();
		params.put("mode", "3");
		params.put("id_user", user.serverId+"");
		params.put("secretkey", user.secretkey);
		if(userId!=null)
			params.put("id_profile", userId);
		new GeneralTask(resultListener, mContext, params).execute(new Void[]{});
	}

	public void getMyInfo(final OnGetUserInfo listener){
		getUserInfo(listener, null);
	}
}

