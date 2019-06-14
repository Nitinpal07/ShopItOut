package nitin.luckyproject.shopitout;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import nitin.luckyproject.shopitout.Model.Data;


public class HomeActivity extends AppCompatActivity {


    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Data> list;
    MyAdapter adapter;
    FloatingActionButton mfab_btn;
    Toolbar mtoolbar;
    FirebaseAuth mFirebaseauth;



    public HomeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeactivity);


//toolbar setTitle
        mtoolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Daily Shopping List");

        recyclerView =  findViewById(R.id.recycler_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        reference = FirebaseDatabase.getInstance().getReference().child("Daily Shopping List").child("Shopping List").getRef();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        String type = dataSnapshot1.child("type").getValue(String.class);
                        String date = dataSnapshot1.child("date").getValue(String.class);
                        String amount = dataSnapshot1.child("amount").getValue(String.class);
                        String note = dataSnapshot1.child("note").getValue(String.class);
                        String id = dataSnapshot1.child("id").getValue(String.class);

                        Data p = new Data(type, amount, note, date, id);
                        list.add(p);


                }
                adapter = new MyAdapter(HomeActivity.this, list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }

        });

        mfab_btn = findViewById(R.id.fab);
        mfab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog();
            }
        });
    }


    private void CustomDialog() {
        mFirebaseauth = FirebaseAuth.getInstance();


        reference = FirebaseDatabase.getInstance().getReference("Daily Shopping List").child("Shopping List").push();

        AlertDialog.Builder mydialog = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater infalter = LayoutInflater.from(HomeActivity.this);
        View myview = infalter.inflate(R.layout.input_data, null);

        final AlertDialog dialog = mydialog.create();

        dialog.setView(myview);

        final EditText type = myview.findViewById(R.id.item_type);
        final EditText amount = myview.findViewById(R.id.item_amount);
        final EditText note = myview.findViewById(R.id.item_note);
        Button btnsave = myview.findViewById(R.id.btn_save_item);

        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String mtype = type.getText().toString().trim();
                String mamount = amount.getText().toString().trim();
                String mnote = note.getText().toString().trim();

                if (TextUtils.isEmpty(mtype)) {
                    type.setError("REQUIRED FIELD..");
                    return;
                }
                if (TextUtils.isEmpty(mamount)) {
                    amount.setError("REQUIRED FIELD..");
                    return;
                }
                if (TextUtils.isEmpty(mnote)) {
                    note.setError("REQUIRED FIELD..");
                    return;
                }
                String id = reference.push().getKey();
                //date format

                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                Data data = new Data(mtype, mamount, mnote, date, id);

                reference.setValue(data);
                Toast.makeText(HomeActivity.this, "STORED TO DATABASE", Toast.LENGTH_LONG).show();

                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        Context context;
        ArrayList<Data> datas;

        MyAdapter(Context c, ArrayList<Data> p) {
            context = c;
            datas = p;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_data, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.type.setText(datas.get(position).getType());
            holder.amount.setText(datas.get(position).getAmount());
            holder.note.setText(datas.get(position).getNote());
            holder.date.setText(datas.get(position).getDate());

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView type, amount, note, date;

            MyViewHolder(View itemView) {
                super(itemView);
                type =  itemView.findViewById(R.id.type);
                amount =  itemView.findViewById(R.id.amount_item);
                note = itemView.findViewById(R.id.note);
                date = itemView.findViewById(R.id.date);
            }

        }
    }

}
