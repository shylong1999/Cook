package com.cnpm.dcmm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class Fragment_add extends Fragment {
    String imageuser="";
    StorageReference storageReference;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferencedish;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    FirebaseUser user;
    public static final int Pick_IMAGE_REQUEST=1;
    Uri image_uri;
    Button upload;
    Button chooseimage;
    EditText namedish;
    TextView textView;
    ProgressDialog pd;
    ImageView imageDish;
    TextView make;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_add,container,false);
        imageDish= (ImageView) v.findViewById(R.id.ImageDish);
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Waiting...");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("User");
        databaseReferencedish=firebaseDatabase.getReference();

        storageReference=FirebaseStorage.getInstance().getReference();

        Query query=databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String email= ds.child("email").getValue().toString();
                    String image=  ds.child("image").getValue().toString();
                    imageuser=image;


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        make=v.findViewById(R.id.cachlam);
        namedish=v.findViewById(R.id.namedish);
        upload=v.findViewById(R.id.upload);


        imageDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opentfile();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadfile();
            }
        });
        return v;
    }

    private void opentfile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Pick_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Pick_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            image_uri = data.getData();
            Picasso.get().load(image_uri).fit().centerCrop().into(imageDish);


        }
    }
    private void uploadfile() {
        pd.show();
        String filePathAndName= "dish/"+"image_"+System.currentTimeMillis();
        StorageReference storageReference2nd=storageReference.child(filePathAndName);
        storageReference2nd.putFile(image_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloadUri= uriTask.getResult();
                        if(uriTask.isSuccessful()){
                            pd.dismiss();
                           databaseReferencedish.child("Dish").push().setValue(new Dish(downloadUri.toString(),namedish.getText().toString(),user.getEmail(),imageuser,make.getText().toString(),TimeDate()))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(),"image update",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(getActivity(),"Error Update",Toast.LENGTH_LONG).show();
                                        }
                                    });

                        }
                        else {
                            pd.dismiss();
                            Toast.makeText(getActivity(),"SOME ERROR",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    public String TimeDate() {
        String time=null;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("hh:mm a");
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd:MM:yyyy");

        time= simpleDateFormat.format(calendar.getTime())+" At " +dateFormat.format(calendar.getTime());


        return time;
    }
}
