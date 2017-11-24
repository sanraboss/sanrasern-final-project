package com.example.sanraboss.myfirstandroidapp.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sanraboss.myfirstandroidapp.Class.DrawingView;
import com.example.sanraboss.myfirstandroidapp.R;

public class PreviewActivity extends AppCompatActivity implements View.OnTouchListener {

    private final String TAG = "PREVIEW_ACTIVITY";
    DrawingView dv ;
    private String pathToPhoto;
    private ImageView image;
    private Button clearButton;
    private Button peakButton;
    private Button centerMidButton;
    private Button patioleButton;
    private Button zoomButton;

    //Zoom Component
    private boolean zoomMode = false;
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                pathToPhoto = null;
            } else {
                pathToPhoto = extras.getString("pictureName");
            }
        } else {
            pathToPhoto = (String) savedInstanceState.getSerializable("pictureName");
        }

//        image.setBackgroundResource(R.drawable.test);
        setContentView(R.layout.activity_preview);
        dv = (DrawingView) findViewById(R.id.fbv);
        initComponents();

    }

    private void initComponents() {
        image = (ImageView) findViewById(R.id.picture);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(pathToPhoto,options);
        image.setImageBitmap(bitmap);
        image.setOnTouchListener(this);

        clearButton = (Button) findViewById(R.id.clear_drawing);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dv.clear();
            }
        });
        peakButton = (Button) findViewById(R.id.draw_peak);
        zoomButton = (Button) findViewById(R.id.zoom_mode);
        zoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomMode = !zoomMode;
                if(zoomMode) {
                    dv.setVisibility(View.INVISIBLE);
                    zoomButton.setText("Draw Mode");
                }
                else {
                    dv.setVisibility(View.VISIBLE);
                    zoomButton.setText("Zoom Mode");
                }
            }
        });
        peakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dv.setPoint("peak");
            }
        });
        centerMidButton = (Button) findViewById(R.id.draw_center_mid);
        centerMidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dv.setPoint("center-mid");
            }
        });
        patioleButton = (Button) findViewById(R.id.draw_patiole);
        patioleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dv.setPoint("patiole");
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;
        Log.d(TAG, event.toString());
        dumpEvent(event);
        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG"); // write to LogCat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG)
                {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                }
                else if (mode == ZOOM)
                {
                    // pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f)
                    {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist; // setting the scaling of the
                        // matrix...if scale > 1 means
                        // zoom in...if scale < 1 means
                        // zoom out
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }
    /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event)
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event)
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP)
        {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++)
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }

}
