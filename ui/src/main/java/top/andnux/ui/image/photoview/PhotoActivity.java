package top.andnux.ui.image.photoview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import top.andnux.ui.R;


public class PhotoActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private TextView tvMum;
    private List<PhotoFragment> fragments;
    private FragmentAdapter adapter;
    private ArrayList<String> mUrls = new ArrayList<>();
    private int index = 0;

    public static void newInstance(Activity activity, ArrayList<String> urls, int index) {
        Intent intent = new Intent(activity, PhotoActivity.class);
        intent.putExtra("urls", urls);
        intent.putExtra("index", index);
        activity.startActivity(intent);
    }

    public static void newInstance(Activity activity, ArrayList<String> urls) {
        Intent intent = new Intent(activity, PhotoActivity.class);
        intent.putExtra("urls", urls);
        intent.putExtra("index", 0);
        activity.startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_photo);
        viewPager = findViewById(R.id.viewPager);
        tvMum = findViewById(R.id.tvMum);
        mUrls = getIntent().getStringArrayListExtra("urls");
        index = getIntent().getIntExtra("index", 0);
        fragments = new ArrayList<>();
        for (String url : mUrls) {
            fragments.add(PhotoFragment.newInstance(url));
        }
        if (mUrls.size() > 1) {
            tvMum.setVisibility(View.VISIBLE);
            tvMum.setText((index + 1) + "/" + mUrls.size());
        } else {
            tvMum.setVisibility(View.GONE);
        }
        adapter = new FragmentAdapter<PhotoFragment>(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(index, false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mUrls.size() > 0) {
            tvMum.setVisibility(View.VISIBLE);
            tvMum.setText((position + 1)  + "/" + mUrls.size());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
