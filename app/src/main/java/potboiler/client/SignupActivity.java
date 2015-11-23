package potboiler.client;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import potboiler.client.api.ApiClient;
import potboiler.client.model.ServerResponseStatus;
import potboiler.client.util.AuthAdapter;

public class SignupActivity extends ActionBarActivity implements View.OnClickListener, ApiClient.OnSignup, AuthAdapter.AuthListener {

    private EditText mLogin, mPassword, mPassword2;
    private ProgressDialog mProgressDialog;

    private AuthAdapter mAuthAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuthAdapter = new AuthAdapter(this, this);

        initViews();
        initDlg();
    }

    private void initDlg(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Пожалуйста подождите...");
    }


    private void initViews(){
        ((Button)findViewById(R.id.signup)).setOnClickListener(this);
        mLogin = (EditText)findViewById(R.id.login);
        mPassword = (EditText)findViewById(R.id.password);
        mPassword2 = (EditText)findViewById(R.id.password2);

        ((ImageView)findViewById(R.id.signin_fb)).setOnClickListener(this);
        ((ImageView)findViewById(R.id.signin_vk)).setOnClickListener(this);
        ((ImageView)findViewById(R.id.signin_ok)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.signup){
            String login = mLogin.getText().toString();
            String password = mPassword.getText().toString();
            String password2 = mPassword2.getText().toString();
            if(login.toString().equals("")||password.trim().equals("")){
                Toast.makeText(this, "Введите данные",Toast.LENGTH_LONG).show();
            }
            else if(!password.trim().equals(password2.trim())){
                Toast.makeText(this, "Пароли не совпадают",Toast.LENGTH_LONG).show();
            }
            else {
                mProgressDialog.show();
                new ApiClient(this).signup(this, 0, login, login, password, "","0");
            }
        }
        if(v.getId()==R.id.signin_fb){
            mAuthAdapter.fbLogin(this);
        }
        if(v.getId()==R.id.signin_vk){
            mAuthAdapter.vkLogin(this);
        }
        if(v.getId()==R.id.signin_ok){
            mAuthAdapter.okLogin();
        }
    }

    @Override
    public void onSignup(ServerResponseStatus responseStatus) {
        mProgressDialog.dismiss();
        if(responseStatus==ServerResponseStatus.OK){
            Intent intent = new Intent(this, PotsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this,responseStatus.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFbLogin(String email, String token, String userId) {
        mProgressDialog.show();
        new ApiClient(this).signup(this, 2, email, email, "", token, userId);
    }

    @Override
    public void onVkLogin(String token, String userId) {
        mProgressDialog.show();
        new ApiClient(this).signup(this, 1, "", "", "", token, userId);
    }

    @Override
    public void onOkLogin(String token) {
        mProgressDialog.show();
        new ApiClient(this).signup(this, 3, "", "", "", token, "0");
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mAuthAdapter.vkActivityResult(requestCode,resultCode,data)) {
            mAuthAdapter.fbActivityResult(requestCode,resultCode,data);
        }
    }
}
