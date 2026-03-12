package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements BiographyAdapter.OnBiographyClickListener {

    private List<Biography> biographyList = new ArrayList<>();
    private List<Biography> filteredList = new ArrayList<>();
    private BiographyAdapter adapter;
    private RecyclerView rvBiographies;
    private TextInputEditText etSearch;
    private ExtendedFloatingActionButton fabAdd;

    private ActivityResultLauncher<String> imagePickerLauncher;
    private String selectedImageUri = null;
    private ImageView ivDialogPhotoPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvBiographies = findViewById(R.id.rvBiographies);
        etSearch = findViewById(R.id.etSearch);
        fabAdd = findViewById(R.id.fabAdd);

        setupImagePicker();
        loadInitialData();

        filteredList.addAll(biographyList);
        adapter = new BiographyAdapter(filteredList, this);
        rvBiographies.setLayoutManager(new LinearLayoutManager(this));
        rvBiographies.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        fabAdd.setOnClickListener(v -> showBiographyDialog(null));
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri.toString();
                        if (ivDialogPhotoPreview != null) {
                            ivDialogPhotoPreview.setImageURI(uri);
                        }
                    }
                }
        );
    }

    private void loadInitialData() {
        biographyList.add(new Biography(UUID.randomUUID().toString(),
                "Miguel Hidalgo", "Guanajuato", "Grito de Dolores",
                "Inició la Guerra de Independencia de México.",
                R.drawable.miguel_hidalgo));
        biographyList.add(new Biography(UUID.randomUUID().toString(),
                "Benito Juárez", "Oaxaca", "Leyes de Reforma",
                "Presidente de México, impulsor de las Leyes de Reforma.",
                R.drawable.benito_juarez));
        biographyList.add(new Biography(UUID.randomUUID().toString(),
                "Josefa Ortiz", "Morelia", "Conspiración de Querétaro",
                "Heroína de la Independencia.",
                R.drawable.josefa_ortiz));
        biographyList.add(new Biography(UUID.randomUUID().toString(),
                "Emiliano Zapata", "Morelos", "Plan de Ayala",
                "Líder agrario de la Revolución Mexicana.",
                R.drawable.emiliano_zapata));
        biographyList.add(new Biography(UUID.randomUUID().toString(),
                "Frida Kahlo", "Coyoacán", "Pintura Surrealista",
                "Pintora icónica mexicana.",
                R.drawable.frida_kahlo));
    }

    private void filter(String query) {
        String lowerCaseQuery = query.toLowerCase().trim();
        filteredList.clear();
        for (Biography bio : biographyList) {
            if (bio.getName().toLowerCase().contains(lowerCaseQuery) ||
                    bio.getHistoricalEvent().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(bio);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void showBiographyDialog(Biography biography) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_biography, null);

        TextInputEditText etName = dialogView.findViewById(R.id.etName);
        TextInputEditText etBirthPlace = dialogView.findViewById(R.id.etBirthPlace);
        TextInputEditText etHistoricalEvent = dialogView.findViewById(R.id.etHistoricalEvent);
        TextInputEditText etLifeData = dialogView.findViewById(R.id.etLifeData);
        ivDialogPhotoPreview = dialogView.findViewById(R.id.ivDialogPhoto);
        FloatingActionButton btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);

        selectedImageUri = null;

        if (biography != null) {
            etName.setText(biography.getName());
            etBirthPlace.setText(biography.getBirthPlace());
            etHistoricalEvent.setText(biography.getHistoricalEvent());
            etLifeData.setText(biography.getLifeData());
            if (biography.getImageUri() != null) {
                selectedImageUri = biography.getImageUri();
                ivDialogPhotoPreview.setImageURI(Uri.parse(selectedImageUri));
            } else {
                ivDialogPhotoPreview.setImageResource(biography.getImageResId());
            }
        }

        btnSelectImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        new MaterialAlertDialogBuilder(this)
                .setTitle(biography == null ? "Nuevo Personaje" : "Editar Personaje")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String name = etName.getText().toString();
                    String birthPlace = etBirthPlace.getText().toString();
                    String historicalEvent = etHistoricalEvent.getText().toString();
                    String lifeData = etLifeData.getText().toString();

                    if (biography == null) {
                        Biography newBio = new Biography(UUID.randomUUID().toString(), name, birthPlace, historicalEvent, lifeData, R.drawable.ic_launcher_background);
                        if (selectedImageUri != null) {
                            newBio.setImageUri(selectedImageUri);
                        }
                        biographyList.add(newBio);
                    } else {
                        biography.setName(name);
                        biography.setBirthPlace(birthPlace);
                        biography.setHistoricalEvent(historicalEvent);
                        biography.setLifeData(lifeData);
                        if (selectedImageUri != null) {
                            biography.setImageUri(selectedImageUri);
                        }
                    }
                    filter(etSearch.getText().toString());
                    Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onEdit(Biography biography) {
        showBiographyDialog(biography);
    }

    @Override
    public void onDelete(Biography biography) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Eliminar")
                .setMessage("¿Eliminar a " + biography.getName() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    biographyList.remove(biography);
                    filter(etSearch.getText().toString());
                })
                .setNegativeButton("No", null)
                .show();
    }
}
