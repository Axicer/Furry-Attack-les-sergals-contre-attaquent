#version 150

out vec4 color;

in vec2 pass_texcoord;

uniform vec3 primaryColor;
uniform vec3 secondaryColor;
uniform sampler2D tex;

void main(){
	color = texture(tex, pass_texcoord);
	if(color.r == 1 && color.g == 1 && color.b == 1)discard;
	if(color.r == 1 && color.b == 1){
		color = vec4(primaryColor, 1.0);
	}
	if(color.r == 1 && color.g == 1){
		color = vec4(secondaryColor, 1.0);
	}
}