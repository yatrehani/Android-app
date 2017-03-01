package ds.cmu.edu.currencyconverter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


/*
 * This class is the CurrencyConverter class which acts as the controller.
 * It extends AppCompatActivity and provides capabilities by providing UI interfacing methods to find conversion rate of USD to INR using cloud webservice on Heroku cloud which interacts with API.
 * Author - Yatin Rehani
 * Date - 11-11-16
 *
 */
public class CurrencyConverter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * The click listener will need a reference to this object, so that upon successfully finding currency rates from Fixer, it
         * can callback to this object with the result.  The "this" of the OnClick will be the OnClickListener, not
         * this CurrencyConverter.
         *
         */
        final CurrencyConverter currencyConverter = this;

        /*
         * Find the "submit" button, and add a listener to it
         */
        Button submitButton = (Button)findViewById(R.id.submit);


        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                String inputAmount = ((EditText)findViewById(R.id.searchTerm)).getText().toString();
                GetCurrency gc = new GetCurrency();
                gc.search(inputAmount, currencyConverter); // Done asynchronously in another thread.  It calls ip.currencyReady() in this thread when complete.
            }
        });
    }

    /*
     * This is called by the GetCurrency object when the currency is ready.  This allows for passing back the value of currency for updating the TextView
     */
    public void currencyReady(String currencyUSD) {
      /*  TextView textView3 = (TextView)findViewById(R.id.textView3);
        TextView searchView = (EditText)findViewById(R.id.searchTerm);
        TextView status = (TextView)findViewById(R.id.status);
        if (currencyUSD != null) {
            textView3.setText(currencyUSD);
            status.setText("Here is the currency converted from USD " + searchView.getText().toString() + " to INR");
        } else {
	        status.setText("Sorry, I could not convert currency" + searchView.getText().toString());
        }
        searchView.setText(""); */

        TextView textView3 = (TextView)findViewById(R.id.textView3);
        TextView searchView = (EditText)findViewById(R.id.searchTerm);
        TextView status = (TextView)findViewById(R.id.status);
        if (currencyUSD != null && currencyUSD.contains("Success")) {
            textView3.setText(currencyUSD.split("/")[0]);
            status.setText("Here is the currency converted from USD " + searchView.getText().toString() + " to INR");
        } else if(currencyUSD !=null) {
                textView3.setText("");
                status.setText("You entered Invalid currency value " + searchView.getText().toString()+" "+currencyUSD.split("/")[1]);
            }
            else {
            textView3.setText("");
            status.setText("You entered Invalid currency value " + searchView.getText().toString()+" Please Enter numerical value");
        }
        searchView.setText("");

}
}