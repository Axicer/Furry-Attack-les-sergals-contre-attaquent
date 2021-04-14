#version 460

in vec2 vertices;
in vec2 texCoord;
in vec2 borderTexCoord;
in vec2 decorationTexCoord;

out vec2 pass_TexCoord;
out vec2 pass_borderTexCoord;
out vec2 pass_decorationTexCoord;

void main(){
    pass_TexCoord = texCoord;
    pass_borderTexCoord = borderTexCoord;
    pass_decorationTexCoord = decorationTexCoord;
    //converting coords from 0...1 to -1...1
    gl_Position = vec4((vertices.x*2)-1, ((1.0 - vertices.y)*2)-1, 1.0, 1.0);
}