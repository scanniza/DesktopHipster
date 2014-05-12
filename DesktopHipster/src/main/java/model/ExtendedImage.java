package model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import view.ThumbnailData;
import filter.FiltersEnum;

/**
 * Image class extending BufferedImage for having preview and thumbnail sized
 * versions.
 * 
 * @author Edvard Hubinette
 * @revised by Simon Arneson
 * @revised Edvard H��binette
 * @revised Lovisa J��berg
 */

public class ExtendedImage extends BufferedImage implements ThumbnailData,
		Serializable {

	private static int id;
	private final int imageID;

	private transient TreeSet<String> tags = new TreeSet<String>();
	private transient BufferedImage preview;
	private transient BufferedImage thumbnail;

	// Map for managing image edit versions
	private transient TreeMap<FiltersEnum, BufferedImage> versions = new TreeMap<FiltersEnum, BufferedImage>();

	public ExtendedImage(ImageIcon image) {
		super(image.getIconWidth(), image.getIconHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = this.createGraphics();
		image.paintIcon(null, g, 0, 0);
		g.dispose();

		preview = filter.ImageTools.toBufferedImage(getScaledInstance(600, -1,
				Image.SCALE_SMOOTH));
		thumbnail = filter.ImageTools.toBufferedImage(getScaledInstance(100,
				-1, Image.SCALE_FAST));
		imageID = id += 1;
	}

	/**
	 * Adds an edited version of this image to a list of versions
	 * 
	 * @param filterName
	 *            The name of the filter used
	 * @param image
	 *            The edited image to stash
	 */
	public void addVersion(FiltersEnum filterName, BufferedImage image) {
		versions.put(filterName, image);
	}

	/**
	 * Gets a version of the image with a specific filter
	 * 
	 * @param filterName
	 *            The name of the filter used
	 * @return The edited image to stash
	 * @throws NoSuchVersionException
	 *             Image version with that filter does not exist
	 */
	public BufferedImage getVersion(FiltersEnum filterName)
			throws NoSuchVersionException {
		if (versions.containsKey(filterName)) {
			return versions.get(filterName);
		} else {
			throw new NoSuchVersionException();
		}
	}

	/**
	 * Returns a smaller version of the image
	 * 
	 * @return Preview-sized version of this image
	 */
	public BufferedImage getPreview() {
		return preview;
	}

	public void setPreview(BufferedImage newPreview) {
		preview = newPreview;
	}

	/**
	 * Returns a small version of the image
	 * 
	 * @return Thumbnail-sized version of this image
	 */
	public BufferedImage getThumbnail() {
		return thumbnail;
	}

	@Override
	public List<BufferedImage> getVersions() {
		return new ArrayList<BufferedImage>(versions.values());
	}
	@Override
	public TreeSet<String> getTags() {
		return new TreeSet<String>(tags);
	}
	public boolean addTag(String tag){
		if(tags.contains(tag))
			return false;
		else
			tags.add(tag);
		return true;
	}
	public void removeTag(String tag) {
		tags.remove(tag);
	}
	@Override
	public BufferedImage getSelectedVersion() {
		return thumbnail;
	}

	public void setThumbnailSize(int width) {
		thumbnail = filter.ImageTools.toBufferedImage(getScaledInstance(width,
				width, Image.SCALE_FAST));
	}

	public int getID() {
		return imageID;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		ImageIO.write(preview, "png", out);
		ImageIO.write(thumbnail, "png", out);
		ImageIO.write((BufferedImage)this, "png", out);
		out.writeInt(versions.size());
		for (Entry<FiltersEnum, BufferedImage> entry : versions.entrySet()) {
			out.writeObject(entry.getKey());
			ImageIO.write(entry.getValue(), "png", out);
		}
		out.flush();
		out.close();
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		preview = ImageIO.read(in);
		thumbnail = ImageIO.read(in);
		
		Graphics g = this.createGraphics();
		ImageIcon image = new ImageIcon(ImageIO.read(in));
		image.paintIcon(null, g, 0, 0);
		g.dispose();
		
		versions = new TreeMap<FiltersEnum, BufferedImage>();
		final int n = in.readInt();
		for (int i = 0; i < n; i++){
			FiltersEnum key = (FiltersEnum)in.readObject();
			versions.put(key, ImageIO.read(in));
		}
		in.close();
	}
}
