/**
 * Created by Ram_Thirupathy on 8/9/2016.
 */
package com.ramkt.mobiAuto.utils;
import java.awt.image.BufferedImage;
import java.util.List;

import org.sikuli.core.search.RegionMatch;
import org.sikuli.core.search.algorithm.TemplateMatcher;

public class SikuliUtil {

	private BufferedImage mBaseImage;//mImage to be compared

	/**
	 * This is the method which compare the video screenshots 
	 * @param MobileDriver to take device screenshot.
	 * @return boolean images are matching or not
	 */
	public boolean compareImage(BufferedImage newImage) {
		List<RegionMatch>  listOfMatches  = TemplateMatcher.findMatchesByGrayscaleAtOriginalResolution(newImage, mBaseImage,1, 0.9);
		boolean isMatching = listOfMatches.iterator().hasNext();
		mBaseImage = newImage;
		return isMatching;
	}

	public void setBaseImage(BufferedImage mImage){
		mBaseImage = mImage;
	}
}
