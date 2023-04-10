#version 130

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_texels_per_pixel;
uniform vec2 u_texture_size;

// If we see a distortion when using transparent pixels, it may be because Cole Cecil's method did not use the alpha value in this link.
// https://csantosbh.wordpress.com/2014/01/25/manual-texture-filtering-for-pixelated-games-in-webgl/
// I'm not interested in this right now as I don't use transparent pixels.
vec2 uv_colececil( vec2 uv, vec2 texture_size ) {
    vec2 pixel = uv * texture_size;

    // "I chose this alpha because it looked nice in my demo"
    vec2 alpha = vec2(1);
    vec2 locationWithinTexel = fract(pixel);

    // .5 i vec2(.5,.5)'e çevirmek GEREKLİ OLABİLİR!!!
    vec2 interpolationAmount = clamp(locationWithinTexel * u_texels_per_pixel / alpha, vec2(0,0), vec2(.5,.5)) +
        clamp((locationWithinTexel - vec2(1,1)) * u_texels_per_pixel / alpha + .5, vec2(0,0), vec2(.5,.5));

    // Calculate the inverse of u_texels_per_pixel so we multiply here instead of dividing.
    //vec2 interpolationAmount = clamp(locationWithinTexel * u_texels_per_pixel / alpha, 0, .5) +
    //clamp((locationWithinTexel - 1) * u_texels_per_pixel / alpha + .5, 0, .5);

    return (floor(pixel) + interpolationAmount) / texture_size;
}

void main () {
    //vec2 texture_size = textureSize(u_texture0, 0);

    vec2 pixel = gl_FragCoord.xy;
    vec2 final_uv = uv_colececil( v_texCoords, u_texture_size);

    //vec4 color = texture2D(u_texture0, final_uv);
    //gl_FragColor = color;

    //gl_FragColor = v_color * texture2D(u_texture0, final_uv);
    gl_FragColor = v_color * texture2D(u_texture, final_uv);
}

