package com.wherearethey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingFormatArgumentException;

public class AddPersonActivity extends AppCompatActivity {

    AppCompatSpinner spinner_com, spinner_height;
    ArrayAdapter<CharSequence> adapter_com, adapter_height;

    NumberPicker numberPicker;

    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;

    AppCompatImageView img;
    Button mAddButton;

    TextInputEditText mFullName, mDescription, mLastSeen, mVicinity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://where-are-they-cb43f.appspot.com/photos/");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef = database.getReferenceFromUrl("https://where-are-they-cb43f.firebaseio.com/");

        spinner_com = (AppCompatSpinner) findViewById(R.id.complexion_spinner);
        adapter_com = ArrayAdapter.createFromResource(this, R.array.complexion_array, android.R.layout.simple_spinner_item);
        adapter_com.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_com.setAdapter(adapter_com);

        spinner_height = (AppCompatSpinner) findViewById(R.id.height_spinner);
        adapter_height = ArrayAdapter.createFromResource(this, R.array.heights_array, android.R.layout.simple_spinner_item);
        adapter_height.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_height.setAdapter(adapter_height);

        numberPicker = (NumberPicker) findViewById(R.id.agePicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(120);
        numberPicker.setWrapSelectorWheel(true);

        img = (AppCompatImageView) findViewById(R.id.uploadImg);
        mAddButton = (Button) findViewById(R.id.btn_add);
        mFullName = (TextInputEditText) findViewById(R.id.txtFullName);
        mDescription = (TextInputEditText) findViewById(R.id.txtDescription);
        mLastSeen = (TextInputEditText) findViewById(R.id.txtLastSeen);
        mVicinity = (TextInputEditText) findViewById(R.id.txtVicinity);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePath != null){
                    pd.show();

                    File file = new File(filePath.getPath());
                    String name = file.getName();
                    StorageReference childRef = storageRef.child((name));

                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filePath);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(AddPersonActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(AddPersonActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(AddPersonActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
                }

                DatabaseReference personsRef = databaseRef.child("missing_persons");
                DatabaseReference newPersonRef = personsRef.push();

                newPersonRef.setValue(new MissingPerson(mFullName.getText().toString(), spinner_com.getSelectedItem().toString(), spinner_height.getSelectedItem().toString(),
                        storageRef.getDownloadUrl().toString(), numberPicker.getValue(), mDescription.getText().toString(), mLastSeen.getText().toString(), mVicinity.getText().toString()));

                //Map<String, MissingPerson> MissingPersons = new HashMap<String, MissingPerson>();
                //MissingPersons.put()
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                img.setImageBitmap(bitmap);
                img.setBackground(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
