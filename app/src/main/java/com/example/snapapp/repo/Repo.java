package com.example.snapapp.repo;

import android.graphics.Bitmap;

import com.example.snapapp.TaskListener;
import com.example.snapapp.Updatable;
import com.example.snapapp.model.Snap;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repo {

    private static Repo repo = new Repo();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    public List<Snap> snaps = new ArrayList<>();
    private final String COLLECTION_PATH = "snaps";
    private Updatable activity;

    public static Repo repo() {
        return repo; }

    public void setup(Updatable a, List<Snap> list) {
        activity = a;
        snaps = list;
        startListener();

    }

    public void setup2(List<Snap> list) {
        snaps = list;
        startListener();

    }


    public void startListener(){
        db.collection(COLLECTION_PATH).addSnapshotListener((values, error) ->{
            snaps.clear();
            for(DocumentSnapshot snap: values.getDocuments()){
                snaps.add(new Snap(snap.getId()));

            }
            // have a reference to TakePicture, and call a update()
            activity.update(null);
        });
    }

    public void uploadBitmap(Bitmap bitmap, String imageText){
        System.out.println("123");
        DocumentReference doc = db.collection(COLLECTION_PATH).document();
        Map<String, String> map = new HashMap<>();
        doc.set(map);
        String id = doc.getId();

        StorageReference ref = storage.getReference(id);
        ByteArrayOutputStream baoas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baoas);
        ref.putBytes(baoas.toByteArray()).addOnCompleteListener(snap -> {
            System.out.println("upload complete" + snap);
        }).addOnFailureListener(exception -> {
            System.out.println("upload failed" + exception);
        });
    }



    public void downloadBitmap(String id, TaskListener taskListener){ // Skal bruges til at dl billede til listen på forside onClick
        StorageReference ref = storage.getReference(id);
        int max = 1920 * 1080; // you are free to set the limit here
        ref.getBytes(max).addOnSuccessListener(bytes -> {
            taskListener.receive(bytes);
            System.out.println("Download OK");
        }).addOnFailureListener(ex -> {
            System.out.println("error in download " + ex);
        });
    }

    //Metode til at slette når vi lukker aktiviteten
    public void deleteImage(Snap image){
        DocumentReference documentReference = db.collection(COLLECTION_PATH).document(image.getId());
        documentReference.delete();
        StorageReference storageReference = storage.getReference(image.getId());
        storageReference.delete();
    }


}