import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends GameObject{

	private Handler handler;
	
	Random r = new Random();
	int choose = 0;
	int hp = 100;
	int ammo = 15;
	
	
	private BufferedImage enemy_image;
	
	public Enemy(int x, int y, ID id, Handler handler, SpriteSheet ss) {
		super(x, y, id, ss);
		this.handler = handler;
		
		enemy_image = ss.grabImage(4, 1, 32, 32);
	}

	public void tick() {
		
		x += velX;
		y += velY;
		
		choose = r.nextInt(10);
		
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ID.Block) {
				if(getBoundsBig().intersects(tempObject.getBounds())) { 
					x += (velX * 5) * -1;
					y += (velY * 5) * -1;
					velX *= -1;
					velY *= -1;
				}else if(choose == 0) {
					velX = (r.nextInt(4 - -4) + -4);
					velY = (r.nextInt(4 - -4) + -4);
					
				}
			}
			
			if(tempObject.getId() == ID.Bullet) {
				if(getBounds().intersects(tempObject.getBounds())) {
				hp -= 33.4;
				handler.removeObject(tempObject);
				}
			}
			
			if(ammo >= 11) {
			//handler.addObject(new EBullet(tempObject.getX() + 16, tempObject.getY() + 16, ID.EBullet, handler, r.nextInt(1000), r.nextInt(563), ss));
				ammo--;
			}	
		}
		
		if(hp <= 0) handler.removeObject(this);
		
	}

	public void render(Graphics g) {
		g.drawImage(enemy_image, x, y, null);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 32, 32);
	}
	
	public Rectangle getBoundsBig() {
		return new Rectangle(x-16, y-16, 64, 64);
	}

}
