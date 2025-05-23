package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.urlpreview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;

public class TelegramPreview extends RelativeLayout {

    private View view;
    Context context;
    private PreviewMetaData meta;

    LinearLayout linearLayout;
    ImageView imageView;
    TextView textViewTitle;
    TextView textViewDesp;
    TextView textViewUrl;
    TextView textViewOriginalUrl;

    private String main_url;

    private boolean isDefaultClick = true;

    private LinkListener linkListener;


    public TelegramPreview(Context context) {
        super(context);
        this.context = context;
    }

    public TelegramPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public TelegramPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TelegramPreview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public void initView() {

        if(findLinearLayoutChild() != null) {
            this.view = findLinearLayoutChild();
        } else  {
            this.view = this;
            inflate(context, R.layout.telegram_link_layout,this);
        }

        linearLayout = (LinearLayout) findViewById(R.id.telegram_preview_link_card);
        imageView = (ImageView) findViewById(R.id.telegram_preview_link_image);
        textViewTitle = (TextView) findViewById(R.id.telegram_preview_link_title);
        textViewDesp = (TextView) findViewById(R.id.telegram_preview_link_desc);
        textViewUrl = (TextView) findViewById(R.id.telegram_preview_link_url);

        textViewOriginalUrl = (TextView) findViewById(R.id.telegram_preview_link_host_url);

        textViewOriginalUrl.setText(main_url);
        removeUnderlines((Spannable)textViewOriginalUrl.getText());

        try {
            if(meta.getImageurl().equals("") || meta.getImageurl().isEmpty()) {
                imageView.setVisibility(GONE);
            } else {
                imageView.setVisibility(VISIBLE);
                Glide.with(this).load(meta.getImageurl()).into(imageView);
            }
        }catch (Exception ex){

        }
        if(meta.getTitle().isEmpty() || meta.getTitle().equals("")) {
            textViewTitle.setVisibility(GONE);
        } else {
            textViewTitle.setVisibility(VISIBLE);
            textViewTitle.setText(meta.getTitle());
        }
        if(meta.getUrl().isEmpty() || meta.getUrl().equals("")) {
            textViewUrl.setVisibility(GONE);
        } else {
            textViewUrl.setVisibility(VISIBLE);
            textViewUrl.setText(meta.getUrl());
        }
        if(meta.getDescription().isEmpty() || meta.getDescription().equals("")) {
            textViewDesp.setVisibility(GONE);
        } else {
            textViewDesp.setVisibility(VISIBLE);
            textViewDesp.setText(meta.getDescription());
        }


        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDefaultClick) {
                    onLinkClicked();
                } else {
                    if(linkListener != null) {
                        linkListener.onClicked(view, meta);
                    } else {
                        onLinkClicked();
                    }
                }
            }
        });

    }

    private void onLinkClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(main_url));
        context.startActivity(intent);
    }

    protected LinearLayout findLinearLayoutChild() {
        if (getChildCount() > 0 && getChildAt(0) instanceof LinearLayout) {
            return (LinearLayout) getChildAt(0);
        }
        return null;
    }

    public void setLinkFromMeta(PreviewMetaData previewMetaData) {
        meta = previewMetaData;
        initView();
    }

    public PreviewMetaData getMetaData() {
        return meta;
    }


    public void setDefaultClickListener(boolean isDefault) {
        isDefaultClick = isDefault;
    }

    public void setClickListener(LinkListener linkListener1) {
        linkListener = linkListener1;
    }

    public void loadUrl(String url, final ViewListener viewListener) {
        main_url = url;
        LinkPreview linkPreview = new LinkPreview(new ResponseListener() {
            @Override
            public void onData(PreviewMetaData metaData) {
                meta = metaData;

                if(meta.getTitle().isEmpty() || meta.getTitle().equals("")) {
                    viewListener.onPreviewSuccess(true);
                }

                initView();
            }

            @Override
            public void onError(Exception e) {
                viewListener.onFailedToLoad(e);
            }
        });
        linkPreview.getPreview(url);
    }

    private static void removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);

        for(URLSpan span:spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanHandler(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
    }

}
