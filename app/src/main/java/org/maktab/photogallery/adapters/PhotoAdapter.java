package org.maktab.photogallery.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.maktab.photogallery.R;
import org.maktab.photogallery.databinding.ListItemPhotoBinding;
import org.maktab.photogallery.viewmodel.PhotoGalleryViewModel;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private PhotoGalleryViewModel mViewModel;

    public PhotoAdapter(PhotoGalleryViewModel viewModel) {
        mViewModel = viewModel;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemPhotoBinding listItemPhotoBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mViewModel.getApplication()),
                R.layout.list_item_photo,
                parent,
                false);
        return new PhotoHolder(listItemPhotoBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        holder.bindPhoto(position);
    }

    @Override
    public int getItemCount() {
        return mViewModel.getItems().size();
    }

    class PhotoHolder extends RecyclerView.ViewHolder {

        private ListItemPhotoBinding mListItemPhotoBinding;

        public PhotoHolder(ListItemPhotoBinding listItemPhotoBinding) {
            super(listItemPhotoBinding.getRoot());
            mListItemPhotoBinding = listItemPhotoBinding;
            mListItemPhotoBinding.setViewModel(mViewModel);
        }

        public void bindPhoto(int position) {
            mListItemPhotoBinding.setPosition(position);

            Picasso.get()
                    .load(mViewModel.getItems().get(position).getUrl())
                    .placeholder(R.mipmap.ic_android)
                    .into(mListItemPhotoBinding.imageviewPhoto);
        }
    }
}
