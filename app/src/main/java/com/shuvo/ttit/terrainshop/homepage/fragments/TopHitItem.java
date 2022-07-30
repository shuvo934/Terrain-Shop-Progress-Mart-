package com.shuvo.ttit.terrainshop.homepage.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.homepage.fragments.adapters.TopHitAdapter;
import com.shuvo.ttit.terrainshop.homepage.fragments.lists.NewArrivalItemList;
import com.shuvo.ttit.terrainshop.homepage.fragments.lists.TopHitItemList;
import com.shuvo.ttit.terrainshop.productDetails.ProductDetails;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopHitItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopHitItem extends Fragment implements TopHitAdapter.ClickedItem{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView topHitView;
    RecyclerView.LayoutManager layoutManager;
    TopHitAdapter topHitAdapter;

    TextView noProduct;

    ArrayList<TopHitItemList> topHitItemLists;

    CircularProgressIndicator circularProgressIndicator;
    ImageButton refresh;

    private Boolean conn = false;
    private Boolean connected = false;
    private Connection connection;

    public TopHitItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopHitItem.
     */
    // TODO: Rename and change types and number of parameters
    public static TopHitItem newInstance(String param1, String param2) {
        TopHitItem fragment = new TopHitItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    Context mContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_hit_item, container, false);

        topHitView = view.findViewById(R.id.top_hit_list_view);
        noProduct = view.findViewById(R.id.no_product_msg_top_hit);
        noProduct.setVisibility(View.GONE);
        circularProgressIndicator = view.findViewById(R.id.progress_indicator_top_hit_item_fragment);
        circularProgressIndicator.setVisibility(View.GONE);
        refresh = view.findViewById(R.id.refresh_button_for_top_hit_item);
        refresh.setVisibility(View.GONE);

        topHitItemLists = new ArrayList<>();

        topHitView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false);
        topHitView.setLayoutManager(layoutManager);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Check().execute();
            }
        });

        new Check().execute();

        return view;
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {

        Bitmap bmp = topHitItemLists.get(CategoryPosition).getImage();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();

        String filename = "bitmap.png";
        try {
            //Write file
            filename = "bitmap.png";
            FileOutputStream stream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

            //Cleanup
            stream.close();
//            bmp.recycle();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(mContext, ProductDetails.class);
        intent.putExtra("PRODUCT NAME", topHitItemLists.get(CategoryPosition).getIem_name());
        intent.putExtra("CARD RATE PRICE", topHitItemLists.get(CategoryPosition).getCard_rate());
        intent.putExtra("PRODUCT QTY", topHitItemLists.get(CategoryPosition).getPackaging_unit_qty());
        intent.putExtra("ITEM ID", topHitItemLists.get(CategoryPosition).getIem_item_id());
        intent.putExtra("PRODUCT DETAILS", topHitItemLists.get(CategoryPosition).getItemDetails());
        intent.putExtra("STOCK MSG", topHitItemLists.get(CategoryPosition).getStock_availability());
        intent.putExtra("NEW TAG", topHitItemLists.get(CategoryPosition).getNew_tag());

        intent.putExtra("PRODUCT IMAGE", filename);
        intent.putExtra("IEM ID",topHitItemLists.get(CategoryPosition).getIem_id());
        intent.putExtra("IEM IEM ID",topHitItemLists.get(CategoryPosition).getIem_iem_id());
        intent.putExtra("ITEM UNIT", topHitItemLists.get(CategoryPosition).getItem_unit());
        intent.putExtra("REAL RATE PRICE", topHitItemLists.get(CategoryPosition).getReal_rate());
        intent.putExtra("DISCOUNT DTL", topHitItemLists.get(CategoryPosition).getDiscount_dtl());
        intent.putExtra("DISCOUNT TYPE", topHitItemLists.get(CategoryPosition).getDiscount_type());
        intent.putExtra("DISCOUNT VALUE",topHitItemLists.get(CategoryPosition).getDiscount_value());
        intent.putExtra("STOCK QTY",topHitItemLists.get(CategoryPosition).getStock_qty());
        startActivity(intent);
    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }

        return false;
    }

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                TopHitData();
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

                conn = false;
                connected = false;

                topHitAdapter = new TopHitAdapter(topHitItemLists,mContext, TopHitItem.this);
                topHitView.setAdapter(topHitAdapter);

                if (topHitItemLists.size() == 0) {
                    noProduct.setVisibility(View.VISIBLE);
                } else {
                    noProduct.setVisibility(View.GONE);
                }


            }else {
                refresh.setVisibility(View.VISIBLE);
                Toast.makeText(mContext, "Slow Internet or No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(getContext())
//                        .setMessage("Slow Internet or Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel", null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new Check().execute();
//                        dialog.dismiss();
//                    }
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
            }
        }

    }

    public void TopHitData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            topHitItemLists = new ArrayList<>();
            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("select IEM_ID, IEM_IEM_ID, IEM_TYPE, IEM_NAME, IEM_THUMB,IEM_ITEM_ID,\n" +
                    "ITEM_PACK.GET_ITEM_UNIT(IEM_ITEM_ID) as ITM_UNIT,\n" +
                    "case when ITEM_PACK.GET_ITEM_PACKAGING_QTY(IEM_ITEM_ID) = 0 then  \n" +
                    " '(' ||ITEM_PACK.GET_ITEM_PACKAGING_QTY(IEM_ITEM_ID)||' '||ITEM_PACK.GET_ITEM_PACKAGING_UNIT(IEM_ITEM_ID) ||')' else \n" +
                    "ITEM_PACK.GET_ITEM_PACKAGING_QTY(IEM_ITEM_ID)||' '||ITEM_PACK.GET_ITEM_PACKAGING_UNIT(IEM_ITEM_ID) end AS PACKAGING_UNIT_QTY,\n" +
                    "CASE WHEN ITEM_PACK.GET_ITEM_SELLING_DISCOUNT(IEM_ITEM_ID) >0 THEN   TO_CHAR(ITEM_PACK.GET_ITEM_SELLING_DISCOUNT(IEM_ITEM_ID)) else\n" +
                    "TO_CHAR(ITEM_PACK.GET_ITEM_SELLING_PRICE(IEM_ITEM_ID)) end AS CARD_RATE,\n" +
                    "CASE WHEN ITEM_PACK.GET_ITEM_SELLING_DISCOUNT(IEM_ITEM_ID) >0 THEN  \n" +
                    "'Price : '||TO_CHAR(ITEM_PACK.GET_ITEM_SELLING_PRICE(IEM_ITEM_ID)) ELSE '' END as CARD_OTHER_INFO, \n" +
                    "SUBSTR(ITEM_PACK.GET_ITEM_DISCOUNT_DTL(IEM_ITEM_ID),1,3) as DISCOUNT_DTL,\n" +
                    "case when (SELECT DISTINCT IEM.IEM_ITEM_ID FROM ITEM_ECOM_MST IEM WHERE IEM.IEM_ITEM_ID = ITEM_ECOM_MST.IEM_ITEM_ID AND IEM_IEM_ID = 1) \n" +
                    "    IS NOT NULL THEN 'NEW' ELSE 'N' END AS NEW_ARRIVAL_TAG,\n" +
                    "decode(ITEM_PACK.GET_ECOMITEM_PRE_STOCK(IEM_ITEM_ID,NULL,NULL,SYSDATE),0,'StockOut','E') as ECOM_DIS,\n" +
                    "TO_CHAR(ITEM_PACK.GET_ITEM_SELLING_PRICE(IEM_ITEM_ID)) as REAL_PRICE,\n" +
                    "ITEM_PACK.GET_ITEM_STOCK_QTY(ITEM_ECOM_MST.IEM_ITEM_ID, ITEM_DTL.ITEM_COLOR_ID,ITEM_DTL.ITEM_SIZE_ID,SYSDATE) as STOCK\n" +
                    "from ITEM_ECOM_MST,ITEM_DTL\n" +
                    "where iem_type in (3)\n" +
                    "AND ITEM_ECOM_MST.IEM_ITEM_ID = ITEM_DTL.ITEM_ID\n" +
                    "AND IEM_IEM_ID =3\n" +
                    "and TO_CHAR(ITEM_PACK.GET_ITEM_SELLING_PRICE(IEM_ITEM_ID))>0\n" +
                    "ORDER by iem_id");

            while (resultSet.next()) {
                Blob b=resultSet.getBlob(5);
                byte[] barr =b.getBytes(1,(int)b.length());
                Bitmap bitmap = BitmapFactory.decodeByteArray(barr,0,barr.length);
                String discount_type = "";
                String discount_value = "";

                String dd = resultSet.getString(11);

                if (dd != null) {
                    if (!dd.isEmpty()) {
                        if (dd.contains("%")) {
                            discount_type = "%";
                            discount_value = dd.substring(0,dd.length()-1);
                        }
                        else {
                            discount_type = "SR";
                            discount_value = dd.substring(0,dd.length()-1);
                        }
                    }
                    else {
                        discount_type = "";
                        discount_value = "";
                    }
                }
                else {
                    discount_type = null;
                    discount_value = null;
                }
                topHitItemLists.add(new TopHitItemList(resultSet.getString(1), resultSet.getString(2),resultSet.getString(3),
                        resultSet.getString(4),bitmap,resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),
                        resultSet.getString(9),resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),
                        resultSet.getString(13),resultSet.getString(14),"",discount_type,discount_value,resultSet.getString(15)));
            }
            connected = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}