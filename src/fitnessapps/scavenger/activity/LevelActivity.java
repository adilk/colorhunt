package fitnessapps.scavenger.activity;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import fitnessapps.scavenger.activity.LevelActivity;
import fitnessapps.scavenger.activity.R;
import fitnessapps.scavenger.components.MyTimer;
import fitnessapps.scavenger.data.ColorEnum;
import fitnessapps.scavenger.data.GlobalState;
import fitnessapps.scavenger.data.Histogram;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LevelActivity extends Activity {

	// Views
	private TextView timerView = null;
	private ImageView imageView = null;
	private TextView levelTaskView = null;
	private Button snapButton;
	// Camera
	private SurfaceView preview = null;
	private SurfaceHolder previewHolder = null;
	private Camera mCamera = null;
	private boolean inPreview = false;
	private boolean cameraConfigured = false;
	private static final int CENTER_WIDTH = 9;
	private static final int CENTER_HEIGHT = 9;
	private Bitmap bitmapPicture;
	private static final int REQ_WIDTH = 512;
	private static final int REQ_HEIGHT = 384;
	// Colors
	private Histogram histogram = null;
	private int colorImgDrawable;
	private ColorEnum currentColor = null;
	private static final String BROWN = "Brown";
	private static final String GREY = "Grey";
	private static final String RED = "Red";
	private static final String BLUE = "Blue";
	private static final String GREEN = "Green";
	private static final String PURPLE = "Purple";
	private static final String PINK = "Pink";
	private static final String ORANGE = "Orange";
	// Intents
	private Intent startIntent;
	private Intent currIntent;
	private Intent finalIntent;
	// Sounds
	private SoundPool pool;
	private int crowdCheer;
	// Timer
	private MyTimer countdownTimer = null;
	private static final long COUNTDOWN_INTERVAL = 1000;
	
	/********************INITIALIZE METHODS*****************************/
	private void initIntents() {
		startIntent = new Intent(this, StartGameActivity.class);
		currIntent = new Intent(this, LevelActivity.class);
		finalIntent = new Intent(this, FinalScoreActivity.class);
	}

	private void initSounds() {
		pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		crowdCheer = pool.load(this, R.raw.crowd_cheer, 1);
	}
	
	public void initGameTimer(long numberOfMiliSec) {
		countdownTimer = new MyTimer(numberOfMiliSec, COUNTDOWN_INTERVAL,
				timerView, this);
	}

	/**************** CAMERA METHODS ***********************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camerapreview);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		initSounds();
		timerView = (TextView) findViewById(R.id.timerView);
		levelTaskView = (TextView) findViewById(R.id.currentLevelTaskText);
		imageView = (ImageView) findViewById(R.id.currentColorImage);
		snapButton = (Button) findViewById(R.id.buttonSnap);
		// must be called after timerView is instantiated
		initGameTimer(GlobalState.taskDurationMili);

		initIntents();

		preview = (SurfaceView) findViewById(R.id.preview);
		previewHolder = preview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		alertTask();

	}

	@Override
	public void onResume() {
		super.onResume();
		mCamera = Camera.open();
		Parameters params = mCamera.getParameters();
		List<Camera.Size> sizes = params.getSupportedPictureSizes();

		params.setPictureSize(sizes.get(0).width, sizes.get(0).height);
		mCamera.setParameters(params);
		mCamera.startPreview();
	}

	@Override
	public void onPause() {
		if (inPreview) {
			mCamera.stopPreview();
		}

		mCamera.release();
		mCamera = null;
		inPreview = false;

		super.onPause();

	}

	private PictureCallback myPictureCallback_JPG = new PictureCallback() {

		public void onPictureTaken(byte[] arg0, Camera arg1) {
			int score = 0;
			//int[] rgb = getRGBData(arg0);
			stopTimer();
			
			bitmapPicture = getBitmap(arg0, REQ_WIDTH, REQ_HEIGHT);
			
			score = getPixelScore(bitmapPicture);
			currentColor.setColorScore(score);
			mCamera.startPreview();
			
			taskCompleted();
		}
	};
	
	/*
	private int[] getRGBData(byte[] data) {
		 // Preprocessing
        Log.i("DEBUG", "Try For Image Processing");
        Camera.Parameters mParameters = mCamera.getParameters();
        Camera.Size mSize = mParameters.getPreviewSize();
        int mWidth = mSize.width;
        int mHeight = mSize.height;
        int[] mIntArray = new int[mWidth * mHeight];

        // Decode Yuv data to integer array
        decodeYUV420SP(mIntArray, data, mWidth, mHeight);
        
        return mIntArray;
	}
	
	static public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width,
	        int height) {
		final int frameSize = width * height;
    	
    	for (int j = 0, yp = 0; j < height; j++) {
    		int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
    		for (int i = 0; i < width; i++, yp++) {
    			int y = (0xff & ((int) yuv420sp[yp])) - 16;
    			if (y < 0) y = 0;
    			if ((i & 1) == 0) {
    				v = (0xff & yuv420sp[uvp++]) - 128;
    				u = (0xff & yuv420sp[uvp++]) - 128;
    			}
    			
    			int y1192 = 1192 * y;
    			int r = (y1192 + 1634 * v);
    			int g = (y1192 - 833 * v - 400 * u);
    			int b = (y1192 + 2066 * u);
    			
    			if (r < 0) r = 0; else if (r > 262143) r = 262143;
    			if (g < 0) g = 0; else if (g > 262143) g = 262143;
    			if (b < 0) b = 0; else if (b > 262143) b = 262143;
    			
    			rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
    		}
    	}
	}

	*/
	private static Bitmap getBitmap(final byte[] array, int reqWidth, int reqHeight) {
		
		// First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeByteArray(array, 0, array.length, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeByteArray(array, 0, array.length, options);
	}
	
	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) 
		{
			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	/*
	private void taskFailed() {
		vibrate();
		Toast.makeText(
				this,
				"Sorry that wasn't " + currentColor
						+ " enough! Try another color!", Toast.LENGTH_LONG)
				.show();
		alertTask();
	} */

	private Camera.Size getBestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}

		return (result);
	}

	private void initPreview(int width, int height) {
		if (mCamera != null && previewHolder.getSurface() != null) {
			try {
				mCamera.setPreviewDisplay(previewHolder);
			} catch (Throwable t) {
				Log.e("PreviewDemo-surfaceCallback",
						"Exception in setPreviewDisplay()", t);
			}

			if (!cameraConfigured) {
				Camera.Parameters parameters = mCamera.getParameters();
				Camera.Size size = getBestPreviewSize(width, height, parameters);

				if (size != null) {
					parameters.setPreviewSize(size.width, size.height);
					mCamera.setParameters(parameters);
					cameraConfigured = true;
				}
			}
		}
	}
	
	public void onTakePicture(View view) {

		snapButton.setClickable(false);
		mCamera.takePicture(null, null, myPictureCallback_JPG);

	}

	private void startPreview() {
		if (cameraConfigured && mCamera != null) {
			mCamera.startPreview();
			inPreview = true;
		}
	}

	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		public void surfaceCreated(SurfaceHolder holder) {
			// Empty
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// Empty
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			initPreview(w, h);

			startPreview();
		}
	};
	
	/************* END CAMERA METHODS *********************************/

	/*****************PIXELS AND HISTOGRAM*******************************/
	
	public int getPixelScore(Bitmap bitMap) {
		int numPixels = 0;
		/*if (mCamera != null) {
			Camera.Size mSize = mCamera.getParameters().getPreviewSize();
	        int mWidth = mSize.width;
	        int mHeight = mSize.height;
			histogram = new Histogram(rgbData, mWidth, mHeight);
			int imageSize = getPixelCountWithoutBrownAndGrey(histogram, mWidth, mHeight);
			int colorPixels = getAmountOfColorInHistogram(histogram);
			
			numPixels = (colorPixels * 100) / imageSize;
        }
		return numPixels;*/
		
		//boolean goodPic = false;
		histogram = new Histogram(bitMap);
		int width = bitMap.getWidth();
		int height = bitMap.getHeight();
		int grey = histogram.getGreyPixels();
		int brown = histogram.getBrownPixels();
		int imageSize = width * height;
		int colorPixels = getColorPixels(histogram);
		int adjustedSize = imageSize - brown - grey;
		numPixels = (colorPixels * 100) / (adjustedSize / 4);
		
		
		return numPixels;
	}
	
	public int getColorPixels(Histogram mHist) {
		int colorPix = -1;
		switch (currentColor) {
		case RED:
			colorPix = mHist.getRedPixels();
			break;
		case BLUE:
			colorPix = mHist.getBluePixels();
			break;
		case GREEN:
			colorPix = mHist.getGreenPixels();
			break;
		case ORANGE:
			colorPix = mHist.getOrangePixels();
			break;
		case PINK:
			colorPix = mHist.getPinkPixels();
			break;
		case PURPLE:
			colorPix = mHist.getPurplePixels();
			break;
		}
		return colorPix;
	}
	
	/*
	private int getImageSize(Bitmap bitMap) {
		return bitMap.getWidth() * bitMap.getHeight();
	}
	private int isEnoughColorPixels(int colorPixels, int adjustedSize) {
		int adjPixels = (colorPixels * 100) / adjustedSize;
		return adjPixels;
	}*/

	private int getPixelCountWithoutBrownAndGrey(Histogram hist, int w, int h) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		//GenerateHistogramTask task = new GenerateHistogramTask();
		//task.execute(histogram);
		return (h * w) - hist.getGreyPixels()
				- hist.getBrownPixels();
	}

	private int getAmountOfColorInHistogram(Histogram mHist)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, SecurityException, NoSuchMethodException {
		java.lang.reflect.Method method;
		method = mHist.getClass().getMethod(
				"get" + currentColor.getStringVersion() + "Pixels");
		return (Integer) method.invoke(mHist);

	}
	
	/*****************END PIXELS AND HISTOGRAM*******************************/

	

	public void stopTimer() {
		countdownTimer.cancel();
	}
        
        /**
         *This is the method that prompts the player to begin their search.
         * 
         */
	public void alertTask() {
		currentColor = getNewColor();
		colorImgDrawable = currentColor.getResource();

		// Dialog
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.custom_dialog);
		dialog.setTitle("Task " + GlobalState.currentTask + " out of " + GlobalState.tasksToComplete);
		dialog.setCancelable(false);
		// Text View
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText("Take a picture of something that is " + currentColor.getStringVersion() +
				" The timer starts NOW!");
		// Image View
		ImageView image = (ImageView) dialog.findViewById(R.id.imageview);
		image.setImageResource(colorImgDrawable);
		countdownTimer.start();
		
		Button button = (Button) dialog.findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (imageView != null && levelTaskView != null) {
					imageView.setBackgroundResource(colorImgDrawable);
					levelTaskView.setText("Task: " + GlobalState.currentTask);
				}
				snapButton.setClickable(true);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	public ColorEnum getNewColor() {
		ColorEnum newColor = GlobalState.randColorList.get(GlobalState.currentIdx);
		while (newColor.getStringVersion().equals(BROWN) || 
				newColor.getStringVersion().equals(GREY)) {
			GlobalState.currentIdx++;
			newColor = GlobalState.randColorList.get(GlobalState.currentIdx);
			
		}
		return newColor;
	}
	/*
	public int randomizeColorSelection() {
		Random gen = new Random();
		int pickedNumber = gen.nextInt(6) + 1;
		return pickedNumber;
	}*/

	/****************** LEVELS AND TASKS **********************************/
	/*
	public int getLevelNumber() {
		return GlobalState.level_number;
	}

	public void setLevelNumber(int levelNum) {
		GlobalState.level_number = levelNum;
	}*/
	/*
	public void checkTaskProgress() {
		if (taskNumber <= GlobalState.tasksToComplete) {
			alertTask();
		} else {
			gameCompleted();
		}
	}*/

	public void taskCompleted() {
		int taskNumber = GlobalState.currentTask + 1;
		if (GlobalState.tasksToComplete < taskNumber) {
			pool.play(crowdCheer, 1, 1, 1, 0, 1);
			gameCompleted(true);
		} else {
			gameCompleted(false);
		}
	}
	/*
	public void incrementTasksToComplete() {
		GlobalState.tasksToComplete += 2;
	}

	public void decrementTasksToComplete() {
		GlobalState.tasksToComplete -= 2;
	}*/
	/*
	public void incrementLevelDuration() {
		GlobalState.levelDurationMili += 45000;
	}

	public void decrementLevelDuration() {
		GlobalState.levelDurationMili -= 45000;
	}*/

	public void gameCompleted(boolean complete) {
		onStop();
		showEndOfTaskAlert(complete);
	}
	/*
	public boolean randomOutputPicture() {
		Random rnd = new Random();
		int genNum = rnd.nextInt(2) + 1;
		if (genNum == 1) {
			return true;
		} else {
			return false;
		}
	}*/

	
	/*
	private void vibrate() {
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(200);
	}*/
	
	/*
	private void incrementLevel() {
		GlobalState.level_number++;
	}

	private void incrementGameDifficulty() {
		incrementLevel();
		incrementLevelDuration();
		incrementTasksToComplete();
	} */

	public void showEndOfTaskAlert(final boolean gameCompleted) {
		AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
		int tasksLeft = GlobalState.tasksToComplete - GlobalState.currentTask;
		
		if (gameCompleted) {
			alertBox.setMessage("Congratulations! You completed the " + "" +
					"scavenger hunt! You're score for " + currentColor.getStringVersion() + 
					" was " + Integer.toString(currentColor.getColorScore()));
			GlobalState.currentTask = 1;
            GlobalState.currentIdx = 0;
		} else {
			alertBox.setMessage("Only " + tasksLeft + 
					" more colors to go! Keep hunting! You're score for " + 
					currentColor.getStringVersion() + " was " + 
					Integer.toString(currentColor.getColorScore()));
		}
		alertBox.setCancelable(false);
		alertBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int arg1) {
				if (gameCompleted) {
					startActivity(startIntent);
				} else {
					GlobalState.currentTask++;
					GlobalState.currentIdx++;
					startActivity(currIntent);
				}
				dialogInterface.cancel();
			}
		});
		alertBox.show();
	}

	/*
	private boolean isGameTimeRemaining() {
		return countdownTimer.getSecondsRemaining() >= 0;
	}*/
	
	@Override
	public void onBackPressed() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            onStop();
		            GlobalState.currentTask = 1;
		            GlobalState.currentIdx = 0;
		            startActivity(startIntent);
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            dialog.cancel();
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to quit?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();

	}

	@Override
	public void onStop() {
		stopTimer();

		super.onStop();
	}
	
/*
	private class GenerateHistogramTask extends AsyncTask<Histogram, Integer, Histogram> {
	     
		private Bitmap currentBitmap; 
		private Histogram currentHistogram; 
		
		public GenerateHistogramTask() {
			currentBitmap = null;
			currentHistogram = null;
		}
		
		@Override
		protected Histogram doInBackground(Histogram... params) {
	         int count = urls.length;
	         long totalSize = 0;
	         for (int i = 0; i < count; i++) {
	             totalSize += Downloader.downloadFile(urls[i]);
	             publishProgress((int) ((i / (float) count) * 100));
	             // Escape early if cancel() is called
	             if (isCancelled()) break;
	         }
	         return totalSize; 
			currentHistogram = params[0];
			currentBitmap = currentHistogram.getBitmap();
			
			java.lang.reflect.Method method;
			try {
				method = currentHistogram.getClass()
						.getDeclaredMethod("generateHistogram");
				method.invoke(currentHistogram, currentBitmap);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			return currentHistogram;
			
	     }
		
	     protected void onProgressUpdate(Integer... progress) {
	         //setProgressPercent(progress[0]);
	     }

	     protected void onPostExecute(Long result) {
	         //showDialog("Downloaded " + result + " bytes");
	     }

	 } */

}
