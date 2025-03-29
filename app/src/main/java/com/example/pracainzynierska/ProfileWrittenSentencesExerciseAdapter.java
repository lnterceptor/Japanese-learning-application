package com.example.pracainzynierska;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;

public class ProfileWrittenSentencesExerciseAdapter extends ProfileWrittenExerciseAdapter{

    public ProfileWrittenSentencesExerciseAdapter(@NonNull Context context, List<RecognitionComplexObject> objectList) {
        super(context, objectList);
    }
}
