package com.example.sanraboss.myfirstandroidapp.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sanraboss.myfirstandroidapp.Class.DrawingView;
import com.example.sanraboss.myfirstandroidapp.R;

public class PreviewActivity extends AppCompatActivity {

    DrawingView dv ;
    private String pathToPhoto;
    private ImageView image;
    private Button clearButton;

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


//        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        mPaint.setDither(true);
//        mPaint.setColor(Color.GREEN);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeJoin(Paint.Join.ROUND);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        setContentView(dv);

    }

    private void initComponents() {
        image = (ImageView) findViewById(R.id.picture);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(pathToPhoto,options);
        image.setImageBitmap(bitmap);

        clearButton = (Button) findViewById(R.id.clear_drawing);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dv.clear();
            }
        });
    }
//    public class DrawingView extends ViewGroup {
//
//        public int width;
//        public int height;
//        private Bitmap mBitmap;
//        private Canvas mCanvas;
//        private Path mPath;
//        private Paint mBitmapPaint;
//        Context context;
//        private Paint circlePaint;
//        private Path circlePath;
//        ImageView imageView;
//
//        public DrawingView(Context c) {
//            super(c);
//            context=c;
//            mPath = new Path();
//            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
//            circlePaint = new Paint();
//            circlePath = new Path();
//            circlePaint.setAntiAlias(true);
//            circlePaint.setColor(Color.BLUE);
//            circlePaint.setStyle(Paint.Style.STROKE);
//            circlePaint.setStrokeJoin(Paint.Join.MITER);
//            circlePaint.setStrokeWidth(4f);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            Bitmap bitmap = BitmapFactory.decodeFile(pathToPhoto,options);
//            imageView = new ImageView(getContext());
//            imageView.setBackgroundResource(R.drawable.test);
////            imageView = new ImageView(getContext());
////            imageView.setBackgroundResource(R.drawable.test);
////            setBackgroundColor(Color.TRANSPARENT);
////            setContentView(R.layout.drawing_layout);
//        }
//
//        @Override
//        protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
//
//        }
//
//        @Override
//        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//            super.onSizeChanged(w, h, oldw, oldh);
//
//            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//            mCanvas = new Canvas(mBitmap);
//        }
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//            super.onDraw(canvas);
//
//            canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
//            canvas.drawPath( mPath,  mPaint);
//            canvas.drawPath( circlePath,  circlePaint);
//        }
//
//        private float mX, mY;
//        private static final float TOUCH_TOLERANCE = 4;
//
//        private void touch_start(float x, float y) {
//            mPath.reset();
//            mPath.moveTo(x, y);
//            mX = x;
//            mY = y;
//        }
//
//        private void touch_move(float x, float y) {
//            float dx = Math.abs(x - mX);
//            float dy = Math.abs(y - mY);
//            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
//                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
//                mX = x;
//                mY = y;
//
//                circlePath.reset();
//                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
//            }
//        }
//
//        private void touch_up() {
//            mPath.lineTo(mX, mY);
//            circlePath.reset();
//            // commit the path to our offscreen
//            mCanvas.drawPath(mPath,  mPaint);
//            // kill this so we don't double draw
//            mPath.reset();
//        }
//
//        @Override
//        public boolean onTouchEvent(MotionEvent event) {
//            float x = event.getX();
//            float y = event.getY();
//
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    touch_start(x, y);
//                    invalidate();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    touch_move(x, y);
//                    invalidate();
//                    break;
//                case MotionEvent.ACTION_UP:
//                    touch_up();
//                    invalidate();
//                    break;
//            }
//            return true;
//        }
//    }
}
