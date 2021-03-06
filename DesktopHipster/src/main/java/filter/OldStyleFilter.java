package filter;

import java.awt.image.BufferedImage;

public class OldStyleFilter implements IFilter {

	public BufferedImage applyFilter(BufferedImage image) {
		image = ImageTools.applyEnhancedColors(image, 20, 5);
		return ImageTools.applySepia(image);
	}


}
