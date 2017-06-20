package Model;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Adam on 11/20/2016.
 */

public class GetListKhoa {
//    private OkHttpClient okHttpClient;
//    private Request request;
    private DerectionListener mListener;
    String Url = "http://192.168.43.108:88/api/khoas";
    ArrayList<String> ListKhoa = new ArrayList<String>();

    public GetListKhoa(DerectionListener listener) {
        mListener=listener;
    }

    public void CreateUrltam() {
        new DowloadJson().execute(Url);

//        okHttpClient = new OkHttpClient();
//        request = new Request.Builder().url(Url).build();
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("OnFailure", e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                try {
//                    parseJson(response.body().string());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });

    }

    private class  DowloadJson extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder content = new StringBuilder();
            try{
                // create a url object
                URL url = new URL(params[0]);

                // create a urlconnection object
                URLConnection urlConnection = url.openConnection();

                // wrap the urlconnection in a bufferedreader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;

                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line + "\n");
                }
                return content.toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String datajson) {
            try
            {
                parseJson(datajson);
            }catch (Exception e){
                e.printStackTrace();
            }
            super.onPostExecute(datajson);
        }

        private void parseJson(String data) throws JSONException {
        if (data == null) {
            return;
        }
        JSONArray arrrListkhoa = new JSONArray(data);

        if (arrrListkhoa.length() == 0) {
            return;
        }
        for (int i = 0; i < arrrListkhoa.length(); i++) {
            JSONObject object = arrrListkhoa.getJSONObject(i);

            String tenkhoa = object.getString("tenkhoa");
            ListKhoa.add(tenkhoa);
        }


        mListener.onSuccessGetListKhoa(ListKhoa);

    }


}}
