#version 460

in vec2 pass_stoneTexCoord;
in vec2 pass_borderTexCoord;
in vec2 pass_decorationTexCoord;

out vec4 color;

uniform sampler2D stoneAtlas;
uniform sampler2D borderAtlas;
uniform sampler2D decorationAtlas;

void main(){
	vec4 stone = texture(stoneAtlas, pass_stoneTexCoord);
	vec4 decoration = texture(decorationAtlas, pass_decorationTexCoord);
	vec4 border = texture(borderAtlas, pass_borderTexCoord);

	if(border.w >= 0.9){
		color = border;
	}else if(decoration.w >= 0.9){
		color = decoration;
	}else{
		color = stone;
	}

}