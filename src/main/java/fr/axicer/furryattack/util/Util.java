package fr.axicer.furryattack.util;

import org.lwjgl.glfw.GLFW;

public class Util {
	
	public static <T> boolean contains(T[] args, T val) {
		for(int i = 0 ; i < args.length ; i++) {
			if(args[i].equals(val)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getKeyRepresentation(int val) {
		if(val == GLFW.GLFW_KEY_SPACE) {
			return "SPACE";
		}else if(val == GLFW.GLFW_KEY_ESCAPE) {
			return "ESCAPE";
		}else if(val == GLFW.GLFW_KEY_LEFT_SHIFT) {
			return "LSHIFT";
		}else if(val == GLFW.GLFW_KEY_RIGHT_SHIFT) {
			return "RSHIFT";
		}else if(val == GLFW.GLFW_KEY_LEFT_CONTROL) {
			return "LCONTROL";
		}else if(val == GLFW.GLFW_KEY_RIGHT_CONTROL) {
			return "RCONTROL";
		}else if(val == GLFW.GLFW_KEY_TAB) {
			return "TAB";
		}else if(val == GLFW.GLFW_KEY_LEFT_ALT) {
			return "LALT";
		}else if(val == GLFW.GLFW_KEY_RIGHT_ALT) {
			return "RALT";
		}else if(val == GLFW.GLFW_KEY_LEFT_SUPER) {
			return "LSUPER";
		}else if(val == GLFW.GLFW_KEY_RIGHT_SUPER) {
			return "RSUPER";
		}else if(val == GLFW.GLFW_KEY_DELETE) {
			return "DEL";
		}else if(val == GLFW.GLFW_KEY_ENTER) {
			return "ENTER";
		}else if(val == GLFW.GLFW_KEY_NUM_LOCK) {
			return "NUMLOCK";
		}else if(val == GLFW.GLFW_KEY_KP_ENTER) {
			return "KPENTER";
		}else if(val == GLFW.GLFW_KEY_CAPS_LOCK) {
			return "CAPSLOCK";
		}else if(val == GLFW.GLFW_KEY_KP_ADD) {
			return "KPADD";
		}else if(val == GLFW.GLFW_KEY_KP_DECIMAL) {
			return "KPDECIMAL";
		}else if(val == GLFW.GLFW_KEY_KP_DIVIDE) {
			return "KPDIVIDE";
		}else if(val == GLFW.GLFW_KEY_KP_EQUAL) {
			return "KPEQUALS";
		}else if(val == GLFW.GLFW_KEY_KP_MULTIPLY) {
			return "KPMULTIPLY";
		}else if(val == GLFW.GLFW_KEY_KP_SUBTRACT) {
			return "KPSUBSTRACT";
		}else if(val == GLFW.GLFW_KEY_KP_0) {
			return "KP0";
		}else if(val == GLFW.GLFW_KEY_KP_1) {
			return "KP1";
		}else if(val == GLFW.GLFW_KEY_KP_2) {
			return "KP2";
		}else if(val == GLFW.GLFW_KEY_KP_3) {
			return "KP3";
		}else if(val == GLFW.GLFW_KEY_KP_4) {
			return "KP4";
		}else if(val == GLFW.GLFW_KEY_KP_5) {
			return "KP5";
		}else if(val == GLFW.GLFW_KEY_KP_6) {
			return "KP6";
		}else if(val == GLFW.GLFW_KEY_KP_7) {
			return "KP7";
		}else if(val == GLFW.GLFW_KEY_KP_8) {
			return "KP8";
		}else if(val == GLFW.GLFW_KEY_KP_9) {
			return "KP9";
		}else if(val == GLFW.GLFW_KEY_INSERT) {
			return "INSERT";
		}else if(val == GLFW.GLFW_KEY_PAUSE) {
			return "PAUSE";
		}else if(val == GLFW.GLFW_KEY_PAGE_UP) {
			return "PAGUP";
		}else if(val == GLFW.GLFW_KEY_PAGE_DOWN) {
			return "PAGEDOWN";
		}else if(val == GLFW.GLFW_KEY_UP) {
			return "UP";
		}else if(val == GLFW.GLFW_KEY_DOWN) {
			return "DOWN";
		}else if(val == GLFW.GLFW_KEY_LEFT) {
			return "LEFT";
		}else if(val == GLFW.GLFW_KEY_RIGHT) {
			return "RIGHT";
		}else if(val == GLFW.GLFW_KEY_END) {
			return "END";
		}else if(val == GLFW.GLFW_KEY_F1) {
			return "F1";
		}else if(val == GLFW.GLFW_KEY_F2) {
			return "F2";
		}else if(val == GLFW.GLFW_KEY_F3) {
			return "F3";
		}else if(val == GLFW.GLFW_KEY_F4) {
			return "F4";
		}else if(val == GLFW.GLFW_KEY_F5) {
			return "F5";
		}else if(val == GLFW.GLFW_KEY_F6) {
			return "F6";
		}else if(val == GLFW.GLFW_KEY_F7) {
			return "F7";
		}else if(val == GLFW.GLFW_KEY_F8) {
			return "F8";
		}else if(val == GLFW.GLFW_KEY_F9) {
			return "F9";
		}else if(val == GLFW.GLFW_KEY_F10) {
			return "F10";
		}else if(val == GLFW.GLFW_KEY_F11) {
			return "F11";
		}else if(val == GLFW.GLFW_KEY_F12) {
			return "F12";
		}else if(val == GLFW.GLFW_KEY_F13) {
			return "F13";
		}else if(val == GLFW.GLFW_KEY_F14) {
			return "F14";
		}else if(val == GLFW.GLFW_KEY_F15) {
			return "F15";
		}else if(val == GLFW.GLFW_KEY_F16) {
			return "F16";
		}else if(val == GLFW.GLFW_KEY_F17) {
			return "F17";
		}else if(val == GLFW.GLFW_KEY_F18) {
			return "F18";
		}else if(val == GLFW.GLFW_KEY_F19) {
			return "F19";
		}else if(val == GLFW.GLFW_KEY_F20) {
			return "F20";
		}else if(val == GLFW.GLFW_KEY_F21) {
			return "F21";
		}else if(val == GLFW.GLFW_KEY_F22) {
			return "F22";
		}else if(val == GLFW.GLFW_KEY_F23) {
			return "F23";
		}else if(val == GLFW.GLFW_KEY_F24) {
			return "F24";
		}else if(val == GLFW.GLFW_KEY_F25) {
			return "F25";
		}else if(val == GLFW.GLFW_KEY_PRINT_SCREEN) {
			return "IMPECR";
		}else if(val == GLFW.GLFW_KEY_SCROLL_LOCK) {
			return "SCROLLLOCK";
		}else if(val == GLFW.GLFW_KEY_UNKNOWN) {
			return "UNKNOWN";
		}else {
			return String.valueOf((char) val);
		}
	}
}
