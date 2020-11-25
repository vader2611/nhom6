package vn.edu.poly.apptruyen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.edu.poly.apptruyen.adapter.TruyenTranhAdapter;
import vn.edu.poly.apptruyen.api.ApiLayTruyen;
import vn.edu.poly.apptruyen.interfaces.LayTruyenVe;
import vn.edu.poly.apptruyen.object.TruyenTranh;

public class ManHinhChuActivity extends AppCompatActivity implements LayTruyenVe {
    GridView gdvDSTruyen;
    DrawerLayout drawerLayout;
    TruyenTranhAdapter adapter;
    ArrayList<TruyenTranh> truyenTranhArrayList;
    EditText edtTimKiem;
    ImageView imgThoat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_chu);



        init();
        anhXa();
        setUp();
        setClick();
        new ApiLayTruyen(this).execute();
    }
    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }


    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }

    public void ClickHome(View view) {
        recreate();
    }





    public void ClickLogout(View view) {
        logout(this );
    }

    public static void logout(final Activity activity) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
                System.exit(0);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //show
        builder.show();
    }


    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
     @Override
     public void onBackPressed(){
        AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirn Exit..!!");
        alertDialogBuilder.setIcon(R.drawable.nutout);
        alertDialogBuilder.setMessage("Are you sure You want to exit");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ManHinhChuActivity.this,"",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog= alertDialogBuilder.create();
        alertDialog.show();



        }




    private void init(){
        truyenTranhArrayList= new ArrayList<>();

        adapter= new TruyenTranhAdapter(this,0, truyenTranhArrayList);
    }
    private void anhXa(){
        drawerLayout = findViewById(R.id.drawer_layout);
        gdvDSTruyen= findViewById(R.id.gdvDSTruyen);
        edtTimKiem= findViewById(R.id.edtTimKiem);
    }
    private void setUp(){
        gdvDSTruyen.setAdapter(adapter);
    }
    private void setClick(){
        edtTimKiem.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s= edtTimKiem.getText().toString();
                adapter.sortTruyen(s);

            }
        });
        gdvDSTruyen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TruyenTranh truyenTranh= truyenTranhArrayList.get(i);
                Bundle b= new Bundle();
                b.putSerializable("truyen",truyenTranh);
                Intent intent= new Intent(ManHinhChuActivity.this,ChapActivity.class);
                intent.putExtra("data",b);
                startActivity(intent);
            }
        });
    }

    @Override
    public void batDau() {
        Toast.makeText(this,"Đang tải", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ketThuc(String data) {
        try {
            truyenTranhArrayList.clear();
            JSONArray arr = new JSONArray(data);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                truyenTranhArrayList.add(new TruyenTranh(o));
            }
            adapter= new TruyenTranhAdapter(this,0, truyenTranhArrayList);
            gdvDSTruyen.setAdapter(adapter);
        } catch (JSONException e) {

        }
    }

    @Override
    public void biLoi() {
        Toast.makeText(this,"Lỗi kết nối", Toast.LENGTH_SHORT).show();

    }

    public void update(View view) {
        new ApiLayTruyen(this).execute();
    }

}


