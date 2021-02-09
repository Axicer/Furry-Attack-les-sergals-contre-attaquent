package fr.axicer.furryattack.client.render.shader;

import org.joml.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class Shader {

    private static final Logger LOGGER = LoggerFactory.getLogger(Shader.class);

    private int program,vs,gs,fs;
    private final String vsFilePath, gsFilePath, fsFilePath;
    private boolean valid;

    public Shader(String vertexFilePath, String geometryFilePath, String fragmentFilePath, boolean init, boolean linkAndValidate) {
        vsFilePath = vertexFilePath;
        gsFilePath = geometryFilePath;
        fsFilePath = fragmentFilePath;

        if(init){
            valid = init();
        }
        if(linkAndValidate){
            valid = linkAndValidate();
        }
    }

    /**
     * create the shader
     * @return true if the compilation is perfect
     */
    protected boolean init() {
        program = glCreateProgram(); //creating program

        if(vsFilePath != null) {
            //creating vertex shader
            vs = glCreateShader(GL_VERTEX_SHADER);
            glShaderSource(vs, readFile(vsFilePath));
            glCompileShader(vs);
            if(glGetShaderi(vs, GL_COMPILE_STATUS) != 1){
                LOGGER.error(glGetShaderInfoLog(vs, 2048));
                return false;
            }else{
                glAttachShader(program, vs);
            }
        }
        if(gsFilePath != null) {
            //creating geometry shader
            gs = glCreateShader(GL_GEOMETRY_SHADER);
            glShaderSource(gs, readFile(gsFilePath));
            glCompileShader(gs);
            if(glGetShaderi(gs, GL_COMPILE_STATUS) != 1){
                LOGGER.error(glGetShaderInfoLog(gs, 2048));
                return false;
            }else {
                glAttachShader(program, gs);
            }
        }
        if(fsFilePath != null) {
            //creating fragment shader
            fs = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(fs, readFile(fsFilePath));
            glCompileShader(fs);
            if(glGetShaderi(fs, GL_COMPILE_STATUS) != 1){
                LOGGER.error(glGetShaderInfoLog(fs, 2048));
                return false;
            }else{
                glAttachShader(program, fs);
            }
        }

        return true;
    }

    /**
     * validate and link the shader, no operation should be done on shader after that excepts binding and unbinding
     * @return true if link and validate is done, false elsewhere
     */
    protected boolean linkAndValidate() {
        glLinkProgram(program);
        if(glGetProgrami(program, GL_LINK_STATUS) != 1){
            LOGGER.error(glGetProgramInfoLog(program, 2048));
            return false;
        }
        glValidateProgram(program);
        if(glGetProgrami(program, GL_VALIDATE_STATUS) != 1){
            LOGGER.error(glGetProgramInfoLog(program, 2048));
            return false;
        }

        glDeleteShader(vs);
        glDeleteShader(gs);
        glDeleteShader(fs);
        return true;
    }

    public void bind() {
        if(!valid){
            LOGGER.error("This shader program has not been validated please, look at logs.");
            return;
        }
        glUseProgram(program);
    }

    public int getProgram() {
        return program;
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void remove() {
        glDeleteProgram(program);
    }

    public void setUniformFloat(String name, float v) {
        glUniform1f(glGetUniformLocation(program, name), v);
    }

    public void setUniformBoolean(String name, boolean v) {
        glUniform1i(glGetUniformLocation(program, name), v?1:0);
    }

    public void setUniformInt(String name, int i) {
        glUniform1i(glGetUniformLocation(program, name), i);
    }

    public void setUniformVec2f(String name, Vector2f vec) {
        glUniform2f(glGetUniformLocation(program, name), vec.x, vec.y);
    }

//    public void setUniformVec3f(String name, Vector3f vec) {
//        glUniform3f(glGetUniformLocation(program, name), vec.x, vec.y, vec.z);
//    }
//
//    public void setUniformVec4f(String name, Vector4f vec) {
//        glUniform4f(glGetUniformLocation(program, name), vec.x, vec.y, vec.z, vec.w);
//    }
//
//    public void setUniformMat4f(String name, Matrix4f mat) {
//        FloatBuffer matBuff = BufferUtils.createFloatBuffer(4*4);
//        mat.get(matBuff);
//        glUniformMatrix4fv(glGetUniformLocation(program, name), false, matBuff);
//    }

    private String readFile(String filename){
        var inputStream = Shader.class.getResourceAsStream("/shaders/"+filename);
        var inputStreamReader = new InputStreamReader(inputStream);
        var reader = new BufferedReader(inputStreamReader);
        var builder = new StringBuilder();

        try{
            String line;
            while((line = reader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
            }
        }catch (IOException ex){
            LOGGER.error(ex.getMessage());
            return "";
        }finally {
            try {
                reader.close();
                inputStreamReader.close();
                inputStream.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }

        return builder.toString();
    }
}
