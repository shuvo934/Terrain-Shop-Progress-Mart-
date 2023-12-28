package com.shuvo.ttit.terrainshop.myorders.orderDetails;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.myorders.adpaters.OrderDetailsAdapter;
import com.shuvo.ttit.terrainshop.myorders.lists.OrderDetailsList;
import com.shuvo.ttit.terrainshop.pdfCreation.PDFUtils;
import com.shuvo.ttit.terrainshop.pdfCreation.PdfDocumentAdapter;
import com.shuvo.ttit.terrainshop.pdfCreation.model.SuperHeroModel;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderDetails extends AppCompatActivity {

    TextView orderNo;
    String order_no = "";

    TextView address;
    String d_address = "";

    TextView oDate;
    String o_date = "";

    TextView dDate;
    String d_date = "";

    TextView dTime;
    String d_time = "";

    TextView deliveryCharge;

    TextView dStatus;
    String d_status = "";

    RecyclerView odsView;
    RecyclerView.LayoutManager layoutManager;
    OrderDetailsAdapter orderDetailsAdapter;

    ArrayList<OrderDetailsList> orderDetailsLists;

    TextView subTotal;
    int sub_total = 0;

    TextView totalPrice;
    String total_price = "";

    Connection connection;
    private Boolean conn = false;
    private Boolean connected = false;
    String som_id = "";
    String delivery_charge = "";

    RelativeLayout layout;
    CircularProgressIndicator circularProgressIndicator;

    ImageView orderDBar;
    private AsyncTask mTask;
    ScrollView orderScroll;
    ImageView print;

    private static final  String FILE_PRINT = "last_file_print.pdf";
    private AlertDialog alertDialog;

//    ArrayList<SuperHeroModel> superHeroModels = new ArrayList<>();
    InnerPrinterCallback innerPrinterCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        orderNo = findViewById(R.id.order_no_order_details);
        address = findViewById(R.id.delivery_address_order_details);
        oDate = findViewById(R.id.order_date_order_details);
        dDate = findViewById(R.id.delivery_date_order_details);
        dTime = findViewById(R.id.delivery_time_order_details);
        dStatus = findViewById(R.id.order_tracking_details);

        odsView = findViewById(R.id.order_summary_list_view_order_details);

        subTotal = findViewById(R.id.subtotal_of_order_summary_order_details);

        totalPrice = findViewById(R.id.total_price_of_order_order_details);

        layout = findViewById(R.id.order_details_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_orders_details);
        circularProgressIndicator.setVisibility(View.GONE);

        orderDBar = findViewById(R.id.back_logo_check_my_orders_details);

        print = findViewById(R.id.print_order_details);
        print.setVisibility(View.GONE);

        deliveryCharge = findViewById(R.id.delivery_fee_total_order_details);

        orderScroll = findViewById(R.id.order_details_scroll_view);

        orderDetailsLists = new ArrayList<>();

        Intent intent = getIntent();
        order_no = intent.getStringExtra("ORDER NO");
        d_address = intent.getStringExtra("DELIVERY ADDRESS");
        d_date = intent.getStringExtra("DELIVERY DATE");
        o_date = intent.getStringExtra("ORDER DATE");
        som_id = intent.getStringExtra("SOM ID");
        delivery_charge = intent.getStringExtra("DELIVERY FEE");
        d_time = intent.getStringExtra("DELIVERY TIME");
        d_status = intent.getStringExtra("DELIVERY STATUS");

        String orno = "ORDER NO: \n" + order_no;
        orderNo.setText(orno);
        address.setText(d_address);
        dDate.setText(d_date);
        oDate.setText(o_date);
        String dc = "৳ " + delivery_charge;
        deliveryCharge.setText(dc);
        dTime.setText(d_time);
        d_status = "ORDER " +d_status;
        dStatus.setText(d_status);

        odsView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        odsView.setLayoutManager(layoutManager);

        orderDBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTask != null) {
                    if (mTask.getStatus().toString().equals("RUNNING")) {
                        Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
                    } else {
                        finish();
                    }
                }
                else {
                    finish();
                }
            }
        });

        InnerResultCallback innerResultCallback = new InnerResultCallback() {
            @Override
            public void onRunResult(boolean isSuccess) throws RemoteException {

            }

            @Override
            public void onReturnString(String result) throws RemoteException {

            }

            @Override
            public void onRaiseException(int code, String msg) throws RemoteException {

            }

            @Override
            public void onPrintResult(int code, String msg) throws RemoteException {

            }
        };

        innerPrinterCallBack = new InnerPrinterCallback() {
            @Override
            protected void onConnected(SunmiPrinterService service) {
                Toast.makeText(OrderDetails.this, "Printer Connected", Toast.LENGTH_SHORT).show();

                try {
                    System.out.println("PRINTER");
                    service.setAlignment(1,innerResultCallback);
                    service.printTextWithFont("ORDER DETAILS\n","",36, innerResultCallback);
                    service.printText("-------------------------------------------------------\n",innerResultCallback);
                    service.printTextWithFont("ORDER CONFIRMED\n","",30, innerResultCallback);
                    service.printText("-------------------------------------------------------\n",innerResultCallback);
                    service.printText("ORDER NO: "+order_no+"\n",innerResultCallback);
                    service.printText("-------------------------------------------------------\n",innerResultCallback);

                    service.printColumnsString(new String[]{"ADDRESS: ", "",d_address},new int[]{2,2,5},
                            new int[]{0,1,2},innerResultCallback);
                    service.printColumnsString(new String[]{"ORDER DATE: ", "",o_date},new int[]{2,2,5},
                            new int[]{0,1,2},innerResultCallback);
                    service.printColumnsString(new String[]{"DELIVERY DATE: ", "",d_date},new int[]{2,2,5},
                            new int[]{0,1,2},innerResultCallback);
                    service.printColumnsString(new String[]{"DELIVERY TIME: ", "",d_time},new int[]{2,2,5},
                            new int[]{0,1,2},innerResultCallback);

                    service.printText("-------------------------------------------------------\n",innerResultCallback);
                    service.printTextWithFont("ORDER SUMMARY\n","",30, innerResultCallback);
                    service.printText("-------------------------------------------------------\n",innerResultCallback);


                    for (int i = 0; i < orderDetailsLists.size(); i++) {
                        service.printColumnsString(new String[]{orderDetailsLists.get(i).getItem_name(), "x"+orderDetailsLists.get(i).getSod_qty(),"TK "+orderDetailsLists.get(i).getOrder_rate()},new int[]{5,2,2},
                                        new int[]{0,1,2},innerResultCallback);
                    }
//                    service.printColumnsString(new String[]{"5 Star Chocolate bar-25 GM", "x1","$40"},new int[]{5,2,2},
//                            new int[]{0,1,2},innerResultCallback);
//                    service.printColumnsString(new String[]{"DAIRY MILK 30% SUGAR 100 GM", "x1","$300"},new int[]{5,2,2},
//                            new int[]{0,1,2},innerResultCallback);
//                    service.printColumnsString(new String[]{"LOTTE WHITE COOKIE", "x2","$200"},new int[]{5,2,2},
//                            new int[]{0,1,2},innerResultCallback);
                    int subtotal= 0;
                    for (int i = 0; i < orderDetailsLists.size() ; i++) {
                        subtotal = subtotal + Integer.parseInt(orderDetailsLists.get(i).getOrder_rate());
                    }

                    service.printText("-------------------------------------------------------\n",innerResultCallback);
                    service.printColumnsString(new String[]{"Sub Total", "","TK " +String.valueOf(subtotal)},new int[]{5,2,2},
                            new int[]{0,1,2},innerResultCallback);
                    service.printColumnsString(new String[]{"Delivery Charge", "","TK " + delivery_charge},new int[]{5,2,2},
                            new int[]{0,1,2},innerResultCallback);
                    subtotal = subtotal + Integer.parseInt(delivery_charge);
                    service.printColumnsString(new String[]{"TOTAL", "","TK " +String.valueOf(subtotal)},new int[]{5,2,2},
                            new int[]{0,1,2},innerResultCallback);
                    service.printText("-------------------------------------------------------\n",innerResultCallback);
                    service.printTextWithFont("THANK YOU\n\n","",36, innerResultCallback);
                    service.lineWrap(3,innerResultCallback);
                    service.cutPaper(innerResultCallback);
                    disconnect();

                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected void onDisconnected() {

                Toast.makeText(OrderDetails.this, "Printer Disconnected", Toast.LENGTH_SHORT).show();
            }
        };

        alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Please Wait")
                .create();


//        superHeroModels.add(new SuperHeroModel("Apple",
//                "https://chaldn.com/_mpimage/green-apple-50-gm-1-kg?src=https%3A%2F%2Feggyolk.chaldal.com%2Fapi%2FPicture%2FRaw%3FpictureId%3D119065&q=best&v=1&m=400",
//                "Discover the innovative world of Apple and shop everything iPhone, iPad, Apple Watch, Mac, and Apple TV, plus explore accessories, entertainment"));

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result;
                try {
                    System.out.println("PRINTER First");
                    result = InnerPrinterManager.getInstance().bindService(getApplicationContext(),innerPrinterCallBack);
//                    Toast.makeText(OrderDetails.this, "Result: "+result, Toast.LENGTH_SHORT).show();
                } catch (InnerPrinterException e) {
                    result = false;
                    e.printStackTrace();
                }
                if (!result) {
                    alertDialog.show();
                    createPDFFile(new StringBuilder(getFilepath()).toString());
                }


            }
        });


        mTask = new Check().execute();


    }


    private void disconnect() {
        try {
            InnerPrinterManager.getInstance().unBindService(OrderDetails.this, innerPrinterCallBack);
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }
    }
    private void createPDFFile(String path) {
        if (new File(path).exists()) {
            new File(path).delete();
        }
        System.out.println(path);

        try {
            File file = new File(path);
            Document document = new Document();
            //Save
            PdfWriter.getInstance(document,new FileOutputStream(path));
            //Open
            document.open();
            //Setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("SHUVO");
            document.addCreator("TTIT");
            //Font Setting
            BaseColor color = new BaseColor(0,153,204,255);
            float fontSize = 11.0f;
            //Custom font
            BaseFont fontName = BaseFont.createFont("assets/font/Roboto-Regular.ttf","UTF-8",BaseFont.EMBEDDED);
            Font descriptionFont = new Font(fontName,14.0f,Font.NORMAL,BaseColor.BLACK);
            Font textFont = new Font(fontName, fontSize,Font.BOLD,BaseColor.BLACK);

            //Create title of Document
            Font titleFont = new Font(fontName,28.0f,Font.BOLD,BaseColor.BLACK);
            Font secondTitleFont = new Font(fontName,18.0f,Font.BOLD,BaseColor.BLACK);
            PDFUtils.addNewItem(document,"ORDER DETAILS\n", Element.ALIGN_CENTER,titleFont);
            PDFUtils.addNewItem(document,"--", Element.ALIGN_CENTER,titleFont);
            PDFUtils.addNewItem(document,"ORDER NO: " + order_no, Element.ALIGN_CENTER,textFont);
            PDFUtils.addLineSeperator(document);
            PDFUtils.addNewItem(document,"ORDER CONFIRMED",Element.ALIGN_CENTER,secondTitleFont);
            PDFUtils.addLineSeperator(document);

            //Add more information
            PDFUtils.addNewItemWithLeftAndRight(document,"ADDRESS: ",d_address,textFont,textFont);
            PDFUtils.addNewItemWithLeftAndRight(document,"ORDER DATE: ",o_date,textFont,textFont);
            PDFUtils.addNewItemWithLeftAndRight(document,"DELIVERY DATE: ",d_date,textFont,textFont);
            PDFUtils.addNewItemWithLeftAndRight(document,"DELIVERY TIME: ",d_time,textFont,textFont);
            PDFUtils.addLineSeperator(document);
            PDFUtils.addNewItem(document,"ORDER SUMMARY", Element.ALIGN_LEFT,secondTitleFont);
//            PDFUtils.addNewItem(document,"--", Element.ALIGN_CENTER,titleFont);
//            PDFUtils.addNewItem(document,"Ahasanul Haque", Element.ALIGN_LEFT,textFont);
//            PDFUtils.addLineSeperator(document);

            //Add detail
            PDFUtils.addLineSeperator(document);
//            PDFUtils.addNewItem(document, "DETAIL \n List",Element.ALIGN_CENTER,titleFont);
//            PDFUtils.addLineSeperator(document);

            //Use Rxjava, fetch Image from URL and add to PDF
            Observable.fromIterable(orderDetailsLists)
                    .flatMap(model -> getBitmapFromORDER(this,model,document))
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(model ->{
                       //On next
                       //Each Item, we will add detail
                        PDFUtils.addNewItemWithLeftAndRight(document,model.getItem_name()+"\n"+model.getSod_qty() + " Pcs","TK "+model.getOrder_rate(),textFont,textFont);

//                        PDFUtils.addLineSeperator(document);

                        PDFUtils.addNewItem(document, " ",Element.ALIGN_CENTER,textFont);

//                        PDFUtils.addLineSeperator(document);
                    },throwable -> {
                        //On Error
                        alertDialog.dismiss();
                        Toast.makeText(this,""+throwable.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    },() -> {
                        //On Complete
                        //When Completed, close document
                        int subtotal= 0;
                        for (int i = 0; i < orderDetailsLists.size() ; i++) {
                            subtotal = subtotal + Integer.parseInt(orderDetailsLists.get(i).getOrder_rate());
                        }

                        PDFUtils.addLineSeperator(document);
                        PDFUtils.addNewItemWithLeftAndRight(document,"Sub Total", "TK "+String.valueOf(subtotal),secondTitleFont,secondTitleFont);
//                        PDFUtils.addNewItem(document, " ",Element.ALIGN_CENTER,textFont);
                        PDFUtils.addNewItemWithLeftAndRight(document,"Delivery Charge", "TK "+delivery_charge,secondTitleFont,secondTitleFont);

                        subtotal = subtotal + Integer.parseInt(delivery_charge);
                        PDFUtils.addLineSeperator(document);

                        PDFUtils.addNewItemWithLeftAndRight(document,"TOTAL","TK "+String.valueOf(subtotal),secondTitleFont,secondTitleFont);
                        PDFUtils.addNewItem(document, " ",Element.ALIGN_CENTER,textFont);

                        PDFUtils.addLineSeperator(document);
                        PDFUtils.addNewItem(document,"THANK YOU",Element.ALIGN_CENTER,secondTitleFont);
                        document.close();
                        alertDialog.dismiss();
//                        Toast.makeText(this,"Success!", Toast.LENGTH_SHORT).show();

                        printPDF();
                    });

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            alertDialog.dismiss();
        }
    }

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
//        PrintAttributes attributes = new PrintAttributes.Builder()
//                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
//                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
//                .setResolution(new PrintAttributes.Resolution("res", "Resolution", 300, 300))
////                .setFlags(PrintAttributes.FLAGS_SILENT) // Enable silent printing
//                .build();
        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(this,new StringBuilder(getFilepath())
                    .toString(),FILE_PRINT);
            printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());
