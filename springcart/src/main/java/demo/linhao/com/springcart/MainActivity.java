package demo.linhao.com.springcart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class MainActivity extends Activity {
    private ImageView mPhoto;
    private EditText mWords;
    private IWXAPI iwxapi;
    private Button share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实例化
        iwxapi = WXAPIFactory.createWXAPI(this,"wxf4881f29e3cf9218");
        iwxapi.registerApp("wxf4881f29e3cf9218");
        mPhoto = (ImageView) findViewById(R.id.photo);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,null);
                /**
                 * EXTERNAL_CONTENT_URI 获取系统图库的信息
                 */
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,100);
            }
        });
        mWords = (EditText) findViewById(R.id.words);
        //getAssets获取AssetManager
        mWords.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/test.ttf"));

        share = (Button) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wecharShare();
//                mPhoto.setImageBitmap(generateSpringCart());
                share.setVisibility(View.VISIBLE);
            }
        });
    }

    private void wecharShare() {
        WXWebpageObject webpage = new WXWebpageObject();

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "WebPage Title";
        msg.description = "WebPage Description";
        msg.mediaObject = new WXImageObject(generateSpringCart());

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);
    }
    private Bitmap generateSpringCart()
    {
        share.setVisibility(View.INVISIBLE);
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        return  view.getDrawingCache();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100)
        {
            if (data != null)
            {
                mPhoto.setImageURI(data.getData());
            }
        }
    }
}
