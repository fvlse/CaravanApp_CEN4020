package edu.fsu.cen4020.cen_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    // Initial Main Activity Skeleton

    public static FirebaseUser currentUser = null;
    public FirebaseAuth firebaseAuth;
    public DatabaseReference mDatabase;
    public DatabaseReference dbRef;

    // Travel Selection Buttons
    public Button mCreateButton, mJoinButton, mVerifyButton;
    public Button mExistUser, mRegisterUser, mSkipButton;
    public EditText mPartyName, mPartyPassword;
    public Spinner mTravelSpinner;
    public Button mLoginButton;
    public boolean loggedIN = false;

    public TextView mViewPartyName, mViewPartyPassword, mViewTravelType, mViewStartLocation, mViewDestination;
    public TextView mViewWelcomeUser;

    public EditText mTextBoxID, mTextBoxPass;

    public void init()
    {

        // Get reference of Firebase Database for reads/writes
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("partys");

        mViewWelcomeUser = (TextView) findViewById(R.id.welcome_User);
        mCreateButton = (Button) findViewById(R.id.create_button);
        mJoinButton = (Button) findViewById(R.id.join_button);

        // TODO: Add logout button

        String welcomeMsg = "Welcome, " + currentUser.getEmail() + "!";
        mViewWelcomeUser.setText(welcomeMsg);

        // Launchable intents go here (button launches)

        /* Road Button */
        // launches FragmentList intent to invite participants to join party
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.join_party);
                // set view items
                mTextBoxID = (EditText) findViewById(R.id.textBoxID);
                mTextBoxPass = (EditText) findViewById(R.id.textBoxPass);
                mVerifyButton = (Button) findViewById(R.id.verify_button);
                Log.i("MainActivity:", "Join Button");
            }
        }) ;

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, MainActivity.class);
                //startActivity(intent);
                setContentView(R.layout.create_party);
                // set view items
                mPartyName = (EditText) findViewById(R.id.partyName);
                mPartyPassword = (EditText) findViewById(R.id.partyPassword);
                mTravelSpinner = (Spinner) findViewById(R.id.travelSpinner);

                mViewPartyName = (TextView) findViewById(R.id.viewPartyName);
                mViewPartyPassword = (TextView) findViewById(R.id.viewPartyPassword);
                mViewTravelType = (TextView) findViewById(R.id.viewTravelType);
                mViewStartLocation = (TextView) findViewById(R.id.viewStartLocation);
                mViewDestination = (TextView) findViewById(R.id.viewDestination);

                Log.i("MainActivity:", "Join Button");
            }
        }) ;

    }

    // Do verification of Party ID and Password
    // Raymond and Roberto
    public void navigateVerify(View view) {
        setContentView(R.layout.activity_main);
        if (mTextBoxID.getText().toString().isEmpty())
        {

        }
        if (mTextBoxPass.getText().toString().isEmpty())
        {

        }
        init();
        Log.i("MainActivity", "Verify button clicked.");
    }

    // Do verification of Party ID and Password
    public void navigateMain(View view) {
        setContentView(R.layout.activity_main);
        init();
        Log.i("MainActivity", "Main button clicked.");
    }

    // Do verification of Party ID and Password
    public void goLogin(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void goRegister(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    // SKIP BUTTON
    // For testing purposes -- created by victor and phalguna
    public void goSkip(View view) {
        setContentView(R.layout.activity_main);
        init();
    }

    // Do verification of Party ID and Password
    public void createParty(View view) {
        //Maybe set content view to 'creating...'?
        Log.i("MainActivity", "Create Party button clicked.");

        boolean check = true;
        mViewPartyName.setTextColor(Color.BLACK);
        mViewPartyPassword.setTextColor(Color.BLACK);
        mViewTravelType.setTextColor(Color.BLACK);

        // Verify that all required fields are filled
        if (mPartyName.getText().toString().isEmpty())
        {
            Log.i("MainActivity", "Party Name empty.");
            mViewPartyName.setTextColor(Color.RED);
            check = false;
        }
        if (mPartyPassword.getText().toString().isEmpty())
        {
            Log.i("MainActivity", "Party Password empty.");
            mViewPartyPassword.setTextColor(Color.RED);
            check = false;
        }
        if (mTravelSpinner.getSelectedItemPosition() == 0)
        {
            Log.i("MainActivity", "Selection invalid.");
            mViewTravelType.setTextColor(Color.RED);
            check = false;
        }

        if (check) {
            Toast.makeText(getApplicationContext(), "Success: creating party...",
                    Toast.LENGTH_LONG).show();


            FirebaseDatabase db = FirebaseDatabase.getInstance();
            // Generate a Key?
            String key = db.getReference("partys").push().getKey();

            // created by phalguna and ray
	            //Party Id will be created from creater userid and groupname
		        //will be done through database

            // Create travel party on Firebase

            /**
             * Vals:
             *      private String partyKey; // assigned
                    private String partyName;
                    private String leader;
                    private String[] followers;
                    private double start_lat;
                    private double start_long;
                    private double end_lat;
                    private double end_long;
             */

            FirebaseUser user = firebaseAuth.getCurrentUser();

            String partyKey = key; // set party key here from phalguna and rays generated key
            String partyName = mPartyName.getText().toString();
            String leader = user.getEmail().toString();
            String followers = "testUser1";     // should be none at current time
            double start_lat = 0;
            double start_long = 0;
            double end_long = 0;
            double end_lat = 0;

            Partys party = new Partys(partyKey, partyName, leader, followers, start_lat, start_long, end_long, end_lat);

            // create the database entry in firebase
            dbRef.child(partyKey).setValue(party);

            Toast.makeText(getApplicationContext(), "Party creation successful!",
                    Toast.LENGTH_LONG).show();

            // TODO: Launch party monitoring screen

            Intent intent = new Intent(MainActivity.this, JourneyActivity.class);
            startActivity(intent);


        }
        else {
            Toast.makeText(getApplicationContext(), "Error: Please fix the invalid fields.",
                    Toast.LENGTH_LONG).show();
        }

    }

    // Initial creation of MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (currentUser==null)  {
            setContentView(R.layout.welcome_login);
            mLoginButton = (Button) findViewById(R.id.loginButton);
            mExistUser = (Button) findViewById(R.id.existUser);
            mRegisterUser = (Button) findViewById(R.id.registerUser);
            mSkipButton = (Button) findViewById(R.id.skipButton);
        }
        else {
            setContentView(R.layout.activity_main);
            // Initialize buttons and functionality in activity_main layout
            init();
        }
    }

}
