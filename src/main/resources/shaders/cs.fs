#version 120

uniform sampler2D sampler;
varying vec2 tex_coords;
varying vec3 colors;
uniform float ambientStrength = 1.0f;


void main(){
 
    vec3 ambient = ambientStrength * colors;
    
    gl_FragColor =  texture2D(sampler,tex_coords)* vec4(ambient,1.0f);
	//gl_FragColor = texture2D(sampler,tex_coords)* mix(vec4(colors,1), vec4(1,1,1,1),0.1);

}
