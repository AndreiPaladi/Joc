package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;
import java.sql.*;
import utilz.LoadSave;

public class Game implements Runnable {

	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 60;
	private final int UPS_SET =200;

	private Playing playing;
	private Menu menu;
	private GameOptions gameOptions;
	private AudioOptions audioOptions;
	private AudioPlayer audioPlayer;


	//SQL STUFF
	Connection connection = null;
	Statement stmt = null;
	ResultSet rs = null;
	String[] sqlQuery = new String[10];

	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1.5f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

	public Game() {
		initClasses();

		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocusInWindow();

		startGameLoop();
	}

	private void initClasses() {
		audioOptions = new AudioOptions(this);
		audioPlayer = new AudioPlayer();
		menu = new Menu(this);
		playing = new Playing(this);
		gameOptions = new GameOptions(this);
		try{
		InitSQL();
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		switch (Gamestate.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
			playing.update();
			break;
		case OPTIONS:
			gameOptions.update();
			break;
		case LOAD:
            try {
                LoadSQL();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            break;
		case SAVE:
				SaveSQL();
			break;
		case QUIT:
		default:
			System.exit(0);
			break;

		}
	}

	//	TABLE  LastSave
	//	Fields
	//	Level INTEGER
	// 	Health INTEGER
	//	Energy FLOAT
	private void LoadSQL() throws SQLException {
		int Level=0, Health=0;
		float Energy=0;

		stmt = connection.createStatement();
		rs = stmt.executeQuery("select * from LastSave;");
		if (rs.next()) {
			Level = rs.getInt("Level");
			Health = rs.getInt("Health");
			Energy = rs.getFloat("Energy");

		} else {
			System.out.println("No data found in LastSave table.");
		}
		while(playing.getLevelManager().getLevelIndex()!=Level)
			playing.getLevelManager().loadNextLevel();

		playing.getPlayer().setHealth(Health);
		playing.getPlayer().setPower(Energy);
		if (rs != null) rs.close();
		if (stmt != null) stmt.close();
		Gamestate.state=Gamestate.PLAYING;
	}

	private void SaveSQL()
	{
		int Level=playing.getLevelManager().getLevelIndex(), Health=playing.getPlayer().getCurrentHealth();
		float Energy=playing.getPlayer().getPower();


		String sql = "UPDATE LastSave SET Level = " + Level + ", Health = " + Health + ", Energy = " + Energy +";";
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Gamestate.state=Gamestate.MENU;
	}

	private void InitSQL() throws SQLException,ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");
		System.out.println("JDBC driver for SQLite loaded...");
		connection = DriverManager.getConnection("jdbc:sqlite:Save.db");
		System.out.println("Connected to database ... ");

	}

	public void render(Graphics g) {
		switch (Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		case OPTIONS:
			gameOptions.draw(g);
			break;
		default:
			break;
		}
	}

	@Override
	public void run() {

		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {
			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;

			}
		}

	}

	public void windowFocusLost() {
		if (Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();
	}

	public Menu getMenu() {
		return menu;
	}

	public Playing getPlaying() {
		return playing;
	}

	public GameOptions getGameOptions() {
		return gameOptions;
	}

	public AudioOptions getAudioOptions() {
		return audioOptions;
	}

	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
}