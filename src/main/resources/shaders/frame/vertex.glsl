#version 460

in vec2 vertices;
in vec2 stoneTexCoord;
in vec2 borderTexCoord;
in vec2 decorationTexCoord;

out vec2 pass_stoneTexCoord;
out vec2 pass_borderTexCoord;
out vec2 pass_decorationTexCoord;

void main(){
    pass_stoneTexCoord = stoneTexCoord;
    pass_borderTexCoord = borderTexCoord;
    pass_decorationTexCoord = decorationTexCoord;
    //converting coords from 0...1 to -1...1
	gl_Position = vec4((vertices*2)-1, 1.0, 1.0);
}