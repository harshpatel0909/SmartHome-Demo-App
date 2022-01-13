 package com.example.SmartHome;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.SmartHome.Connection.MySingleton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    TextInputEditText textInputEditTextUsername, textInputEditTextPassword;
    Button buttonLogin;
    TextView textViewSignUp;
    SharedPreferences sp;
    CheckBox checkBoxKeepLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textInputEditTextUsername = findViewById(R.id.usernamelogin);
        textInputEditTextPassword = findViewById(R.id.passwordlogin);
        buttonLogin = findViewById(R.id.btnlogin);
        textViewSignUp = findViewById(R.id.SignupText);
        checkBoxKeepLogin = findViewById(R.id.cbKeepLogin);

        sp=getSharedPreferences("MyUserPref",Context.MODE_PRIVATE);
        String loginstatus = sp.getString(getResources().getString(R.string.loginstate),"");

        if (loginstatus.equals("loggedin")){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = textInputEditTextUsername.getText().toString();
                String password = textInputEditTextPassword.getText().toString();
                if (!username.equals("") && !password.equals("")){
                    login(username, password);
                }else{
                    Toast.makeText(getApplicationContext(), "All fields are Required", Toast.LENGTH_SHORT).show();
                }


            }
        });



    }
    private void login(String username, String password) {
        ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Login...");
        progressDialog.show();
        String uRl = MySingleton.getUrl()+"login.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals(" Login Success")) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username", username);
                    if (checkBoxKeepLogin.isChecked()) {
                        editor.putString(getResources().getString(R.string.loginstate),"loggedin");
                    }
                    else {
                        editor.putString(getResources().getString(R.string.loginstate),"loggedout");
                    }
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("username", username);
                param.put("password", password);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(Login.this).

                addToRequestQueue(request);
    }
}