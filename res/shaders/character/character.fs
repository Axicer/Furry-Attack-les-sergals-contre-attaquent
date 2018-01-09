#version 150

out vec4 color;

in vec2 pass_texcoord;

uniform vec3 primaryColor;
uniform vec3 secondaryColor;
uniform sampler2D tex;

vec4 blur9(sampler2D image, vec2 uv, vec2 resolution, vec2 direction) {
  vec4 color = vec4(0.0);
  vec2 off1 = vec2(1.3846153846) * direction;
  vec2 off2 = vec2(3.2307692308) * direction;
  color += texture2D(image, uv) * 0.2270270270;
  color += texture2D(image, uv + (off1 / resolution)) * 0.3162162162;
  color += texture2D(image, uv - (off1 / resolution)) * 0.3162162162;
  color += texture2D(image, uv + (off2 / resolution)) * 0.0702702703;
  color += texture2D(image, uv - (off2 / resolution)) * 0.0702702703;
  return color;
}

void main(){
	color = texture(tex, pass_texcoord);
	if(color.r == 1 && color.g == 1 && color.b == 1)discard;
	if(color.r == 1 && color.g == 0 && color.b == 1){
		color = vec4(primaryColor, 1.0);
	}
	if(color.r == 1 && color.g == 1 && color.b == 0){
		color = vec4(secondaryColor, 1.0);
	}
	//color = blur9(tex, pass_texcoord, vec2(1920,1080), vec2(1.0, 0.0));
}