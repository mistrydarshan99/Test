package com.darshan.Test.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.darshan.Test.R;
import com.darshan.Test.model.UserModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by vcsdev0075 on 3/18/14.
 */
public class UserListAdapter extends BaseAdapter {

    private Activity activity;
    private List<UserModel> info;
    private boolean showCheckBox;
    private final LayoutInflater inflater;
    private DisplayImageOptions options;
    public static ImageLoader imageLoader;
    String query = "http://test.fortalented.com/manage/";

    public UserListAdapter(Activity activity, List<UserModel> info) {
        this.activity = activity;
        this.info = info;
        inflater = LayoutInflater.from(activity);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));

        options = new DisplayImageOptions.Builder().cacheOnDisc(true)
                .showStubImage(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.images).build();
    }

    public class ViewHolder {
        private TextView emailName, emailSub, emailTime;
        private ImageView userPhoto;
    }

    public List<UserModel> getInfo() {
        return info;
    }

    public void setInfo(List<UserModel> info) {
        this.info = info;
    }

    public boolean isShowCheckBox() {
        return showCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
    }

    @Override
    public int getCount() {
        return null != info ? info.size() : 0;
    }

    @Override
    public UserModel getItem(int position) {
        return info.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final int temp = position;
        final UserModel objDrList = getItem(position);
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_user_row, parent, false);
            viewHolder.emailName = (TextView) convertView.findViewById(R.id.txtemailname);
            viewHolder.emailSub = (TextView) convertView.findViewById(R.id.txtemailsub);
            viewHolder.emailTime = (TextView) convertView.findViewById(R.id.txttime);
            viewHolder.userPhoto = (ImageView) convertView.findViewById(R.id.userImage);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.emailName.setText(objDrList.getEmailName());
        viewHolder.emailSub.setText(objDrList.getEmailSub());
        viewHolder.emailTime.setText(objDrList.getEmailTime());


        imageLoader.displayImage(
                "file://"+objDrList.getAttachmentName(),
                viewHolder.userPhoto, options);

        return convertView;
    }
}
