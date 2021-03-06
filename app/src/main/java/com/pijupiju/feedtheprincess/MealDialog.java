package com.pijupiju.feedtheprincess;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MealDialog extends AppCompatDialogFragment {
    private final static String TAG = MealDialog.class.getSimpleName();
    private MealDialogListener listener;
    Button btnLeft;
    Button btnRight;
    Button btnBottle;
    EditText etTime;
    EditText etMl;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String methodName = Objects.requireNonNull(new Object() {
        }.getClass().getEnclosingMethod()).getName();
        Log.d(TAG, "-> " + methodName);

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.MyDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_meal, null);

        btnLeft = view.findViewById(R.id.btnLeft);
        btnRight = view.findViewById(R.id.btnRight);
        btnBottle = view.findViewById(R.id.btnBottle);
        etTime = view.findViewById(R.id.etTime);
        etMl = view.findViewById(R.id.etMl);

        String mealType = "";
        String mealDetail = "";
        String id = "";
        String nextMeal = "";
        final Boolean updated;
        if (getArguments() != null) {
            nextMeal = getArguments().getString("nextMeal");
            mealType = getArguments().getString("etType");
            mealDetail = getArguments().getString("etDetail");
            id = getArguments().getString("id");
        }
        Log.d(TAG, "nextMeal: " + nextMeal + ", mealType: " + mealType + ", mealDetail: " + mealDetail + ", id: " + id);
        if (!Objects.equals(mealDetail, "")) {
            updated = true;
            assert mealType != null;
            switch (mealType) {
                case "LEVA_SIKA":
                    btnLeft.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    break;
                case "DESNA_SIKA":
                    btnRight.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    break;
                case "DOHRANICA":
                    btnBottle.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    break;
            }
            if (Objects.requireNonNull(mealDetail).length() > 5) {
                etTime.setText(mealDetail.substring(0,5));
                etMl.setText(mealDetail.substring(7,9));
            } else {
                etTime.setText(mealDetail);
            }
        } else {
            updated = false;
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            DateFormat dateFormatForDisplay = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String currentTime = dateFormatForDisplay.format(date);

            etTime.setText(currentTime);

            DateFormat dateFormatForId = new SimpleDateFormat("yyyy-MM-dd-()-s", Locale.getDefault());
            id = dateFormatForId.format(date);

            if (Objects.requireNonNull(nextMeal).equals("LEVA_SIKA")) {
                btnLeft.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            } else {
                btnRight.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }

        final String finalId = id;
        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mealTime = etTime.getText().toString();
                        String actualId = Objects.requireNonNull(finalId).replace("()", mealTime.replace(":", "-"));

                        MainActivity.MealType mealType = null;
                        Integer selectColor = getResources().getColor(R.color.colorPrimaryDark);

                        if (selectColor.equals(((ColorDrawable) btnLeft.getBackground()).getColor())) {
                            mealType = MainActivity.MealType.LEVA_SIKA;
                        } else if (selectColor.equals(((ColorDrawable) btnRight.getBackground()).getColor())) {
                            mealType = MainActivity.MealType.DESNA_SIKA;
                        } else if (selectColor.equals(((ColorDrawable) btnBottle.getBackground()).getColor())) {
                            mealType = MainActivity.MealType.DOHRANICA;
                        }

                        Integer mealMl;
                        String mealMlInput = etMl.getText().toString().trim();
                        if (mealMlInput.equals("")) {
                            mealMl = 0;
                        } else {
                            mealMl = Integer.parseInt(mealMlInput);
                        }

                        Pattern pattern = Pattern.compile("(?:[01]\\d|2[0123]):(?:[012345]\\d)");
                        Matcher matcher = pattern.matcher(mealTime);
                        if (matcher.matches()) {
                            listener.setMeal(mealTime, mealType, mealMl, actualId, updated);
                        } else {
                            Toast.makeText(getContext(), String.format(getString(R.string.msg_incorrect_time), mealTime), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleButton(btnLeft);
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleButton(btnRight);
            }
        });
        btnBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleButton(btnBottle);
            }
        });

        return builder.create();
    }

    private void toggleButton(Button button) {
        String methodName = Objects.requireNonNull(new Object() {
        }.getClass().getEnclosingMethod()).getName();
        Log.d(TAG, "-> " + methodName);

        button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        if (!btnLeft.equals(button)) {
            btnLeft.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        if (!btnRight.equals(button)) {
            btnRight.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        if (!btnBottle.equals(button)) {
            btnBottle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        if (button.equals(btnBottle)) {
            etMl.setEnabled(true);
        } else {
            etMl.setEnabled(false);
        }
    }

    @Override
    public void onAttach(Context context) {
        String methodName = Objects.requireNonNull(new Object() {
        }.getClass().getEnclosingMethod()).getName();
        Log.d(TAG, "-> " + methodName);

        super.onAttach(context);
        listener = (MealDialogListener) context;
    }

    public interface MealDialogListener {
        void setMeal(String mealTime, MainActivity.MealType mealType, Integer mealMl, String id, Boolean updated);
    }

}
