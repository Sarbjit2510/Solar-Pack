package com.pragyaware.anu.solarpack.mActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mUtil.Constants;
import com.squareup.picasso.Picasso;

public class PreviewActivity extends AppCompatActivity {

    private ImageView previewImgVw;
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        previewImgVw = (ImageView) findViewById(R.id.previewImgVw);

        url = Constants.IMG_URL + getIntent().getStringExtra("data") + "|200|200|80";

        Picasso.with(this).load(url).into(previewImgVw);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
    }
}
