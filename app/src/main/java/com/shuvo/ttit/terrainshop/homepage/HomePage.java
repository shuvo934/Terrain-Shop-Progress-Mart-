package com.shuvo.ttit.terrainshop.homepage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.cart.OrderCart;
import com.shuvo.ttit.terrainshop.homepage.adapters.CategoryAdapter;
import com.shuvo.ttit.terrainshop.homepage.adapters.CustomExpandAdapter2nd;
import com.shuvo.ttit.terrainshop.homepage.adapters.SliderAdapter;
import com.shuvo.ttit.terrainshop.homepage.adapters.UserAdapter;
import com.shuvo.ttit.terrainshop.homepage.fragments.DiscountItem;
import com.shuvo.ttit.terrainshop.homepage.fragments.NewArrivalItem;
import com.shuvo.ttit.terrainshop.homepage.fragments.TopHitItem;
import com.shuvo.ttit.terrainshop.homepage.lists.CategoryList;
import com.shuvo.ttit.terrainshop.homepage.lists.CategoryListForNavItem;
import com.shuvo.ttit.terrainshop.homepage.lists.NavigationUserList;
import com.shuvo.ttit.terrainshop.homepage.lists.SliderItem;
import com.shuvo.ttit.terrainshop.login.Login;
import com.shuvo.ttit.terrainshop.login.list.UserInfoList;
import com.shuvo.ttit.terrainshop.myorders.MyOrders;
import com.shuvo.ttit.terrainshop.products.Products;
import com.shuvo.ttit.terrainshop.profile.UserProfile;
import com.shuvo.ttit.terrainshop.search.SearchProduct;
import com.shuvo.ttit.terrainshop.subcategory.SubCategory;
import com.shuvo.ttit.terrainshop.subcategory.lists.SubCategoryList;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import static com.shuvo.ttit.terrainshop.cart.OrderCart.myCartList;
import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.terrainshop.firstpage.ChoicePage.loginDone;
import static com.shuvo.ttit.terrainshop.login.Login.userInfoLists;

public class HomePage extends AppCompatActivity implements CategoryAdapter.ClickedItem, NavigationView.OnNavigationItemSelectedListener, UserAdapter.MenuClickedItem {

    SearchView searchView;
    SliderView sliderView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView tool;

    ArrayList<SliderItem> sliderItems;

    final Random mRandom = new Random(-2067702723);

    RecyclerView view;
    RecyclerView.LayoutManager layoutManager;
    CategoryAdapter categoryAdapter;

    ArrayList<CategoryList> categoryLists;
    TabLayout tabLayout;
    TabItem newArrivals, topHits, dealDiscount;

    private AsyncTask mTask;

    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE_ESHOP";

    public static final String AD_ID = "AD_ID";
    public static final String AD_CODE = "AD_CODE";
    public static final String AD_NAME = "AD_NAME";
    public static final String AD_ADDRESS = "AD_ADDRESS";
    public static final String AD_PHONE = "AD_PHONE";
    public static final String AD_EMAIL = "AD_EMAIL";
    public static final String AD_DIV_ID = "AD_DIV_ID";
    public static final String AD_DIST_ID = "AD_DIST_ID";
    public static final String AD_DD_ID = "AD_DD_ID";
    public static final String AD_DIV_NAME = "AD_DIV_NAME";
    public static final String AD_DIST_NAME = "AD_DIST_NAME";
    public static final String AD_DD_NAME = "AD_DD_NAME";
    public static final String LOGIN_TF = "TRUE_FALSE";

    SharedPreferences sharedPreferences;

    LinearLayout progressLay;
    CircularProgressIndicator circularProgressIndicator;
    ScrollView homeScroll;
    ImageButton refresh;

    private Boolean conn = false;
    private Boolean connected = false;

    @SuppressLint("StaticFieldLeak")
    public static TextView cartBadge;

    FrameLayout cartLayout;

    public static ExpandableListView expandableListView;
    private CustomExpandAdapter2nd adapter;
    public static boolean fromPicture = false;

    RecyclerView recyclerView;
    UserAdapter userAdapter;
    RecyclerView.LayoutManager layoutManagerNav;

