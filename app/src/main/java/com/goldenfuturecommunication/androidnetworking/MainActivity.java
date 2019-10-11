package com.goldenfuturecommunication.androidnetworking;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
ImageView imageView,imageView2,imageView3,imageView4,imageView5,imageView6;
    private ImageLoader imageLoader;
    Button fetch,fetch2;
    List<List_Data> list_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_data=new ArrayList<>();

        imageView=(ImageView)findViewById(R.id.imageView);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        imageView3=(ImageView)findViewById(R.id.imageView3);
        imageView4=(ImageView)findViewById(R.id.imageView4);
        imageView5=(ImageView)findViewById(R.id.imageView5);
        imageView6=(ImageView)findViewById(R.id.imageView6);
        fetch=(Button)findViewById(R.id.fetch);
        fetch2=(Button)findViewById(R.id.fetch2);

        imageLoader = ImageLoader.getInstance(); // Get singleton instance
        imageLoader.init(ImageLoaderConfiguration
                .createDefault(MainActivity.this));


        // from Web
        imageLoader.displayImage("https://images.unsplash.com/photo-1500622944204-b135684e99fd?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80", imageView);

        // from drawables
        imageLoader.displayImage("drawable://" + R.drawable.img1, imageView2);
        //from SD card
        imageLoader.displayImage("file:///mnt/sdcard/bird.jpg", imageView3);
        //video thumbnail
        imageLoader.displayImage("file:///mnt/sdcard/myvideo.mp4", imageView4);

//load image as circular
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new CircleBitmapDisplayer())
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(R.drawable.avatar)
                .showImageOnFail(R.drawable.avatar)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        imageLoader.displayImage("https://images.unsplash.com/photo-1500622944204-b135684e99fd?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80", imageView5,options);



        //load empty or wrong url
        DisplayImageOptions option = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(R.drawable.avatar)
                .showImageOnFail(R.drawable.avatar)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        imageLoader.displayImage("abc", imageView6,option);




        /*
        "content://media/external/images/media/13" // from content provider
"content://media/external/video/media/13" // from content provider (video thumbnail)
"assets://image.png" // from assets
*/


        AndroidNetworking.initialize(getApplicationContext());

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.post("https://furiousbuddy.com/Android/GFCAttendance/test.php")
                        .addBodyParameter("emp_id", "Amit")
                        .addBodyParameter("emp_name", "Shekhar")
                        .addBodyParameter("designation", "developer")
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(MainActivity.this, response+"", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });

        fetch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.post("https://furiousbuddy.com/Android/GFCAttendance/check_emp.php")
                        .addBodyParameter("emp_id", "GFC/EMP0001")
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                    JSONArray array = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject ob = array.getJSONObject(i);
                                        List_Data listData = new List_Data(
                                                ob.getString("employee_id"),
                                                ob.getString("employee_name"),
                                                ob.getString("designation")
                                        );
                                        list_data.add(listData);
                                        Toast.makeText(MainActivity.this, listData.getEmp_id()+"  "+listData.getName()+"  "+listData.getDesignation(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(MainActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


    }
}
