package com.lovemsky.wwshare.app.login.fragment.post.idea.imagelist;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.lovemsky.wwshare.R;
import com.lovemsky.wwshare.app.login.fragment.post.picselect.bean.ImageInfo;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/7.
 */
public class ImgListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<ImageInfo> mDatas = new ArrayList<ImageInfo>();
    private Context mContext;
    private View mView;
    private OnFooterViewClickListener onFooterViewClickListener;

    private DisplayImageOptions mImageItemOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.message_image_default)
            .showImageForEmptyUri(R.drawable.message_image_default)
            .showImageOnFail(R.drawable.timeline_image_failure)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .considerExifParams(true)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    private static final int ITEM_TYPE_IMAGE = 1;
    private static final int ITEM_TYPE_DELETE = 2;


    public ImgListAdapter(ArrayList<ImageInfo> datas, Context context) {
        this.mDatas = datas;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_IMAGE) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.compose_idea_img_item, parent, false);
            ImgViewHolder viewHolder = new ImgViewHolder(mView);
            return viewHolder;
        } else if (viewType == ITEM_TYPE_DELETE) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.compose_idea_img_footerview, parent, false);
            FooterViewHoder footerHolder = new FooterViewHoder(mView);
            return footerHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (holder instanceof ImgViewHolder) {
            ImageLoader.getInstance().displayImage("file:///" + mDatas.get(position).getImageFile().getAbsolutePath(), ((ImgViewHolder) holder).imageView, mImageItemOptions);
            ((ImgViewHolder) holder).delteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemRemoved(position);
                    mDatas.remove(position);
                    notifyItemRangeChanged(position, mDatas.size() + 1);
                }
            });
        } else if (holder instanceof FooterViewHoder) {
            ((FooterViewHoder) holder).addpic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFooterViewClickListener.OnFooterViewClick();
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size() + 1;
        } else {
            return 0;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (mDatas.size() > 0 && position == mDatas.size()) {
            return ITEM_TYPE_DELETE;
        } else {
            return ITEM_TYPE_IMAGE;
        }

    }

    public void setData(ArrayList<ImageInfo> data) {
        this.mDatas = data;
    }

    protected class ImgViewHolder extends ViewHolder {

        public ImageView imageView;
        public ImageView delteImg;

        public ImgViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageitem);
            delteImg = (ImageView) view.findViewById(R.id.deleteitem);

        }
    }

    protected class FooterViewHoder extends ViewHolder {

        public ImageView addpic;

        public FooterViewHoder(View view) {
            super(view);
            addpic = (ImageView) view.findViewById(R.id.addpic);
        }
    }

    public interface OnFooterViewClickListener {
        public void OnFooterViewClick();
    }

    public void setOnFooterViewClickListener(OnFooterViewClickListener onFooterViewClickListener) {
        this.onFooterViewClickListener = onFooterViewClickListener;
    }

}