    ArrayList<NavigationUserList> navigationUserLists;
    ArrayList<CategoryListForNavItem> categoryListForNavItems;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        searchView = findViewById(R.id.search_product_text);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);
        tool = findViewById(R.id.nav_icon_main_menu);
        sliderView = findViewById(R.id.imageSlider);
        view = findViewById(R.id.category_list_view);
        tabLayout = findViewById(R.id.three_tab_layout);
        newArrivals = findViewById(R.id.new_arrivals_tab);
        topHits = findViewById(R.id.top_hit_tab);
        dealDiscount = findViewById(R.id.deal_discount_tab);

        progressLay = findViewById(R.id.progress_bar_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_home_page);
        homeScroll = findViewById(R.id.home_page_scroll_view);
        refresh = findViewById(R.id.refresh_button_for_all_homepage);
        cartBadge = findViewById(R.id.cart_badge);
        cartBadge.setVisibility(View.GONE);
        cartLayout = findViewById(R.id.cart_layout);
        expandableListView = findViewById(R.id.nav_list_second);
        recyclerView = findViewById(R.id.user_info_list_view);

        progressLay.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.GONE);
        homeScroll.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);

        navigationUserLists = new ArrayList<>();
        categoryListForNavItems = new ArrayList<>();

        categoryLists = new ArrayList<>();

        sliderItems = new ArrayList<>();

