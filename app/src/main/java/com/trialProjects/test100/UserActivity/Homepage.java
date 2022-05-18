package com.trialProjects.test100.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trialProjects.test100.FirebaseServices.DbQuery;
import com.trialProjects.test100.R;
import com.trialProjects.test100.Student.FragmentClasses_Student;
import com.trialProjects.test100.Teacher.FragmentClasses_Teacher;
import com.trialProjects.test100.activities.SignIn;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final String FULLNAME = "fullName";
    public static final String EMAIL = "email";
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private FirebaseAuth app_auth;
    private FirebaseFirestore app_firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app_auth = FirebaseAuth.getInstance();
        app_firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String fullName = intent.getStringExtra(FULLNAME);
        String email = intent.getStringExtra(EMAIL);

        drawer = findViewById(R.id.teacher_drawer_Layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            openFragment(new FragmentHome());
            navigationView.setCheckedItem(R.id.nav_home);
        }

        getuserData(fullName, email);
    }

    private void getuserData(String fullName, String email) {
        View headerView = navigationView.getHeaderView(0);
        TextView tv_fullName, tv_email;

        tv_fullName = headerView.findViewById(R.id.user_name);
        tv_email = headerView.findViewById(R.id.user_email);

        tv_fullName.setText(fullName);
        tv_email.setText(email);
    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:
                openFragment(new FragmentHome());
                break;
            case R.id.nav_profile:
                openFragment(new FragmentProfile());
                break;
            case R.id.nav_classes:
                checkUserType();
                break;
            case R.id.nav_logOut:
                logOut();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), SignIn.class));
        finish();
    }

    private void checkUserType() {
        FirebaseUser userID = app_auth.getCurrentUser();
        DocumentReference userType = DbQuery.app_fireStore.collection("USERS").document(userID.getUid());
        userType.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getString("userType").equals("Teacher")) {
                    openFragment(new FragmentClasses_Teacher());
                }
                else if(documentSnapshot.getString("userType").equals("Student")) {
                    openFragment(new FragmentClasses_Student());
                }
            }
        });
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
    }
}