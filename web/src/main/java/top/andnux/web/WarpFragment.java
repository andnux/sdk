package top.andnux.web;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class WarpFragment extends Fragment {

    private WrapListener mListener;

    private WarpFragment(WrapListener listener) {
        mListener = listener;
    }

    static WarpFragment getInstance(WrapListener listener){
        return new WarpFragment(listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mListener != null){
            mListener.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
