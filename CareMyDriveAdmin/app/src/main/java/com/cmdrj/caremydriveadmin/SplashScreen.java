package com.cmdrj.caremydriveadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    int AUTHUI_REQUEST_CODE = 10001;
//    Animation btmAnim;

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;

//    RelativeLayout splash_screen_RL1, splash_screen_RL2;
//    ImageView splash_screen_logoService, splash_screen_logoCar;
    Button splash_screen_loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


//        btmAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
//
//        splash_screen_RL1 = findViewById(R.id.splash_screen_RL1);
//        splash_screen_RL2 = findViewById(R.id.splash_screen_RL2);
//        splash_screen_logoService = findViewById(R.id.splash_screen_logoService);
//        splash_screen_logoName = findViewById(R.id.splash_screen_logoName);
        splash_screen_loginButton = findViewById(R.id.splash_screen_loginButton);


//        splash_screen_RL2.setAnimation(btmAnim);

//        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(splash_screen_logoCar, "scaleX", 0.7f);
//        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(splash_screen_logoCar, "scaleY", 0.7f);
//        scaleDownX.setDuration(1500);
//        scaleDownY.setDuration(1500);

//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int logoService_height = getResources().getDrawable(R.drawable.logo_service).getMinimumHeight();
//        ObjectAnimator moveUpY = ObjectAnimator.ofFloat(splash_screen_logoCar, "translationY", -1* (metrics.heightPixels - (logoService_height - 25)  -25));
//        moveUpY.setDuration(1500);

////        Log.d("TAG", "where + " + -1* (metrics.heightPixels - logoService_height));

//        AnimatorSet scaleDown = new AnimatorSet();
//        AnimatorSet moveUp = new AnimatorSet();
//
//        scaleDown.play(scaleDownX).with(scaleDownY);
//        moveUp.play(moveUpY);
//
//        scaleDown.setStartDelay(500);
//        moveUp.setStartDelay(500);
//        scaleDown.start();
//        moveUp.start();

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        if (fUser != null) {
            splash_screen_loginButton.setText("Start");
        }

        splash_screen_loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                splash_screen_loginButton.setEnabled(false);
                if (fUser == null) {

                    splash_screen_loginButton.setText("Logging-In.....");

                    List<AuthUI.IdpConfig> provider = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().setAllowedCountries(Collections.singletonList("IN")).setDefaultNumber(getString(R.string.admin_PhoneNo)).build());

                    Intent intent = AuthUI.getInstance()
                            .createSignInIntentBuilder()
//                    MaterialComponents.DayNight.DarkActionBar
                            .setTheme(R.style.LoginTheme)
                            .setAvailableProviders(provider)
//                            .setTosAndPrivacyPolicyUrls("https://q4sdg.com/","https://q4sdg.com/") // TODO : enter site T&C and policy
                            .setIsSmartLockEnabled(false)
                            .setLogo(R.drawable.logo)   // TODO : change logo
                            .build();

                    startActivityForResult(intent,AUTHUI_REQUEST_CODE);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                    Intent intent = new Intent(SplashScreen.this,LoginRegister.class);
//                    startActivity(intent);
//                    finish();
//                    startLoginActivity();
                }
                else
                {
                    splash_screen_loginButton.setText("Starting.....");

                    if(fUser.getUid().equals(getString(R.string.admin_uid)))
                    {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    else
                    {
                        AuthUI.getInstance().signOut(SplashScreen.this);
                        fUser = null;
                        Snackbar.make(findViewById(android.R.id.content), "No Luck!", Snackbar.LENGTH_LONG).show();

                    }

                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AUTHUI_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {

                fUser = fAuth.getCurrentUser();

                if( ! (fUser.getPhoneNumber().equals(getString(R.string.admin_PhoneNo)))){
                    AuthUI.getInstance().signOut(this);
//                    fUser = fAuth.getCurrentUser();
//                    Log.d("TAG", "RishabhJain + " + fUser.getPhoneNumber());
                    fUser = null;
                    Snackbar.make(findViewById(android.R.id.content), "Only Admin is allowed to use this app. Please Download \"Care My Drive\" or \"Care My Drive (Workshop)\" to register as a client as a workshop respectively.", Snackbar.LENGTH_LONG).show();

                    splash_screen_loginButton.setEnabled(true);
                    splash_screen_loginButton.setText("LogIn");

                }

                else {

                    if(fUser.getUid().equals(getString(R.string.admin_uid)))
                    {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        this.finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    else
                    {
                        AuthUI.getInstance().signOut(this);
                        fUser = null;
                        Snackbar.make(findViewById(android.R.id.content), "No Luck!", Snackbar.LENGTH_LONG).show();
                    }


                }
            }
            else {

                splash_screen_loginButton.setEnabled(true);
                splash_screen_loginButton.setText("LogIn");


                IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
                if(idpResponse == null)
                    Toast.makeText(this, "Sign-In Canceled!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, ""+idpResponse.getError(), Toast.LENGTH_SHORT).show();

            }
        }
        else
        {
            splash_screen_loginButton.setEnabled(true);
            splash_screen_loginButton.setText("LogIn");
        }
    }
}