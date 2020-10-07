package org.maktab.photogallery.controller.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.maktab.photogallery.R;
import org.maktab.photogallery.model.GalleryItem;
import org.maktab.photogallery.repository.PhotoRepository;
import org.maktab.photogallery.services.ThumbnailDownloader;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoGalleryFragment extends Fragment {

    public static final int SPAN_COUNT = 3;
    public static final String TAG = "PGF";
    private RecyclerView mRecyclerView;
    private PhotoAdapter mAdapter;

    private PhotoRepository mRepository;

    private ThumbnailDownloader mThumbnailDownloader;

    public PhotoGalleryFragment() {
        // Required empty public constructor
    }

    public static PhotoGalleryFragment newInstance() {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = PhotoRepository.getInstance();

        mThumbnailDownloader = new ThumbnailDownloader();
        //start the thread inside message loop
        mThumbnailDownloader.start();
        //start the looper inside message loop
        mThumbnailDownloader.getLooper();

        FetchItemTasks fetchItemTasks = new FetchItemTasks();
        fetchItemTasks.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mThumbnailDownloader.quit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        findViews(view);
        initViews();
//        setupAdapter(null);

        return view;
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.photo_gallery_recyclerview);
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
    }

    private void setupAdapter(List<GalleryItem> items) {
        mAdapter = new PhotoAdapter(items);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTitle;
        private GalleryItem mItem;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewTitle = (TextView) itemView;
        }

        public void bindPhoto(GalleryItem item) {
            mItem = item;
//            mTextViewTitle.setText(mItem.getCaption());

            //TODO: put a message in ThumbnailDownloader queue
            mThumbnailDownloader.queueThumbnailMessage(item.getUrl());
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<GalleryItem> mItems;

        public List<GalleryItem> getItems() {
            return mItems;
        }

        public void setItems(List<GalleryItem> items) {
            mItems = items;
        }

        public PhotoAdapter(List<GalleryItem> items) {
            mItems = items;
        }

        @NonNull
        @Override
        public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(getContext());
            return new PhotoHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
            holder.bindPhoto(mItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    private class FetchItemTasks extends AsyncTask<String, Void, List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(String... params) {
            List<GalleryItem> items = mRepository.getItems();

            return items;
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            setupAdapter(items);
        }
    }
}