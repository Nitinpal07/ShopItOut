package nitin.luckyproject.shopitout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Date;
import java.util.Locale;

import nitin.luckyproject.shopitout.Model.Data;


public class HomeActivity extends AppCompatActivity {

    private Query query;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseauth;
    private Toolbar mtoolbar;
    private FloatingActionButton mfab_btn;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Data,Myviewholder> adapter;
    private FirebaseRecyclerOptions<Data> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeactivity);


//toolbar setTitle
        mtoolbar =findViewById(R.id.home_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Daily Shopping List");

        mFirebaseauth= FirebaseAuth.getInstance();
       FirebaseUser muser= mFirebaseauth.getCurrentUser();
        String uid= muser.getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Daily Shopping List").child("Shopping List").child(uid);

        //Record of RecyclerView
        recyclerView =findViewById(R.id.recycler_home);
        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);

        mDatabaseReference.keepSynced(true);

        query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Shopping List");


        options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(query,Data.class)
                .build();


        adapter =new FirebaseRecyclerAdapter<Data,Myviewholder>(options) {

            @NonNull
            @Override
            public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view =LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_data,parent,false);
                return new Myviewholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Myviewholder myviewholder, int i, @NonNull Data data) {

                myviewholder.setType(data.getType());
                myviewholder.setNote(data.getNote());
                myviewholder.setDate(data.getDate());
                myviewholder.setAmount(data.getAmount());
            }
        };


        recyclerView.setAdapter(adapter);

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


    public static class Myviewholder extends RecyclerView.ViewHolder{

        View myView;
        public Myviewholder(View itemview){
            super(itemview);
            myView=itemview;
        }
        public void setType(String type){
            TextView mType =myView.findViewById(R.id.type);
            mType.setText(type);
        }
        public void setNote(String note){
            TextView mNote =myView.findViewById(R.id.note);
            mNote.setText(note);
        }
        public void setDate(String date){
            TextView mDate =myView.findViewById(R.id.date);
            mDate.setText(date);
        }
        public void setAmount(int amount){
            TextView mamount =myView.findViewById(R.id.amount_item);
            String samount =String.valueOf(amount);
            mamount.setText(samount);
        }


    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }
}
