import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	
	private boolean isRunning = false;
	private Thread thread;
	private Handler handler;
	private Camera camera;
	private SpriteSheet ss;
	
	private BufferedImage level = null;
	private BufferedImage sprite_sheet = null;
	private BufferedImage floor = null;
	
	public int ammo = 50;
	public int hp = 100;
	private int time = 0;
	
	public Game(){
		new Window(1000, 563, "Wizard Game", this);
		start();
		
		handler = new Handler();
		
		camera = new Camera(0, 0);
		this.addKeyListener(new KeyInput(handler));
		
		BufferedImageLoader loader = new BufferedImageLoader();
		level = loader.loadImage("/wizard_level.png");
		sprite_sheet = loader.loadImage("/wizard_images.png");
		
		ss = new SpriteSheet(sprite_sheet);
		
		floor = ss.grabImage(4, 2, 32, 32);
	
		this.addMouseListener(new MouseInput(handler, camera, this, ss));
		
		loadLevel (level);

	}
	
	private void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	private void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		float frames = 0;
		while(isRunning){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				delta--;
			}
			if(isRunning)
				render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	
	public static float clamp(float var, float min, float max) {
		if(var >= max) 
			return var = max;
		else if(var <= min)
			return var = min;
		else
			return var;
	}
	
	private void tick() 
	 {
	  for(int i = 0; i < handler.object.size(); i++)
	  {
	   if(handler.object.get(i).getId() == ID.Player)
	   {
	    camera.tick(handler.object.get(i));
	   }
	  }
	  handler.tick();
	  time++;
	  
	 }
	
	private void render() 
	 {
	  BufferStrategy bs = this.getBufferStrategy();
	  if(bs == null)
	  {
	   this.createBufferStrategy(3);
	   return;
	  }
	  
	  Graphics g =  bs.getDrawGraphics();
	  Graphics2D g2d = (Graphics2D) g;
	  //////////////////////////////////////
	  
	  g2d.translate(-camera.getX(), -camera.getY());
	  
	  for(int xx = 0; xx < 30*72; xx+=32) {
		  for(int yy = 0; yy < 30*72; yy+=32) {
			  g.drawImage(floor, xx, yy, null);
		  }
	  }
	  
	  handler.render(g);
	  
	  g2d.translate(camera.getX(), camera.getY());
	  
	  g.setColor(Color.white);
	  g.drawString("Ammo: " + ammo, 5, 15);
	  g.drawString("Time: " + (time/100), 105, 15);
	  
	  //////////////////////////////////////
	  
	  g.dispose();
	  bs.show();}

	 
	
	//loading the level
	private void loadLevel(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		
		for(int xx = 0; xx < w; xx++) {
			for(int yy = 0; yy < h; yy++) {
				int pixel = image.getRGB(xx, yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				if(red == 255)
					handler.addObject(new Block(xx*32, yy*32, ID.Block, ss));
				
				if(green == 255 && blue == 0)
					handler.addObject(new Enemy(xx*32, yy*32, ID.Enemy, handler, ss));
				
				if(blue == 255 && green == 0)
					handler.addObject(new Wizard(xx*32, yy*32, ID.Player, handler, this, ss));
				
				if(blue == 255 && green == 255)
					handler.addObject(new Crate(xx*32, yy*32, ID.Crate, ss));
				
			}
		}
	}
	
	
	
	public static void main(String args[]) {
		new Game();
	}

}
