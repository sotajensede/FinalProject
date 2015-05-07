package edu.mecc.jesse.finalproject;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private DBAdapter myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openDB();

        Button btnAdd = (Button) findViewById(R.id.btnOne);
        Button btnClear = (Button) findViewById(R.id.btnTwo);
        Button btnDisplay = (Button) findViewById(R.id.btnThree);

        final EditText editText = (EditText) findViewById(R.id.editText);

        //ADD BUTTON
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String wholeStr = editText.getText().toString();
                String[] splitStr = wholeStr.split("\\s+");

                if(splitStr.length < 3)
                    onClick_AddRecord(v);
                else if(splitStr.length == 4)
                    myDB.updateRow(Long.parseLong(splitStr[0],10), splitStr[1],
                            Integer.parseInt(splitStr[2]), splitStr[3]);
                else
                    myDB.insertRow(splitStr[0], Integer.parseInt(splitStr[1]), splitStr[2]);
            }
        });
        // CLEAR BUTTON
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.deleteAll();
            }
        });
        //DISPLAY BUTTON
        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRecordSet(myDB.getAllRows());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDB = new DBAdapter(this);
        myDB.open();
    }

    private void closeDB() {
        myDB.close();
    }

    private void displayText(String message) {
        TextView textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText(message);
    }

    public void onClick_AddRecord(View v) {
        displayText("Clicked add record!");

        long newId = myDB.insertRow("Jenny", 5559, "Green");

        Cursor cursor = myDB.getRow(newId);
        displayRecordSet(cursor);
    }

    private void displayRecordSet (Cursor cursor) {
        String message = "";
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String name = cursor.getString(DBAdapter.COL_NAME);
                int studentNumber = cursor.getInt(DBAdapter.COL_STUDENTNUM);
                String favColor = cursor.getString(DBAdapter.COL_FAVCOLOR);

                message += "id=" + id + ", name = " + name + ", # = " + studentNumber
                        + ", color = " + favColor + "\n";
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        displayText(message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
