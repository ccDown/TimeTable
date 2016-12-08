package kuan.com.timetable.activity;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import kuan.com.timetable.PanelActivity;
import kuan.com.timetable.R;
import uk.co.senab.photoview.PhotoView;

import static kuan.com.timetable.PanelActivity.getViewBitmap;


/**
 * Created by kys-34 on 2016/12/6 0006.
 */

public class DialogActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_touchitem);
        PhotoView photoView=(PhotoView)findViewById(R.id.photoview);
        photoView.setImageDrawable(new BitmapDrawable(getViewBitmap(PanelActivity.main_gridview)));

        TextView back=(TextView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
