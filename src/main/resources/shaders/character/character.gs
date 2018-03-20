#version 150

layout ( points ) in;
layout ( triangle_strip, max_vertices = 6 ) out;

out vec2 pass_texcoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

uniform float characterWidth;
uniform float characterHeight;

uniform float spriteWidth;
uniform float offsetX;

void createVertex(vec3 offset, vec2 texoffset){

	vec4 worldPosition = gl_in[0].gl_Position + vec4(offset.x*characterWidth/2.0, offset.y*characterHeight/2.0, offset.z, 0.0);
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * worldPosition;
	
	float xtex = (offsetX+texoffset.x)*spriteWidth;
	
	pass_texcoord = vec2(xtex, texoffset.y);
	
	EmitVertex();
}

void main(){
	
	createVertex(vec3(-1.0, 1.0, -0.8), vec2(0.0, 0.0));
	createVertex(vec3(1.0, 1.0, -0.8), vec2(1.0, 0.0));
	createVertex(vec3(1.0, -1.0, -0.8), vec2(1.0, 1.0));
	
	createVertex(vec3(-1.0, 1.0, -0.8), vec2(0.0, 0.0));
	createVertex(vec3(1.0, -1.0, -0.8), vec2(1.0, 1.0));
	createVertex(vec3(-1.0, -1.0, -0.8), vec2(0.0, 1.0));
	
	EndPrimitive();
	
}