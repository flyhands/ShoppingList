package android.vogella.de.shoppinglist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class CardAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context c;
    List<Item> list;


    public CardAdapter(Context c, List<Item> list){
        this.c = c;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = inflater.inflate(R.layout.card_layout_main, null);
        TextView itemTv = v.findViewById(R.id.cardTitle);
        TextView priceTv = v.findViewById(R.id.price);
        ImageView imageView = v.findViewById(R.id.cardImage);

        itemTv.setText(list.get(position).getName());
        priceTv.setText(list.get(position).getPrice());
        new DownLoadImageTask(imageView, list.get(position).getImage()).execute();

        return v;
    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;
        String url;
        public DownLoadImageTask(ImageView imageView, String url){
            this.imageView = imageView;
            this.url = url;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            Bitmap logo = null;
            try{
                InputStream is = new URL(url).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
