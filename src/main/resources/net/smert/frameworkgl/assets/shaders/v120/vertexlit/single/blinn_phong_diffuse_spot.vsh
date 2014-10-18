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
    vec4 eyeVertex = gl_ModelViewMatrix * gl_Vertex;
    vec3 eyeLightDir = gl_LightSource[0].position.xyz - eyeVertex.xyz;
    float dist = length(eyeLightDir);
    eyeLightDir = normalize(eyeLightDir);

    // No attenuation for a directional light
    float attenuationFactor = 1.0 / (gl_LightSource[0].constantAttenuation
                                   + gl_LightSource[0].linearAttenuation * dist
                                   + gl_LightSource[0].quadraticAttenuation * dist * dist);
    attenuationFactor *= clamp(1.0 - (dist * dist) / (uRadius * uRadius), 0.0, 1.0);

    // Calculate cone's light influence
    float coneCosAngle = dot(-eyeLightDir, normalize(gl_LightSource[0].spotDirection));
    float coneEffect = (coneCosAngle < gl_LightSource[0].spotCosCutoff) ? 0.0 : pow(coneCosAngle, gl_LightSource[0].spotExponent);
    attenuationFactor *= coneEffect;

    // Calculate lambert term
    float NdotL = max(dot(eyeNormal, eyeLightDir), 0.0);

    // Calculate diffuse
    vec4 lightDiffuse = NdotL * (gl_LightSource[0].diffuse * matDiffuse);

    gl_FrontColor = emissionAmbient + attenuationFactor * (lightAmbient + lightDiffuse);
    gl_Position = ftransform();
}