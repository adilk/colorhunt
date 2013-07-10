package fitnessapps.scavenger.activity;

import java.util.ArrayList;
import java.util.List;
import fitnessapps.scavenger.data.ColorEnum;
import fitnessapps.scavenger.data.GlobalState;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FinalScoreActivity extends Activity {

	// Views
	private ListView colorListView;
	private ListView scoreListView;
	// Color
	private List<ColorEnum> colorList;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> listItems;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalscore);
        setBackground();
        colorList = GlobalState.randColorList;
        colorListView = (ListView) findViewById(R.id.colorList);
        scoreListView = (ListView) findViewById(R.id.scoreList);
        listItems = new ArrayList<String>();
        
        //populateScores(colorList);
    }
    
    private void populateScores(List<ColorEnum> list) {
    	/*TextView colorText;
    	TextView scoreText;
    	ColorEnum currColor;
    	for (int i=0; i < list.size(); i++) {
    		colorText = (TextView) colorListView.getChildAt(i);
    		scoreText = (TextView) scoreListView.getChildAt(i);
    		currColor = list.get(i);
    		colorText.setText(currColor.getStringVersion());
    		scoreText.setText(Integer.toString(currColor.getColorScore()));
    	} */
    	
    	adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        colorListView.setAdapter(adapter);
    }
    
    private void setBackground() {
		RelativeLayout relLay = (RelativeLayout) findViewById(R.id.scoreLayout);
		Drawable bg = relLay.getBackground();
		bg.setAlpha(100);
	}
    
    @Override
    public void onBackPressed() {
    	Intent restart = new Intent(this, StartGameActivity.class);
		startActivity(restart);
	}
}
