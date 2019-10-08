package top.andnux.utils.fragment;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentProxy extends Fragment {

    private static final String TAG = "FragmentProxy";

    private ProxyListener mListener;

    public interface ProxyListener {

        void onRequestPermissionsResult(int requestCode,
                                        @NonNull String[] permissions,
                                        @NonNull int[] grantResults);

        void onActivityResult(int requestCode,
                              int resultCode,
                              @Nullable Intent data);
    }

    public static FragmentProxy with(FragmentActivity activity) {
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        FragmentProxy fragment = (FragmentProxy) supportFragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new FragmentProxy();
            fragmentTransaction.add(android.R.id.content, fragment, TAG);
        } else {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commitNow();
        return fragment;
    }


    public FragmentProxy setListener(ProxyListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mListener != null) {
            mListener.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mListener != null) {
            mListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
