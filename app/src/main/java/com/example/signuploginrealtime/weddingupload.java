
package com.example.signuploginrealtime;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class weddingupload extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText titleEditText;
    private EditText copies;
    private EditText Service_Description;
    private ImageView wrapperImageView;
    private Button uploadButton;

    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference wrappersReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weddingupload);

        // Initialize Firebase storage and database references
        storageReference = FirebaseStorage.getInstance().getReference().child("wedding");
        wrappersReference = FirebaseDatabase.getInstance().getReference("wedding");

        titleEditText = findViewById(R.id.Organization);
        Service_Description = findViewById(R.id.Service_Description);
        copies = findViewById(R.id.priceEditText);

        wrapperImageView = findViewById(R.id.wrapperImageView);
        uploadButton = findViewById(R.id.uploadButton);

        wrapperImageView.setOnClickListener(v -> openFileChooser());
        uploadButton.setOnClickListener(v -> uploadWrapper());


    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadWrapper() {
        // Get title and description
        String title = titleEditText.getText().toString();
        String author = Service_Description.getText().toString();
        String copiess = copies.getText().toString();

        if (imageUri != null) {
            StorageReference fileReference = storageReference.child("books").child(title + System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Create a new News object with title, description, and imageUrl
                            wedding news = new wedding(title, author,copiess, uri.toString());

                            // Add the news to the database under the title
                            DatabaseReference newWrapperRef = wrappersReference.child(title);
                            newWrapperRef.setValue(news);

                            Toast.makeText(weddingupload.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(weddingupload.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            wrapperImageView.setImageURI(imageUri);
        }
    }
}