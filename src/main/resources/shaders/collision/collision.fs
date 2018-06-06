#version 150

in vec2 pass_texcoord;

out vec4 color;

uniform sampler2D tex;

void main(){
	color = texture(tex, pass_texcoord);
}