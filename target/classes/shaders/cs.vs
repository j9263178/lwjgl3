#version 120

attribute vec3 color;
attribute vec3 vertices;
attribute vec2 textures;

varying vec2 tex_coords;
varying vec3 colors;

uniform mat4 projection;
uniform mat4 texmodifier;


void main()
{
    
	colors = color;
    
	tex_coords = (texmodifier * vec4(textures,0,1)).xy;

	gl_Position = projection * vec4(vertices,1);

}
