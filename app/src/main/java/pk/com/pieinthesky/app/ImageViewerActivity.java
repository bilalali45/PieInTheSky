package pk.com.pieinthesky.app;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.provider.DocumentFile;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.github.chrisbanes.photoview.PhotoView;
import pk.com.pieinthesky.app.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;

public class ImageViewerActivity extends AppCompatActivity {

    ArrayList<String> pictureList = null;
    int pictureIndex=0;
    int totalImages = 0;
    int currentIndex=0;

    private ViewPager viewPager;
    private TextView tvImageIndex;

    LayoutInflater layoutInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            window.setStatusBarColor(Color.BLACK);
        }

        pictureList = getIntent().getStringArrayListExtra("PictureList");
        pictureIndex = getIntent().getIntExtra("PictureIndex", 0);


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tvImageIndex = (TextView) findViewById(R.id.tvImageIndex);

        loadImages();
    }

    private void loadImages() {

        totalImages = pictureList.size();
        if (totalImages> 0) {
            ImageAdapter imageAdapter = new ImageAdapter(this, pictureList);
            viewPager.setAdapter(imageAdapter);

            viewPager.setCurrentItem(pictureIndex);
            tvImageIndex.setText((pictureIndex + 1) + "/" + totalImages);
            currentIndex = pictureIndex;

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    tvImageIndex.setText((position + 1) + "/" + totalImages);
                    currentIndex = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            finish();
        }
    }

    public void actionBar_onClick(View v) {
/*
        if (documentFileList.get(currentPosition).getIsUploaded()) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(ImageViewerActivity.this);
            builder.setTitle("");
            builder.setMessage("Are you sure to delete?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean isDeleted = delete(currentPosition);
                    if (isDeleted) {
                        if(position>0) {
                            position = position - 1;
                        }
                        loadImages();
                        Toast.makeText(ImageViewerActivity.this, "File is deleted!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("NO", null);
            builder.show();
        }*/


    }

    /*    private boolean delete(int position) {

            String fileName = documentFileList.get(position).getFileName();
            long fileId = documentFileList.get(currentPosition).getFileId();

            String path = Environment.getExternalStorageDirectory().getPath() + "/scanDoc/" + fileName;

            File tempFile = new File(path);
            boolean deleted = tempFile.delete();
            if (deleted) {
                ApplicationDAL appDAL = new ApplicationDAL(this);
                appDAL.deleteDocumentFile(fileId);
            }
            return deleted;
          }
    */
    class ImageAdapter extends PagerAdapter {
        Context context;
        ArrayList<String>  pictureList;

        ImageAdapter(Context context, ArrayList<String>  pictureList) {
            this.context = context;
            this.pictureList = pictureList;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return pictureList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.image_viewer_adapter_item, container, false);

            PhotoView imageView = (PhotoView) itemView.findViewById(R.id.imageView);

            Glide.with(ImageViewerActivity.this)
                    .load(pictureList.get(position))
                    .override( Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .placeholder(R.drawable.image_slider_loading)
                    .into(imageView);

            LinearLayout cancel_button = itemView.findViewById(R.id.cancel_button);
            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            /*
            String fileName = documentFileList.get(position).getFileName();

            String path = Environment.getExternalStorageDirectory().getPath() + "/scanDoc/" + fileName;
            Bitmap b = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(b);
*/

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }

    }


}
