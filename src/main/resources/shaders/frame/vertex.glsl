#version 460

in vec2 vertices;
in vec2 texCoords;

out vec2 pass_texCoords;

void main(){
    pass_texCoords = texCoords;
    //converting coords from 0...1 to -1...1
	gl_Position = vec4((vertices*2)-1, 1.0, 1.0);
}