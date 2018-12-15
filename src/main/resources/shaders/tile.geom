#version 150

layout ( points ) in;
layout ( triangle_strip, max_vertices = 6 ) out;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

uniform float size;

void createVertex(vec3 offset){

	vec4 worldPosition = gl_in[0].gl_Position + vec4(offset.x*size, offset.y*size, offset.z, 0.0);
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * worldPosition;
	
	EmitVertex();
}

void main(){

	createVertex(vec3(0.0, 0.0, -1.0));
	createVertex(vec3(1.0, 0.0, -1.0));
	createVertex(vec3(1.0, -1.0, -1.0));
	
	createVertex(vec3(0.0, 0.0, -1.0));
	createVertex(vec3(1.0, -1.0, -1.0));
	createVertex(vec3(0.0, -1.0, -1.0));
	
	EndPrimitive();
}