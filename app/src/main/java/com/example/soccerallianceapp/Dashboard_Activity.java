package com.example.soccerallianceapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.soccer_alliance_project_test.R;
import com.example.soccerallianceapp.pojo.ViewTeamDetail.TeamDetails;
import com.example.soccerallianceapp.pojo.ViewTeamDetail.ViewTeamDetail;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public Toolbar DashboardToolbar;
    public DrawerLayout DashboarddrawerLayout;
    public NavigationView DashboardNavigationView;
    public NavController DashboardNavController;
    public TextView username;
    public ImageView userImage;
    String usertype;
    FirebaseAuth fAuth;
    String uid="",name,imageUri;
    int team_id;
    Getdataservice service;
    String TAG= "Dashboard activity screen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Log.i(TAG, "onCreate: before setup navigation");
        setupNavigation();

    }


    public void setupNavigation(){

        DashboardToolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(DashboardToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Log.i(TAG, "setupNavigation: ");
        DashboarddrawerLayout = findViewById(R.id.dashboard_drawer_layout);

        DashboardNavigationView = findViewById(R.id.dashboard_navigationview);
        username = DashboardNavigationView.getHeaderView(0).findViewById(R.id.nav_header_user_name);
        userImage = DashboardNavigationView.getHeaderView(0).findViewById(R.id.nav_header_userprofile_image);


        if(getIntent().getExtras()!=null){
            if(getIntent().getStringExtra("user_type").equals("Team_Manager")){
                DashboardNavigationView.getMenu().clear();
                DashboardNavigationView.inflateMenu(R.menu.team_manager_menu);
                usertype = "Team_Manager";
                name = getIntent().getStringExtra("name");
                imageUri = getIntent().getStringExtra("imageUri");
                username.setText("Welcome \n"+name);

                Glide.with(this).load(imageUri).centerCrop().into(userImage);

                service = RetroFitInstance.getRetrofitInstance().create(Getdataservice.class);

                fAuth = FirebaseAuth.getInstance();
                uid =fAuth.getCurrentUser().getUid();

                getTeamid(uid,service);

            }
            else if(getIntent().getStringExtra("user_type").equals("League_Manager")){
                Log.i(TAG, "setupNavigation: league manager");
                DashboardNavigationView.getMenu().clear();
                DashboardNavigationView.inflateMenu(R.menu.league_manager_menu);
                usertype ="League_Manager";
                name = getIntent().getStringExtra("name");
                imageUri = getIntent().getStringExtra("imageUri");
                username.setText("Welcome \n"+name);

                Glide.with(this).load(imageUri).centerCrop().into(userImage);
            }


        }

        else {
            DashboardNavigationView.getMenu().clear();
            DashboardNavigationView.inflateMenu(R.menu.guest_menu);
            usertype = "Guest";
        }
        DashboardNavController = Navigation.findNavController(this,R.id.dashboard_host_fragment);

        NavigationUI.setupActionBarWithNavController(this,DashboardNavController,DashboarddrawerLayout);
        NavigationUI.setupWithNavController(DashboardNavigationView,DashboardNavController);

        DashboardNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this,R.id.dashboard_host_fragment),DashboarddrawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (DashboarddrawerLayout.isDrawerOpen(GravityCompat.START)){

            DashboarddrawerLayout.closeDrawer(GravityCompat.START);

        }else {
            super.onBackPressed();
        }
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setCheckable(true);

        Log.i(TAG, "onNavigationItemSelected: menu on top");
        DashboarddrawerLayout.closeDrawers();

        int id = menuItem.getItemId();

        switch (id) {
            
            //Log.i(TAG, "onNavigationItemSelected: inside switch");

            case R.id.guest_dashboard_btn:
                if (DashboardNavController.getCurrentDestination().getId() != R.id.home_Fragment) {
                    DashboardNavController.navigate(R.id.home_Fragment);
                }
                break;

            case R.id.guest_Teams_btn:
                if (DashboardNavController.getCurrentDestination().getId() == R.id.home_Fragment) {
                    DashboardNavController.navigate(R.id.teamListFragment);
                }
                break;

            case R.id.guest_list_of_country_btn:
                if (DashboardNavController.getCurrentDestination().getId() == R.id.home_Fragment) {
                    DashboardNavController.navigate(R.id.country_List_Fragment);
                }
                break;

            case R.id.guest_login_btn:
                if (DashboardNavController.getCurrentDestination().getId() == R.id.home_Fragment) {

                    Log.i(TAG, "onNavigationItemSelected: login guest");
                    startActivity(new Intent(Dashboard_Activity.this,MainActivity.class));
                    finish();
                    DashboardNavController.navigate(R.id.home_Fragment);
                }
                break;

            case R.id.team_dashboard_btn:
                if (DashboardNavController.getCurrentDestination().getId() != R.id.home_Fragment) {
                    DashboardNavController.navigate(R.id.home_Fragment);
                }
                break;

            case R.id.team_my_team_btn:
                if (DashboardNavController.getCurrentDestination().getId() == R.id.home_Fragment) {
                    DashboardNavController.navigate(R.id.my_Team_Fragment);
                }
                break;

            case R.id.team_player_btn:
                if (DashboardNavController.getCurrentDestination().getId() == R.id.home_Fragment) {

                    Bundle bundle = new Bundle();
                    bundle.putString("Coming_from","dashboard");
                    bundle.putInt("team_id",team_id);
                    DashboardNavController.navigate(R.id.player_List_Fragment,bundle);
                }
                break;

            case R.id.team_my_profile_btn:
                if (DashboardNavController.getCurrentDestination().getId() == R.id.home_Fragment) {
                    DashboardNavController.navigate(R.id.team_mngr_my_profile_Fragment);
                }
                break;
            case R.id.team_logout_btn:
                if (DashboardNavController.getCurrentDestination().getId() == R.id.home_Fragment) {
                    Log.i(TAG, "onNavigationItemSelected: team");
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Dashboard_Activity.this,MainActivity.class));
                    finish();
                }
                break;

            case R.id.league_dashboard_btn:
                if (DashboardNavController.getCurrentDestination().getId() != R.id.home_Fragment) {
                    DashboardNavController.navigate(R.id.home_Fragment);
                }
                break;

            case R.id.league_my_league_btn:
                if (DashboardNavController.getCurrentDestination().getId() == R.id.home_Fragment) {
                    DashboardNavController.navigate(R.id.LeagueListFragment);
                }
                break;

            case R.id.league_team_list_btn:
                if (DashboardNavController.getCurrentDestination().getId() == R.id.home_Fragment) {
                    DashboardNavController.navigate(R.id.teamListFragment);
                }
                break;

            case R.id.league_my_profile_btn:
                if (DashboardNavController.getCurrentDestination().getId() == R.id.home_Fragment) {
                    DashboardNavController.navigate(R.id.team_mngr_my_profile_Fragment);
                }
                break;


            case R.id.league_logout_btn:
                if (DashboardNavController.getCurrentDestination().getId() == R.id.home_Fragment) {
                    Log.i(TAG, "onNavigationItemSelected: league");
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Dashboard_Activity.this,MainActivity.class));
                    finish();
                }
                break;

        }

        return true;

    }

    private void getTeamid(String uid, Getdataservice service) {

        Call<ViewTeamDetail> call = service.ViewTeamDetail(uid);

        call.enqueue(new Callback<ViewTeamDetail>() {
            @Override
            public void onResponse(Call<ViewTeamDetail> call, Response<ViewTeamDetail> response) {
                ViewTeamDetail viewTeam = response.body();



                TeamDetails teamDetails = viewTeam.getTeamDetails();

                if(teamDetails!= null){

                    team_id = teamDetails.getTeamId();

                    System.out.println("id of team "+team_id);

                }

            }

            @Override
            public void onFailure(Call<ViewTeamDetail> call, Throwable t) {

            }
        });

        System.out.println("teamid"+team_id);

        //getPlayerlist(team_id,service);
    }

}
