#version 150

in vec3 vertices;
in vec2 texture_coordinate;

out vec2 pass_textureCoordinate;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void main(){
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(vertices, 1.0);
	pass_textureCoordinate = texture_coordinate;
}