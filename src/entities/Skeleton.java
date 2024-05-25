package entities;

import static utilz.Constants.EnemyConstants.*;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;

import gamestates.Playing;
import main.Game;

public class Skeleton extends Enemy {

	private int attackBoxOffsetX;

	public Skeleton(float x, float y) {
		super(x, y, SKELETON_WIDTH, SKELETON_HEIGHT, SKELETON);
		initHitbox(20, 30);
		initAttackBox();
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (50 * Game.SCALE), (int) (20 * Game.SCALE));
		attackBoxOffsetX = (int) (Game.SCALE * 15);
	}

	public void update(int[][] lvlData, Player player,Playing playing) {
		updateBehavior(lvlData, player,playing);
		updateAnimationTick();
		updateAttackBox();
	}
	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;
	}

	private void updateBehavior(int[][] lvlData, Player player, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			updateInAir(lvlData,player);
		else {
			switch (state) {
			case IDLE:
				newState(RUNNING);
				break;
			case RUNNING:
				if (canSeePlayer(lvlData, player)) {
					turnTowardsPlayer(player);
					if (isPlayerCloseForAttack(player))
						newState(ATTACK);
				}

				move(lvlData,player);
				break;
			case ATTACK:
				if (aniIndex == 0)
				{
					attackChecked = false;
					playing.getGame().getAudioPlayer().playAttackSound();
				}
				if (aniIndex == 8 && !attackChecked)
					checkPlayerHit(attackBox, player);

				break;
			case HIT:
				break;
			}
		}
	}

	public int flipX() {
		if (walkDir == RIGHT)
			return width;
		else
			return 0;
	}

	public int flipW() {
		if (walkDir == RIGHT)
			return -1;
		else
			return 1;
	}
}