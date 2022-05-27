package com.example.doancuoiky.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.doancuoiky.R;

public class ListAvatarActivity extends Activity {

    private TableLayout tblListNameAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_avatar);

        tblListNameAvatar = findViewById(R.id.table_layout_list_avatar_name);
        int rows = 5;
        int columns = 3;

        // tạo dòng và cột
        for(int i = 1;i <= rows; i++){
            TableRow tableRow = new TableRow(this);

            // tạo cột -> ImageView
            for(int j = 1;j <= columns; j++){
                ImageView imgAvatar = new ImageView(this);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(200,200);

                final int position = columns * (i-1) + j - 1;

                String PACKAGE_NAME = getApplicationContext().getPackageName();
                int idImg = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" +
                        ChangeInfoActivity.arrayListAvatar.get(position),null,null);
                imgAvatar.setPadding(10,10,10,10);
                imgAvatar.setLayoutParams(layoutParams);
                imgAvatar.setImageResource(idImg);

                // thêm ImageView vào TableRow
                tableRow.addView(imgAvatar);

                imgAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra("imageSelected",ChangeInfoActivity.arrayListAvatar.get(position));
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });
            }

            // add TableRow vào TableLayout
            tblListNameAvatar.addView(tableRow);
        }
    }
}