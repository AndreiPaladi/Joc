package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] skeletonArr;
	private BufferedImage[][] skeleton_magesArr;
	private ArrayList<Skeleton> skeletons = new ArrayList<>();
	private ArrayList<Skeleton_Mage> skeleton_mages = new ArrayList<>();

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}

	public void loadEnemies(Level level) {
		skeletons = level.getSkeletons();
		skeleton_mages=level.getSkeletonMages();
	}

	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false;
		for (Skeleton c : skeletons)
			if (c.isActive()) {
				c.update(lvlData, player,playing);
				isAnyActive = true;
			}
		for (Skeleton_Mage sm : skeleton_mages)
			if (sm.isActive()) {
				sm.update(lvlData, player);
				isAnyActive = true;
			}
		if (!isAnyActive)
			playing.setLevelCompleted(true);
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawSkeletons(g, xLvlOffset);

	}

	private void drawSkeletons(Graphics g, int xLvlOffset) {
		for (Skeleton s : skeletons)
			if (s.isActive()) {
				g.drawImage(skeletonArr[s.getState()][s.getAniIndex()], (int) s.getHitbox().x+(s.flipW()== 1 ? 48 : -16)- xLvlOffset,
						(int) s.getHitbox().y - SKELETON_DRAWOFFSET_Y, SKELETON_WIDTH * -s.flipW(), SKELETON_HEIGHT, null);

			}
		for (Skeleton_Mage s : skeleton_mages)
			if (s.isActive()) {
				g.drawImage(skeleton_magesArr[s.getState()][s.getAniIndex()], (int) s.getHitbox().x+(s.flipW()== 1 ? 48 : -16)- xLvlOffset,
						(int) s.getHitbox().y - SKELETON_DRAWOFFSET_Y, SKELETON_WIDTH * -s.flipW(), SKELETON_HEIGHT, null);
				//s.drawHitbox(g, xLvlOffset);
				//s.drawAttackBox(g, xLvlOffset);
				}


	}



	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for (Skeleton s : skeletons)
			if (s.isActive())
				if (s.getCurrentHealth() > 0)
					if (attackBox.intersects(s.getHitbox())) {
						s.hurt(5);
						return;
					}
		for (Skeleton_Mage s : skeleton_mages)
			if (s.isActive())
				if (s.getCurrentHealth() > 0)
					if (attackBox.intersects(s.getHitbox())) {
						s.hurt(5);
						return;
					}


	}

	private void loadEnemyImgs() {
		skeletonArr = new BufferedImage[5][13];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SKELETON_SPRITE);
		for (int j = 0; j < skeletonArr.length; j++)
			for (int i = 0; i < skeletonArr[j].length; i++)
				skeletonArr[j][i] = temp.getSubimage(i * SKELETON_WIDTH_DEFAULT, j * SKELETON_HEIGHT_DEFAULT, SKELETON_WIDTH_DEFAULT, SKELETON_HEIGHT_DEFAULT);
		skeleton_magesArr = new BufferedImage[5][21];
		temp = LoadSave.GetSpriteAtlas(LoadSave.SKELETON_MAGES_SPRITE);
		for (int j = 0; j < skeleton_magesArr.length; j++)
			for (int i = 0; i < skeleton_magesArr[j].length; i++)
				skeleton_magesArr[j][i] = temp.getSubimage(i * SKELETON_WIDTH_DEFAULT, j * SKELETON_HEIGHT_DEFAULT, SKELETON_WIDTH_DEFAULT, SKELETON_HEIGHT_DEFAULT);
	}

	public void resetAllEnemies() {
		for (Skeleton s : skeletons)
			s.resetEnemy();
		for (Skeleton_Mage s : skeleton_mages)
			s.resetEnemy();
	}

}
