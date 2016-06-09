package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;




/**
 * Created by Administrator on 2016/1/22.
 */
public class NetWork {

    public String login(Map<String,String> map,String requestURL){
        String result="";
        try{
            URL url=new URL(requestURL);
            HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();

            urlConn.setDoInput(true);//璁剧疆杈撳叆娴侀噰鐢ㄥ瓧鑺傛祦
            urlConn.setDoOutput(true);//璁剧疆杈撳嚭娴侀噰鐢ㄥ瓧鑺傛祦
            urlConn.setRequestMethod("POST");
            urlConn.setUseCaches(false);//璁剧疆缂撳瓨
            // 璁剧疆璇锋眰鐨勮秴鏃舵椂闂�
            urlConn.setReadTimeout(5000);
            urlConn.setConnectTimeout(5000);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//璁剧疆methed鍙傛暟
            urlConn.setRequestProperty("Charset", "utf-8");

            urlConn.connect();//閾炬帴鏃㈠線鏈嶅姟鍣ㄥ彂閫佹秷鎭�
            PrintWriter pw=new PrintWriter(urlConn.getOutputStream());
            StringBuffer buffer=new StringBuffer();
            for(Map.Entry<String, String> entry : map.entrySet()){
                buffer.append(URLEncoder.encode(entry.getKey(), "utf-8") + "=" + URLEncoder.encode(entry.getValue(), "utf-8") + "&");
//                        responseStr+=(URLEncoder.encode(entry.getKey(),"UTF_8")+"="+URLEncoder.encode(entry.getValue(),"UTF-8"));
            }
//                    responseStr="phone="+URLEncoder.encode("13280928213","UTF_8")+"&password="+URLEncoder.encode("123","UTF_8");
            pw.print(buffer);
            pw.flush();
            pw.close();

            InputStream inputStream=urlConn.getInputStream();
            int httpCode=urlConn.getResponseCode();
            if(httpCode==200){

                byte[] bufferBytes = new byte[1];
//                StringBuffer sb = new StringBuffer();
                BufferedReader bf=new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf-8")));
                //瀹氫箟String绫诲瀷鐢ㄤ簬鍌ㄥ瓨鍗曡鏁版嵁
                String line=null;
                //鍒涘缓StringBuffer瀵硅薄鐢ㄤ簬瀛樺偍鎵�鏈夋暟鎹�
                StringBuffer sb=new StringBuffer();
                while((line=bf.readLine())!=null){
                    sb.append(line);
                }
                result=sb.toString();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public String doGet(Map<String,String> map,String requestUrl){
        String result="";
        try{
            StringBuffer buffer=new StringBuffer("?");
            if(map!=null)
            for(Map.Entry<String,String> mapItem:map.entrySet() ){
                buffer.append(URLEncoder.encode(mapItem.getKey(), "utf-8")+"="+URLEncoder.encode(mapItem.getValue(), "utf-8")+"&");
            }
            URL url=new URL(requestUrl+buffer.toString());
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//璁剧疆methed鍙傛暟
            conn.setRequestProperty("Charset", "utf-8");

            conn.connect();//閾炬帴鏃㈠線鏈嶅姟鍣ㄥ彂閫佹秷鎭�
            InputStream inputStream=conn.getInputStream();
            int httpCode=conn.getResponseCode();
            if(httpCode==200){

                byte[] bufferBytes = new byte[1];
//                StringBuffer sb = new StringBuffer();
                BufferedReader bf=new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf-8")));
                //瀹氫箟String绫诲瀷鐢ㄤ簬鍌ㄥ瓨鍗曡鏁版嵁
                String line=null;
                //鍒涘缓StringBuffer瀵硅薄鐢ㄤ簬瀛樺偍鎵�鏈夋暟鎹�
                StringBuffer sb=new StringBuffer();
                while((line=bf.readLine())!=null){
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String doPost(Map<String,String> map,String requestURL){
        String result="";
        try{
            URL url=new URL(requestURL);
            HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();

            urlConn.setDoInput(true);//璁剧疆杈撳叆娴侀噰鐢ㄥ瓧鑺傛祦
            urlConn.setDoOutput(true);//璁剧疆杈撳嚭娴侀噰鐢ㄥ瓧鑺傛祦
            urlConn.setRequestMethod("POST");
            urlConn.setUseCaches(false);//璁剧疆缂撳瓨
            // 璁剧疆璇锋眰鐨勮秴鏃舵椂闂�
            urlConn.setReadTimeout(5000);
            urlConn.setConnectTimeout(5000);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//璁剧疆methed鍙傛暟
            urlConn.setRequestProperty("Charset", "utf-8");

            urlConn.connect();//閾炬帴鏃㈠線鏈嶅姟鍣ㄥ彂閫佹秷鎭�
            PrintWriter pw=new PrintWriter(urlConn.getOutputStream());
            StringBuffer buffer=new StringBuffer();
            if(map!=null)
            for(Map.Entry<String, String> entry : map.entrySet()){
                buffer.append(URLEncoder.encode(entry.getKey(), "utf-8") + "=" + URLEncoder.encode(entry.getValue(), "utf-8") + "&");
//                        responseStr+=(URLEncoder.encode(entry.getKey(),"UTF_8")+"="+URLEncoder.encode(entry.getValue(),"UTF-8"));
            }
//                    responseStr="phone="+URLEncoder.encode("13280928213","UTF_8")+"&password="+URLEncoder.encode("123","UTF_8");
            pw.print(buffer);
            pw.flush();
            pw.close();

            InputStream inputStream=urlConn.getInputStream();
            int httpCode=urlConn.getResponseCode();
            if(httpCode==200){
                byte[] bufferBytes = new byte[1];
//                StringBuffer sb = new StringBuffer();
                BufferedReader bf=new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf-8")));
                //瀹氫箟String绫诲瀷鐢ㄤ簬鍌ㄥ瓨鍗曡鏁版嵁
                String line=null;
                //鍒涘缓StringBuffer瀵硅薄鐢ㄤ簬瀛樺偍鎵�鏈夋暟鎹�
                StringBuffer sb=new StringBuffer();
                while((line=bf.readLine())!=null){
                    sb.append(line);
                }
                result=sb.toString();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String submiteImage(String url,String photoUrl){


        return  "";
    }


}