//            printManager.print("Document",printDocumentAdapter,attributes);
        }catch (Exception e) {

            Toast.makeText(this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private ObservableSource<SuperHeroModel> getBitmapFromUrl(Context context, SuperHeroModel model, Document document) {
        return Observable.fromCallable(()->{

            Bitmap bitmap = Glide.with(context)
                    .asBitmap()
                    .load(model.getImage())
                    .submit().get();

            Image image = Image.getInstance(bitmapToByteArray(bitmap));
            image.scaleAbsolute(50,50);
            document.add(image);

            return model;
        });
    }

    private ObservableSource<OrderDetailsList> getBitmapFromORDER(Context context, OrderDetailsList model, Document document) {
        return Observable.fromCallable(()->{

//            Bitmap bitmap = Glide.with(context)
//                    .asBitmap()
//                    .load(model.getImage())
//                    .submit().get();

            Image image = Image.getInstance(bitmapToByteArray(model.getBitmap()));
            image.scaleAbsolute(50,50);
            document.add(image);
            return model;
        });
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        return stream.toByteArray();
    }

    private String getFilepath() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator
                + FILE_PRINT );
//        if (!dir.exists()) {
//            dir.delete();
//        }
        System.out.println(dir.getPath());
        return  dir.getPath();
    }

    @Override
    public void onBackPressed() {
        if (mTask != null) {
            if (mTask.getStatus().toString().equals("RUNNING")) {
                Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
        else {
            finish();
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

            circularProgressIndicator.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            orderScroll.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                OrderHistoryQuery();
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
            layout.setVisibility(View.VISIBLE);

            if (conn) {

                orderScroll.setVisibility(View.VISIBLE);
                print.setVisibility(View.VISIBLE);
                orderDetailsAdapter = new OrderDetailsAdapter( OrderDetails.this,orderDetailsLists);
                odsView.setAdapter(orderDetailsAdapter);

                sub_total= 0;
                for (int i = 0; i < orderDetailsLists.size() ; i++) {
                    sub_total = sub_total + Integer.parseInt(orderDetailsLists.get(i).getOrder_rate());
                }


                String st = "৳ " + String.valueOf(sub_total);
                subTotal.setText(st);

                sub_total = sub_total + Integer.parseInt(delivery_charge);
                total_price = "৳ " + String.valueOf(sub_total);
                totalPrice.setText(total_price);




            }else {
                orderScroll.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(OrderDetails.this)
                        .setMessage("Please Check Your Internet Connection")
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

    public void OrderHistoryQuery () {

        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();
            orderDetailsLists = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("SELECT SOD_ITEM_ID, SOD_QTY,SOD_DEFINE_RATE,\n" +
                    "(SOD_DEFINE_RATE - (CASE WHEN SOD_DISCOUNT_TYPE is NULL then 0 \n" +
                    "else \n" +
                    "(CASE WHEN SOD_DISCOUNT_TYPE = '%' then SOD_DEFINE_RATE * SOD_DISCOUNT_VALUE/100 \n" +
                    "else\n" +
                    "SOD_DISCOUNT_VALUE end) \n" +
                    "end)) as CARD_RATE,\n" +
                    "((SOD_DEFINE_RATE - (CASE WHEN SOD_DISCOUNT_TYPE is NULL then 0 \n" +
                    "else \n" +
                    "(CASE WHEN SOD_DISCOUNT_TYPE = '%' then SOD_DEFINE_RATE * SOD_DISCOUNT_VALUE/100 \n" +
                    "else\n" +
                    "SOD_DISCOUNT_VALUE end) \n" +
                    "end)) * SOD_QTY) as TOTAL,\n" +
                    "(SELECT IEM_NAME\n" +
                    "FROM ITEM_ECOM_MST\n" +
                    "WHERE iem_id = (Select max(iem_id) from ITEM_ECOM_MST WHERE IEM_ITEM_ID = SOD_ITEM_ID)) NAME,SOD_DISCOUNT_TYPE, \n" +
                    "(SELECT IEM_THUMB\n" +
                    "FROM ITEM_ECOM_MST\n" +
                    "WHERE iem_id = (Select max(iem_id) from ITEM_ECOM_MST WHERE IEM_ITEM_ID = SOD_ITEM_ID)) PIC,ITEM_PACK.GET_ITEM_UNIT(SOD_ITEM_ID)\n" +
                    "FROM SALES_ORDER_DTL\n" +
                    "WHERE SOD_SOM_ID = "+som_id+"");

            while (rs.next()) {
                Blob b=rs.getBlob(8);
                byte[] barr =b.getBytes(1,(int)b.length());
                Bitmap bitmap = BitmapFactory.decodeByteArray(barr,0,barr.length);
                orderDetailsLists.add(new OrderDetailsList(rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),bitmap,rs.getString(9)));
            }
            rs.close();

            stmt.close();

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}