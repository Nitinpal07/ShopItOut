package nitin.luckyproject.shopitout;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button btnlogin;
    private TextView signUp;
    private FirebaseAuth mauth;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email =findViewById(R.id.email_login);
        pass = findViewById(R.id.email_password);
        btnlogin =findViewById(R.id.btn_login);
        signUp=findViewById(R.id.sign_up);
        mauth = FirebaseAuth.getInstance();

         if(mauth.getCurrentUser()!=null){
             startActivity(new Intent(getApplicationContext(),HomeActivity.class));
         }

         mDialog = new ProgressDialog(this);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail= email.getText().toString().trim();
                String mPass= pass.getText().toString().trim();

                if(TextUtils.isEmpty(mEmail)){
                    email.setError("Required Field..");
                    return;
                }
                if(TextUtils.isEmpty(mPass)){
                    pass.setError("Required Field..");
                    return;
                }
                mDialog.setMessage("Processing..");
                mDialog.show();
               mauth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                           Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                          mDialog.dismiss();
                       }
                       else{
                           Toast.makeText(MainActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
                       mDialog.dismiss();
                       }
                   }
               });

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });
    }
}
