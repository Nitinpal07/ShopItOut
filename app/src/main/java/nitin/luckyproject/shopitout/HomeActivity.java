package nitin.luckyproject.shopitout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Locale;

import nitin.luckyproject.shopitout.Model.Data;


public class HomeActivity extends AppCompatActivity {


private DatabaseReference mDatabaseReference;
private FirebaseAuth mFirebaseauth;
private Toolbar mtoolbar;
private FloatingActionButton mfab_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeactivity);


        mtoolbar =findViewById(R.id.home_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Daily Shopping List");

        mFirebaseauth= FirebaseAuth.getInstance();
        FirebaseUser muser= mFirebaseauth.getCurrentUser();
        String uid= muser.getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Daily Shopping List").child("Shopping List").child(uid);


       mfab_btn =findViewById(R.id.fab);
       mfab_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                CustomDialog();
           }
       });
    }


    private void CustomDialog(){
        AlertDialog.Builder mydialog =new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater infalter=LayoutInflater.from(HomeActivity.this);
        View myview= infalter.inflate(R.layout.input_data,null);

        final AlertDialog dialog = mydialog.create();

        dialog.setView(myview);

        final EditText type= myview.findViewById(R.id.item_type);
        final EditText amount=myview.findViewById(R.id.item_amount);
        final EditText note =myview.findViewById(R.id.item_note);
        Button btnsave=myview.findViewById(R.id.btn_save_item);

        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String mtype=type.getText().toString().trim();
                String mamount=amount.getText().toString().trim();
                String mnote=note.getText().toString().trim();
                int ammount = Integer.parseInt(mamount);
                if(TextUtils.isEmpty(mtype)){
                    type.setError("REQUIRED FIELD..");
                    return;
                }
                if(TextUtils.isEmpty(mamount)){
                    amount.setError("REQUIRED FIELD..");
                    return;
                }
                if(TextUtils.isEmpty(mnote)){
                    note.setError("REQUIRED FIELD..");
                    return;
                }
                 String id= mDatabaseReference.push().getKey();
                //date format

                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                Data data = new Data(mtype,ammount,mnote,date,id);

                 mDatabaseReference.setValue(data);
                 Toast.makeText(HomeActivity.this, "STORED TO DATABASE", Toast.LENGTH_LONG).show();

                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
