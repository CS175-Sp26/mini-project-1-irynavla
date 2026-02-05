package edu.sjsu.android.project1irynavlasiuk;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.sjsu.android.project1irynavlasiuk.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Set initial text (assuming you added interest_rate_label to strings.xml)
        binding.Interest.setText(getString(R.string.interest_rate_label, 0.0));

        binding.seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // progress is 0 to 200
                // dividing by 10.0 converts it to 0.0 to 20.0
                double rate = progress / 10.0;

                // Update the label with 1 decimal place (e.g., 5.5%)
                binding.Interest.setText(String.format("Interest rate: %.1f%%", rate));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        binding.button4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    // 1. Get the input from the EditText
                    String input = binding.editTextNumberDecimal.getText().toString();

                    String decimalPattern = "^\\d+(\\.\\d{1,2})?$";

                    if (input.isEmpty()) {
                        binding.Press.setText("Please enter the principle. \nThen Press CALCULATE for monthly payments.");
                        return;
                    } else if (!input.matches(decimalPattern)) {
                        binding.Press.setText("Please enter a valid number. 2 decimal digits max. \nThen Press CALCULATE for monthly payments.");
                    } else {

                        double P = Double.parseDouble(input);

                        // 2. Get interest rate from SeekBar
                        double annualRate = binding.seekBar4.getProgress();
                        double J = ((annualRate / 100.0) / 12.0); // Monthly rate

                        // 3. Get term from RadioGroup
                        int years = 15; // Default
                        if (binding.radioButton9.isChecked()) years = 20;
                        else if (binding.radioButton11.isChecked()) years = 30;
                        int N = years * 12; // Total months

                        // 4. Mortgage Formula: M = P [ r(1+r)^n ] / [ (1+r)^n – 1 ]
                        double M;
                        if (annualRate == 0) {
                            M = (P / N);
                        } else {
                            M = P * (J * Math.pow(1+J, N)) / (Math.pow(1 + J, N) -1);
                        }

                        // 5. Add Insurance if CheckBox is checked (0.1% of P)
                        if (binding.checkBox.isChecked()) {
                            M += (P * 0.001);
                        }

                        // 6. UPDATE THE SCREEN
                        // This is the "change" you see at the bottom
                        binding.Press.setText(String.format("Monthly payment: $%.2f", M));

                    }
                }
                catch(NumberFormatException e){
                    binding.Press.setText("Please enter a valid number. 2 decimal digits max. \nThen Press CALCULATE for monthly payments.");
                    }
            }
        });
        // 1. Set the click listener for the Uninstall button
        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 2. Create the Intent with the DELETE action
                // Use Uri.parse to tell Android which specific package to remove
                Intent delete = new Intent(Intent.ACTION_DELETE,
                        Uri.parse("package:" + getPackageName()));

                // 3. Start the system activity
                startActivity(delete);
            }
        });
        binding.editTextNumberDecimal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // If the length is greater than 0, set it to green and LEAVE it that way
                if (s.length() > 0) {
                    binding.editTextNumberDecimal.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.green))
                    );
                } else {
                    // Only turns back to gray/default if the user deletes all text
                    binding.editTextNumberDecimal.setBackgroundTintList(
                            ColorStateList.valueOf(Color.GRAY)
                    );
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
//
    }
}