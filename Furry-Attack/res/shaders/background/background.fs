#version 150 core

in vec2 pass_textureCoordinate;

out vec4 color;

uniform sampler2D tex;

void main(){
	color = texture2D(tex, pass_textureCoordinate)+vec4(1,1,1,1);
}