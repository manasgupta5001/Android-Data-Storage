package com.example.storage;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import android.os.Environment;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    EditText uname, pwd;
    Button saveBtn,rdBtn;
    FileOutputStream fstream;
    FileInputStream ifstream;


    EditText inputText;
    TextView response;
    Button saveButton,readButton;

    String filename = "File.txt";
    String filepath = "MyFileStorage";
    File myExternalFile;
    String myData = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uname = (EditText)findViewById(R.id.txtName);
        pwd = (EditText)findViewById(R.id.txtPwd);
        saveBtn = (Button)findViewById(R.id.btnSave);
        rdBtn=(Button)findViewById(R.id.rdFile);



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = uname.getText().toString()+"\n";
                String password = pwd.getText().toString();
                try {
                    fstream = openFileOutput("user_details", Context.MODE_PRIVATE);
                    fstream.write(username.getBytes());
                    fstream.write(password.getBytes());
                    fstream.close();
                    Toast.makeText(getApplicationContext(), "Details Saved Successfully",Toast.LENGTH_SHORT).show();
                    uname.setText("");
                    pwd.setText("");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

       rdBtn.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               try {
                   ifstream = openFileInput("user_details");
                   StringBuffer sbuffer = new StringBuffer();
                   int i;
                   while ((i = ifstream.read())!= -1){
                       sbuffer.append((char)i);
                   }
                   fstream.close();
                   String details[] = sbuffer.toString().split("\n");
                   uname.setText(details[0]);
                   pwd.setText(details[1]);
                   Toast.makeText(getApplicationContext(), "Details Retrieved Successfully",Toast.LENGTH_LONG).show();
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       });


        inputText = (EditText) findViewById(R.id.myInputText);
        response = (TextView) findViewById(R.id.response);
        saveButton = (Button) findViewById(R.id.saveExternalStorage);
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                  final  FileOutputStream fos = new FileOutputStream(myExternalFile);
                    fos.write(inputText.getText().toString().getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputText.setText("");
                response.setText("Saved to External Storage...");
            }
        });

        readButton = (Button) findViewById(R.id.getExternalStorage);

        readButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    FileInputStream fis = new FileInputStream(myExternalFile);
                    DataInputStream in = new DataInputStream(fis);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String strLine;
                    while ((strLine = br.readLine()) != null) {
                        myData = myData + strLine;
                    }

                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                inputText.setText(myData);
                response.setText("Data retrieved from Internal Storage...");
            }
        });

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            saveButton.setEnabled(false);
        }
        else {
            myExternalFile = new File(getExternalFilesDir(filepath), filename);
        }


    }
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }


}
