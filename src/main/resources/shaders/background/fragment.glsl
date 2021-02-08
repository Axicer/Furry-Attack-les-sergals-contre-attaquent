#version 460

in vec2 pass_texCoords;

out vec4 color;

uniform sampler2D tex;

void main(){
	color = texture(tex, pass_texCoords);
}