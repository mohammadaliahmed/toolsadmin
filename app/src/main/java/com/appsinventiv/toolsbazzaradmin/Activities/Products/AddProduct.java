package com.appsinventiv.toolsbazzaradmin.Activities.Products;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Adapters.SelectedImagesAdapter;
import com.appsinventiv.toolsbazzaradmin.Interfaces.ProductObserver;
import com.appsinventiv.toolsbazzaradmin.Models.Product;
import com.appsinventiv.toolsbazzaradmin.Models.SelectedAdImages;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.CompressImage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddProduct extends AppCompatActivity implements ProductObserver {
    TextView categoryChoosen;
    StorageReference mStorageRef;
    DatabaseReference mDatabase;
    Button pick, upload;
    private static final int REQUEST_CODE_CHOOSE = 23;
    List<Uri> mSelected;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SelectedImagesAdapter adapter;
    Bundle extras;
    ArrayList<SelectedAdImages> selectedAdImages = new ArrayList<>();
    ArrayList<String> imageUrl = new ArrayList<>();

    String imagePath;
    EditText e_title, e_sku, e_subtitle, e_costPrice, e_wholesalePrice,
            e_retailPrice, e_minOrderQty, e_measurement, e_sizes, e_colors, e_description,
            e_oldRetailPrice, e_oldWholesalePrice;
    String productId;
    ProgressBar progressBar;
    Spinner spinner;
    ArrayList<VendorModel> vendorModelArrayList = new ArrayList<>();
    VendorModel vendor;
    ProductObserver observer;
    ArrayList<Integer> skuList = new ArrayList<>();
    long newSku = 10001;
    RadioGroup radioGroup;
    RadioButton selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        this.setTitle("Add Product");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        observer = AddProduct.this;

        getPermissions();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        categoryChoosen = findViewById(R.id.categoryChoosen);
        categoryChoosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddProduct.this, CategoryChooser.class);
                startActivityForResult(i, 1);
            }
        });
        pick = findViewById(R.id.pick);
        upload = findViewById(R.id.upload);
        e_title = findViewById(R.id.title);
        recyclerView = findViewById(R.id.recyclerview);
        e_subtitle = findViewById(R.id.subtitle);
        e_costPrice = findViewById(R.id.costPrice);
        e_wholesalePrice = findViewById(R.id.wholeSalePrice);
        e_retailPrice = findViewById(R.id.retailPrice);
        e_minOrderQty = findViewById(R.id.minOrder);
        e_measurement = findViewById(R.id.measurement);
        e_sku = findViewById(R.id.productSku);
        progressBar = findViewById(R.id.prgress);
        spinner = findViewById(R.id.chooseVendor);
        radioGroup = findViewById(R.id.radioGroup);
        e_description = findViewById(R.id.description);
        e_sizes = findViewById(R.id.size);
        e_colors = findViewById(R.id.color);
        e_oldWholesalePrice = findViewById(R.id.oldWholeSalePrice);
        e_oldRetailPrice = findViewById(R.id.oldRetailPrice);
        showPickedPictures();

        getSKUFromDb();
        getVendorsFromDb();


        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelected != null) {
                    mSelected.clear();
                    selectedAdImages.clear();
                    imageUrl.clear();
                }
                Matisse.from(AddProduct.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(5)
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (extras == null) {
                    CommonUtils.showToast("Choose Category");
                } else if (e_title.getText().length() == 0) {
                    e_title.setError("Enter title");
                } else if (e_subtitle.getText().length() == 0) {
                    e_subtitle.setError("Enter subtitle");
                } else if (e_costPrice.getText().length() == 0) {
                    e_costPrice.setError("Enter price");
                } else if (e_wholesalePrice.getText().length() == 0) {
                    e_wholesalePrice.setError("Enter whole sale price");
                } else if (e_retailPrice.getText().length() == 0) {
                    e_retailPrice.setError("Enter price");
                } else {
                    List<String> container = new ArrayList<>();
                    if (e_sizes.getText().length() > 0) {
                        String[] sizes = e_sizes.getText().toString().split(",");
                        container = Arrays.asList(sizes);

                    }
                    List<String> container1 = new ArrayList<>();

                    if (e_colors.getText().length() > 0) {
                        String[] colors = e_colors.getText().toString().split(",");
                        container1 = Arrays.asList(colors);

                    }


                    progressBar.setVisibility(View.VISIBLE);
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    selected = findViewById(selectedId);
                    productId = mDatabase.push().getKey();
                    mDatabase.child("Products").child(productId).setValue(new Product(
                            productId,
                            e_title.getText().toString(),
                            e_subtitle.getText().toString(),
                            "true",
                            Integer.parseInt("" + newSku),
                            "",
                            extras.getString("mainCategory"),
                            extras.getString("subCategory"),
                            System.currentTimeMillis(),
                            Float.parseFloat(e_costPrice.getText().toString()),
                            Float.parseFloat(e_wholesalePrice.getText().toString()),
                            Float.parseFloat(e_retailPrice.getText().toString()),
                            Long.parseLong(e_minOrderQty.getText().length() > 0 ? e_minOrderQty.getText().toString() : "" + 1),
                            e_measurement.getText().toString(),
                            vendor,
                            selected.getText().toString(),
                            e_description.getText().toString(),
                            container,
                            container1,
                            Float.parseFloat(e_oldWholesalePrice.getText().length() > 0 ? e_oldWholesalePrice.getText().toString() : "" + 0),
                            Float.parseFloat(e_oldRetailPrice.getText().length() > 0 ? e_oldRetailPrice.getText().toString() : "" + 0),
                            0


                    )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            int count = 0;
                            for (String img : imageUrl) {

                                putPictures(img, "" + productId, count);
                                count++;
                                observer.onUploaded(count, imageUrl.size());

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }


            }
        });


    }

    private void getSKUFromDb() {
        mDatabase.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    long abc = Integer.parseInt("" + dataSnapshot.getChildrenCount());

                    newSku = newSku + abc;

                    e_sku.setText("" + newSku);

                } else {
                    e_sku.setText("" + newSku);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showPickedPictures() {
        selectedAdImages = new ArrayList<>();
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(AddProduct.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        adapter = new SelectedImagesAdapter(AddProduct.this, selectedAdImages);
        recyclerView.setAdapter(adapter);
    }

    private void getVendorsFromDb() {
        mDatabase.child("Vendors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot != null) {
                            VendorModel model = snapshot.getValue(VendorModel.class);
                            if (model != null) {
                                vendorModelArrayList.add(model);
                                setUpSpinner();
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpSpinner() {
//        final String[] items = new String[]{"Select Vendor", "Attock","Faisalabad","Hyderabad","Islamabad","Karachi","Lahore","Mardan","Multan","Peshawar","Quetta","Sialkot"};
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < vendorModelArrayList.size(); i++) {
            items.add("" + vendorModelArrayList.get(i).getVendorName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_dropdown, items);
//        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

                vendor = vendorModelArrayList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    public void putPictures(String path, final String key, final int count) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        Uri file = Uri.fromFile(new File(path));

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        observer.putThumbnailUrl(count, "" + downloadUrl);
                        mDatabase.child("Products").child(productId).child("pictures").child("" + count).setValue("" + downloadUrl);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast("There was some error uploading pic");

                    }
                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        selectedAdImages.clear();
        if (data != null) {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                recyclerView.setVisibility(View.VISIBLE);

                mSelected = Matisse.obtainResult(data);
                for (Uri img :
                        mSelected) {
                    selectedAdImages.add(new SelectedAdImages("" + img));
                    adapter.notifyDataSetChanged();
                    CompressImage compressImage = new CompressImage(AddProduct.this);
                    imageUrl.add(compressImage.compressImage("" + img));
                }
//                CompressImage compressImage = new CompressImage(this);
//                imagePath = compressImage.compressImage("" + mSelected.get(0));
            }
            if (requestCode == 1) {
                extras = data.getExtras();
                if (extras.getString("subCategory") != null) {
                    categoryChoosen.setText("Category: " + extras.getString("subCategory"));

                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onUploaded(int count, int arraySize) {
        if (count == arraySize) {
            Intent i = new Intent(AddProduct.this, ProductUploaded.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void putThumbnailUrl(int count, String url) {
        if (count == 0) {
            mDatabase.child("Products").child(productId).child("thumbnailUrl").setValue(url);
        }
    }
}
