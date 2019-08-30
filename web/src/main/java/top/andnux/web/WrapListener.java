package top.andnux.web;

import android.content.Intent;

import androidx.annotation.NonNull;

public interface WrapListener {

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
