package com.ui.letcook.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.ui.letcook.Model.Dish;
import com.ui.letcook.R;


import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    EditText mota,nguyenlieu;
    TextView textView;
    ProgressDialog pd;
    ImageView imageDish;
    TextView make;
    int sobai;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_add,container,false);
        imageDish= (ImageView) v.findViewById(R.id.ImageDish);
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Vui lòng chờ trong giây lát...");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("User");
        databaseReferencedish=firebaseDatabase.getReference("Dish");
        //
        mota=v.findViewById(R.id.motachung);
        nguyenlieu=v.findViewById(R.id.nguyenlieu);




        storageReference=FirebaseStorage.getInstance().getReference();

        Query query=databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String email= ds.child("email").getValue().toString();
                    String image=  ds.child("image").getValue().toString();
                    sobai= Integer.parseInt(ds.child("sobaidang").getValue().toString());
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
                String ten=namedish.getText().toString();
                String motangan=mota.getText().toString();
                String nguyenlieu1=nguyenlieu.getText().toString();
                String cachlam=make.getText().toString();
                if(ten.isEmpty()){
                    namedish.setError("Mời nhập tên món ăn!");
                    namedish.requestFocus();
                    return;
                }
                if(motangan.isEmpty()){
                    mota.setError("Mời nhập mô tả ngắn về món ăn!");
                    mota.requestFocus();
                    return;
                }
                if(nguyenlieu1.isEmpty()){
                    nguyenlieu.setError("Mời nhập nguyên liệu món ăn!!");
                    nguyenlieu.requestFocus();
                    return;
                }
                if(cachlam.isEmpty()){
                    mota.setError("Mời nhập nguyên liệu món ăn!!");
                    make.requestFocus();
                    return;
                }
                else uploadfile();
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
        String image="https://firebasestorage.googleapis.com/v0/b/dcmm-bc67e.appspot.com/o/dish%2Fimage_1573225436453?alt=media&token=a60d47f5-e184-43cf-af00-b0b04ef5d54b";
        pd.show();
        String filePathAndName= "dish/"+"image_"+System.currentTimeMillis();
        StorageReference storageReference2nd=storageReference.child(filePathAndName);
        if(image_uri!=null) {
            storageReference2nd.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadUri = uriTask.getResult();
                            if (uriTask.isSuccessful()) {
                                pd.dismiss();
                                String id = databaseReferencedish.push().getKey();
                                Dish dish = new Dish(downloadUri.toString(), namedish.getText().toString(), mota.getText().toString(), nguyenlieu.getText().toString(), user.getEmail(), imageuser, make.getText().toString(), TimeDate(), 0, id, 0);
                                databaseReferencedish.child(id).setValue(dish)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(), "Đăng thành công!", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(getActivity(), "Lỗi mạng!", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                namedish.setText("");
                                mota.setText("");
                                make.setText("");
                                nguyenlieu.setText("");
                                Picasso.get().load(R.drawable.camera).placeholder(R.drawable.camera).fit().centerCrop().into(imageDish);


                            } else {
                                pd.dismiss();
                                Toast.makeText(getActivity(), "SOME ERROR", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
            databaseReference.child(user.getUid()).child("sobaidang").setValue(sobai+1);

        }

        if(image_uri==null){
                            pd.dismiss();
                            Toast.makeText(getActivity(),"Vui lòng chọn ảnh món ăn!", Toast.LENGTH_LONG).show();}

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
