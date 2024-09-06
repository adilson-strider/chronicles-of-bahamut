package main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InputManager implements KeyListener {

	private final int NUM_KEY = 600;
	private boolean[] keys = new boolean[NUM_KEY];
	private boolean[] keyslast = new boolean[NUM_KEY];
	private Map<Integer, Long> keyHeldDelayMap;
	private long delayDuration = 100; // Delay duration in milliseconds

	private ArrayList<KeyListener> listeners = new ArrayList<>();

	public static InputManager Instance;

	private InputManager() {
		keyHeldDelayMap = new HashMap<>();
	}

	public static InputManager getInstance() {
		if (Instance == null) {
			Instance = new InputManager();
		}
		return Instance;
	}

	public void update() {
		for (int i = 0; i < NUM_KEY; i++) {
			keyslast[i] = keys[i];
		}
	}

	public void addKeyListener(KeyListener listener) {
		listeners.add(listener);
	}

	public boolean isKeyHeld(int keyCode) {
		return keys[keyCode];
	}

	public boolean isKeyHeldWithDelay(int keyCode) {
		long currentTime = System.currentTimeMillis();
		if (keys[keyCode]) {
			if (!keyHeldDelayMap.containsKey(keyCode) || currentTime - keyHeldDelayMap.get(keyCode) >= delayDuration) {
				keyHeldDelayMap.put(keyCode, currentTime);
				return true;
			}
		} else {
			keyHeldDelayMap.remove(keyCode);
		}
		return false;
	}

	public boolean isKeyDown(int keyCode) {
		return keys[keyCode] && !keyslast[keyCode];
	}

	public boolean isKeyUp(int keyCode) {
		return !keys[keyCode] && keyslast[keyCode];
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void registerComponent(Component component) {
		component.addKeyListener(this);
		component.setFocusable(true);
		component.requestFocusInWindow();
	}

	private void notifyListeners(KeyEvent e) {
		for (KeyListener listener : listeners) {
			switch (e.getID()) {
				case KeyEvent.KEY_PRESSED:
					listener.keyPressed(e);
					break;
				case KeyEvent.KEY_RELEASED:
					listener.keyReleased(e);
					break;
				case KeyEvent.KEY_TYPED:
					listener.keyTyped(e);
					break;
			}
		}
	}
}
