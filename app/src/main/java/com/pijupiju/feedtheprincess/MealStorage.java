package com.pijupiju.feedtheprincess;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MealStorage {
    private final static String TAG = MealStorage.class.getSimpleName();
    private static MealStorage instance;

    public static MealStorage getInstance() {
        if (instance == null)
            instance = new MealStorage();
        return instance;
    }

    List<Meal> mealList = new ArrayList<>();

    List<Meal> getMealList(Context context) {
        String methodName = Objects.requireNonNull(new Object() {
        }.getClass().getEnclosingMethod()).getName();
        Log.d(TAG, "-> " + methodName);

        mealList = RetrieveMealList(context, "meals-active");
        return mealList;
    }

    void saveMeals(Context context) {
        String methodName = Objects.requireNonNull(new Object() {
        }.getClass().getEnclosingMethod()).getName();
        Log.d(TAG, "-> " + methodName);

        storeMeals(context, "meals-active", mealList);
    }

    public void saveHistoricalData(Context context, Meal meal) {
        String methodName = Objects.requireNonNull(new Object() {
        }.getClass().getEnclosingMethod()).getName();
        Log.d(TAG, "-> " + methodName);

        String fileName = "historical-data";
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_APPEND);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(meal);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public String getNextMeal() {
        String methodName = Objects.requireNonNull(new Object() {
        }.getClass().getEnclosingMethod()).getName();
        Log.d(TAG, "-> " + methodName);

        Log.d(TAG, String.valueOf(mealList));

        try {
            MainActivity.MealType lastMeal = mealList.get(mealList.size() - 1).getMealType();
            MainActivity.MealType oneBeforeMeal = mealList.get(mealList.size() - 2).getMealType();

            if (lastMeal.equals(MainActivity.MealType.DESNA_SIKA) && oneBeforeMeal.equals(MainActivity.MealType.DESNA_SIKA) ||
                    (lastMeal.equals(MainActivity.MealType.LEVA_SIKA) && oneBeforeMeal.equals(MainActivity.MealType.DESNA_SIKA))) {
                return "LEVA_SIKA";
            }
            if (lastMeal.equals(MainActivity.MealType.LEVA_SIKA) && oneBeforeMeal.equals(MainActivity.MealType.LEVA_SIKA) ||
                    lastMeal.equals(MainActivity.MealType.DESNA_SIKA) && oneBeforeMeal.equals(MainActivity.MealType.LEVA_SIKA)) {
                Log.d("INSPECT", "LEVA_SIKA + LEVA_SIKA = DESNA_SIKA");
                return "DESNA_SIKA";
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return "";
    }

    private List<Meal> RetrieveMealList(Context context, String fileName) {
        String methodName = Objects.requireNonNull(new Object() {
        }.getClass().getEnclosingMethod()).getName();
        Log.d(TAG, "-> " + methodName);

        List<Meal> meals = new ArrayList<>();
        FileInputStream fileInputStream;
        try {
            fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            meals = (List<Meal>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        Log.d(TAG, "mealList: " + String.valueOf(meals));
        return meals;
    }


    private void storeMeals(Context context, String fileName, List<Meal> meals) {
        String methodName = Objects.requireNonNull(new Object() {
        }.getClass().getEnclosingMethod()).getName();
        Log.d(TAG, "-> " + methodName);

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(meals);
            objectOutputStream.close();
            fileOutputStream.close();
            Log.d(TAG, "mealList [" + fileName + "]: " + String.valueOf(meals));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}
