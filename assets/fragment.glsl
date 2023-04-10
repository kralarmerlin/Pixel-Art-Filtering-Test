#version 130

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_texels_per_pixel;
uniform vec2 u_texture_size;

// If we see a distortion when using transparent pixels, it may be because Cole Cecil's method did not use the alpha value in this link.
// https://csantosbh.wordpress.com/2014/01/25/manual-texture-filtering-for-pixelated-games-in-webgl/
vec2 uv_colececil( vec2 uv, vec2 texture_size ) {
    vec2 pixel = uv * texture_size;

    vec2 alpha = vec2(1);
    vec2 locationWithinTexel = fract(pixel);

    vec2 interpolationAmount = clamp(locationWithinTexel * u_texels_per_pixel / alpha, vec2(0,0), vec2(.5,.5)) +
        clamp((locationWithinTexel - vec2(1,1)) * u_texels_per_pixel / alpha + .5, vec2(0,0), vec2(.5,.5));

    return (floor(pixel) + interpolationAmount) / texture_size;
}

void main () {
    vec2 pixel = gl_FragCoord.xy;
    vec2 final_uv = uv_colececil( v_texCoords, u_texture_size);

    gl_FragColor = v_color * texture2D(u_texture, final_uv);
}

