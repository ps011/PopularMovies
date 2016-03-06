package tk.prasheelsoni.popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    protected ProgressDialog pDialog;
   ArrayList<JSONObject> movie = new ArrayList<>();
    ArrayList<JSONObject> movie_popularity = new ArrayList<>();
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p//w500/";
    GridView gridview;
    String[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gridview = (GridView) findViewById(R.id.grid_view);
        LoadData l = new LoadData(this);
        l.execute();
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
try {
    Intent z = new Intent(MainActivity.this, Details.class)
            .putExtra("movie", movie.get(i).get("title").toString())
            .putExtra("vote_average", movie.get(i).get("vote_average").toString())
            .putExtra("overview", movie.get(i).get("overview").toString())
            .putExtra("poster_path", IMAGE_BASE_URL+(movie.get(i).get("backdrop_path").toString()))
            .putExtra("release_date", (movie.get(i).get("release_date").toString()));
    Log.i("Test", movie.get(i).toString());
    startActivity(z);
}catch (Exception e)
{

}
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mi=new MenuInflater(this);
        mi.inflate(R.menu.menu,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {

            case R.id.sort_rating :

                Collections.sort(movie, new Comparator<JSONObject>() {

                    public int compare(JSONObject a, JSONObject b) {
                        Double valA = null;
                        Double valB = null;

                        try {
                            valA = Double.parseDouble(a.get("vote_average").toString());
                            valB = Double.parseDouble(b.get("vote_average").toString());
                        } catch (JSONException e) {
                            //do something
                        }

                        return valB.compareTo(valA);
                    }
                });
                new LoadData(this).execute();

                break;
            case R.id.sort_vote :
                Collections.sort(movie, new Comparator<JSONObject>() {

                    public int compare(JSONObject a, JSONObject b) {
                        Double valA = null;
                        Double valB = null;

                        try {
                            valA = Double.parseDouble(a.get("popularity").toString());
                            valB = Double.parseDouble(b.get("popularity").toString());
                        } catch (JSONException e) {
                            //do something
                        }

                        return valB.compareTo(valA);
                    }
                });



                new LoadData(this).execute();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadData extends AsyncTask<ArrayList<String>, Void, String[]> {
        Context m;


        URL url;
        LoadData(Context c) {
            m = c;
        }

        @Override
        protected String[] doInBackground(ArrayList<String>... strings) {
            HttpURLConnection link = null;
            if (movie.isEmpty()) {
            try {

                    url = new URL("https://api.themoviedb.org/3/discover/movie?api_key=API_KEY");
                    link = (HttpURLConnection) url.openConnection();
                    link.connect();
                    String data = ConvertToString(link);
                    JSONObject j = new JSONObject(data);
                    JSONArray results = j.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movie_array = results.getJSONObject(i);
                        movie.add(movie_array);
                    }

                }catch(IOException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
            images = new String[movie.size()];
            for(int i=0;i<movie.size();i++){
                try {
                    images[i] = IMAGE_BASE_URL+movie.get(i).get("poster_path").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return images;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setIndeterminate(false);
             pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            pDialog.dismiss();
            gridview.setAdapter(new ImageAdapter(m));

        }

        public String ConvertToString(HttpURLConnection link) {
            String result = null;
            StringBuffer sb = new StringBuffer();
            InputStream is = null;

            try {
                is = new BufferedInputStream(link.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String inputLine = "";
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                result = sb.toString();
                Log.v("Result", result);
            } catch (Exception e) {
                Log.i("Prasheel", "Error reading InputStream");
                result = null;
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        Log.i("Prasheel", "Error closing InputStream");
                    }
                }
            }

            return result;
        }
    }


    class ImageAdapter extends BaseAdapter {
        Context mContext;


        ImageAdapter(Context c) {
            mContext = c;

        }


        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int i) {
            return images[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView imageView;
            if (view == null) {
                
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams((int) mContext.getResources().getDimension(R.dimen.width), (int) mContext.getResources().getDimension(R.dimen.height)));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) view;
            }
            Picasso.with(mContext).load(images[i]).into(imageView);


            return imageView;
        }


    }


}
