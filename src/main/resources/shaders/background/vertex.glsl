#version 460

in vec2 vertices;
in vec2 texCoords;

out vec2 pass_texCoords;

void main(){
    pass_texCoords = texCoords;
	gl_Position = vec4(vertices, 0.0, 1.0);
}