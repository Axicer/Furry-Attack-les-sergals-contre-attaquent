#version 150

in vec2 pass_texcoord;

out vec4 color;

uniform sampler2D tex;
uniform vec3 textColor;

void main() {
	color = texture(tex, pass_texcoord)*vec4(textColor, 1.0);
}