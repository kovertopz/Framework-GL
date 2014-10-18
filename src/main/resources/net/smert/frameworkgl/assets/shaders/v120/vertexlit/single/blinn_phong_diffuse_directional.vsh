#version 120

////////////////////////////////////////////////////////////////////////////////
// http://www.glprogramming.com/red/chapter05.html                            //
//                                                                            //
// color = (matEmission + globalAmbient * matAmbient) +                       //
//         AttenuationFactor( 1.0 / ( Kc + Kl*d + Kq*d^2 ) ) *                //
//         [ (lightAmbient * matAmbient) +                                    //
//           (max(N.L,0) * lightDiffuse * matDiffuse) +                       //
//           (max(N.H,0)^matShininess * lightSpecular * matSpecular) ]        //
////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// Uniforms                                                                   //
////////////////////////////////////////////////////////////////////////////////
uniform float uColorMaterialAmbient = 1.0;
uniform float uColorMaterialDiffuse = 1.0;
uniform float uColorMaterialEmission = 0.0;
uniform float uRadius = 0.0;

////////////////////////////////////////////////////////////////////////////////
// Main                                                                       //
////////////////////////////////////////////////////////////////////////////////
void main(void)
{
    vec4 matAmbient = mix(gl_FrontMaterial.ambient, gl_Color, uColorMaterialAmbient);
    vec4 matDiffuse = mix(gl_FrontMaterial.diffuse, gl_Color, uColorMaterialDiffuse);
    vec4 matEmission = mix(gl_FrontMaterial.emission, gl_Color, uColorMaterialEmission);

    // Transform normal into eye space. gl_NormalMatrix is the transpose of the
    // inverse of the upper leftmost 3x3 of gl_ModelViewMatrix.
    vec3 eyeNormal = normalize(gl_NormalMatrix * gl_Normal);

    // Calculate emission and global ambient light
    vec4 emissionAmbient = matEmission + (gl_LightModel.ambient * matAmbient);

    // Calculate ambient
    vec4 lightAmbient = gl_LightSource[0].ambient * matAmbient;

    // Transform the vertex into eye space
    vec3 eyeLightDir = normalize(gl_LightSource[0].position.xyz);

    // Calculate lambert term
    float NdotL = max(dot(eyeNormal, eyeLightDir), 0.0);

    // Calculate diffuse
    vec4 lightDiffuse = NdotL * (gl_LightSource[0].diffuse * matDiffuse);

    // No attenuation for a directional light
    gl_FrontColor = emissionAmbient + (lightAmbient + lightDiffuse);
    gl_Position = ftransform();
}