//        searchView.clearFocus();
//        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);


        sliderItems.add(new SliderItem(R.drawable.hero_1,"70% Discount\nWinter Collection"));
        sliderItems.add(new SliderItem(R.drawable.hero_2,"70% Discount\nWinter Collection"));
        sliderItems.add(new SliderItem(R.drawable.hero_3,"50% Discount\nSummer Collection"));

        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        boolean loginfile = sharedPreferences.getBoolean(LOGIN_TF,false);

        if (loginfile) {

            String ad_id = sharedPreferences.getString(AD_ID, null);
            String ad_code = sharedPreferences.getString(AD_CODE, null);
            String ad_name = sharedPreferences.getString(AD_NAME,null);
            String ad_address = sharedPreferences.getString(AD_ADDRESS,null);
            String ad_phone = sharedPreferences.getString(AD_PHONE, null);
            String ad_email = sharedPreferences.getString(AD_EMAIL,null);
            String ad_div_id = sharedPreferences.getString(AD_DIV_ID, null);
            String ad_dist_id = sharedPreferences.getString(AD_DIST_ID, null);
            String ad_dd_id = sharedPreferences.getString(AD_DD_ID, null);
            String ad_div_name = sharedPreferences.getString(AD_DIV_NAME, null);
            String ad_dist_name = sharedPreferences.getString(AD_DIST_NAME, null);
            String ad_dd_name = sharedPreferences.getString(AD_DD_NAME, null);

            userInfoLists = new ArrayList<>();
            userInfoLists.add(new UserInfoList(ad_id,ad_code,ad_name,ad_address,ad_phone,ad_email,ad_div_id,ad_dist_id,ad_dd_id,ad_div_name,ad_dist_name,ad_dd_name));

            System.out.println(userInfoLists.get(0).getAd_name());
        }


        // navigation bar implementation
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout , R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTask != null) {
                    if (mTask.getStatus().toString().equals("RUNNING")) {
                        Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
                    } else {
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                }
                else {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    drawerLayout.openDrawer(GravityCompat.START);
                }

            }
        });


        // Search Edited
        EditText txtSearch = ((EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setTextColor(Color.parseColor("#40739e"));
        txtSearch.setTextSize(14);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.patua_one);
        txtSearch.setTypeface(typeface);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mTask != null) {
                    if (mTask.getStatus().toString().equals("RUNNING")) {
                        Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        System.out.println(query);
                        View view = getCurrentFocus();
                        if (view != null) {
                            view.clearFocus();
                        }
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        Intent intent = new Intent(HomePage.this, SearchProduct.class);
                        intent.putExtra("SEARCH PRODUCT", query);
                        startActivity(intent);
                        searchView.setQuery("",true);
                        return false;
                    }
                }
                else {
                    System.out.println(query);
                    View view = getCurrentFocus();
                    if (view != null) {
                        view.clearFocus();
                    }
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    Intent intent = new Intent(HomePage.this, SearchProduct.class);
                    intent.putExtra("SEARCH PRODUCT", query);
                    startActivity(intent);
                    searchView.setQuery("",true);
                    return false;
                }

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //searchView.setIconifiedByDefault(true);
                return false;
            }
        });

        // Image Slider Animation
        sliderView.setSliderAdapter(new SliderAdapter(this,sliderItems));
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();

        // Category implementation
        view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        view.setLayoutManager(layoutManager);

        // nav menu Implementation
        recyclerView.setHasFixedSize(true);
        layoutManagerNav = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManagerNav);


        // tab layout implementation
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tabLayout.selectTab(tab);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_for_tab,new NewArrivalItem()).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_for_tab,new TopHitItem()).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_for_tab,new DiscountItem()).commit();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTask = new Check().execute();
            }
        });

        cartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTask != null) {
                    if (mTask.getStatus().toString().equals("RUNNING")) {
                        Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(HomePage.this, OrderCart.class);
                        startActivity(intent);
                    }
                }
                else {
                    Intent intent = new Intent(HomePage.this, OrderCart.class);
                    startActivity(intent);
                }

            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                System.out.println(categoryListForNavItems.get(i).getCategoryName());

                if (expandableListView.isGroupExpanded(i)) {
                    expandableListView.collapseGroup(i);
                }

                if (categoryListForNavItems.get(i).getBelowCat() != null) {
                    if (categoryListForNavItems.get(i).getBelowCat().equals("2")) {
                        Intent intent = new Intent(HomePage.this, SubCategory.class);
                        intent.putExtra("IEM ID", categoryListForNavItems.get(i).getIem_id());
                        intent.putExtra("IEM NAME", categoryListForNavItems.get(i).getCategoryName());
                        startActivity(intent);
                    }
                    else if (categoryListForNavItems.get(i).getBelowCat().equals("3")) {
                        Intent intent = new Intent(HomePage.this, Products.class);
                        intent.putExtra("IEM ID", categoryListForNavItems.get(i).getIem_id());
                        intent.putExtra("IEM NAME", categoryListForNavItems.get(i).getCategoryName());
                        startActivity(intent);
                    }
                }
                else {
                    Intent intent = new Intent(HomePage.this, Products.class);
                    intent.putExtra("IEM ID", categoryListForNavItems.get(i).getIem_id());
                    intent.putExtra("IEM NAME", categoryListForNavItems.get(i).getCategoryName());
                    startActivity(intent);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                //System.out.println(categoryLists.get(i).toString());
                if (!fromPicture)  {
                    expandableListView.collapseGroup(i);
                }
                fromPicture = false;
//                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                fromPicture = false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                String selectedItem =  (categoryListForNavItems.get(i).getSubCategoryLists().get(i1).getIem_name());
                System.out.println(selectedItem);

                if (categoryListForNavItems.get(i).getSubCategoryLists().get(i1).getSubBelow() != null) {
                    if (categoryListForNavItems.get(i).getSubCategoryLists().get(i1).getSubBelow().equals("3")) {
                        Intent intent = new Intent(HomePage.this, Products.class);
                        intent.putExtra("IEM ID", categoryListForNavItems.get(i).getSubCategoryLists().get(i1).getIem_id());
                        intent.putExtra("IEM NAME", categoryListForNavItems.get(i).getSubCategoryLists().get(i1).getIem_name());
                        startActivity(intent);
                    }
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        mTask = new Check().execute();

        closeKeyBoard();

    }

    public static void CartItemCheck() {
        int count = myCartList.size();
        if (count == 0) {
            if (cartBadge.getVisibility() != View.GONE) {
                Log.i("assa","bujhlam2");

                cartBadge.setVisibility(View.GONE);
            }
        }
        else {
            cartBadge.setText(String.valueOf(Math.min(count, 99)));
            Log.i("assa","bujhlam3");

            if (cartBadge.getVisibility() != View.VISIBLE) {
                Log.i("assa","bujhlam4");

                cartBadge.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        userCheck();
        CartItemCheck();
        super.onResume();
    }

    private void closeKeyBoard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public int generateRandomColor() {
        // This is the base color which will be mixed with the generated one
        final int baseColor = Color.WHITE;

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);
        System.out.println(mRandom.nextInt());

        final int red = (baseRed + mRandom.nextInt(256)) / 2;
        final int green = (baseGreen + mRandom.nextInt(256)) / 2;
        final int blue = (baseBlue + mRandom.nextInt(256)) / 2;

        return Color.rgb(red, green, blue);
    }

    @Override
    public void onMenuClicked(int CategoryPosition) {

        switch (navigationUserLists.get(CategoryPosition).getId()) {
            case "user_profile": {
                Intent intent = new Intent(HomePage.this, UserProfile.class);
                startActivity(intent);
                break;
            }
            case "my_orders": {
                Intent intent = new Intent(HomePage.this, MyOrders.class);
                startActivity(intent);
                break;
            }
            case "nav_log_out":
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(HomePage.this)
                        .setTitle("LOG OUT!")
                        .setMessage("Do you want to log out?")
                        .setIcon(R.drawable.terrain_shop_icon)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userInfoLists.clear();
                                userInfoLists = new ArrayList<>();

                                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                editor1.remove(AD_ID);
                                editor1.remove(AD_CODE);
                                editor1.remove(AD_NAME);
                                editor1.remove(AD_ADDRESS);
                                editor1.remove(AD_PHONE);
                                editor1.remove(AD_EMAIL);
                                editor1.remove(AD_DIV_ID);
                                editor1.remove(AD_DIST_ID);
                                editor1.remove(AD_DD_ID);
                                editor1.remove(AD_DIV_NAME);
                                editor1.remove(AD_DIST_NAME);
                                editor1.remove(AD_DD_NAME);
                                editor1.remove(LOGIN_TF);
                                editor1.apply();
                                editor1.commit();
                                loginDone = false;
                                Toast.makeText(getApplicationContext(),"Log Out Successful",Toast.LENGTH_SHORT).show();
                                userCheck();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Do nothing
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case "nav_login": {
                Intent intent = new Intent(HomePage.this, Login.class);
                startActivity(intent);
                break;
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {
        if (categoryLists.get(CategoryPosition).getBelowCat() != null) {
            if (categoryLists.get(CategoryPosition).getBelowCat().equals("2")) {
                Intent intent = new Intent(HomePage.this, SubCategory.class);
                intent.putExtra("IEM ID", categoryLists.get(CategoryPosition).getIem_id());
                intent.putExtra("IEM NAME", categoryLists.get(CategoryPosition).getCategoryName());
                startActivity(intent);
            }
            else if (categoryLists.get(CategoryPosition).getBelowCat().equals("3")) {
                Intent intent = new Intent(HomePage.this, Products.class);
                intent.putExtra("IEM ID", categoryLists.get(CategoryPosition).getIem_id());
                intent.putExtra("IEM NAME", categoryLists.get(CategoryPosition).getCategoryName());
                startActivity(intent);
            }
        }
        else {
            Intent intent = new Intent(HomePage.this, Products.class);
            intent.putExtra("IEM ID", categoryLists.get(CategoryPosition).getIem_id());
            intent.putExtra("IEM NAME", categoryLists.get(CategoryPosition).getCategoryName());
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//        int id = item.getItemId();
//        System.out.println("ITEM ID: "+id);
//        item.setChecked(true);
//
//        if (id == R.id.nav_logIn) {
////            Intent intent = new Intent(HomePage.this, Login.class);
////            startActivity(intent);
//        }
//        else if (id == R.id.nav_logOut) {
////            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(HomePage.this)
////                    .setTitle("LOG OUT!")
////                    .setMessage("Do you want to log out?")
////                    .setIcon(R.drawable.terrain_shop_icon)
////                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                            userInfoLists.clear();
////                            userInfoLists = new ArrayList<>();
////
////                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
////                            editor1.remove(AD_ID);
////                            editor1.remove(AD_CODE);
////                            editor1.remove(AD_NAME);
////                            editor1.remove(AD_ADDRESS);
////                            editor1.remove(AD_PHONE);
////                            editor1.remove(AD_EMAIL);
////                            editor1.remove(AD_DIV_ID);
////                            editor1.remove(LOGIN_TF);
////                            editor1.apply();
////                            editor1.commit();
////                            loginDone = false;
////                            userCheck();
////                        }
////                    })
////                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                            //Do nothing
////                        }
////                    });
////            AlertDialog alertDialog = alertDialogBuilder.create();
////            alertDialog.show();
//        }
//        else if (id == R.id.my_orders) {
////            Intent intent = new Intent(HomePage.this, MyOrders.class);
////            startActivity(intent);
//        }
//        else if (id == R.id.user_profile) {
////            Intent intent = new Intent(HomePage.this, UserProfile.class);
////            startActivity(intent);
//        }
//        else {
//            for (int i = 0 ; i < categoryLists.size() ; i++) {
//                if (id == Integer.parseInt(categoryLists.get(i).getIem_id())) {
//                    if (categoryLists.get(i).getBelowCat() != null) {
//                        if (categoryLists.get(i).getBelowCat().equals("2")) {
//                            Intent intent = new Intent(HomePage.this, SubCategory.class);
//                            intent.putExtra("IEM ID", categoryLists.get(i).getIem_id());
//                            intent.putExtra("IEM NAME", categoryLists.get(i).getCategoryName());
//                            startActivity(intent);
//                        }
//                        else if (categoryLists.get(i).getBelowCat().equals("3")) {
//                            Intent intent = new Intent(HomePage.this, Products.class);
//                            intent.putExtra("IEM ID", categoryLists.get(i).getIem_id());
//                            intent.putExtra("IEM NAME", categoryLists.get(i).getCategoryName());
//                            startActivity(intent);
//                        }
//                    }
//                    else {
//                        Intent intent = new Intent(HomePage.this, Products.class);
//                        intent.putExtra("IEM ID", categoryLists.get(i).getIem_id());
//                        intent.putExtra("IEM NAME", categoryLists.get(i).getCategoryName());
//                        startActivity(intent);
//                    }
//                    break;
//                }
//            }
////            System.out.println("ID: " + id);
////            Toast.makeText(getApplicationContext(),item.getTitle().toString(),Toast.LENGTH_SHORT).show();
//        }
//
//        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            if (mTask != null) {
                if (mTask.getStatus().toString().equals("RUNNING")) {
                    Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
                } else {
                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(HomePage.this)
                            .setTitle("EXIT!")
                            .setMessage("Do you want to exit?")
                            .setIcon(R.drawable.terrain_shop_icon)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do nothing
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
            else {
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(HomePage.this)
                        .setTitle("EXIT!")
                        .setMessage("Do you want to exit?")
                        .setIcon(R.drawable.terrain_shop_icon)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Do nothing
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }

    public void userCheck() {
        boolean loginfile = sharedPreferences.getBoolean(LOGIN_TF,false);
        navigationUserLists = new ArrayList<>();
        //System.out.println("CHECKING LOGIN INFO FOR CHOICEPAGE: "+ loginDone);
//        navigationUserLists.add(new NavigationUserList("PROFILE", "user_profile"));
//        navigationUserLists.add(new NavigationUserList("MY ORDERS", "my_orders"));
        //navigationUserLists.add(new NavigationUserList("MY FAVORITES","my_favorites"));
//        navigationUserLists.add(new NavigationUserList("VOUCHERS","my_vouchers"));
//        navigationUserLists.add(new NavigationUserList("LOG OUT", "nav_log_out"));
//        navigationUserLists.add(new NavigationUserList("LOGIN", "nav_login"));


        userAdapter = new UserAdapter(navigationUserLists,this,HomePage.this);
        recyclerView.setAdapter(userAdapter);
        if (!loginfile) {
            loginfile = loginDone;
        }
        if (loginfile) {
//            Menu menu = navigationView.getMenu();
//            menu.findItem(R.id.nav_logIn).setVisible(false);
//
//            menu.findItem(R.id.user_profile).setVisible(true);
//            menu.findItem(R.id.my_orders).setVisible(true);
//            menu.findItem(R.id.my_favorites).setVisible(true);
//            menu.findItem(R.id.my_vouchers).setVisible(true);
//            menu.findItem(R.id.nav_logOut).setVisible(true);
            navigationUserLists.add(new NavigationUserList("PROFILE", "user_profile"));
            navigationUserLists.add(new NavigationUserList("MY ORDERS", "my_orders"));
            navigationUserLists.add(new NavigationUserList("LOG OUT", "nav_log_out"));

            //View headerView = navigationView.getHeaderView(0);
            TextView userImage = (TextView) findViewById(R.id.user_image_in_header);
            TextView userName = (TextView) findViewById(R.id.user_name_in_header);
            TextView email = (TextView) findViewById(R.id.user_mail_after_login);

            String name = userInfoLists.get(0).getAd_name();
            String mail = userInfoLists.get(0).getAd_email();

            userName.setText(name);
            email.setText(mail);

            char first = name.charAt(0);
            String firstTO = String.valueOf(first);
            System.out.println(firstTO);

            userImage.setText(firstTO);
            userImage.setBackgroundResource(R.drawable.ic_circle);
            userAdapter = new UserAdapter(navigationUserLists,this,HomePage.this);
            recyclerView.setAdapter(userAdapter);
        }
        else {
//            Menu menu = navigationView.getMenu();
//
//            menu.findItem(R.id.user_profile).setVisible(false);
//            menu.findItem(R.id.my_orders).setVisible(false);
//            menu.findItem(R.id.my_favorites).setVisible(false);
//            menu.findItem(R.id.my_vouchers).setVisible(false);
//            menu.findItem(R.id.nav_logOut).setVisible(false);
//
//            menu.findItem(R.id.nav_logIn).setVisible(true);

            navigationUserLists.add(new NavigationUserList("LOGIN", "nav_login"));


//            View headerView = navigationView.getHeaderView(0);
            TextView userImage = (TextView) findViewById(R.id.user_image_in_header);
            TextView userName = (TextView) findViewById(R.id.user_name_in_header);
            TextView email = (TextView) findViewById(R.id.user_mail_after_login);

            userName.setText("NO USER");
            email.setText("");
            userImage.setText("");
            userImage.setBackgroundResource(R.drawable.account_circle_24);
            userAdapter = new UserAdapter(navigationUserLists,this,HomePage.this);
            recyclerView.setAdapter(userAdapter);
        }
    }

    public boolean isConnected () {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isOnline () {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressLay.setVisibility(View.VISIBLE);
            circularProgressIndicator.setVisibility(View.VISIBLE);
            homeScroll.setVisibility(View.GONE);
            refresh.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                CheckAllData();
                if (connected) {
                    conn = true;
                }

            } else {
                conn = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            circularProgressIndicator.setVisibility(View.GONE);

            if (conn) {

                progressLay.setVisibility(View.GONE);
                refresh.setVisibility(View.GONE);
                homeScroll.setVisibility(View.VISIBLE);

                categoryAdapter = new CategoryAdapter(HomePage.this,categoryLists,HomePage.this);
                view.setAdapter(categoryAdapter);

                closeKeyBoard();

//                Menu menu = navigationView.getMenu();
//                SubMenu subMenu1 = menu.findItem(R.id.all_categories).getSubMenu();
//
//                for (int i = 0 ; i < categoryLists.size(); i++) {
//                    System.out.println("Menu Size: " + menu.size());
//                    subMenu1.add(R.id.categories_id_off_all, Integer.parseInt(categoryLists.get(i).getIem_id()), menu.size()+i+1, "  "+categoryLists.get(i).getCategoryName());
////                    SubMenu subMenu = subMenu1.findItem(Integer.parseInt(categoryLists.get(i).getIem_id())).getSubMenu();
////                    subMenu.add(123455566, 1012,menu.size()+i+1,"NEW SSSUUBBB");
//                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_for_tab,new NewArrivalItem()).commit();

                adapter = new CustomExpandAdapter2nd(HomePage.this,categoryListForNavItems);
                expandableListView.setAdapter(adapter);

                conn = false;
                connected = false;

            } else {
                homeScroll.setVisibility(View.GONE);
                progressLay.setVisibility(View.VISIBLE);
                refresh.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(HomePage.this)
                        .setMessage("Slow Internet or Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mTask = new Check().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public void CheckAllData () {

        try {
            Connection connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            categoryLists = new ArrayList<>();
            categoryListForNavItems = new ArrayList<>();
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select IEM_ID, IEM_TYPE, IEM_NAME,\n" +
                    "(Select AVG(iem_type) from ITEM_ECOM_MST A where A.iem_iem_id = B.IEM_ID) as below\n" +
                    "from ITEM_ECOM_MST B where iem_type = 1 \n" +
                    "                    and IEM_ID NOT IN (1,2,3)\n" +
                    "                    order by IEM_ID");

            while (rs.next()) {
                categoryLists.add(new CategoryList(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),generateRandomColor()));
                categoryListForNavItems.add(new CategoryListForNavItem(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),new ArrayList<>()));

            }
            rs.close();

            for (int i = 0 ; i < categoryListForNavItems.size(); i++) {
                String iem_id = categoryListForNavItems.get(i).getIem_id();
                ArrayList<SubCategoryList> subCategoryLists = new ArrayList<>();
                ResultSet resultSet = stmt.executeQuery("select IEM_ID, IEM_IEM_ID, IEM_TYPE, IEM_NAME, IEM_THUMB,\n" +
                        "                    (Select AVG(iem_type) from ITEM_ECOM_MST A where A.iem_iem_id = B.IEM_ID) as below\n" +
                        "                    from ITEM_ECOM_MST B where iem_iem_id = "+iem_id+"\n" +
                        "                                        order by IEM_ID");

                while (resultSet.next()) {
                    Blob b=resultSet.getBlob(5);
                    Bitmap bitmap = null;
                    if (b != null) {
                        byte[] barr =b.getBytes(1,(int)b.length());
                        bitmap = BitmapFactory.decodeByteArray(barr,0,barr.length);
                    }
                    subCategoryLists.add(new SubCategoryList(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),bitmap,resultSet.getString(6)));
                }
                resultSet.close();

                categoryListForNavItems.get(i).setSubCategoryLists(subCategoryLists);
            }

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}