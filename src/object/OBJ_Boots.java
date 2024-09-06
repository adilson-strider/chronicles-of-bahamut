package object;

import entity.Entity;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_Boots extends Entity {
	
	public OBJ_Boots() {
		name = "Boots";
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/images/objects/boots.png"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}
