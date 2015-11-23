package potboiler.client.util;

import android.app.ProgressDialog;
import android.content.Context;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonUtils {
    private static ProgressDialog mProgressDialog;

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static ProgressDialog showProgressDialog(Context context){
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Пожалуйста подождите...");
        mProgressDialog.show();
        return mProgressDialog;
    }

    public static void hideProgressDialog(){
        if(mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }
}
