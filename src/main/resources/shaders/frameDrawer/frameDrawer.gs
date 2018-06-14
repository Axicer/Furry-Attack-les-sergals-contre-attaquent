#version 150

layout ( points ) in;
layout ( triangle_strip, max_vertices = 6 ) out;

out vec2 pass_textureCoordinate;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

uniform float screenWidth;
uniform float screenHeight;

void createVertex(vec3 offset, vec2 texOffset){

	vec4 worldPosition = gl_in[0].gl_Position + vec4(offset.x*screenWidth/2, offset.y*screenHeight/2, offset.z, 0.0);
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * worldPosition;
	
	pass_textureCoordinate = texOffset;
	
	EmitVertex();
}

void main(){
	
	createVertex(vec3(-1.0, 1.0, -1), vec2(0.0, 1.0));
	createVertex(vec3(1.0, 1.0, -1), vec2(1.0, 1.0));
	createVertex(vec3(1.0, -1.0, -1), vec2(1.0, 0.0));
	
	createVertex(vec3(-1.0, 1.0, -1), vec2(0.0, 1.0));
	createVertex(vec3(1.0, -1.0, -1), vec2(1.0, 0.0));
	createVertex(vec3(-1.0, -1.0, -1), vec2(0.0, 0.0));
	
	EndPrimitive();
}