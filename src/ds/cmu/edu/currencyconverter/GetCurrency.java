package ds.cmu.edu.currencyconverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import android.os.AsyncTask;



/*
 * This class provides capabilities to find conversion rate of USD to INR using cloud webservice on Heroku cloud which interacts with API.  The method "search" is the entry to the class.
 * Network operations cannot be done from the UI thread, therefore this class makes use of an AsyncTask inner class that will do the network
 * operations in a separate worker thread.  However, any UI updates should be done in the UI thread so avoid any synchronization problems.
 * onPostExecution runs in the UI thread, and it calls the ImageView currencyReady method to do the update.
 * Author - Yatin Rehani
 * Date - 11-11-16
 *
 */
public class GetCurrency {
	CurrencyConverter ip = null;
	
	/*
	 * search is the public GetCurrency method.  Its arguments are the search term, and the CurrencyConverter object that called it.  This provides a callback
	 * path such that the currencyReady method in that object is called when the currency is available from the search.
	 */
	public void search(String searchTerm, CurrencyConverter ip) {
		this.ip = ip;
		new AsyncCloudSearch().execute(searchTerm);
	}

	/*
	 * AsyncTask provides a simple way to use a thread separate from the UI thread in which to do network operations.
	 * doInBackground is run in the helper thread.
	 * onPostExecute is run in the UI thread, allowing for safe UI updates.
	 */
    private class AsyncCloudSearch extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return search(urls[0]);
        }

        protected void onPostExecute(String currency) {
        	ip.currencyReady(currency);
        }

        /* 
         * Search using Heroku cloud webapp for the currency conversion, and further call getResponseText
         */
        private String search(String searchTerm) {
            String currencyURL = "https://fathomless-meadow-43883.herokuapp.com/CurrencyConvertor/";
            try {
                String response = getResponseText(currencyURL+searchTerm);
                JSONObject jsonObject=(new JSONObject(response));
                String currencyInINR=jsonObject.getString("value");
                String message=jsonObject.getString("message");
                return currencyInINR+"/"+message;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
        /*
        * Connnect to Heroku cloud webapp for the currency conversion, and returns converted currency from USD to INR
        */
        private String getResponseText(String stringUrl) throws IOException
        {
            StringBuilder response  = new StringBuilder();

            URL url = new URL(stringUrl);
            HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()),8192);
                String strLine = null;
                while ((strLine = input.readLine()) != null)
                {
                    response.append(strLine);
                }
                input.close();
            }
            return response.toString();
        }

    }
}