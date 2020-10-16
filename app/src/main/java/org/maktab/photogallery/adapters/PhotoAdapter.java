package org.maktab.photogallery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.maktab.photogallery.R;
import org.maktab.photogallery.databinding.ListItemPhotoBinding;
import org.maktab.photogallery.data.model.GalleryItem;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private Context mContext;
    private List<GalleryItem> mItems;

    public List<GalleryItem> getItems() {
        return mItems;
    }

    public void setItems(List<GalleryItem> items) {
        mItems = items;
    }

    public PhotoAdapter(Context context, List<GalleryItem> items) {
        mItems = items;
        mContext = context.getApplicationContext();
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemPhotoBinding listItemPhotoBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.list_item_photo,
                parent,
                false);
        return new PhotoHolder(listItemPhotoBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        holder.bindPhoto(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class PhotoHolder extends RecyclerView.ViewHolder {

        private ListItemPhotoBinding mListItemPhotoBinding;
        private GalleryItem mItem;

        public PhotoHolder(ListItemPhotoBinding listItemPhotoBinding) {
            super(listItemPhotoBinding.getRoot());
            mListItemPhotoBinding = listItemPhotoBinding;
        }

        public void bindPhoto(GalleryItem item) {
            mItem = item;

            Picasso.get()
                    .load(mItem.getUrl())
                    .placeholder(R.mipmap.ic_android)
                    .into(mListItemPhotoBinding.imageviewPhoto);
        }
    }
}
