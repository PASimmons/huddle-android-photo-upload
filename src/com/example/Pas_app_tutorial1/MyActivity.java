package com.example.Pas_app_tutorial1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyActivity extends Activity {

    private static final Logger logger = Logger.getLogger("MyActivity");
    private final static int ACTION_CODE_NONE = -1;

    private int nextActionCode = 0;
    private int currentActionCode = ACTION_CODE_NONE;

    // UI items bound to Android resources in onCreate()
    private ImageView huddleLogo;
    private Button takePhoto;

    // Initially empty, we'll use this to display a photo we've taken
    private ImageView currentPhoto = null;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        huddleLogo = (ImageView)findViewById( R.id.imageView);

        takePhoto = (Button)findViewById( R.id.button);
        takePhoto.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent( nextActionCode++ );
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( RESULT_OK != resultCode )
        {
            logger.log( Level.WARNING, String.format("Bad result from photo activity, found result % for requestCode % ",resultCode, requestCode ));
        }
        else if( requestCode == currentActionCode)
        {
            final Bundle extras = data.getExtras();
            final Bitmap imageBitMap = (Bitmap)extras.get("data");
            this.currentPhoto.setImageBitmap( imageBitMap );

        }
        currentActionCode = ACTION_CODE_NONE;

    }

    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        currentActionCode = actionCode;
        startActivityForResult(takePictureIntent, actionCode);
    }

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
