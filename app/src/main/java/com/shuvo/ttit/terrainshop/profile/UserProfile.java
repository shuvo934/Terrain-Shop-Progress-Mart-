package com.shuvo.ttit.terrainshop.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.terrainshop.R;

import static com.shuvo.ttit.terrainshop.login.Login.userInfoLists;

public class UserProfile extends AppCompatActivity {

    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    TextView userName;
    ImageView editName;

    TextView userAddress;
    ImageView editAddress;

    TextView userMail;
    ImageView editMail;

    TextView userNumber;
    //ImageView editMail;

    TextView userLocation;
    ImageView editLocation;

    RelativeLayout userprofileBar;

    MaterialButton save;

    String user_name = "";
    String user_address = "";
    String user_mail = "";
    String user_number = "";
    String user_location = "";
    String ad_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        fullLayout = findViewById(R.id.user_profile_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_user_profile);
        circularProgressIndicator.setVisibility(View.GONE);

        userName = findViewById(R.id.user_profile_name);
        editName = findViewById(R.id.edit_image_name);

        userAddress = findViewById(R.id.user_profile_address);
        editAddress = findViewById(R.id.edit_image_address);

        userMail = findViewById(R.id.user_profile_mail);
        editMail = findViewById(R.id.edit_image_mail);

        userNumber = findViewById(R.id.user_profile_mobile_number);

        userLocation = findViewById(R.id.user_profile_location);
        editLocation = findViewById(R.id.edit_image_location);

        userprofileBar = findViewById(R.id.user_profile_action_bar);

        save = findViewById(R.id.user_profile_save_button);
        save.setVisibility(View.GONE);

        userprofileBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ad_id = userInfoLists.get(0).getAd_id();

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, UpdateProfile.class);
                intent.putExtra("ATTRIBUTE", user_name);
                intent.putExtra("WHICH TO UPDATE", "NAME");
                startActivity(intent);
            }
        });

        editMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, UpdateProfile.class);
                intent.putExtra("ATTRIBUTE", user_mail);
                intent.putExtra("WHICH TO UPDATE", "EMAIL");
                startActivity(intent);
            }
        });

        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, UpdateProfile.class);
                intent.putExtra("ATTRIBUTE", user_address);
                intent.putExtra("WHICH TO UPDATE", "ADDRESS");
                startActivity(intent);
            }
        });

        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, UpdateLocation.class);
                startActivity(intent);
            }
        });

    }

    private void DataCheck() {
        user_name = userInfoLists.get(0).getAd_name();
        userName.setText(user_name);

        user_address = userInfoLists.get(0).getAd_address();
        userAddress.setText(user_address);

        user_mail = userInfoLists.get(0).getAd_email();
        userMail.setText(user_mail);

        user_number = userInfoLists.get(0).getAd_phone();
        userNumber.setText(user_number);

        String div = "";
        if (userInfoLists.get(0).getAd_div_name() != null) {
            if (!userInfoLists.get(0).getAd_div_name().isEmpty()) {
                div = userInfoLists.get(0).getAd_div_name();
            }
        }
        String thana = "";
        if (userInfoLists.get(0).getAd_thana_name() != null) {
            if (!userInfoLists.get(0).getAd_thana_name().isEmpty()) {
                thana = userInfoLists.get(0).getAd_thana_name();
            }
        }
        if (div.isEmpty() && thana.isEmpty()) {
            user_location = "No Location";
        }
        else if (div.isEmpty() && !thana.isEmpty()) {
            user_location = thana;
        }
        else if (!div.isEmpty() && thana.isEmpty()) {
            user_location = div;
        }
        else if (!div.isEmpty() && !thana.isEmpty()) {
            user_location = thana + ", " + div;
        }

        userLocation.setText(user_location);
    }

    @Override
    protected void onResume() {
        DataCheck();
        super.onResume();
    }
}