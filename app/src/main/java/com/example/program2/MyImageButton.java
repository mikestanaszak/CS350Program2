package com.example.program2;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageButton;
//Created a custom ImageButton class so that I can store an ID within the ImageButton.
//Useful for the ImageButtons on "OnePlayer" and "TwoPlayer" when selecting colors
public class MyImageButton extends AppCompatImageButton {
    private int ImageID;
    public MyImageButton(Context context, int imgID) {
        super(context);
        ImageID = imgID;
    }

    public int getImageID(){
        return ImageID;
    }
}
