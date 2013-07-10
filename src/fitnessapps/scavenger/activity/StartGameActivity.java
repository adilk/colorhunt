package fitnessapps.scavenger.activity;

import fitnessapps.scavenger.data.ColorEnum;
import fitnessapps.scavenger.data.GlobalState;
import fitnessapps.scavenger.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartGameActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    }
    
    public void startGame() {
    	Intent levelOne = new Intent(this, LevelActivity.class);
    	GlobalState.randColorList = ColorEnum.randomizeColorOrder();
    	startActivity(levelOne);
    }
    
    public void setEasy(View view) {
    	GlobalState.taskDurationMili = 31000;
    	startGame();
    }
    
    public void setMedium(View view) {
    	GlobalState.taskDurationMili = 21000;
    	startGame();
    }
    
    public void setHard(View view) {
    	GlobalState.taskDurationMili = 11000;
    	startGame();
    }
    
    @Override
	 public void onBackPressed() {
		 moveTaskToBack(true);
	 }
}