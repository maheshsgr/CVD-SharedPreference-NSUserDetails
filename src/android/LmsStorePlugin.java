package com.lms.greatlakes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LmsStorePlugin extends CordovaPlugin {

	private CallbackContext callBackContext;
	private static final String TAG = "LmsStorePlugin";

	@Override
	public boolean execute(String action, final JSONArray args,
			final CallbackContext callbackContext) throws JSONException {
		// TODO Auto-generated method stub
		Log.d(TAG, "********** LMS STORE PLUGIN ****");
		super.execute(action, args, callbackContext);
		try {
			this.callBackContext = callbackContext;
			if (action.equals("doLogOut")) {
				doLogout();
				return true;
			} else if (action.equals("getAuthInformation")) {
				SharedPreferences settings = cordova
						.getActivity()
						.getApplicationContext()
						.getSharedPreferences("lmsGlobal", Context.MODE_PRIVATE);
				final JSONObject jsonObject = new JSONObject();
				jsonObject.put("domainName",settings.getString("domainName", ""));
				//send AccessToken and RefreshToken //
				jsonObject.put("accessToken",settings.getString("accessToken", ""));
				jsonObject.put("accessUserID", settings.getInt("userId", 0));
				jsonObject.put("accessUserName",settings.getString("userName", ""));
				jsonObject.put("isNewOauth",settings.getString("isNewOauth", ""));
				jsonObject.put("refreshToken",settings.getString("refreshToken", ""));
				jsonObject.put("tokenExpiredTime",settings.getString("tokenExpiredTime", ""));
				//send code //
				jsonObject.put("clientId",settings.getString("clientId", ""));
				jsonObject.put("redirectUri",settings.getString("redirectUri", ""));
				jsonObject.put("clientSecret", settings.getString("clientSecret", ""));
				jsonObject.put("code",settings.getString("code", ""));
				
				jsonObject.put("apiToken",settings.getString("apiToken", ""));
				jsonObject.put("accessType", settings.getString("accessType", ""));
				this.cordova.getThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						try {
							Log.d(TAG + "********** getAuthInformation ****",
									jsonObject.toString());
							callbackContext.success(jsonObject.toString());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							Log.e(TAG, e.getMessage());
							callbackContext.error(e.getMessage());
							e.printStackTrace();
						}
					}
				});
				return true;
			}else if (action.equals("updateAccessToken")){
				updateInformation(args);
				return true;
			}

			return false;
		} catch (Exception e) {
			Log.d("Exception", e.getMessage());
			return false;
		}
	}

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		// TODO Auto-generated method stub
		super.initialize(cordova, webView);
	}

	@SuppressWarnings("deprecation")
	private void doLogout() {
		Log.d(TAG, "********** doLogout() ****");
		try {
			CookieManager.getInstance().removeAllCookie();
			updateSharedPreferences("", 0 ,"","","","","");
			this.cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Context context = cordova.getActivity()
							.getApplicationContext();
					Intent intent = new Intent(context, DomainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			});
			this.cordova.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					if (cordova.getActivity() instanceof CordovaWebViewActivity) {
						cordova.getActivity().finish();
					}
				}
			});
		} catch (Exception e) {
			Log.d("Exception", e.getMessage());
		}
	}

	@Override
	public void onPause(boolean multitasking) {
		// TODO Auto-generated method stub
		super.onPause(multitasking);
	}

	@Override
	public void onResume(boolean multitasking) {
		// TODO Auto-generated method stub
		super.onResume(multitasking);
	}
	private void updateInformation(JSONArray args) throws JSONException{
		Log.d(TAG, "********** updateInformation() ****");	
//		[access_token,accessUserID,accessUserName,apiToken,isNewOauth,refresh_token,tokenExpiredTime]
		Log.d(TAG,args.getString(0));
		Log.d(TAG,args.getString(1));
		Log.d(TAG,args.getString(2));
		Log.d(TAG,args.getString(3));
		Log.d(TAG,args.getString(4));
		Log.d(TAG,args.getString(5));
		Log.d(TAG,args.getString(6));
		updateSharedPreferences(args.getString(0),Integer.parseInt(args.getString(1)),args.getString(2),args.getString(3),args.getString(4),args.getString(5),args.getString(6));
	}

	private void updateSharedPreferences(String accessToken, int userId , String userName, String apiToken , String isNewOauth, String refreshToken , String tokenExpiredTime) {
		SharedPreferences settings = this.cordova.getActivity()
				.getApplicationContext()
				.getSharedPreferences("lmsGlobal", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("accessToken", accessToken);
		editor.putInt("userId", userId);
		editor.putString("userName", userName);
		editor.putString("apiToken", apiToken);
		
		editor.putString("isNewOauth", isNewOauth);
		editor.putString("refreshToken", refreshToken);
		editor.putString("tokenExpiredTime", tokenExpiredTime);
		
		editor.commit();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
