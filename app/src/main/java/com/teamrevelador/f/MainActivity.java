package com.teamrevelador.f;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.teamrevelador.f.Constants.SharedPreferencesConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String EMAIL = "email";
    CallbackManager callbackManager;
    LoginButton loginButton;
    SharedPreferences sharedPreferences;

    private ProfileTracker profileTracker;
    private String firstName,lastName, email,birthday,gender;
    private URL profilePicture;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(SharedPreferencesConstants.prefsName, MODE_PRIVATE);

        callbackManager = CallbackManager.Factory.create();


        boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
        loginButton = findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(EMAIL,"user_status","public_profile","user_posts","read_custom_friendlists"));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                sharedPreferences.edit()
                        .putBoolean(SharedPreferencesConstants.loginStatus, true)
                        .putString(SharedPreferencesConstants.prefsAccessToken,loginResult.getAccessToken()+"")
                        .apply();
                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e("TAG",object.toString());
                        Log.e("TAG",response.toString());

                        try {
                            userId = object.getString("id");
                            profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");
                            if(object.has("first_name"))
                                firstName = object.getString("first_name");
                            if(object.has("last_name"))
                                lastName = object.getString("last_name");
                            if (object.has("email"))
                                email = object.getString("email");
                            if (object.has("birthday"))
                                birthday = object.getString("birthday");
                            if (object.has("gender"))
                                gender = object.getString("gender");

                            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                            intent.putExtra("name",firstName);
                            intent.putExtra("surname",lastName);
                            intent.putExtra("imageUrl",profilePicture.toString());
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //Here we put the requested fields to be returned from the JSONObject
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, birthday, gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(MainActivity.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(MainActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void fbButtonClicked(View view) {
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
