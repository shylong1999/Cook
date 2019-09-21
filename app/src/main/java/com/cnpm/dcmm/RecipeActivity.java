package com.cnpm.dcmm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {
    DatabaseReference databaseReferenceup;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    ImageView imageDish;
    ImageView imageuser;
    TextView nameuser;
    TextView making;
    TextView namedish;
    private RecyclerView recyclerView;
    String stringimageDish;
    String stringimageuser;
    String stringemailuser;
    String stringnamedish;
    String stringmaking;
    ArrayList<Comment> listcmt;
    CommentAdapter2 commentAdapter;
    ImageButton send;
    EditText editcmt;
    ImageView imageviewcm;
    ScrollView scrollView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        imageuser=(ImageView) findViewById(R.id.imageuserdish);
        imageDish=findViewById(R.id.imagedish);
        nameuser= findViewById(R.id.nameuserdish);
        making=findViewById(R.id.making);
        namedish= findViewById(R.id.namedish);

        Toast.makeText(RecipeActivity.this,firebaseUser.getEmail().toString(),Toast.LENGTH_LONG).show();
        //getdata
        stringimageDish=getIntent().getStringExtra("image");
        stringimageuser=getIntent().getStringExtra("imageuser");
        stringemailuser=getIntent().getStringExtra("emailuser");
        stringnamedish=getIntent().getStringExtra("namedish");
        stringmaking=getIntent().getStringExtra("making");

        //set
        Picasso.get().load(stringimageuser).into(imageuser);
        Picasso.get().load(stringimageDish).into(imageDish);
        nameuser.setText(stringemailuser);
        making.setText(stringmaking);
        namedish.setText(stringnamedish);

        //comment
        editcmt=findViewById(R.id.editcmt);
        send=findViewById(R.id.uploadcmt);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadComment();
            }
        });
        imageviewcm=findViewById(R.id.usercmt);
        Picasso.get().load(stringimageuser).into(imageviewcm);
        scrollView=findViewById(R.id.scroll);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerviewcmt);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        databaseReferenceup=FirebaseDatabase.getInstance().getReference();
        firebaseDatabase= FirebaseDatabase.getInstance();
//        databaseReference=firebaseDatabase.getReference();
//        databaseReference.child("Comments").push().setValue(new Comment("https://firebasestorage.googleapis.com/v0/b/dcmm-bc67e.appspot.com/o/user%2Fimage_0cvKf7X6mfVWZDjWPfFquSLgxUC3?alt=media&token=be2ca594-b195-47a9-b518-35effbeb1f5a","cmt@gmail.com","ngon vl"));
//        listViewcmt.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                scrollView.requestDisallowInterceptTouchEvent(true);
//                int action = event.getActionMasked();
//                switch (action) {
//                    case MotionEvent.ACTION_UP:
////                        scrollView.requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//                return false;
//            }
//        });
//        expandListViewHeight(listViewcmt);
        loadComment();
    }
    private void loadComment(){
        listcmt = new ArrayList<>();
        listcmt.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference("Comments");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listcmt.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Comment comment = postSnapshot.getValue(Comment.class);
                    if(comment.getIddish().equals(stringnamedish))
                    listcmt.add(comment);
                }
                 commentAdapter = new CommentAdapter2(RecipeActivity.this,listcmt);
                recyclerView.setAdapter(commentAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RecipeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void uploadComment(){
        databaseReferenceup.child("Comments").push().setValue(new Comment(stringnamedish,stringimageuser,stringemailuser,editcmt.getText().toString()));
        editcmt.setText("");
    }

}
