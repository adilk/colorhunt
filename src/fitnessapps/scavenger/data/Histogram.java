package fitnessapps.scavenger.data;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Histogram {

	private Bitmap bitmap;
	private int size;
	private int redPixels;
	private int greenPixels;
	private int bluePixels;
	private int purplePixels;
	private int orangePixels;
	private int greyPixels;
	private int brownPixels;
	private int pinkPixels;

	public Histogram(Bitmap bmp) {
		bitmap = bmp;
		generateHistogram(bitmap);
	}
    public int getRedPixels(){
	return redPixels;
    }

    public int getGreenPixels(){
	return greenPixels;
    }

    public int getBluePixels(){
	return bluePixels;
    }

    public int getPurplePixels(){
	return purplePixels;
    }

    public int getOrangePixels(){
	return orangePixels;
    }

    public int getGreyPixels(){
	return greyPixels;
    }

    public int getBrownPixels(){
	return brownPixels;
    }

    public int getPinkPixels(){
	return pinkPixels;
    }

	private void generateHistogram(Bitmap bmp) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		size = width*height;

		for (int i = 0; i < width; i+=2) {
			for (int j = 0; j < height; j+=2) {
				int pixColor = bmp.getPixel(i, j);

				if (isRedZero(pixColor)) {
					if (isGreenZero(pixColor)) {
						if (isBlueZero(pixColor)) {
							greyPixels++;
						} else if (isBlueOne(pixColor)) {
							greyPixels++;
						} else if (isBlueTwo(pixColor)) {
							bluePixels++;
						} else if (isBlueThree(pixColor)) {
							bluePixels++;
						}
					} else if (isGreenOne(pixColor)) {
						if (isBlueZero(pixColor)) {
							greyPixels++;
						} else if (isBlueOne(pixColor)) {
							greyPixels++;
						} else if (isBlueTwo(pixColor)) {
							bluePixels++;
						} else if (isBlueThree(pixColor)) {
							bluePixels++;
						}
					} else if (isGreenTwo(pixColor)) {
						if (isBlueZero(pixColor)) {
							greenPixels++;
						} else if (isBlueOne(pixColor)) {
							greenPixels++;
						} else if (isBlueTwo(pixColor)) {
							greyPixels++;
						} else if (isBlueThree(pixColor)) {
							bluePixels++;
						}
					} else if (isGreenThree(pixColor)) {
						if (isBlueZero(pixColor)) {
							greenPixels++;
						} else if (isBlueOne(pixColor)) {
							greenPixels++;
						} else if (isBlueTwo(pixColor)) {
							greenPixels++;
						} else if (isBlueThree(pixColor)) {
							greyPixels++;
						}
					}
				}

				if (isRedOne(pixColor)) {
					if (isGreenZero(pixColor)) {
						if (isBlueZero(pixColor)) {
							greyPixels++;
						} else if (isBlueOne(pixColor)) {
							purplePixels++;
						} else if (isBlueTwo(pixColor)) {
							bluePixels++;
						} else if (isBlueThree(pixColor)) {
							bluePixels++;
						}
					} else if (isGreenOne(pixColor)) {
						if (isBlueZero(pixColor)) {
							brownPixels++;
						} else if (isBlueOne(pixColor)) {
							greyPixels++;
						} else if (isBlueTwo(pixColor)) {
							bluePixels++;
						} else if (isBlueThree(pixColor)) {
							bluePixels++;
						}
					} else if (isGreenTwo(pixColor)) {
						if (isBlueZero(pixColor)) {
							greenPixels++;
						} else if (isBlueOne(pixColor)) {
							greenPixels++;
						} else if (isBlueTwo(pixColor)) {
							greyPixels++;
						} else if (isBlueThree(pixColor)) {
							bluePixels++;
						}
					} else if (isGreenThree(pixColor)) {
						if (isBlueZero(pixColor)) {
							greenPixels++;
						} else if (isBlueOne(pixColor)) {
							greenPixels++;
						} else if (isBlueTwo(pixColor)) {
							greenPixels++;
						} else if (isBlueThree(pixColor)) {
							greyPixels++;
						}
					}
				}

				if (isRedTwo(pixColor)) {
					if (isGreenZero(pixColor)) {
						if (isBlueZero(pixColor)) {
							redPixels++;
						} else if (isBlueOne(pixColor)) {
							redPixels++;
						} else if (isBlueTwo(pixColor)) {
							purplePixels++;
						} else if (isBlueThree(pixColor)) {
							bluePixels++;
						}
					} else if (isGreenOne(pixColor)) {
						if (isBlueZero(pixColor)) {
							brownPixels++;
						} else if (isBlueOne(pixColor)) {
							brownPixels++;
						} else if (isBlueTwo(pixColor)) {
							purplePixels++;
						} else if (isBlueThree(pixColor)) {
							bluePixels++;
						}
					} else if (isGreenTwo(pixColor)) {
						if (isBlueZero(pixColor)) {
							brownPixels++;
						} else if (isBlueOne(pixColor)) {
							brownPixels++;
						} else if (isBlueTwo(pixColor)) {
							greyPixels++;
						} else if (isBlueThree(pixColor)) {
							bluePixels++;
						}
					} else if (isGreenThree(pixColor)) {
						if (isBlueZero(pixColor)) {
							greenPixels++;
						} else if (isBlueOne(pixColor)) {
							greenPixels++;
						} else if (isBlueTwo(pixColor)) {
							greenPixels++;
						} else if (isBlueThree(pixColor)) {
							greyPixels++;
						}
					}
				}

				if (isRedThree(pixColor)) {
					if (isGreenZero(pixColor)) {
						if (isBlueZero(pixColor)) {
							redPixels++;
						} else if (isBlueOne(pixColor)) {
							redPixels++;
						} else if (isBlueTwo(pixColor)) {
							purplePixels++;
						} else if (isBlueThree(pixColor)) {
							purplePixels++;
						}
					} else if (isGreenOne(pixColor)) {
						if (isBlueZero(pixColor)) {
							redPixels++;
						} else if (isBlueOne(pixColor)) {
							redPixels++;
						} else if (isBlueTwo(pixColor)) {
							purplePixels++;
						} else if (isBlueThree(pixColor)) {
							purplePixels++;
						}
					} else if (isGreenTwo(pixColor)) {
						if (isBlueZero(pixColor)) {
							orangePixels++;
						} else if (isBlueOne(pixColor)) {
							orangePixels++;
						} else if (isBlueTwo(pixColor)) {
							pinkPixels++;
						} else if (isBlueThree(pixColor)) {
							pinkPixels++;
						}
					} else if (isGreenThree(pixColor)) {
						if (isBlueZero(pixColor)) {
							orangePixels++;
						} else if (isBlueOne(pixColor)) {
							greenPixels++;
						} else if (isBlueTwo(pixColor)) {
							brownPixels++;
						} else if (isBlueThree(pixColor)) {
							greyPixels++;
						}
					}
				}

			}
		}
	}

	private boolean isRedZero(int pixCol) {
		int redValue = Color.red(pixCol);
		return redValue < 64;
	}

	private boolean isRedOne(int pixCol) {
		int redValue = Color.red(pixCol);
		return redValue > 63 && redValue < 128;
	}

	private boolean isRedTwo(int pixCol) {
		int redValue = Color.red(pixCol);
		return redValue > 127 && redValue < 192;
	}

	private boolean isRedThree(int pixCol) {
		int redValue = Color.red(pixCol);
		return redValue > 191;
	}

	private boolean isBlueZero(int pixCol) {
		int blueValue = Color.blue(pixCol);
		return blueValue < 64;
	}

	private boolean isBlueOne(int pixCol) {
		int blueValue = Color.blue(pixCol);
		return blueValue > 63 && blueValue < 128;
	}

	private boolean isBlueTwo(int pixCol) {
		int blueValue = Color.blue(pixCol);
		return blueValue > 127 && blueValue < 192;
	}

	private boolean isBlueThree(int pixCol) {
		int blueValue = Color.blue(pixCol);
		return blueValue > 191;
	}

	private boolean isGreenZero(int pixCol) {
		int greenValue = Color.green(pixCol);
		return greenValue < 64;
	}

	private boolean isGreenOne(int pixCol) {
		int greenValue = Color.green(pixCol);
		return greenValue > 63 && greenValue < 128;
	}

	private boolean isGreenTwo(int pixCol) {
		int greenValue = Color.green(pixCol);
		return greenValue > 127 && greenValue < 192;
	}

	private boolean isGreenThree(int pixCol) {
		int greenValue = Color.green(pixCol);
		return greenValue > 191;
	}

	private class Bucket {

		private int size = 0;

		public Bucket(int lower, int upper) {

		}

		public void addItem() {
			size++;
		}

		public int getSize() {
			return size;
		}
	}
}
