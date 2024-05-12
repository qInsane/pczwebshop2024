package com.example.pczwebshop2024;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShopMainPageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_main_page);
        FirebaseApp.initializeApp(ShopMainPageActivity.this);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler);
        FloatingActionButton add = findViewById(R.id.addProduct);
        FloatingActionButton refresh= findViewById(R.id.refresh);
        FloatingActionButton signOut = findViewById(R.id.signOut); // Initialize sign out button

        Animation slideInRightAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

        ViewTreeObserver vto = add.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                add.startAnimation(slideInRightAnim);
                refresh.startAnimation(slideInRightAnim);
                signOut.startAnimation(slideInRightAnim); // Apply animation to sign out button
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    add.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    add.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopMainPageActivity.this, AddProductActivity.class));
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshData();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); // Sign out from Firebase
                Intent intent = new Intent(ShopMainPageActivity.this, MainActivity.class); // Create new intent
                startActivity(intent); // Start MainActivity
                finish(); // Close the current activity
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        db.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Product> arrayList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        Product product = doc.toObject(Product.class);
                        product.setId(doc.getId());
                        arrayList.add(product);
                    }
                    ProductAdapter adapter = new ProductAdapter(ShopMainPageActivity.this, arrayList);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(Product product) {
                            App.product=product;
                            startActivity(new Intent(ShopMainPageActivity.this, EditProductActivity.class));
                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShopMainPageActivity.this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
            }

        });
    }
}




