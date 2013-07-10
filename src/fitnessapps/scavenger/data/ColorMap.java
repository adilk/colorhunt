package fitnessapps.scavenger.data;

import android.graphics.Color;

public class ColorMap {
	private static ColorMap instance;
	
	private final int BUCKETS = 4;
	private final int BUCKET_SIZE = 256/BUCKETS;
	
	/**
	 * 3-dimensional array acting as key to determining pixel color classification
	 * First index is Red level, second index is Green level, third index is Blue level
	 */
	private ColorEnum[][][] colorArray;
	
	
	protected ColorMap(){
		//prevent instantiation
	}
	
	private void populateColorMap(){

		colorArray = new ColorEnum[4][4][4];
		//RED ZERO
		colorArray[0][0][0] = ColorEnum.GREY;
		colorArray[0][0][1] = ColorEnum.GREY;
		colorArray[0][0][2] = ColorEnum.BLUE;
		colorArray[0][0][3] = ColorEnum.BLUE;
		colorArray[0][1][0] = ColorEnum.GREY;
		colorArray[0][1][1] = ColorEnum.GREY;
		colorArray[0][1][2] = ColorEnum.BLUE;
		colorArray[0][1][3] = ColorEnum.BLUE;
		colorArray[0][2][0] = ColorEnum.GREEN;
		colorArray[0][2][1] = ColorEnum.GREEN;
		colorArray[0][2][2] = ColorEnum.GREY;
		colorArray[0][2][3] = ColorEnum.BLUE;
		colorArray[0][3][0] = ColorEnum.GREEN;
		colorArray[0][3][1] = ColorEnum.GREEN;
		colorArray[0][3][2] = ColorEnum.GREEN;
		colorArray[0][3][3] = ColorEnum.GREY;
		//RED 1
		colorArray[1][0][0] = ColorEnum.GREY;
		colorArray[1][0][1] = ColorEnum.PURPLE;
		colorArray[1][0][2] = ColorEnum.BLUE;
		colorArray[1][0][3] = ColorEnum.BLUE;
		colorArray[1][1][0] = ColorEnum.BROWN;
		colorArray[1][1][1] = ColorEnum.GREY;
		colorArray[1][1][2] = ColorEnum.BLUE;
		colorArray[1][1][3] = ColorEnum.BLUE;
		colorArray[1][2][0] = ColorEnum.GREEN;
		colorArray[1][2][1] = ColorEnum.GREEN;
		colorArray[1][2][2] = ColorEnum.GREY;
		colorArray[1][2][3] = ColorEnum.BLUE;
		colorArray[1][3][0] = ColorEnum.GREEN;
		colorArray[1][3][1] = ColorEnum.GREEN;
		colorArray[1][3][2] = ColorEnum.GREEN;
		colorArray[1][3][3] = ColorEnum.GREY;
		//RED 2
		colorArray[2][0][0] = ColorEnum.RED;
		colorArray[2][0][1] = ColorEnum.RED;
		colorArray[2][0][2] = ColorEnum.PURPLE;
		colorArray[2][0][3] = ColorEnum.BLUE;
		colorArray[2][1][0] = ColorEnum.BROWN;
		colorArray[2][1][1] = ColorEnum.BROWN;
		colorArray[2][1][2] = ColorEnum.PURPLE;
		colorArray[2][1][3] = ColorEnum.BLUE;
		colorArray[2][2][0] = ColorEnum.BROWN;
		colorArray[2][2][1] = ColorEnum.BROWN;
		colorArray[2][2][2] = ColorEnum.GREY;
		colorArray[2][2][3] = ColorEnum.BLUE;
		colorArray[2][3][0] = ColorEnum.GREEN;
		colorArray[2][3][1] = ColorEnum.GREEN;
		colorArray[2][3][2] = ColorEnum.GREEN;
		colorArray[2][3][3] = ColorEnum.GREY;
		//RED 3
		colorArray[3][0][0] = ColorEnum.RED;
		colorArray[3][0][1] = ColorEnum.RED;
		colorArray[3][0][2] = ColorEnum.PURPLE;
		colorArray[3][0][3] = ColorEnum.PURPLE;
		colorArray[3][1][0] = ColorEnum.RED;
		colorArray[3][1][1] = ColorEnum.RED;
		colorArray[3][1][2] = ColorEnum.PURPLE;
		colorArray[3][1][3] = ColorEnum.PURPLE;
		colorArray[3][2][0] = ColorEnum.ORANGE;
		colorArray[3][2][1] = ColorEnum.ORANGE;
		colorArray[3][2][2] = ColorEnum.PINK;
		colorArray[3][2][3] = ColorEnum.PINK;
		colorArray[3][3][0] = ColorEnum.ORANGE;
		colorArray[3][3][1] = ColorEnum.GREEN;
		colorArray[3][3][2] = ColorEnum.BROWN;
		colorArray[3][3][3] = ColorEnum.GREY;
	}
	
	public static ColorMap getInstance(){
		if(instance == null){
			instance = new ColorMap();
			instance.populateColorMap();
		}
		return instance;
	}
	
	public String getColorFromInt(int rawColorValue){
		int red = Color.red(rawColorValue);
		int green = Color.green(rawColorValue);
		int blue = Color.blue(rawColorValue);
		
		return getColorFromValue(red, green, blue);
	}
	
	private String getColorFromValue(int r, int g, int b){
		return colorArray[bucketIndex(r)][bucketIndex(g)][bucketIndex(b)].getStringVersion();
	}
	
	private int bucketIndex(int colorChannelValue){
		return (int)Math.floor(colorChannelValue/BUCKET_SIZE);
	}
}
