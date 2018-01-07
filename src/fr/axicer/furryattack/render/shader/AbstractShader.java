package fr.axicer.furryattack.render.shader;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import fr.axicer.furryattack.util.Color;

public abstract class AbstractShader {
	
	public int program,vs,gs,fs;
	
	public AbstractShader(String vertexFilePath, String geometryFilePath, String fragmentFilePath) {
		program = glCreateProgram(); //creating program
		
		if(vertexFilePath != null) {
			//creating vertex shader
			vs = glCreateShader(GL_VERTEX_SHADER);
			glShaderSource(vs, readFile(vertexFilePath));
			glCompileShader(vs);
			if(glGetShaderi(vs, GL_COMPILE_STATUS) != 1){
				System.err.println(glGetShaderInfoLog(vs, 2048));
				System.exit(1);
			}
		}
		
		if(geometryFilePath != null) {
			//creating geometry shader
			gs = glCreateShader(GL_GEOMETRY_SHADER);
			glShaderSource(gs, readFile(geometryFilePath));
			glCompileShader(gs);
			if(glGetShaderi(gs, GL_COMPILE_STATUS) != 1){
				System.err.println(glGetShaderInfoLog(gs, 2048));
				System.exit(1);
			}
		}
		
		if(fragmentFilePath != null) {
			//creating fragment shader
			fs = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(fs, readFile(fragmentFilePath));
			glCompileShader(fs);
			if(glGetShaderi(fs, GL_COMPILE_STATUS) != 1){
				System.err.println(glGetShaderInfoLog(fs, 2048));
				System.exit(1);
			}
		}
		
		
		if(vertexFilePath != null)glAttachShader(program, vs);
		if(geometryFilePath != null)glAttachShader(program, gs);
		if(fragmentFilePath != null)glAttachShader(program, fs);
	}
	
	public void linkAndValidate() {
		glLinkProgram(program);
		if(glGetProgrami(program, GL_LINK_STATUS) != 1){
			System.err.println(glGetProgramInfoLog(program, 2048));
			System.exit(1);
		}
		glValidateProgram(program);
		if(glGetProgrami(program, GL_VALIDATE_STATUS) != 1){
			System.err.println(glGetProgramInfoLog(program, 2048));
			System.exit(1);
		}
	}
	
	public void bind() {
		glUseProgram(program);
	}
	
	public void unbind() {
		glUseProgram(0);
	}
	
	public void setUniformf(String name, float v) {
		glUniform1f(glGetUniformLocation(program, name), v);
	}

	public void setUniformi(String name, int i) {
		glUniform1i(glGetUniformLocation(program, name), i);
	}
	
	public void setUniformvec3f(String name, Vector3f vec) {
		glUniform3f(glGetUniformLocation(program, name), vec.x, vec.y, vec.z);
	}
	
	public void setUniformMat4f(String name, Matrix4f mat) {
		FloatBuffer matbuff = BufferUtils.createFloatBuffer(4*4);
		mat.get(matbuff);
		glUniformMatrix4fv(glGetUniformLocation(program, name), false, matbuff);
	}
	
	private String readFile(String filename){
		StringBuilder string = new StringBuilder();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(AbstractShader.class.getResource("/shaders/"+filename).toURI())));
			String line;
			while((line = br.readLine()) != null){
				string.append(line);
				string.append("\n");
			}
			br.close();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return string.toString();
	}
}
