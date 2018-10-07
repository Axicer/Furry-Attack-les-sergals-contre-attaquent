#version 150

layout ( points ) in;
layout ( triangle_strip, max_vertices = 6 ) out;

out vec2 pass_texcoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

uniform float width;
uniform float height;
uniform vec4 textureBounds;

void createVertex(vec3 offset, vec2 texoffset){

	vec4 worldPosition = gl_in[0].gl_Position + vec4(offset.x*width/2, offset.y*height/2, offset.z, 0.0);
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * worldPosition;
	
	pass_texcoord = texoffset;
	
	EmitVertex();
}

void main(){

	createVertex(vec3(-1.0, 2.0, -0.5), textureBounds.xy);
	createVertex(vec3(1.0, 2.0, -0.5), textureBounds.zy);
	createVertex(vec3(1.0, 0.0, -0.5), textureBounds.zw);
	
	createVertex(vec3(-1.0, 2.0, -0.5), textureBounds.xy);
	createVertex(vec3(1.0, 0.0, -0.5), textureBounds.zw);
	createVertex(vec3(-1.0, 0.0, -0.5), textureBounds.xw);
	
	EndPrimitive();
}