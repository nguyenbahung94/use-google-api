package Model;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Adam on 11/24/2016.
 */

public class GetListBvKhoa {
    //    private OkHttpClient okHttpClient;
//    private Request request;
    private DerectionListener mListener;
    public  String khoa="";
    String Url= "http://192.168.43.108:88/api/bv_khoa";
    String Url2 = "http://192.168.43.108:88/api/BenhViens";
    ArrayList<String> ListKhoa = new ArrayList<String>();
    ArrayList<BenhViens> ListBV = new ArrayList<BenhViens>();
    ArrayList<BenhViens> BV = new ArrayList<BenhViens>();

    public GetListBvKhoa(DerectionListener listener ,String khoa) {
        this.khoa=khoa;
        mListener = listener;
    }

    public void CreateUrltam() {
        Log.e("KHOa Da Chon ",khoa);
        new DowloadJson().execute(Url);
        new DowloadJsonBV().execute(Url2);

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

    private class DowloadJson extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder content = new StringBuilder();
            try {
                // create a url object
                URL url = new URL(params[0]);

                // create a urlconnection object
                URLConnection urlConnection = url.openConnection();

                // wrap the urlconnection in a bufferedreader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;

                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line + "\n");
                }

                return content.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String datajson) {
            try {
                parseJson(datajson);
            } catch (Exception e) {
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
                String mabv = object.getString("mabv");
                String makhoa = object.getString("makhoa");
                if (makhoa.equals(khoa)) {
                    ListKhoa.add(mabv);
                    Log.i("khoa thoa dieu kien",mabv);
                }

            }



        }


    }
    private class DowloadJsonBV extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder content = new StringBuilder();
            try {
                // create a url object
                URL url = new URL(params[0]);

                // create a urlconnection object
                URLConnection urlConnection = url.openConnection();

                // wrap the urlconnection in a bufferedreader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;

                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line + "\n");
                }
                return content.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String datajson) {
            try {
                parseJsonBV(datajson);
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(datajson);
        }
    }
    private void parseJsonBV(String data) throws JSONException {
        if (data == null) {
            return;
        }
        JSONArray arrlistBV = new JSONArray(data);
        if (arrlistBV.length() == 0) {
            return;
        }
        for (int i = 0; i < arrlistBV.length(); i++) {
            JSONObject object = arrlistBV.getJSONObject(i);
            String mabv = object.getString("mabv");
            String tenbv = object.getString("tenbv");
//            Double vido = Double.valueOf(object.getString("vido"));
//            Double kinhdo = Double.valueOf(object.getString("kinhdo"));
//            String placeid=object.getString("placeid");
            BenhViens  bv = new BenhViens();
            bv.setMabv(mabv);
            bv.setName(tenbv);
//            bv.setKinhdo(kinhdo);
//            bv.setVido(vido);
//            bv.setPlaceid(placeid);

            ListBV.add(bv);
        }
        SoSanh();
        mListener.onSuccessGetListBvKhoa(BV);

    }
    private void SoSanh(){
        for (String str:ListKhoa) {
           for (int i=0;i<ListBV.size();i++){
               BenhViens bvs=ListBV.get(i);
               if (str.equals(bvs.getMabv())){
                   BV.add(bvs);
                   Log.i("Benh Vien Thoa Dieu Kien",bvs.toString());
               }
           }
        }
    }

}
