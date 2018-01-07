#version 150

in vec3 vertices;
in vec2 baseTexCoord;

out vec2 pass_texcoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

uniform float spriteWidth;
uniform float offsetX;

void main(){
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(vertices, 1.0);
	
	float xtex = (offsetX+baseTexCoord.x)*spriteWidth;
	
	pass_texcoord = vec2(xtex, baseTexCoord.y);
}