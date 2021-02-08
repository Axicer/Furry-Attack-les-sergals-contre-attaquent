#version 460

in vec2 pass_texCoords;

out vec4 color;

uniform sampler2D atlas;
uniform vec2 cursorPos;
uniform float screenWidth;
uniform float screenHeight;
uniform bool useCursorLight;
uniform bool useDarkBackground;
uniform float darkFactor;
uniform float lightRadius;

void main(){
	color = texture(atlas, pass_texCoords);

	if(useCursorLight){
		// Convert screen coordinates to normalized device coordinates (NDC)
		vec2 ndc = vec2(
		(gl_FragCoord.x / screenWidth),
		(gl_FragCoord.y / screenHeight));
		vec2 diff = vec2(cursorPos.x, 1 - cursorPos.y) - ndc;

		diff.x *= screenWidth/screenHeight;
		float dist = length(diff);

		float lightPresence = 0;
		if(dist != 0){
			lightPresence = clamp(1.0/(dist*(1.0/lightRadius)), 0.0, 2.0)-1.0;
		}else{
			lightPresence = 0.4;
		}
		color = mix(color, vec4(1.0,1.0,153.0/255.0,1.0), clamp(lightPresence, 0.0, 0.3));
	}
	if(useDarkBackground){
		color = mix(color, vec4(0.0,0.0,0.0,1.0), darkFactor);
	}
}