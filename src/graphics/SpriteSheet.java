package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

	private String path;
	public final int WIDTH, HEIGHT;
	public int[] pixels;

	public static SpriteSheet font = new SpriteSheet("res/textures/font.png", 182, 28);

	public SpriteSheet(String path, int width, int height) {
		//System.out.println(new File("res/textures/font.png").exists());
		this.path = path;
		this.WIDTH = width;
		this.HEIGHT = height;
		pixels = new int[WIDTH * HEIGHT];
		load();
	}

	private void load() {
		try {
			BufferedImage image = ImageIO.read(new File(path));//SpriteSheet.class.getResource(path)
			int w = image.getWidth();
			int h = image.getHeight();
			//System.out.println(image);
			pixels = image.getRGB(0, 0, w, h, null, 0, w);
			//System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
