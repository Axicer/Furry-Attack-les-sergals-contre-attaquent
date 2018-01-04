#version 150

in vec3 vertices;
in vec2 texcoord;

out vec2 pass_texcoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void main(){
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(vertices, 1.0);
	pass_texcoord = texcoord;
}